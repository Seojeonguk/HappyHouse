package com.example.happyhouse.domain.service;

import com.example.happyhouse.domain.dto.request.ApartmentDetailInfoReq;
import com.example.happyhouse.domain.dto.request.TradeReq;
import com.example.happyhouse.domain.dto.response.ApartmentBaseInfoRes;
import com.example.happyhouse.domain.dto.response.ApartmentDetailInfoRes;
import com.example.happyhouse.domain.dto.response.GeocodingRes;
import com.example.happyhouse.domain.dto.response.TradeRes;
import com.example.happyhouse.domain.entity.Apartment;
import com.example.happyhouse.domain.entity.ApartmentBaseInformation;
import com.example.happyhouse.domain.entity.ApartmentDetailInformation;
import com.example.happyhouse.domain.entity.Geocoding;
import com.example.happyhouse.domain.repository.ApartmentBaseRepository;
import com.example.happyhouse.domain.repository.ApartmentDetailRepository;
import com.example.happyhouse.domain.repository.ApartmentRepository;
import com.example.happyhouse.domain.repository.GeocodingRepository;
import com.example.happyhouse.util.Fetch;
import com.example.happyhouse.util.Parsing;
import com.example.happyhouse.util.Util;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.Element;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ExternalApiServiceImpl implements ExternalApiService {

    @Value("${external.google.key}")
    private String googleKey;

    @Value("${external.google.geocoding.endpoint}")
    private String geocodingEndpoint;

    @Value("${external.data.key}")
    private String dataKey;

    @Value("${external.data.apt-trade.endpoint}")
    private String aptTradeEndpoint;

    @Value("${external.data.house-trade.endpoint}")
    private String houseTradeEndpoint;

    @Value("${external.data.apt-list.sigungu.endpoint}")
    private String aptSigunguEndpoint;

    @Value("${external.data.apt-info.base.endpoint}")
    private String aptBaseInfoEndpoint;

    @Value("${external.data.apt-info.detail.endpoint}")
    private String aptDetailInfoEndpoint;

    private final ApartmentRepository apartmentRepository;
    private final ApartmentBaseRepository apartmentBaseRepository;
    private final ApartmentDetailRepository apartmentDetailRepository;
    private final GeocodingRepository geocodingRepository;

    @Override
    @Transactional
    public GeocodingRes getGeocoding(String address) throws IOException {
        Geocoding geocoding = geocodingRepository.findByAddress(address);
        if (!Util.isEmpty(geocoding)) {
            return geocoding.toResponse();
        }

        String url = geocodingEndpoint + "/json" +
                "?address=" + address.replaceAll(" ", "+") +
                "&key=" + googleKey +
                "&language=ko";

        String response = Fetch.fetchDataFromAPI(url);
        Geocoding parsingGeocoding = parseGeocodingResponse(response, address);
        geocodingRepository.save(parsingGeocoding);

        return parsingGeocoding.toResponse();
    }

    @Override
    public String getGoogleApiKey() {
        return googleKey;
    }

    @Override
    public List<TradeRes> getTrade(TradeReq tradeReq) throws IOException {
        List<TradeRes> tradeList = new ArrayList<>();

        LocalDate today = LocalDate.now();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMM");
        String searchDate = today.format(formatter);
        if (!Util.isEmpty(tradeReq.getYear()) && !Util.isEmpty(tradeReq.getMonth())) {
            searchDate = tradeReq.getYearMonth();
        }

        String category = tradeReq.getCategory();
        String legalCode = tradeReq.getLegalCode();
        switch (category) {
            case "전체" -> {
                List<TradeRes> houseTrades = fetchTradeData("연립다세대", legalCode, houseTradeEndpoint, searchDate, tradeReq.getSi());
                List<TradeRes> apartTrades = fetchTradeData("아파트", legalCode, aptTradeEndpoint, searchDate, tradeReq.getSi());

                tradeList.addAll(houseTrades);
                tradeList.addAll(apartTrades);
            }
            case "아파트" -> {
                List<TradeRes> apartTrades = fetchTradeData("아파트", legalCode, aptTradeEndpoint, searchDate, tradeReq.getSi());

                tradeList.addAll(apartTrades);
            }
            case "연립다세대" -> {
                List<TradeRes> houseTrades = fetchTradeData("연립다세대", legalCode, houseTradeEndpoint, searchDate, tradeReq.getSi());

                tradeList.addAll(houseTrades);
            }
            case null, default -> log.warn("Not support category: {}", category);
        }

        return tradeList;
    }

    @Override
    @Transactional
    public List<ApartmentBaseInfoRes> getBaseInfo(TradeReq tradeReq) throws IOException {
        String url = aptSigunguEndpoint +
                "?serviceKey=" + dataKey +
                "&sigunguCode=" + tradeReq.getLegalCode().substring(0, 5) +
                "&numOfRows=10000";

        List<Apartment> apartments = Fetch.fetchApartmentInformation(
                tradeReq.getLegalCode().substring(0, 5),
                apartmentRepository::findByLegalDongCodeStartingWith,
                url,
                Apartment::new,
                apartmentRepository::saveAll
        );


        if (apartments.isEmpty()) {
            return List.of();
        }

        List<String> complexCodes = apartments.stream().map(Apartment::getComplexCode).toList();
        List<ApartmentBaseInfoRes> apartmentBaseInfoResList = getBaseInfo(complexCodes).stream().map(ApartmentBaseInfoRes::new).toList();
        apartmentBaseInfoResList.forEach(apartmentBaseInfoRes -> {
            String address = Util.removeSuffix(apartmentBaseInfoRes.getLegalDongAddress(), apartmentBaseInfoRes.getComplexName());
            try {
                GeocodingRes geocodingRes = getGeocoding(address);
                apartmentBaseInfoRes.setGeoCodingRes(geocodingRes);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        return apartmentBaseInfoResList;
    }

    @Override
    public ApartmentDetailInfoRes getDetailInfo(ApartmentDetailInfoReq apartmentDetailInfoReq) throws IOException {
        String complexCode = apartmentDetailInfoReq.getComplexCode();
        if (Util.isEmpty(complexCode)) {
            throw new IllegalArgumentException("Complex code is empty");
        }

        List<ApartmentDetailInformation> aptDetailInfo = getDetailInfo(complexCode);

        return new ApartmentDetailInfoRes(aptDetailInfo.getFirst());
    }

    private List<TradeRes> fetchTradeData(String category, String legalCode, String endPoint, String searchDate, String si) throws IOException {
        String url = endPoint +
                "?serviceKey=" + dataKey +
                "&LAWD_CD=" + legalCode.substring(0, 5) +
                "&DEAL_YMD=" + searchDate;

        String response = Fetch.fetchDataFromAPI(url);
        List<Element> items = Parsing.parseXmlResponse(response);
        List<TradeRes> trades = items.stream().map(item -> new TradeRes(item, category)).toList();
        for (TradeRes trade : trades) {
            String address = si + " " + trade.getLegalDong() + " " + trade.getName();
            GeocodingRes geocodingRes = getGeocoding(address);

            trade.setGeoCodingRes(geocodingRes);
        }

        return trades;
    }

    private List<ApartmentBaseInformation> getBaseInfo(List<String> complexCodes) throws IOException {
        List<ApartmentBaseInformation> baseInfo = new ArrayList<>();
        for (String complexCode : complexCodes) {
            baseInfo.addAll(getBaseInfo(complexCode));
        }
        return baseInfo;
    }

    private List<ApartmentBaseInformation> getBaseInfo(String complexCode) throws IOException {
        String url = aptBaseInfoEndpoint + "?serviceKey=" + dataKey + "&kaptCode=" + complexCode;
        return Fetch.fetchApartmentInformation(
                complexCode,
                apartmentBaseRepository::findByComplexCode,
                url,
                ApartmentBaseInformation::new,
                apartmentBaseRepository::saveAll
        );
    }

    private List<ApartmentDetailInformation> getDetailInfo(String complexCode) throws IOException {
        String url = aptDetailInfoEndpoint + "?serviceKey=" + dataKey + "&kaptCode=" + complexCode;
        return Fetch.fetchApartmentInformation(
                complexCode,
                apartmentDetailRepository::findByComplexCode,
                url,
                ApartmentDetailInformation::new,
                apartmentDetailRepository::saveAll
        );
    }

    private Geocoding parseGeocodingResponse(String response, String address) {
        JSONObject jsonResponse = new JSONObject(response);
        JSONObject firstResult = jsonResponse.getJSONArray("results").getJSONObject(0);

        JSONObject location = firstResult
                .getJSONObject("geometry")
                .getJSONObject("location");

        String formattedAddress = firstResult.getString("formatted_address");
        String lat = String.valueOf(location.getBigDecimal("lat"));
        String lng = String.valueOf(location.getBigDecimal("lng"));

        return new Geocoding(address, lat, lng, formattedAddress);
    }
}

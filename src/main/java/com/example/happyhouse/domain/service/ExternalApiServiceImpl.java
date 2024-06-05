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
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ExternalApiServiceImpl implements ExternalApiService {

    @Value("${external.google.key}")
    private String externalGoogleKey;

    @Value("${external.google.geocoding.endpoint}")
    private String externalGoogleGeocodingEndpoint;

    @Value("${external.data.key}")
    private String externalDataKey;

    @Value("${external.data.apt-trade.endpoint}")
    private String externalDataAptTradeEndpoint;

    @Value("${external.data.house-trade.endpoint}")
    private String externalDataHouseTradeEndpoint;

    @Value("${external.data.apt-list.sigungu.endpoint}")
    private String externalDataAptListSigunguEndpoint;

    @Value("${external.data.apt-info.base.endpoint}")
    private String externalDataAptInfoBaseEndpoint;

    @Value("${external.data.apt-info.detail.endpoint}")
    private String externalDataAptInfoDetailEndpoint;

    private final ApartmentRepository apartmentRepository;
    private final ApartmentBaseRepository apartmentBaseRepository;
    private final ApartmentDetailRepository apartmentDetailRepository;
    private final GeocodingRepository geocodingRepository;

    @Override
    @Transactional
    public GeocodingRes getGeocoding(String address) throws IOException {
        Optional<Geocoding> geocoding = geocodingRepository.findByAddress(address);
        if (geocoding.isPresent()) {
            return geocoding.get().toResponse();
        }

        String url = externalGoogleGeocodingEndpoint + "/json" +
                "?address=" + address.replaceAll(" ", "+") +
                "&key=" + externalGoogleKey +
                "&language=ko";

        String response = Fetch.fetchDataFromAPI(url);
        Geocoding parsingGeocoding = parseGeocodingResponse(response,address);
        geocodingRepository.save(parsingGeocoding);

        return parsingGeocoding.toResponse();
    }

    @Override
    public String getGoogleApiKey() {
        return externalGoogleKey;
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
        if ("전체".equals(category)) {
            List<TradeRes> houseTrades = fetchTradeData("연립다세대", legalCode, externalDataHouseTradeEndpoint, searchDate, tradeReq.getSi());
            List<TradeRes> apartTrades = fetchTradeData("아파트", legalCode, externalDataAptTradeEndpoint, searchDate, tradeReq.getSi());

            tradeList.addAll(houseTrades);
            tradeList.addAll(apartTrades);
        } else if ("아파트".equals(category)) {
            List<TradeRes> apartTrades = fetchTradeData("아파트", legalCode, externalDataAptTradeEndpoint, searchDate, tradeReq.getSi());

            tradeList.addAll(apartTrades);
        } else if ("연립다세대".equals(category)) {
            List<TradeRes> houseTrades = fetchTradeData("연립다세대", legalCode, externalDataHouseTradeEndpoint, searchDate, tradeReq.getSi());

            tradeList.addAll(houseTrades);
        } else {
            log.warn("Not support category: {}", category);
        }

        return tradeList;
    }

    @Override
    @Transactional
    public List<ApartmentBaseInfoRes> getInformation(TradeReq tradeReq) throws IOException {
        List<Apartment> apartments = apartmentRepository.findByLegalDongCodeStartingWith(tradeReq.getLegalCode().substring(0, 5));
        if (apartments.isEmpty()) {
            String url = externalDataAptListSigunguEndpoint +
                    "?serviceKey=" + externalDataKey +
                    "&sigunguCode=" + tradeReq.getLegalCode().substring(0, 5) +
                    "&numOfRows=10000";

            String response = Fetch.fetchDataFromAPI(url);
            List<Element> items = Parsing.parseXmlResponse(response);
            apartments = items.stream().map(Apartment::new).toList();
            apartmentRepository.saveAll(apartments);
        }

        if (apartments.isEmpty()) {
            return List.of();
        }

        List<String> complexCodes = apartments.stream().map(Apartment::getComplexCode).toList();
        List<ApartmentBaseInformation> apartmentBaseInformations = getApartmentBaseInformations(complexCodes);

        List<ApartmentBaseInfoRes> apartmentBaseInfoResList = apartmentBaseInformations.stream().map(ApartmentBaseInfoRes::new).toList();
        apartmentBaseInfoResList.forEach(apartmentBaseInfoRes -> {
            GeocodingRes geocodingRes = null;
            try {
                String address = Util.removeSuffix(apartmentBaseInfoRes.getLegalDongAddress(), apartmentBaseInfoRes.getComplexName());
                geocodingRes = getGeocoding(address);
                apartmentBaseInfoRes.setGeoCodingRes(geocodingRes);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        return apartmentBaseInfoResList;
    }

    @Override
    public ApartmentDetailInfoRes getDetailInformation(ApartmentDetailInfoReq apartmentDetailInfoReq) throws IOException {
        String complexCode = apartmentDetailInfoReq.getComplexCode();
        List<ApartmentDetailInformation> apartmentDetailInformations = apartmentDetailRepository.findByComplexCode(complexCode);
        if (!apartmentDetailInformations.isEmpty()) {
            return new ApartmentDetailInfoRes(apartmentDetailInformations.get(0));
        }

        String url = externalDataAptInfoDetailEndpoint +
                "?serviceKey=" + externalDataKey +
                "&kaptCode=" + complexCode;

        String response = Fetch.fetchDataFromAPI(url);
        List<Element> items = Parsing.parseXmlResponse(response);
        apartmentDetailInformations = items.stream().map(ApartmentDetailInformation::new).toList();
        apartmentDetailRepository.saveAll(apartmentDetailInformations);

        return new ApartmentDetailInfoRes(apartmentDetailInformations.get(0));
    }

    private List<TradeRes> fetchTradeData(String category, String legalCode, String endPoint, String searchDate, String si) throws IOException {
        String url = endPoint +
                "?serviceKey=" + externalDataKey +
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

    private List<ApartmentBaseInformation> getApartmentBaseInformations(List<String> complexCodes) throws IOException {
        return complexCodes.stream()
                .flatMap(complexCode -> {
                    try {
                        return getApartmentBaseInformation(complexCode).stream();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toList());
    }

    private List<ApartmentBaseInformation> getApartmentBaseInformation(String complexCode) throws IOException {
        List<ApartmentBaseInformation> apartmentBaseInformations = apartmentBaseRepository.findByComplexCode(complexCode);
        if (!apartmentBaseInformations.isEmpty()) {
            return apartmentBaseInformations;
        }

        String url = externalDataAptInfoBaseEndpoint +
                "?serviceKey=" + externalDataKey +
                "&kaptCode=" + complexCode;

        String response = Fetch.fetchDataFromAPI(url);
        List<Element> items = Parsing.parseXmlResponse(response);
        apartmentBaseInformations = items.stream().map(ApartmentBaseInformation::new).toList();
        apartmentBaseRepository.saveAll(apartmentBaseInformations);

        return apartmentBaseInformations;
    }

    private List<ApartmentDetailInformation> getApartmentDetailInformations(List<String> complexCodes) throws IOException {
        return complexCodes.stream()
                .flatMap(complexCode -> {
                    try {
                        return getApartmentDetailInformation(complexCode).stream();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toList());
    }

    private List<ApartmentDetailInformation> getApartmentDetailInformation(String complexCode) throws IOException {
        List<ApartmentDetailInformation> apartmentDetailInformations = apartmentDetailRepository.findByComplexCode(complexCode);
        if (!apartmentDetailInformations.isEmpty()) {
            return apartmentDetailInformations;
        }

        String url = externalDataAptInfoDetailEndpoint +
                "?serviceKey=" + externalDataKey +
                "&kaptCode=" + complexCode;

        String response = Fetch.fetchDataFromAPI(url);
        List<Element> items = Parsing.parseXmlResponse(response);
        apartmentDetailInformations = items.stream().map(ApartmentDetailInformation::new).toList();
        apartmentDetailRepository.saveAll(apartmentDetailInformations);

        return apartmentDetailInformations;
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

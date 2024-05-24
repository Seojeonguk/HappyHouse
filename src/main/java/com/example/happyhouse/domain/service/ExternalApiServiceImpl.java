package com.example.happyhouse.domain.service;

import com.example.happyhouse.domain.dto.request.TradeReq;
import com.example.happyhouse.domain.dto.response.GeocodingRes;
import com.example.happyhouse.domain.dto.response.TradeRes;
import com.example.happyhouse.util.Util;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
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

    @Override
    public GeocodingRes getGeocoding(String address) throws IOException {
        String url = externalGoogleGeocodingEndpoint + "/json" +
                "?address=" + address.replaceAll(" ", "+") +
                "&key=" + externalGoogleKey +
                "&language=ko";

        String response = fetchDataFromAPI(url);

        JSONObject jsonResponse = new JSONObject(response);
        JSONObject firstResult = jsonResponse.getJSONArray("results").getJSONObject(0);

        JSONObject location = firstResult
                .getJSONObject("geometry")
                .getJSONObject("location");

        String formattedAddress = firstResult.getString("formatted_address");
        String lat = String.valueOf(location.getBigDecimal("lat"));
        String lng = String.valueOf(location.getBigDecimal("lng"));

        return new GeocodingRes(lat, lng, formattedAddress);
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

    private String fetchDataFromAPI(String path) throws IOException {
        URL url = new URL(path);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");

        log.info(path);

        BufferedReader rd;
        if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } else {
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }

        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }
        rd.close();
        conn.disconnect();

        return sb.toString();
    }

    private List<TradeRes> fetchTradeData(String category, String legalCode, String endPoint, String searchDate, String si) throws IOException {
        String url = endPoint +
                "?serviceKey=" + externalDataKey +
                "&LAWD_CD=" + legalCode.substring(0, 5) +
                "&DEAL_YMD=" + searchDate;

        String response = fetchDataFromAPI(url);
        List<Element> items = parseXmlResponse(response);
        List<TradeRes> trades = items.stream().map(item -> new TradeRes(item, category)).toList();
        for (TradeRes trade : trades) {
            String address = si + " " + trade.getLegalDong() + " " + trade.getName();
            GeocodingRes geocodingRes = getGeocoding(address);

            trade.setGeoCodingRes(geocodingRes);
        }

        return trades;
    }

    private List<Element> parseXmlResponse(String xmlResponse) {
        List<Element> items = new ArrayList<>();

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new ByteArrayInputStream(xmlResponse.getBytes()));

            doc.getDocumentElement().normalize();
            NodeList nList = doc.getElementsByTagName("item");

            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    items.add(eElement);
                }
            }
        } catch (Exception e) {
            log.warn(e.getMessage());
        }

        return items;
    }
}

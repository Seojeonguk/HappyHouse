package com.example.happyhouse.domain.service;

import com.example.happyhouse.domain.dto.response.GeocodingRes;
import com.example.happyhouse.domain.dto.response.TradeRes;
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
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
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
        StringBuilder urlBuilder = new StringBuilder(externalGoogleGeocodingEndpoint);
        urlBuilder.append("/json");
        urlBuilder.append("?" + URLEncoder.encode("address", StandardCharsets.UTF_8) + "=" + address.replaceAll(" ", "+"));
        urlBuilder.append("&" + URLEncoder.encode("key", StandardCharsets.UTF_8) + "=" + URLDecoder.decode(externalGoogleKey, StandardCharsets.UTF_8));

        log.info(urlBuilder.toString());

        URL url = new URL(urlBuilder.toString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");

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

        JSONObject response = new JSONObject(sb.toString());
        JSONObject location = response
                .getJSONArray("results")
                .getJSONObject(0)
                .getJSONObject("geometry")
                .getJSONObject("location");

        String lat = String.valueOf(location.getBigDecimal("lat"));
        String lng = String.valueOf(location.getBigDecimal("lng"));

        return new GeocodingRes(lat, lng);
    }

    @Override
    public String getGoogleApiKey() {
        return externalGoogleKey;
    }

    @Override
    public List<TradeRes> getTrade(String category, String legalCode) throws IOException {
        List<TradeRes> tradeList = new ArrayList<>();
        if ("전체".equals(category)) {
            List<TradeRes> houseTrades = apiCall("연립다세대", legalCode, externalDataHouseTradeEndpoint);
            List<TradeRes> apartTrades = apiCall("아파트", legalCode, externalDataAptTradeEndpoint);

            tradeList.addAll(houseTrades);
            tradeList.addAll(apartTrades);
        } else if ("아파트".equals(category)) {
            List<TradeRes> apartTrades = apiCall("아파트", legalCode, externalDataAptTradeEndpoint);

            tradeList.addAll(apartTrades);
        } else if ("연립다세대".equals(category)) {
            List<TradeRes> houseTrades = apiCall("연립다세대", legalCode, externalDataHouseTradeEndpoint);

            tradeList.addAll(houseTrades);
        } else {
            log.warn("Not support category: {}", category);
        }

        return tradeList;
    }

    private List<TradeRes> apiCall(String category, String legalCode, String endPoint) throws IOException {
        StringBuilder urlBuilder = new StringBuilder(endPoint);
        urlBuilder.append("?").append(URLEncoder.encode("serviceKey", StandardCharsets.UTF_8)).append("=").append(externalDataKey);
        urlBuilder.append("&").append(URLEncoder.encode("LAWD_CD", StandardCharsets.UTF_8)).append("=").append(URLEncoder.encode(legalCode.substring(0, 5), StandardCharsets.UTF_8));
        urlBuilder.append("&").append(URLEncoder.encode("DEAL_YMD", StandardCharsets.UTF_8)).append("=").append(URLEncoder.encode("201512", StandardCharsets.UTF_8));

        URL url = new URL(urlBuilder.toString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");

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

        return parseXmlResponse(sb.toString(), category);
    }

    private List<TradeRes> parseXmlResponse(String xmlResponse, String category) {
        List<TradeRes> tradeResList = new ArrayList<>();

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

                    String dealAmount = eElement.getElementsByTagName("거래금액").item(0).getTextContent().trim();
                    int constructionYear = Integer.parseInt(eElement.getElementsByTagName("건축년도").item(0).getTextContent());
                    int dealYear = Integer.parseInt(eElement.getElementsByTagName("년").item(0).getTextContent());
                    String name = eElement.getElementsByTagName(category).item(0).getTextContent();
                    int dealMonth = Integer.parseInt(eElement.getElementsByTagName("월").item(0).getTextContent());
                    int dealDay = Integer.parseInt(eElement.getElementsByTagName("일").item(0).getTextContent());
                    double exclusiveArea = Double.parseDouble(eElement.getElementsByTagName("전용면적").item(0).getTextContent());
                    String lotNumberAddress = eElement.getElementsByTagName("지번").item(0).getTextContent();
                    int floor = Integer.parseInt(eElement.getElementsByTagName("층").item(0).getTextContent());

                    TradeRes res = new TradeRes(dealAmount, constructionYear, dealYear, dealMonth, dealDay, name, exclusiveArea, lotNumberAddress, floor);
                    tradeResList.add(res);
                }
            }
        } catch (Exception e) {
            log.warn(e.getMessage());
        }

        return tradeResList;
    }
}

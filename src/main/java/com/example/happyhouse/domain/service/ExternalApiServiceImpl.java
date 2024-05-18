package com.example.happyhouse.domain.service;

import com.example.happyhouse.domain.dto.response.ApartTradeRes;
import com.example.happyhouse.domain.dto.response.GeocodingRes;
import com.example.happyhouse.domain.dto.response.HouseTradeRes;
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
    public List<ApartTradeRes> getApartTrade() throws IOException {
        StringBuilder urlBuilder = new StringBuilder(externalDataAptTradeEndpoint);
        urlBuilder.append("?" + URLEncoder.encode("serviceKey", "UTF-8") + "=" + externalDataKey); /*Service Key*/
        urlBuilder.append("&" + URLEncoder.encode("LAWD_CD", "UTF-8") + "=" + URLEncoder.encode("11110", "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("DEAL_YMD", "UTF-8") + "=" + URLEncoder.encode("201512", "UTF-8"));

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

        return parseApartXmlResponse(sb.toString());
    }

    @Override
    public List<HouseTradeRes> getHouseTrade() throws IOException {
        StringBuilder urlBuilder = new StringBuilder(externalDataHouseTradeEndpoint);
        urlBuilder.append("?" + URLEncoder.encode("serviceKey","UTF-8") + "=" + externalDataKey );
        urlBuilder.append("&" + URLEncoder.encode("LAWD_CD","UTF-8") + "=" + URLEncoder.encode("11110", "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("DEAL_YMD","UTF-8") + "=" + URLEncoder.encode("201512", "UTF-8"));

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

        return parseHouseXmlResponse(sb.toString());
    }

    private List<ApartTradeRes> parseApartXmlResponse(String xmlResponse) {
        List<ApartTradeRes> apartTradeList = new ArrayList<>();

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
                    String dealType = eElement.getElementsByTagName("거래유형").item(0).getTextContent();
                    int constructionYear = Integer.parseInt(eElement.getElementsByTagName("건축년도").item(0).getTextContent());
                    int dealYear = Integer.parseInt(eElement.getElementsByTagName("년").item(0).getTextContent());
                    String roadName = eElement.getElementsByTagName("도로명").item(0).getTextContent();
                    String registrationDate = eElement.getElementsByTagName("등기일자").item(0).getTextContent();
                    String seller = eElement.getElementsByTagName("매도자").item(0).getTextContent();
                    String buyer = eElement.getElementsByTagName("매수자").item(0).getTextContent();
                    String legalDong = eElement.getElementsByTagName("법정동").item(0).getTextContent();
                    String apartmentName = eElement.getElementsByTagName("아파트").item(0).getTextContent();
                    int dealMonth = Integer.parseInt(eElement.getElementsByTagName("월").item(0).getTextContent());
                    int dealDay = Integer.parseInt(eElement.getElementsByTagName("일").item(0).getTextContent());
                    double exclusiveArea = Double.parseDouble(eElement.getElementsByTagName("전용면적").item(0).getTextContent());
                    String agentLocation = eElement.getElementsByTagName("중개사소재지").item(0).getTextContent();
                    String jibun = eElement.getElementsByTagName("지번").item(0).getTextContent();
                    int regionCode = Integer.parseInt(eElement.getElementsByTagName("지역코드").item(0).getTextContent());
                    int floor = Integer.parseInt(eElement.getElementsByTagName("층").item(0).getTextContent());
                    String releaseReasonDate = eElement.getElementsByTagName("해제사유발생일").item(0).getTextContent();
                    String releaseStatus = eElement.getElementsByTagName("해제여부").item(0).getTextContent();

                    ApartTradeRes res = new ApartTradeRes(dealAmount,dealType,constructionYear,dealYear,roadName,registrationDate,seller,buyer,legalDong,apartmentName,dealMonth,dealDay,exclusiveArea,agentLocation,jibun,regionCode,floor,releaseReasonDate,releaseStatus);
                    apartTradeList.add(res);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return apartTradeList;
    }

    private List<HouseTradeRes> parseHouseXmlResponse(String xmlResponse) {
        List<HouseTradeRes> houseTradeList = new ArrayList<>();

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
                    String dealType = eElement.getElementsByTagName("거래유형").item(0).getTextContent();
                    int constructionYear = Integer.parseInt(eElement.getElementsByTagName("건축년도").item(0).getTextContent());
                    int dealYear = Integer.parseInt(eElement.getElementsByTagName("년").item(0).getTextContent());
                    double landArea = Double.parseDouble(eElement.getElementsByTagName("대지권면적").item(0).getTextContent());
                    String registrationDate = eElement.getElementsByTagName("등기일자").item(0).getTextContent();
                    String seller = eElement.getElementsByTagName("매도자").item(0).getTextContent();
                    String buyer = eElement.getElementsByTagName("매수자").item(0).getTextContent();
                    String legalDong = eElement.getElementsByTagName("법정동").item(0).getTextContent();
                    String apartmentName = eElement.getElementsByTagName("연립다세대").item(0).getTextContent();
                    int dealMonth = Integer.parseInt(eElement.getElementsByTagName("월").item(0).getTextContent());
                    int dealDay = Integer.parseInt(eElement.getElementsByTagName("일").item(0).getTextContent());
                    double exclusiveArea = Double.parseDouble(eElement.getElementsByTagName("전용면적").item(0).getTextContent());
                    String agentLocation = eElement.getElementsByTagName("중개사소재지").item(0).getTextContent();
                    String jibun = eElement.getElementsByTagName("지번").item(0).getTextContent();
                    int regionCode = Integer.parseInt(eElement.getElementsByTagName("지역코드").item(0).getTextContent());
                    int floor = Integer.parseInt(eElement.getElementsByTagName("층").item(0).getTextContent());
                    String releaseReasonDate = eElement.getElementsByTagName("해제사유발생일").item(0).getTextContent();
                    String releaseStatus = eElement.getElementsByTagName("해제여부").item(0).getTextContent();

                    HouseTradeRes res = new HouseTradeRes(dealAmount,dealType,constructionYear,dealYear,landArea,registrationDate,seller,buyer,legalDong,apartmentName,dealMonth,dealDay,exclusiveArea,agentLocation,jibun,regionCode,floor,releaseReasonDate,releaseStatus);
                    houseTradeList.add(res);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return houseTradeList;
    }
}

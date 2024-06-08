package com.example.happyhouse.util;

import org.w3c.dom.Element;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Fetch {

    public static String fetchDataFromAPI(String path) throws IOException {
        URL url = new URL(path);
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

        return sb.toString();
    }

    public static <T> List<T> fetchApartmentInformation(String complexCode,
                                                        Function<String, List<T>> fetchFromRepo,
                                                        String endpoint,
                                                        String serviceKey,
                                                        Function<Element, T> mapToEntity,
                                                        Function<List<T>, List<T>> saveToRepo) throws IOException {
        List<T> information = fetchFromRepo.apply(complexCode);
        if (!information.isEmpty()) {
            return information;
        }

        String url = endpoint + "?serviceKey=" + serviceKey + "&kaptCode=" + complexCode;
        String response = Fetch.fetchDataFromAPI(url);
        List<Element> items = Parsing.parseXmlResponse(response);
        information = items.stream().map(mapToEntity).collect(Collectors.toList());
        saveToRepo.apply(information);

        return information;
    }
}

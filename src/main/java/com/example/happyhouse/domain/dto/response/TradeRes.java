package com.example.happyhouse.domain.dto.response;

import lombok.Getter;
import org.w3c.dom.Element;

@Getter
public class TradeRes extends GeocodingRes {
    private String dealAmount; // 거래금액
    private int constructionYear; // 건축년도
    private int dealYear; // 년
    private int dealMonth; // 월
    private int dealDay; // 일
    private String name; // 연립다세대
    private double exclusiveArea; // 전용면적
    private String lotNumberAddress; // 지번
    private int floor; // 층
    private boolean isApartmentTrading;
    private String legalDong; // 법정동

    public TradeRes(Element element, String category) {
        super();
        this.dealAmount = element.getElementsByTagName("거래금액").item(0).getTextContent().trim();
        this.constructionYear = Integer.parseInt(element.getElementsByTagName("건축년도").item(0).getTextContent());
        this.dealYear = Integer.parseInt(element.getElementsByTagName("년").item(0).getTextContent());
        this.name = element.getElementsByTagName(category).item(0).getTextContent();
        this.dealMonth = Integer.parseInt(element.getElementsByTagName("월").item(0).getTextContent());
        this.dealDay = Integer.parseInt(element.getElementsByTagName("일").item(0).getTextContent());
        this.exclusiveArea = Double.parseDouble(element.getElementsByTagName("전용면적").item(0).getTextContent());
        this.lotNumberAddress = element.getElementsByTagName("지번").item(0).getTextContent();
        this.floor = Integer.parseInt(element.getElementsByTagName("층").item(0).getTextContent());
        this.isApartmentTrading = "아파트".equals(category);
        this.legalDong = element.getElementsByTagName("법정동").item(0).getTextContent().trim();
    }

    public void setGeoCodingRes(GeocodingRes geoCodingRes) {
        super.setGeoCodingRes(geoCodingRes);
    }

    /* unused items in house trade
    private double landArea; // 대지권면적

    */

    /* unused items in apart trade
    private String roadName; // 도로명

    */

    /* unused items
    private String dealType; // 거래유형
    private String registrationDate; // 등기일자
    private String seller; // 매도자
    private String buyer; // 매수자
    private String agentLocation; // 중개사소재지
    private String releaseReasonDate; // 해제사유발생일
    private String releaseStatus; // 해제여부
    private int regionCode; // 지역코드


     */
}

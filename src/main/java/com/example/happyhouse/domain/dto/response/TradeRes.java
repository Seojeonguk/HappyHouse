package com.example.happyhouse.domain.dto.response;

import lombok.Getter;

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

    public TradeRes(String dealAmount, int constructionYear, int dealYear, int dealMonth, int dealDay, String name, double exclusiveArea, String lotNumberAddress, int floor, boolean isApartmentTrading, GeocodingRes geocodingRes) {
        super(geocodingRes);
        this.dealAmount = dealAmount;
        this.constructionYear = constructionYear;
        this.dealYear = dealYear;
        this.dealMonth = dealMonth;
        this.dealDay = dealDay;
        this.name = name;
        this.exclusiveArea = exclusiveArea;
        this.lotNumberAddress = lotNumberAddress;
        this.floor = floor;
        this.isApartmentTrading = isApartmentTrading;
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
    private String legalDong; // 법정동

     */
}

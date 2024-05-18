package com.example.happyhouse.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ApartTradeRes {
    private String dealAmount; // 거래금액
    private String dealType; // 거래유형
    private int constructionYear; // 건축년도
    private int dealYear; // 년
    private String roadName; // 도로명
    private String registrationDate; // 등기일자
    private String seller; // 매도자
    private String buyer; // 매수자
    private String legalDong; // 법정동
    private String apartmentName; // 아파트
    private int dealMonth; // 월
    private int dealDay; // 일
    private double exclusiveArea; // 전용면적
    private String agentLocation; // 중개사소재지
    private String jibun; // 지번
    private int regionCode; // 지역코드
    private int floor; // 층
    private String releaseReasonDate; // 해제사유발생일
    private String releaseStatus; // 해제여부
}

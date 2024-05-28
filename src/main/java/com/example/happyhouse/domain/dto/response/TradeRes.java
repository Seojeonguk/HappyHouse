package com.example.happyhouse.domain.dto.response;

import com.example.happyhouse.util.Parsing;
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
        this.dealAmount = Parsing.getElementTextContent(element, "거래금액");
        this.constructionYear = Integer.parseInt(Parsing.getElementTextContent(element, "건축년도"));
        this.dealYear = Integer.parseInt(Parsing.getElementTextContent(element, "년"));
        this.name = Parsing.getElementTextContent(element, category);
        this.dealMonth = Integer.parseInt(Parsing.getElementTextContent(element, "월"));
        this.dealDay = Integer.parseInt(Parsing.getElementTextContent(element, "일"));
        this.exclusiveArea = Double.parseDouble(Parsing.getElementTextContent(element, "전용면적"));
        this.lotNumberAddress = Parsing.getElementTextContent(element, "지번");
        this.floor = Integer.parseInt(Parsing.getElementTextContent(element, "층"));
        this.isApartmentTrading = "아파트".equals(category);
        this.legalDong = Parsing.getElementTextContent(element, "법정동");
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

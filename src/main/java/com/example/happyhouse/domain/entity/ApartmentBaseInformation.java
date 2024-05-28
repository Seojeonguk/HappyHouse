package com.example.happyhouse.domain.entity;

import com.example.happyhouse.util.Parsing;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.w3c.dom.Element;

@Entity
@NoArgsConstructor
@Table(name = "apt_base_info")
public class ApartmentBaseInformation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "apt_base_id")
    private long id;

    @Getter
    private String complexCode; // kaptCode 단지코드

    @Getter
    private String complexName; // kaptName 단지명

    @Getter
    private String legalDongAddress; // kaptAddr 법정동주소

    @Getter
    private String saleType; // codeSaleNm 분양형태

    @Getter
    private String heatingType; // codeHeatNm 난방방식

    @Getter
    private Double totalArea; // kaptTarea 연면적

    @Getter
    private Integer buildingCount; // kaptDongCnt 동수

    @Getter
    private Integer householdCount; // kaptdaCnt 세대수

    @Getter
    private String constructor; // kaptBcompany 시공사

    @Getter
    private String developer; // kaptAcompany 시행사

    @Getter
    private String managementOfficeContact; // kaptTel 관리사무소연락처

    @Getter
    private String managementOfficeFax; // kaptFax 관리사무소팩스

    @Getter
    private String website; // kaptUrl 홈페이지주소

    @Getter
    private String complexCategory; // codeAptNm 단지분류

    @Getter
    private String roadNameAddress; // doroJuso 도로명주소

    @Getter
    private Integer unitCount; // hoCnt 호수

    @Getter
    private String managementType; // codeMgrNm 관리방식

    private String corridorType; // codeHallNm 복도유형

    @Getter
    private String approvalDate; // kaptUseDate 사용승인일

    @Getter
    private String managementFeeArea; // kaptMarea 관리비부과면적

    private String householdStatusBelow60; // kaptMparea_60 전용면적별 세대현황 60㎡ 이하
    private String householdStatus60To85; // kaptMparea_85 전용면적별 세대현황 60㎡ ~ 85㎡ 이하
    private String householdStatus85To135; // kaptMparea_135 전용면적별 세대현황 85㎡ ~ 135㎡ 이하
    private String householdStatusAbove135; // kaptMparea_136 전용면적별 세대현황 135㎡ 초과

    @Getter
    private Double totalExclusiveArea; // privArea 단지 전용면적합

    private String legalDongCode; // bjdCode 법정동코드

    @OneToOne(mappedBy = "apartmentBaseInformation")
    Apartment apartment;

    public ApartmentBaseInformation(Element e) {
        this.complexCode = Parsing.getElementTextContent(e, "kaptCode");
        this.complexName = Parsing.getElementTextContent(e, "kaptName");
        this.legalDongAddress = Parsing.getElementTextContent(e, "kaptAddr");
        this.saleType = Parsing.getElementTextContent(e, "codeSaleNm");
        this.heatingType = Parsing.getElementTextContent(e, "codeHeatNm");
        this.totalArea = Parsing.getElementDoubleContent(e, "kaptTarea");
        this.buildingCount = Parsing.getElementIntContent(e, "kaptDongCnt");
        this.householdCount = Parsing.getElementIntContent(e, "kaptdaCnt");
        this.constructor = Parsing.getElementTextContent(e, "kaptBcompany");
        this.developer = Parsing.getElementTextContent(e, "kaptAcompany");
        this.managementOfficeContact = Parsing.getElementTextContent(e, "kaptTel");
        this.managementOfficeFax = Parsing.getElementTextContent(e, "kaptFax");
        this.website = Parsing.getElementTextContent(e, "kaptUrl");
        this.complexCategory = Parsing.getElementTextContent(e, "codeAptNm");
        this.roadNameAddress = Parsing.getElementTextContent(e, "doroJuso");
        this.unitCount = Parsing.getElementIntContent(e, "hoCnt");
        this.managementType = Parsing.getElementTextContent(e, "codeMgrNm");
        this.corridorType = Parsing.getElementTextContent(e, "codeHallNm");
        this.approvalDate = Parsing.getElementTextContent(e, "kaptUsedate");
        this.managementFeeArea = Parsing.getElementTextContent(e, "kaptMarea");
        this.householdStatusBelow60 = Parsing.getElementTextContent(e, "kaptMparea_60");
        this.householdStatus60To85 = Parsing.getElementTextContent(e, "kaptMparea_85");
        this.householdStatus85To135 = Parsing.getElementTextContent(e, "kaptMparea_135");
        this.householdStatusAbove135 = Parsing.getElementTextContent(e, "kaptMparea_136");
        this.totalExclusiveArea = Parsing.getElementDoubleContent(e, "privArea");
        this.legalDongCode = Parsing.getElementTextContent(e, "bjdCode");
    }
}

package com.example.happyhouse.domain.dto.response;

import com.example.happyhouse.domain.entity.ApartmentBaseInformation;
import lombok.Getter;

@Getter
public class ApartmentBaseInfoRes extends GeocodingRes {
    private String complexCode; // kaptCode 단지코드
    private String complexName; // kaptName 단지명
    private String legalDongAddress; // kaptAddr 법정동주소
    private String saleType; // codeSaleNm 분양형태
    private String heatingType; // codeHeatNm 난방방식
    private Double totalArea; // kaptTarea 연면적
    private Integer buildingCount; // kaptDongCnt 동수
    private Integer householdCount; // kaptdaCnt 세대수
    private String constructor; // kaptBcompany 시공사
    private String developer; // kaptAcompany 시행사
    private String managementOfficeContact; // kaptTel 관리사무소연락처
    private String managementOfficeFax; // kaptFax 관리사무소팩스
    private String website; // kaptUrl 홈페이지주소
    private String complexCategory; // codeAptNm 단지분류
    private String roadNameAddress; // doroJuso 도로명주소
    private Integer unitCount; // hoCnt 호수
    private String managementType; // codeMgrNm 관리방식
    private String approvalDate; // kaptUseDate 사용승인일
    private String managementFeeArea; // kaptMarea 관리비부과면적
    private Double totalExclusiveArea; // privArea 단지 전용면적합

    public ApartmentBaseInfoRes(ApartmentBaseInformation apartmentBaseInformation) {
        this.complexCode = apartmentBaseInformation.getComplexCode();
        this.complexName = apartmentBaseInformation.getComplexName();
        this.legalDongAddress = apartmentBaseInformation.getLegalDongAddress();
        this.saleType = apartmentBaseInformation.getSaleType();
        this.heatingType = apartmentBaseInformation.getHeatingType();
        this.totalArea = apartmentBaseInformation.getTotalArea();
        this.buildingCount = apartmentBaseInformation.getBuildingCount();
        this.householdCount = apartmentBaseInformation.getHouseholdCount();
        this.constructor = apartmentBaseInformation.getConstructor();
        this.developer = apartmentBaseInformation.getDeveloper();
        this.managementOfficeContact = apartmentBaseInformation.getManagementOfficeContact();
        this.managementOfficeFax = apartmentBaseInformation.getManagementOfficeFax();
        this.website = apartmentBaseInformation.getWebsite();
        this.complexCategory = apartmentBaseInformation.getComplexCategory();
        this.roadNameAddress = apartmentBaseInformation.getRoadNameAddress();
        this.unitCount = apartmentBaseInformation.getUnitCount();
        this.managementType = apartmentBaseInformation.getManagementType();
        this.approvalDate = apartmentBaseInformation.getApprovalDate();
        this.managementFeeArea = apartmentBaseInformation.getManagementFeeArea();
        this.totalExclusiveArea = apartmentBaseInformation.getTotalExclusiveArea();
    }

    public void setGeoCodingRes(GeocodingRes geoCodingRes) {
        super.setGeoCodingRes(geoCodingRes);
    }
}

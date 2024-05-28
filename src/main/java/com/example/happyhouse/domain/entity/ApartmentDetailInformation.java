package com.example.happyhouse.domain.entity;

import com.example.happyhouse.util.Parsing;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.w3c.dom.Element;

@Entity
@NoArgsConstructor
@Table(name = "apt_detail_info")
public class ApartmentDetailInformation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "apt_detail_id")
    private Long id;

    private String complexCode; // kaptCode 단지코드
    @Getter
    private String complexName; // kaptName 단지명
    private String generalManagementType; // codeMgr 일반관리방식
    private Double generalManagementPersonnel; // kaptMgrCnt 일반관리인원
    private String generalManagementContractCompany; // kaptCcompany 일반관리 계약업체
    private String securityManagementType; // codeSec 경비관리방식
    private Integer securityManagementPersonnel; // kaptdScnt 경비관리인원
    private String securityManagementContractCompany; // kaptdSecCom 경비관리 계약업체
    private String cleaningManagementType; // codeClean 청소관리방식
    private Integer cleaningManagementPersonnel; // kaptdClcnt 청소관리인원
    private String foodWasteDisposalMethod; // codeGarbage 음식물처리방법
    private String disinfectionManagementType; // codeDisinf 소독관리방식
    private Integer disinfectionAnnualFrequency; // kaptdDcnt 소독관리 연간 소독횟수
    private String disinfectionMethod; // disposalType 소독방법
    private String buildingStructure; // codeStr 건물구조
    private Integer waterSupplyCapacity; // kaptdEcapa 수전용량
    private String householdElectricityContractType; // codeEcon 세대전기계약방식
    private String electricalSafetyManagerAppointment; // codeEmgr 전기안전관리자법정선임여부
    private String fireReceiverType; // codeFalarm 화재수신반방식
    private String waterSupplyMethod; // codeWsupply 급수방식
    private String elevatorManagementType; // codeElev 승강기관리형태
    private Integer numberOfElevators; // kaptdEcnt 승강기대수
    private Integer groundParkingSpaces; // kaptdPcnt 주차대수(지상)
    private Integer undergroundParkingSpaces; // kaptdPcntu 주차대수(지하)
    private String parkingControlAndHomeNetwork; // codeNet 주차관제.홈네트워크
    private Integer numberOfCCTVs; // kaptdCccnt CCTV대수
    private String auxiliaryFacilities; // welfareFacility 부대.복리시설
    private String busStopDistance; // kaptdWtimebus 버스정류장 거리
    private String subwayLine; // subwayLine 지하철호선
    private String subwayStationName; // subwayStation 지하철역명
    private String subwayStationDistance; // kaptdWtimesub 지하철역 거리
    private String convenienceFacilities; // convenientFacility 편의시설
    private String educationalFacilities; // educationFacility 교육시설

    @OneToOne(mappedBy = "apartmentDetailInformation")
    Apartment apartment;

    public ApartmentDetailInformation(Element e) {
        this.complexCode = Parsing.getElementTextContent(e, "kaptCode");
        this.complexName = Parsing.getElementTextContent(e, "kaptName");
        this.generalManagementType = Parsing.getElementTextContent(e, "codeMgr");
        this.generalManagementPersonnel = Parsing.getElementDoubleContent(e, "kaptMgrCnt");
        this.generalManagementContractCompany = Parsing.getElementTextContent(e, "kaptCcompany");
        this.securityManagementType = Parsing.getElementTextContent(e, "codeSec");
        this.securityManagementPersonnel = Parsing.getElementIntContent(e, "kaptdScnt");
        this.securityManagementContractCompany = Parsing.getElementTextContent(e, "kaptdSecCom");
        this.cleaningManagementType = Parsing.getElementTextContent(e, "codeClean");
        this.cleaningManagementPersonnel = Parsing.getElementIntContent(e, "kaptdClcnt");
        this.foodWasteDisposalMethod = Parsing.getElementTextContent(e, "codeGarbage");
        this.disinfectionManagementType = Parsing.getElementTextContent(e, "codeDisinf");
        this.disinfectionAnnualFrequency = Parsing.getElementIntContent(e, "kaptdDcnt");
        this.disinfectionMethod = Parsing.getElementTextContent(e, "disposalType");
        this.buildingStructure = Parsing.getElementTextContent(e, "codeStr");
        this.waterSupplyCapacity = Parsing.getElementIntContent(e, "kaptdEcapa");
        this.householdElectricityContractType = Parsing.getElementTextContent(e, "codeEcon");
        this.electricalSafetyManagerAppointment = Parsing.getElementTextContent(e, "codeEmgr");
        this.fireReceiverType = Parsing.getElementTextContent(e, "codeFalarm");
        this.waterSupplyMethod = Parsing.getElementTextContent(e, "codeWsupply");
        this.elevatorManagementType = Parsing.getElementTextContent(e, "codeElev");
        this.numberOfElevators = Parsing.getElementIntContent(e, "kaptEcnt");
        this.groundParkingSpaces = Parsing.getElementIntContent(e, "kaptdPcnt");
        this.undergroundParkingSpaces = Parsing.getElementIntContent(e, "kaptdPcntu");
        this.parkingControlAndHomeNetwork = Parsing.getElementTextContent(e, "codeNet");
        this.numberOfCCTVs = Parsing.getElementIntContent(e, "kaptdCccnt");
        this.auxiliaryFacilities = Parsing.getElementTextContent(e, "welfareFacility");
        this.busStopDistance = Parsing.getElementTextContent(e, "kaptdWtimebus");
        this.subwayLine = Parsing.getElementTextContent(e, "subwayLine");
        this.subwayStationName = Parsing.getElementTextContent(e, "subwayStation");
        this.subwayStationDistance = Parsing.getElementTextContent(e, "kaptdWtimesub");
        this.convenienceFacilities = Parsing.getElementTextContent(e, "convenientFacility");
        this.educationalFacilities = Parsing.getElementTextContent(e, "educationFacility");
    }

}

package com.example.happyhouse.domain.dto.response;

import com.example.happyhouse.domain.entity.ApartmentDetailInformation;
import lombok.Getter;

@Getter
public class ApartmentDetailInfoRes {
    private String complexName;
    private Integer numberOfElevators;
    private Integer groundParkingSpaces;
    private Integer undergroundParkingSpaces;
    private Integer numberOfCCTVs;
    private String auxiliaryFacilities;
    private String busStopDistance;
    private String subwayLine;
    private String subwayStationName;
    private String subwayStationDistance;
    private String convenienceFacilities;
    private String educationalFacilities;

    public ApartmentDetailInfoRes(ApartmentDetailInformation apartmentDetailInformation) {
        this.complexName = apartmentDetailInformation.getComplexName();
        this.numberOfElevators = apartmentDetailInformation.getNumberOfElevators();
        this.groundParkingSpaces = apartmentDetailInformation.getGroundParkingSpaces();
        this.undergroundParkingSpaces = apartmentDetailInformation.getUndergroundParkingSpaces();
        this.numberOfCCTVs = apartmentDetailInformation.getNumberOfCCTVs();
        this.auxiliaryFacilities = apartmentDetailInformation.getAuxiliaryFacilities();
        this.busStopDistance = apartmentDetailInformation.getBusStopDistance();
        this.subwayLine = apartmentDetailInformation.getSubwayLine();
        this.subwayStationName = apartmentDetailInformation.getSubwayStationName();
        this.subwayStationDistance = apartmentDetailInformation.getSubwayStationDistance();
        this.convenienceFacilities = apartmentDetailInformation.getConvenienceFacilities();
        this.educationalFacilities = apartmentDetailInformation.getEducationalFacilities();
    }
}

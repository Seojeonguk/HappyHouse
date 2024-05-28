package com.example.happyhouse.domain.entity;

import com.example.happyhouse.util.Parsing;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.w3c.dom.Element;

@Entity
@NoArgsConstructor
@Table(name = "apt")
public class Apartment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "apt_id")
    private long id;

    @Getter
    String complexCode; // kaptCode 단지코드
    String complexName; // kaptName 단지명
    String si; // as1 시도
    String gu; // as2 시군구
    String dong; // as3 읍면동
    String ri; // as4 리
    String legalDongCode; // bjdCode 법정동코드

    @OneToOne
    @JoinColumn(name = "apt_base_id")
    ApartmentBaseInformation apartmentBaseInformation;

    @OneToOne
    @JoinColumn(name = "apt_detail_id")
    ApartmentDetailInformation apartmentDetailInformation;

    public Apartment(Element element) {
        this.complexCode = Parsing.getElementTextContent(element, "kaptCode");
        this.complexName = Parsing.getElementTextContent(element, "kaptName");
        this.si = Parsing.getElementTextContent(element, "as1");
        this.gu = Parsing.getElementTextContent(element, "as2");
        this.dong = Parsing.getElementTextContent(element, "as3");
        this.ri = Parsing.getElementTextContent(element, "as4");
        this.legalDongCode = Parsing.getElementTextContent(element, "bjdCode");
    }
}

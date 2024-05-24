package com.example.happyhouse.domain.entity;

import com.example.happyhouse.util.ParsingUtil;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.NoArgsConstructor;
import org.w3c.dom.Element;

@Entity
@NoArgsConstructor
public class Apartment {
    @Id
    String complexCode; // kaptCode 단지코드
    String complexName; // kaptName 단지명
    String si; // as1 시도
    String gu; // as2 시군구
    String dong; // as3 읍면동
    String ri; // as4 리
    String legalDongCode; // bjdCode 법정동코드

    public Apartment(Element element) {
        this.complexCode = ParsingUtil.getElementTextContent(element, "kaptCode");
        this.complexName = ParsingUtil.getElementTextContent(element, "kaptName");
        this.si = ParsingUtil.getElementTextContent(element, "as1");
        this.gu = ParsingUtil.getElementTextContent(element, "as2");
        this.dong = ParsingUtil.getElementTextContent(element, "as3");
        this.ri = ParsingUtil.getElementTextContent(element, "as4");
        this.legalDongCode = ParsingUtil.getElementTextContent(element, "bjdCode");
    }
}

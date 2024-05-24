package com.example.happyhouse.domain.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class TradeReq {
    String category;
    String legalCode;
    String year;
    String month;
    String si;

    public String getYearMonth() {
        return year + month;
    }
}

package com.example.happyhouse.domain.dto.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class TokenRes {
    String accessToken;
    String refreshToken;
    String grantType;
    Long accessTokenExpire;
}

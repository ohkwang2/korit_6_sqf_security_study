package com.study.SpringSecurityMybatis.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponseSigninDto {
    private String expireDate;
    private String accessToken;
}

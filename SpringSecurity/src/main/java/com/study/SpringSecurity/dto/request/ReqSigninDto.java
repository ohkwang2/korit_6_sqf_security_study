package com.study.SpringSecurity.dto.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class ReqSigninDto {
    @NotBlank(message = "사용자 이름을 입력하세요.") //빈값만 아니면 자동으로 날라가게끔, default 메세지는 "공백일 수 없습니다." 로 들어가 있음
    private String username;
    @NotBlank(message = "비밀번호를 입력하세요.")
    private String password;
}

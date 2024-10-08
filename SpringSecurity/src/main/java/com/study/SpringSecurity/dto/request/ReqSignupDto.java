package com.study.SpringSecurity.dto.request;

import com.study.SpringSecurity.domain.entity.User;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.validation.constraints.Pattern;

@Data
// signup(@Valid) <- 요기 valid 어노테이션이 있으면 바로 여기로 넘어와서 정규식 실행함
// 에러가 있으면 fieldErrors를 생성하고, BindingResult에 담아줌
public class ReqSignupDto {

    // 정규식은 문자열만 비교 가능 (숫자는 다르게 처리함)
    @Pattern(regexp = "^[a-z0-9]{6,}$", message = "사용자 이름은 6자 이상의 영소문자, 숫자 조합이어야 합니다.")
    private String username;
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[~!@#$%^&*?])[A-Za-z\\d~!@#$%^&*?]{8,16}$", message = "비밀번호는 8자 이상 16자 이하의 영대소문, 숫자, 특수문자(~!@#$%^&*?)를 포함하여야 합니다.")
    private String password;
    private String checkPassword;
    @Pattern(regexp = "^[가-힣]+$", message = "이름은 한글이어야 합니다.")
    private String name;

    public User toEntity(BCryptPasswordEncoder passwordEncoder) {   // IoC 컨테이너에서 passwordEncoder를 가져올 수 없기 때문에 Service에서 매개변수로 받아옴
        return User.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .name(name)
                .build();
    }
}

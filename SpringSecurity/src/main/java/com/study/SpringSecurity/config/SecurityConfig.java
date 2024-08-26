package com.study.SpringSecurity.config;

import com.study.SpringSecurity.security.filter.JwtAccessTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

// 초기 세팅값
// 우리가 만든 SecurityConfig를 적용시키겠다는 의미
@EnableWebSecurity
// Configuarion 어노테이션을 붙여야지만 IoC 컨테이너에 Bean으로 등록됨
// 이 어노테이션을 사용하면 Bean을 생성해서 IoC 컨테이너에 등록할 수 있음
@Configuration
// 추상클래스를 상속 받았지만, 추상 메소드가 없음
// 추상클래스를 상속 받았는데, 추상메소드가 있는지 없는지는 클래스에 빨간줄이 뜨는지 안 뜨는지 확인하면 됨
// 이런 경우 일반적으로 메소드가 비어있고, 상속받아서 재정의해서 사용할 수 있게 함. Override
// 상속받아 재정의할 메소드가 없는 경우 crtl + i 해도 아무 것도 안 나옴
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private JwtAccessTokenFilter jwtAccessTokenFilter;

    @Bean
    // 비밀번호 암호화를 하기 위한 Bean 객체 생성
    // 생성된 객체 하나가 passwordEncoder 메소드 이름으로 객체 등록됨
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    // ctrl + o 해서 오버로딩 할 때, 접근지정자(자물쇠 (접근지정자), 매개변수(Overload 유무) 확인 필요)
    // protected, default 모두 같은 패키지에서 접근(.으로 참조) 가능하지만, pretected의 경우 상속받을 경우 타 패키지에 있어도 접근 가능
    protected void configure(HttpSecurity http) throws Exception {
//        super.configure(http); // 부모의 configure 사용하지 않으려면 애초에 부모를 참조할 필요 없음
        http.formLogin().disable(); // 기존 formlogin을 사용하지 않도록 설정
        http.httpBasic().disable(); // 기본 basic 로그인(confirm 창)을 사용하지 않도록 설정
        // 위 둘을 막은 상태로 접근하면 403 (권한없음)
        // 로그인 하지 않고 접근하면 401 (방법 없음)

        // 스프링 시큐리티가 세션을 생성하지도 않고 기존의 세션을 사용하지도 않도록 설정
//        http.sessionManagement().disable(); // 세션을 사용하지 않도록 설정

        // 스프링 시큐리티가 세션을 생성하지 않도록 설정. 기존의 세션을 완전히 사용하지 않겠다는 뜻은 아님
        // JWT 등의 토큰 인증방식을 사용할 때 설정
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.cors();  // CrossOrigin 사용 (타 서버에서 요청을 받을 수 있도록 허용)
        http.csrf().disable();  // csrf : 위조 방지 스티커 (Token) <- 조금 자세히 찾아볼 것
        http.authorizeRequests()
                .antMatchers("/auth/**", "/h2-console/**") // 앞으로 /auth/signin, /auth/signup 이런식으로 컨트롤러 맵핑 처리할 것
                .permitAll() // 위 mapping에 대해 사용자에게 모든 권한을 줌 (즉, 인증이 필요 없음)
                .anyRequest() // 위 요청 이외의 요청들을
                .authenticated() // 그 외 다른 모든 요청은 인증을 거치도록 설정
                .and()
                .headers()
                .frameOptions()
                .disable(); // i frame 검사 안 하는 것 (h2-console을 연결하기 위해 필요함)

        // jwt 필터 적용(필터 클래스, 비밀번호 인증 필터) Username,Password 필터(아이디, 비밀번호 확인 로직) 실행 이전에 jwt 토큰 필터 먼저 실행하도록 순서 지정
        http.addFilterBefore(jwtAccessTokenFilter, UsernamePasswordAuthenticationFilter.class); // 앞에 처리한 값(Authentication)이 있으면 다음 단계로 넘어감 (아니면 여기서 막힘)
    }
}

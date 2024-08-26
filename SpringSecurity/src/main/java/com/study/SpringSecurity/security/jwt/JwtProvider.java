package com.study.SpringSecurity.security.jwt;

import com.study.SpringSecurity.domain.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtProvider {

    private final Key key;// key는 상수이기 때문에 무조건 초기화 필요

    // @Value <- Springframework꺼로, @Value는 @Autowired와 같음
    public JwtProvider(@Value("${jwt.secret}") String secret){   // @Value("표현식")을 사용할 경우 yml의 secret 값을 가져옴 (요게 String 문자열
        key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret)); //Keys는 JWS 웹 토큰, secret을 decoder 해서 key에 넣어주어야 함 (JTW 문법임)
    }

    public String removeBearer(String token) {
        String originToken = token.substring("Bearer ".length());  // 해당 문자열의 7번째 인덱스부터 값 전체를 가져옴
        return originToken;
    }

    // JWT토큰은 Json 형식 (문자열???)
    public String generateUserToken(User user) {    // 사용자 정보 기준으로 Token 생성
        // util에 있는 Date Date().getTime()을 하면 년월일시분초로 나오는데, 그걸 Date()안에 다시 넣으면 깔끔하게 나옴
        Date expireDate = new Date(new Date().getTime() + (1000l * 60 * 60 * 24 * 30));    // 한 달 : 1000 * 60 * 60 * 24 * 30

        String token = Jwts.builder()
                .claim("userId", user.getId())    // claim은 map과 같음 (Key, Value 값을 추가할 때 사용)
                .expiration(expireDate) // 토큰을 영원히 사용하지 못 하도록 만료시간 설정
                .signWith(key, SignatureAlgorithm.HS256)  // 해당 키를 가지고 인증할 수 있도록 설정 뒤에 알고리즘 설정안 하면 HS512으로 되어 있음
                .compact(); // 그냥 마지막에 build가 compact로 바뀐 builder 패턴

        return token;
    }

    public Claims parseToken(String token) {
        JwtParser jwtParser = Jwts.parser()   // 토큰을 복호화
                .setSigningKey(key)
                .build();    // jwt parser객체로 빌드 해줌

        return jwtParser.parseClaimsJws(token).getPayload();  // Claims 객체로 변환해서 반환. 단, 여기서 예외가 터질 수 있음
    }
}

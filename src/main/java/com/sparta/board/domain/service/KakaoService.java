package com.sparta.board.domain.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.board.domain.entity.Role;
import com.sparta.board.domain.entity.User;
import com.sparta.board.domain.repository.UserRepository;
import com.sparta.board.security.jwt.TokenProvider;
import com.sparta.board.web.dto.KakaoUserInfoDto;
import com.sparta.board.web.dto.auth.TokenDto;
import com.sparta.board.web.dto.auth.TokenReqDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class KakaoService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;

    @Transactional
    public TokenDto forceLogin(String code) throws JsonProcessingException {

        //유저 정보 얻어옴
        KakaoUserInfoDto userInfo = getUserInfo(code);

        //카카오 id가 중복값인지 확인 -> 중복값이면 정보 업데이트
        User findUser = userRepository.findByKakaoId(userInfo.getId()).orElse(null);

        if (findUser == null) {
            String nickname = userInfo.getNickname();
            if (userRepository.existsByNickname(nickname)) {
                StringBuilder sb = new StringBuilder();
                sb.append("kakao");
                sb.append(nickname);
                nickname = sb.toString();
            }
            Integer kakaoId = userInfo.getId();
            //로그인 아이디
            String loginId = nickname + kakaoId;
            if (userRepository.existsByLoginId(loginId)) {
                StringBuilder sb = new StringBuilder();
                sb.append("kakao");
                sb.append(loginId);
                loginId = sb.toString();
            }
            //비밀번호 랜덤값
            String password = passwordEncoder.encode(String.valueOf(UUID.randomUUID()));
            Role role = Role.USER;

            User kakaoUser = User.builder()
                    .loginId(loginId)
                    .loginPassword(password)
                    .kakaoId(kakaoId)
                    .nickname(nickname)
                    .role(role)
                    .build();

            findUser = userRepository.save(kakaoUser);
        }

        //토큰, 인증 객체 생성 후 강제로 SecurityContext에 넣음
        //원래는 인증을 해야하지만 이미 카카오에서 인증을 했으므로 인증절차 X
        SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(findUser.getRole().getAuthority());
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(simpleGrantedAuthority);
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(findUser.getLoginId()
                , findUser.getLoginPassword(),authorities);

        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        //클라이언트에서 받을 토큰 생성
        TokenDto tokenDto = tokenProvider.generateToken(authenticationToken);

        return tokenDto;
    }

    public KakaoUserInfoDto getUserInfo(String code) throws JsonProcessingException {

        // authorization code -> token
        String accessToken = getAccessToken(code);

        // token -> userinfo
        KakaoUserInfoDto userInfo = getUserInfoFromToken(accessToken);

        return userInfo;
    }

    public String getAccessToken(String code) throws JsonProcessingException {
        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HTTP Body 생성
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", "8a68ec3284eb80622771376dd956d8ac");
        body.add("redirect_uri", "http://54.180.104.9/user/kakao/callback");
        body.add("code", code);

        //요청 보내고 응답 받기
        HttpEntity<MultiValueMap<String, String>> req = new HttpEntity<>(body, headers);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> res = rt.exchange("https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                req,
                String.class);

        //access토큰을 가져오자!
        String responseBody = res.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        return jsonNode.get("access_token").asText();
    }

    public KakaoUserInfoDto getUserInfoFromToken(String accessToken) throws JsonProcessingException {

        //header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // 요청 보내고 결과값 받기
        HttpEntity<MultiValueMap<String, String>> req = new HttpEntity<>(headers);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.POST,
                req,
                String.class
        );

        //요청 온거 파싱
        String responseBody = response.getBody();
        ObjectMapper om = new ObjectMapper();
        JsonNode jsonNode = om.readTree(responseBody);
        Integer id = jsonNode.get("id").asInt();
        JsonNode properties = jsonNode.get("properties");
        String nickname = properties.get("nickname").asText();

        return KakaoUserInfoDto.builder()
                .id(id)
                .nickname(nickname)
                .build();
    }
}

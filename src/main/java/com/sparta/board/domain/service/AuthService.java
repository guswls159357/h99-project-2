package com.sparta.board.domain.service;

import com.sparta.board.domain.entity.RefreshToken;
import com.sparta.board.domain.repository.RefreshTokenRepository;
import com.sparta.board.domain.repository.UserRepository;
import com.sparta.board.security.jwt.TokenProvider;
import com.sparta.board.security.util.SecurityUtil;
import com.sparta.board.web.dto.auth.LoginReqDto;
import com.sparta.board.web.dto.auth.TokenDto;
import com.sparta.board.web.dto.auth.TokenReqDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



import static com.sparta.board.security.jwt.TokenProvider.AUTHORIZATION_HEADER;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final UserRepository userRepository;
    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;


    @Transactional
    public TokenDto login(LoginReqDto dto){
        // login ID/Password를 기반으로 Authentication 생성
        UsernamePasswordAuthenticationToken authenticationToken = dto.toAuthentication();

        // 실제로 검증 -> userdetailsService -> loaduserbyusername에서 검증
        // 비밀번호 검증은 DaoAuthenticationProvider에서 구현되어있음!
        Authentication authentication = authenticationManagerBuilder.getObject()
                .authenticate(authenticationToken);

        //JWT생성
        TokenDto tokenDto = tokenProvider.generateToken(authentication);

        //RefreshToken 저장
        RefreshToken refreshToken = RefreshToken.builder()
                .id(authentication.getName())
                .value(tokenDto.getRefreshToken())
                .build();

        refreshTokenRepository.save(refreshToken);

        return tokenDto;

    }

    @Transactional
    public TokenDto reissuance(TokenReqDto tokenReqDto){
        // refresh token 검증
        Integer validResult = tokenProvider.validateToken(tokenReqDto.getRefreshToken());
        if(validResult==-1){
            throw new IllegalArgumentException("유효한 토큰이 아닙니다");
        }

        // Access Token에서 UserId를 가져와 저장되어있는 Refresh토큰을 가져옴
        Authentication authentication = tokenProvider.getAuthentication(tokenReqDto.getAccessToken());
        RefreshToken refreshToken = refreshTokenRepository.findById(authentication.getName()).orElseThrow(() -> {
            throw new IllegalArgumentException("다시 로그인 해주세요");
        });

        // Refresh Token이 서로 일치하는지 확인
        if(!refreshToken.getValue().equals(tokenReqDto.getRefreshToken())){
            throw new IllegalArgumentException("다시 로그인 해주세요");
        }

        // 새로운 access 토큰 생성
        TokenDto tokenDto = tokenProvider.generateToken(authentication);

        // 저장소에 있는 Refresh토큰 값을 변경
        RefreshToken updatedRefreshToken = refreshToken.updateValue(tokenDto.getRefreshToken());
        refreshTokenRepository.save(updatedRefreshToken);

        return tokenDto;
    }

    @Transactional
    public void logout(){
        String currentLoginUserId = SecurityUtil.getCurrentLoginUserId();
        refreshTokenRepository.deleteById(currentLoginUserId);

    }


    public boolean nicknameCheck(String nickname){

        return userRepository.existsByNickname(nickname);
    }

    public boolean loginIdCheck(String loginId) {

        return userRepository.existsByLoginId(loginId);
    }

    public HttpHeaders tokenToHeader(TokenDto tokenDto){
        HttpHeaders headers = new HttpHeaders();
        headers.add(AUTHORIZATION_HEADER,tokenDto.getAccessToken());
        headers.add("Refresh", tokenDto.getRefreshToken());

        return headers;
    }
}

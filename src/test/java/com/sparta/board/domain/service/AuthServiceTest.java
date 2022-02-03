package com.sparta.board.domain.service;

import com.sparta.board.domain.repository.RefreshTokenRepository;
import com.sparta.board.domain.repository.UserRepository;
import com.sparta.board.security.jwt.TokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

class AuthServiceTest {

    private AuthService authService;

    @Mock
    private AuthenticationManagerBuilder authenticationManagerBuilder;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TokenProvider tokenProvider;

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        authService = new AuthService(authenticationManagerBuilder,userRepository
                ,tokenProvider,refreshTokenRepository);
    }

    @Test
    @DisplayName("닉네임 중복")
    void duplicateNickname(){
        String mockNickname = "nickname";

        //존재하는 닉네임이 있다고 가정
        given(userRepository.existsByNickname(mockNickname)).willReturn(true);

        //존재하는 닉네임이 있으므로 true반환
        assertThat(authService.nicknameCheck(mockNickname)).isTrue();
    }

    @Test
    @DisplayName("닉네임 중복X")
    void notDuplicateNickname(){
        String mockNickname = "nickname";

        //존재하는 닉네임이 없다고 가정
        given(userRepository.existsByNickname(mockNickname)).willReturn(false);

        //존재하는 닉네임이 없으므로 false반환
        assertThat(authService.nicknameCheck(mockNickname)).isFalse();
    }

    @Test
    @DisplayName("아이디 중복")
    void duplicateId(){
        String mockId = "loginId";

        //존재하는 닉네임이 있다고 가정
        given(userRepository.existsByLoginId(mockId)).willReturn(true);

        //존재하는 닉네임이 있으므로 true반환
        assertThat(authService.loginIdCheck(mockId)).isTrue();
    }

    @Test
    @DisplayName("아이디 중복X")
    void notDuplicateId(){
        String mockId = "loginId";

        //존재하는 닉네임이 없다고 가정
        given(userRepository.existsByLoginId(mockId)).willReturn(false);

        //존재하는 닉네임이 없으므로 false반환
        assertThat(authService.loginIdCheck(mockId)).isFalse();
    }

}
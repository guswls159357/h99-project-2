package com.sparta.board.domain.service;

import com.sparta.board.domain.repository.UserRepository;
import com.sparta.board.exceptionhandler.ex.FieldException;
import com.sparta.board.web.dto.auth.SignupReqDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

class UserServiceTest {

    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userService = new UserService(passwordEncoder, userRepository);
    }

    @Test
    @DisplayName("비밀번호, 비밀번호 체크가 같음")
    void samePasswordPasswordCheck() {

        SignupReqDto mockSignupReqDto = SignupReqDto.builder()
                .loginId("guswls123")
                .loginPassword("rlagus123!")
                .loginPasswordCheck("rlagus123!")
                .nickname("nickname")
                .build();

        userService.create(mockSignupReqDto);

        //예외가 발생하지 않았다면 다음을 호출
        verify(userRepository).save(any());
        verify(passwordEncoder).encode(any());
    }

    @Test
    @DisplayName("비밀번호, 비밀번호 체크가 다름")
    void diffPasswordPasswordCheck() {

        SignupReqDto mockSignupReqDto = SignupReqDto.builder()
                .loginId("guswls123")
                .loginPassword("rlagus123!")
                .loginPasswordCheck("rlagus123")
                .nickname("nickname")
                .build();

        //예외가 발생해야함
        assertThatThrownBy(()->{userService.create(mockSignupReqDto);})
                .isInstanceOf(FieldException.class);

        //예외가 발생했다면 다음을 호출하지 않음
        verify(userRepository,never()).save(any());
        verify(passwordEncoder,never()).encode(any());
    }
}
package com.sparta.board.web.controller;

import com.sparta.board.domain.service.BoardService;
import com.sparta.board.domain.service.KakaoService;
import com.sparta.board.domain.service.UserService;
import com.sparta.board.security.config.SecurityConfig;
import com.sparta.board.web.dto.auth.SignupReqDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers = UserController.class,
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class))
class UserControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach
    public void setUp() {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserService userService;

    @MockBean
    private KakaoService kakaoService;

    @MockBean
    private BoardService boardService;

    @Test
    @DisplayName("올바른 아이디 회원가입")
    void createWithValidLoginId() throws Exception {

        SignupReqDto reqDto = SignupReqDto.builder()
                .loginId("guswls123")
                .loginPassword("guswls123!")
                .loginPasswordCheck("guswls123!")
                .nickname("nickname")
                .build();

        mvc.perform(post("/user/signup")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                        .param("loginId", reqDto.getLoginId())
                        .param("loginPassword", reqDto.getLoginPassword())
                        .param("loginPasswordCheck", reqDto.getLoginPasswordCheck())
                        .param("nickname", reqDto.getNickname()))
                .andExpect(status().is3xxRedirection());


    }

    @Test
    @DisplayName("올바르지 않은 닉네임 회원가입-빈 값")
    void createWithInvalidNickname1() throws Exception {
        SignupReqDto reqDto = SignupReqDto.builder()
                .loginId("guswls123")
                .loginPassword("guswls123!")
                .loginPasswordCheck("guswls123!")
                .nickname(" ")
                .build();

        mvc.perform(post("/user/signup")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                        .param("loginId", reqDto.getLoginId())
                        .param("loginPassword", reqDto.getLoginPassword())
                        .param("loginPasswordCheck", reqDto.getLoginPasswordCheck())
                        .param("nickname", reqDto.getNickname()))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("올바르지 않은 닉네임 회원가입-패턴 오류")
    void createWithInvalidNickname2() throws Exception {
        SignupReqDto reqDto = SignupReqDto.builder()
                .loginId("guswls123")
                .loginPassword("guswls123!")
                .loginPasswordCheck("guswls123!")
                .nickname("ㅁㄴasd")
                .build();

        mvc.perform(post("/user/signup")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                        .param("loginId", reqDto.getLoginId())
                        .param("loginPassword", reqDto.getLoginPassword())
                        .param("loginPasswordCheck", reqDto.getLoginPasswordCheck())
                        .param("nickname", reqDto.getNickname()))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("올바른 비밀번호 회원가입")
    void createWithValidPassword() throws Exception {
        SignupReqDto reqDto = SignupReqDto.builder()
                .loginId("guswls123")
                .loginPassword("guswls123!")
                .loginPasswordCheck("guswls123!")
                .nickname("asd")
                .build();

        mvc.perform(post("/user/signup")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                        .param("loginId", reqDto.getLoginId())
                        .param("loginPassword", reqDto.getLoginPassword())
                        .param("loginPasswordCheck", reqDto.getLoginPasswordCheck())
                        .param("nickname", reqDto.getNickname()))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @DisplayName("올바르지 않은 비밀번호 회원가입-글자수 오류")
    void createWithInvalidPassword1() throws Exception {
        SignupReqDto reqDto = SignupReqDto.builder()
                .loginId("guswls123")
                .loginPassword("gu")
                .loginPasswordCheck("gu")
                .nickname("asd")
                .build();

        mvc.perform(post("/user/signup")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                        .param("loginId", reqDto.getLoginId())
                        .param("loginPassword", reqDto.getLoginPassword())
                        .param("loginPasswordCheck", reqDto.getLoginPasswordCheck())
                        .param("nickname", reqDto.getNickname()))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("올바르지 않은 비밀번호 회원가입-패턴 오류")
    void createWithInvalidPassword2() throws Exception {
        SignupReqDto reqDto = SignupReqDto.builder()
                .loginId("guswls123")
                .loginPassword("guㅁㄴ")
                .loginPasswordCheck("guㅁㄴ")
                .nickname("asd")
                .build();

        mvc.perform(post("/user/signup")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                        .param("loginId", reqDto.getLoginId())
                        .param("loginPassword", reqDto.getLoginPassword())
                        .param("loginPasswordCheck", reqDto.getLoginPasswordCheck())
                        .param("nickname", reqDto.getNickname()))
                .andExpect(status().isBadRequest());
    }




}
package com.sparta.board.web.controller;

import com.sparta.board.domain.service.BoardService;
import com.sparta.board.domain.service.KakaoService;
import com.sparta.board.domain.service.UserService;
import com.sparta.board.exceptionhandler.ex.FieldsException;
import com.sparta.board.web.dto.auth.SignupReqDto;
import com.sparta.board.web.dto.auth.TokenDto;
import com.sparta.board.web.dto.board.BoardResDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import static com.sparta.board.security.jwt.TokenProvider.AUTHORIZATION_HEADER;

@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
@Slf4j
public class UserController {

    private final UserService userService;
    private final KakaoService kakaoService;
    private final BoardService boardService;

    @GetMapping("/login")
    public String loginForm(){return "login";}

    @GetMapping("/signup")
    public String signupForm(){
        return "signup";
    }

    @PostMapping("/signup")
    public String signup(@Validated @ModelAttribute SignupReqDto dto,
                         BindingResult bindingResult,HttpServletResponse res) {

        if(bindingResult.hasFieldErrors()){
            throw new FieldsException(bindingResult);
        }else{
            userService.create(dto);
            return "redirect:/user/login";
        }

    }


    @GetMapping("/kakao/callback")
    public String kakaoLogin(@RequestParam String code, Model model, HttpServletResponse response) throws IOException, URISyntaxException {
        List<BoardResDto> list = boardService.getList();

        model.addAttribute("board_list", list);

        TokenDto tokenDto = kakaoService.forceLogin(code);
        log.info(tokenDto.getAccessToken());
        log.info(tokenDto.getRefreshToken());

        Cookie accessToken = new Cookie(AUTHORIZATION_HEADER,tokenDto.getAccessToken());
        Cookie refreshToken = new Cookie("refreshToken", tokenDto.getRefreshToken());
        accessToken.setPath("/");
        refreshToken.setPath("/");
        //response.setHeader(AUTHORIZATION_HEADER,tokenDto.getAccessToken());
        //response.setHeader("Refresh",tokenDto.getRefreshToken());

        response.addCookie(accessToken);
        response.addCookie(refreshToken);

        return "home";
    }


}

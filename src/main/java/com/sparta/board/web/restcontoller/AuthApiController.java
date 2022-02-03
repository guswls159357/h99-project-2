package com.sparta.board.web.restcontoller;

import com.sparta.board.domain.service.AuthService;
import com.sparta.board.exceptionhandler.ex.FieldException;
import com.sparta.board.web.dto.auth.LoginReqDto;
import com.sparta.board.web.dto.ResDto;
import com.sparta.board.web.dto.auth.TokenDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthApiController {

    private final AuthService authService;

    @GetMapping("/nickname/{nickname}")
    public ResDto checkNickname(@PathVariable("nickname") String nickname){

        if(authService.nicknameCheck(nickname)){
            throw new FieldException("nickname","중복된 값입니다");
        }else{
            return new ResDto(1,null);
        }
    }

    @GetMapping("/loginId/{loginId}")
    public ResDto checkLoginId(@PathVariable String loginId){

        if(authService.loginIdCheck(loginId)){
            throw new FieldException("loginId","중복된 값입니다");
        }else{
            return new ResDto(1,null);
        }
    }

    @PostMapping("/login")
    public ResponseEntity login(@Validated @RequestBody LoginReqDto dto){
        TokenDto tokenDto = authService.login(dto);
        HttpHeaders headers = authService.tokenToHeader(tokenDto);
        return ResponseEntity.ok()
                .headers(headers)
                .body(null);
    }

    @PostMapping("/logout")
    public ResDto logout(){

        authService.logout();

        return new ResDto(1,null);
    }


}

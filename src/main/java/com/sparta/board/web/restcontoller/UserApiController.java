package com.sparta.board.web.restcontoller;

import com.sparta.board.domain.service.UserService;
import com.sparta.board.web.dto.ResDto;
import com.sparta.board.web.dto.user.UserResDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserApiController {

    private final UserService userService;

    @GetMapping("/user")
    public ResDto getUser(){

        UserResDto currentUser = userService.findLoginUser();

        return new ResDto(1,currentUser);
    }
}

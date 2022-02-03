package com.sparta.board.web.dto.auth;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
public class TokenReqDto {

    @NotNull(message = "빈 값일수 없습니다")
    private String accessToken;
    private String refreshToken;
}

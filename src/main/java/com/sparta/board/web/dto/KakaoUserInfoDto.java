package com.sparta.board.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Data
@Builder
public class KakaoUserInfoDto {

    private Integer id;
    private String nickname;
}

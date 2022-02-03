package com.sparta.board.web.dto.board;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BoardResDto {

    private Integer id;

    private String title;

    private String content;

    private String createdAt;

    private String nickname;

}

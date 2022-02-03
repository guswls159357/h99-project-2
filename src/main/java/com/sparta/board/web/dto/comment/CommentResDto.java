package com.sparta.board.web.dto.comment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class CommentResDto {

    private String content;

    private String nickname;

    private String createdAt;

    private Integer commentId;
}

package com.sparta.board.web.dto.board;

import com.sparta.board.domain.entity.Board;
import com.sparta.board.domain.entity.User;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoardReqDto {

    @Size(max = 10, message = "제목은 10글자 이하여야 합니다")
    private String title;

    @Size(min= 1,max = 50, message = "내용은 1글자 이상 50글자 이하여야 합니다")
    private String content;

    public Board toEntity(User user) {
        return Board.builder()
                .content(this.content)
                .user(user)
                .title(this.title)
                .build();
    }
}

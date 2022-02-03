package com.sparta.board.web.dto.comment;

import com.sparta.board.domain.entity.Board;
import com.sparta.board.domain.entity.Comment;
import com.sparta.board.domain.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class CommentCreateReqDto {

    @NotBlank(message = "빈 값일수 없습니다")
    private String content;

    @NotNull(message = "빈 값일수 없습니다")
    private Integer boardId;

    public Comment toEntity(User user, Board board){
        return Comment.builder()
                .content(this.content)
                .board(board)
                .user(user)
                .build();
    }
}

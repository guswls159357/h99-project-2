package com.sparta.board.web.dto.board;

import com.sparta.board.web.dto.comment.CommentResDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class BoardDetailResDto {

    private Integer boardId;

    private String title;

    private String content;

    private String nickname;

    private String createdAt;

    private List<CommentResDto> commentResDtoList = new ArrayList<>();


}

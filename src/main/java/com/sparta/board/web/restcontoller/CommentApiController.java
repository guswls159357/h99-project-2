package com.sparta.board.web.restcontoller;

import com.sparta.board.domain.service.CommentService;
import com.sparta.board.web.dto.ResDto;
import com.sparta.board.web.dto.comment.CommentCreateReqDto;
import com.sparta.board.web.dto.comment.CommentUpdateReqDto;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comment")
public class CommentApiController {

    private final CommentService commentService;

    @PostMapping
    public ResDto create(@Validated @RequestBody CommentCreateReqDto dto){

        commentService.create(dto);

        return new ResDto(1,null);
    }

    @PatchMapping
    public ResDto update(@Validated @RequestBody CommentUpdateReqDto dto){

        commentService.update(dto);

        return new ResDto(1,null);
    }

    @DeleteMapping("/{commentId}")
    public ResDto delete(@PathVariable("commentId") String commentId){

        commentService.delete(Integer.valueOf(commentId));

        return new ResDto(1,null);
    }

}

package com.sparta.board.web.restcontoller;

import com.sparta.board.domain.service.BoardService;
import com.sparta.board.web.dto.ResDto;
import com.sparta.board.web.dto.board.BoardReqDto;
import com.sparta.board.web.dto.board.BoardResDto;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/board")
public class BoardApiController {

    private final BoardService boardService;

    @GetMapping("/all")
    public ResDto getAll(){

        List<BoardResDto> list = boardService.getList();
        return new ResDto(1,list);
    }

    @GetMapping("/{boardId}")
    public ResDto getDetail(@PathVariable("boardId") Integer boardId){

        return new ResDto(1,boardService.getDetail(boardId));
    }

    @PostMapping
    public ResDto create(@Validated @RequestBody BoardReqDto dto){

        boardService.create(dto);

        return new ResDto(1,null);
    }



}

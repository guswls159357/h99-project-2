package com.sparta.board.web.controller;

import com.sparta.board.domain.service.BoardService;
import com.sparta.board.web.dto.board.BoardDetailResDto;
import com.sparta.board.web.dto.board.BoardReqDto;
import com.sparta.board.web.dto.board.BoardResDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class BoardController {

    private final BoardService boardService;

    @GetMapping(value = {"/","/board"})
    public String home(Model model) {
        List<BoardResDto> list = boardService.getList();

        model.addAttribute("board_list", list);
        return "home";
    }

    @GetMapping("/board/{boardId}")
    public String detail(@PathVariable("boardId") String boardId,
                         Model model){
        BoardDetailResDto board = boardService.getDetail(Integer.valueOf(boardId));
        model.addAttribute("board",board);
        return "detail";
    }

}

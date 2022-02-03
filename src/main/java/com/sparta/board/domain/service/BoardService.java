package com.sparta.board.domain.service;

import com.sparta.board.domain.entity.Board;
import com.sparta.board.domain.entity.User;
import com.sparta.board.domain.repository.BoardRepository;
import com.sparta.board.domain.repository.UserRepository;
import com.sparta.board.exceptionhandler.ex.FieldException;
import com.sparta.board.security.util.SecurityUtil;
import com.sparta.board.web.dto.board.BoardDetailResDto;
import com.sparta.board.web.dto.board.BoardReqDto;
import com.sparta.board.web.dto.board.BoardResDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class BoardService {

    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    @Transactional
    public void create(BoardReqDto dto) {

        User user = userRepository.findByLoginId(SecurityUtil.getCurrentLoginUserId())
                .orElseThrow(() -> new IllegalArgumentException("유저 정보가 존재하지 않습니다"));

        boardRepository.save(dto.toEntity(user));
    }

    public List<BoardResDto> getList() {

        return boardRepository.findAllOrderByCreatedAt().stream().map(board -> board.toResDto())
                .collect(Collectors.toList());
    }

    public BoardDetailResDto getDetail(Integer boardId){

        Board board = boardRepository.findById(boardId).orElseThrow(() ->
                new FieldException("boardId", "존재하지 않습니다"));

        return board.toDetailResDto();
    }
}

package com.sparta.board.domain.service;

import com.sparta.board.domain.entity.Board;
import com.sparta.board.domain.entity.Comment;
import com.sparta.board.domain.entity.User;
import com.sparta.board.domain.repository.BoardRepository;
import com.sparta.board.domain.repository.CommentRepository;
import com.sparta.board.domain.repository.UserRepository;
import com.sparta.board.exceptionhandler.ex.FieldException;
import com.sparta.board.security.util.SecurityUtil;
import com.sparta.board.web.dto.comment.CommentCreateReqDto;
import com.sparta.board.web.dto.comment.CommentUpdateReqDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    @Transactional
    public void create(CommentCreateReqDto dto) {
        String currentLoginUserId = SecurityUtil.getCurrentLoginUserId();
        User currentUser = userRepository.findByLoginId(currentLoginUserId).orElseThrow(() ->
                new FieldException("loginId", "해당 아이디를 가진 유저는 존재하지 않습니다"));
        Board board = boardRepository.findById(dto.getBoardId()).orElseThrow(() ->
                new FieldException("boardId", "해당 아이디를 가진 게시글은 없습니다"));

        commentRepository.save(dto.toEntity(currentUser,board));
        boardRepository.save(board);
        userRepository.save(currentUser);
    }

    @Transactional
    public void delete(Integer commentId){

        String currentLoginUserId = SecurityUtil.getCurrentLoginUserId();

        Comment comment = commentRepository.findById(commentId).orElseThrow(() ->
                new FieldException("commentId", "없는 댓글입니다"));

        if(comment.getUser().getLoginId().equals(currentLoginUserId)){
            commentRepository.deleteById(commentId);
        }else{
            throw new AccessDeniedException("내 댓글만 삭제할 수 있습니다");
        }

    }

    @Transactional
    public void update(CommentUpdateReqDto dto){
        String currentLoginUserId = SecurityUtil.getCurrentLoginUserId();

        Comment comment = commentRepository.findById(dto.getId()).orElseThrow(() ->
                new FieldException("commentId", "없는 댓글입니다"));

        if(comment.getUser().getLoginId().equals(currentLoginUserId)){
            comment.update(dto.getContent());
            commentRepository.save(comment);
        }else{
            throw new AccessDeniedException("내 댓글만 삭제할 수 있습니다");
        }
    }
}

package com.sparta.board.domain.entity;

import com.sparta.board.web.dto.comment.CommentResDto;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "comment",
        indexes = {
                @Index(name = "idx_comment_created_at", columnList = "created_at"),
                @Index(name = "idx_comment_updated_at", columnList = "updated_at")
        })
public class Comment extends TimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Integer id;

    @Column(nullable = false, columnDefinition = "varchar(50)")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    public Comment(String content, Board board, User user) {
        this.content = content;
        setBoard(board);
        setUser(user);
    }

    public void update(String content){
        this.content = content;
    }

    private void setBoard(Board board) {
        this.board = board;
        board.getCommentList().add(this);
    }

    private void setUser(User user) {
        this.user = user;
        user.getCommentList().add(this);
    }

    public CommentResDto toResDto() {

        return CommentResDto.builder()
                .commentId(this.id)
                .createdAt(localDateTimeToString(this.createdAt))
                .nickname(this.user.getNickname())
                .content(this.content)
                .build();
    }

    private String localDateTimeToString(LocalDateTime time) {
        return time.toString().replace("T", " ")
                .substring(0, 19);
    }
}

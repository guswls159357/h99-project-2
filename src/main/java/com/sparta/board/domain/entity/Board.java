package com.sparta.board.domain.entity;

import com.sparta.board.web.dto.board.BoardDetailResDto;
import com.sparta.board.web.dto.board.BoardResDto;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "board",
        indexes = {
                @Index(name = "idx_board_created_at", columnList = "created_at"),
                @Index(name = "idx_board_updated_at", columnList = "updated_at")
        })
public class Board extends TimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Integer id;

    @NotBlank
    @Column(nullable = false, columnDefinition = "varchar(20)")
    private String title;

    @NotBlank
    @Column(nullable = false, columnDefinition = "varchar(100)")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "board",
            cascade = CascadeType.PERSIST,
            orphanRemoval = true)
    private List<Comment> commentList = new ArrayList<>();

    @Builder
    public Board(String title, User user, String content) {
        this.title = title;
        this.content = content;
        setUser(user);
    }


    public BoardResDto toResDto() {
        return BoardResDto.builder()
                .id(this.id)
                .content(this.content)
                .title(this.title)
                .nickname(this.user.getNickname())
                .createdAt(localDateTimeToString(createdAt))
                .build();
    }

    public BoardDetailResDto toDetailResDto() {
        return BoardDetailResDto.builder()
                .boardId(this.id)
                .nickname(this.user.getNickname())
                .content(this.content)
                .title(this.title)
                .createdAt(localDateTimeToString(this.createdAt))
                .commentResDtoList(this.commentList.stream()
                        .map(comment -> comment.toResDto())
                        .collect(Collectors.toList()))
                .build();
    }

    private String localDateTimeToString(LocalDateTime time) {
        return time.toString().replace("T", " ")
                .substring(0, 19);
    }

    private void setUser(User user) {
        this.user = user;
        user.getBoardList().add(this);
    }

}


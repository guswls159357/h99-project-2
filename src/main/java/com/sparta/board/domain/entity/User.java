package com.sparta.board.domain.entity;

import com.sparta.board.web.dto.user.UserResDto;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name="user",
        indexes = {
        @Index(name = "idx_user_created_at", columnList = "created_at"),
        @Index(name = "idx_user_updated_at", columnList = "updated_at")
})
public class User extends TimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Integer id;

    @Column(nullable = false, unique = true, columnDefinition = "varchar(20)")
    private String loginId;

    @Column(nullable = false,columnDefinition = "varchar(100)")
    private String loginPassword;

    @Column(nullable = false,unique = true,columnDefinition = "varchar(20)")
    private String nickname;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private Role role;

    @Column(unique = true)
    private Integer kakaoId;

    @OneToMany(mappedBy = "user",
            fetch = FetchType.LAZY,
            cascade = CascadeType.PERSIST,
            orphanRemoval = true
    )
    private List<Board> boardList = new ArrayList<>();

    @OneToMany(mappedBy = "user",
            fetch = FetchType.LAZY,
            cascade = CascadeType.PERSIST,
            orphanRemoval = true
    )
    private List<Comment> commentList = new ArrayList<>();

    @Builder
    public User(String loginId, String loginPassword, String nickname, Integer kakaoId, Role role) {
        this.loginId = loginId;
        this.loginPassword = loginPassword;
        this.nickname = nickname;
        this.kakaoId = kakaoId;
        this.role = role;
    }

    public void setRole(Role role){
        this.role = role;
    }

    public UserResDto toResDto(){
        return UserResDto.builder()
                .loginId(this.loginId)
                .nickname(this.nickname)
                .kakaoId(this.kakaoId)
                .build();
    }
}

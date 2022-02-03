package com.sparta.board.web.dto.auth;

import com.sparta.board.domain.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SignupReqDto {

    @Pattern(regexp = "^[A-Za-z0-9]{6,15}$", message = "6-15글자의 알파벳 대,소문자와 숫자만 입력 가능합니다")
    private String loginId;

    @Pattern(regexp = "^.*(?=^.{6,15}$)(?=.*\\d)(?=.*[a-zA-Z])(?=.*[!@#]).*$",
            message = "6-15글자의 알파벳 대,소문자와 숫자, 최소 1개 이상의 !,@,#를 입력하셔야 합니다")
    private String loginPassword;

    @NotBlank(message = "빈 값일수 없습니다")
    private String loginPasswordCheck;

    @Pattern(regexp ="^[a-zA-Z0-9]{3,15}$" ,message = "3-15글자의 알파벳 대,소문자와 숫자만 입력 가능합니다")
    private String nickname;

    public User toEntity() {
        return User.builder()
                .loginId(this.loginId)
                .loginPassword(this.loginPassword)
                .nickname(this.nickname)
                .build();
    }
}

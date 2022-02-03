package com.sparta.board.web.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginReqDto {

    @NotBlank(message = "빈 값일수 없습니다")
    private String loginId;

    @NotBlank(message = "빈 값일수 없습니다")
    private String loginPassword;

    public UsernamePasswordAuthenticationToken toAuthentication(){
        return new UsernamePasswordAuthenticationToken(this.loginId, this.loginPassword);
    }
}

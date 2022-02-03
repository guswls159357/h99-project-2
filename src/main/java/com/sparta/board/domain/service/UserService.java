package com.sparta.board.domain.service;

import com.sparta.board.domain.entity.Role;
import com.sparta.board.domain.entity.User;
import com.sparta.board.domain.repository.UserRepository;
import com.sparta.board.exceptionhandler.ex.FieldException;
import com.sparta.board.security.util.SecurityUtil;
import com.sparta.board.web.dto.auth.SignupReqDto;
import com.sparta.board.web.dto.user.UserResDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class UserService{

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Transactional
    public void create(SignupReqDto dto){

        String pwd = dto.getLoginPassword();
        String checkPwd = dto.getLoginPasswordCheck();
        String nickname = dto.getNickname();

        if(!pwd.equals(checkPwd)){
            throw new FieldException("password","비밀번호와 비밀번호 확인이 일치하지 않습니다");
        }else if(pwd.contains(nickname)) {
            throw new FieldException("password", "비밀번호에 닉네임을 포함할 수 없습니다");
        }else{
            dto.setLoginPassword(passwordEncoder.encode(dto.getLoginPassword()));
            User user = dto.toEntity();
            user.setRole(Role.USER);
            userRepository.save(user);
        }
    }

    public UserResDto findLoginUser(){
        String currentLoginUserId = SecurityUtil.getCurrentLoginUserId();
        User user = userRepository.findByLoginId(currentLoginUserId).orElseThrow(() -> {
            throw new IllegalArgumentException("로그인 정보가 잘못되었습니다.");
        });

        return user.toResDto();
    }


}

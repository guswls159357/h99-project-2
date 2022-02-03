package com.sparta.board.security.service;

import com.sparta.board.domain.entity.User;
import com.sparta.board.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {
        return userRepository.findByLoginId(loginId)
                .map(this::createUserDetails)
                .orElseThrow(()->new UsernameNotFoundException(loginId + ": 해당 아이디 정보가 존재하지 않습니다."));

    }

    private UserDetails createUserDetails(User user){
        SimpleGrantedAuthority grantedAuthority = new SimpleGrantedAuthority(user.getRole().toString());

        return new org.springframework.security.core.userdetails.User(
                user.getLoginId(),
                user.getLoginPassword(),
                Collections.singleton(grantedAuthority)
        );
    }
}

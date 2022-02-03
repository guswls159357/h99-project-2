package com.sparta.board.domain.repository;

import com.sparta.board.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Integer> {

    boolean existsByNickname(String nickname);

    @Query("select u from User u where u.loginId = :loginId")
    Optional<User> findByLoginId(@Param("loginId") String loginId);

    Optional<User> findFirstByLoginId(String loginId);

    boolean existsByLoginId(String loginId);

    Optional<User> findByNickname(String nickname);

    Optional<User> findByKakaoId(Integer kakaoId);
}

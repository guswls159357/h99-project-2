package com.sparta.board.domain.repository;

import com.sparta.board.domain.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken,String> {

    @Override
    Optional<RefreshToken> findById(String s);

    @Override
    @Modifying
    @Query(value = "delete from refresh_token where refresh_token.id = :id",nativeQuery = true)
    void deleteById(@Param("id") String id);
}

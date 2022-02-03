package com.sparta.board.domain.repository;


import com.sparta.board.domain.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface BoardRepository extends JpaRepository<Board,Integer> {

    @Query(value = "SELECT * FROM board order by board.created_at desc",
            nativeQuery = true)
    List<Board> findAllOrderByCreatedAt();

}

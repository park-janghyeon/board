package com.study.board.repository;

import com.study.board.entity.BoardEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BoardRepository extends JpaRepository<BoardEntity, Long> {
    // board_hit+1 이거를 MYSQL 쿼리로 표현한다면:
    // update board_table set board_hits = board_hits+1 where id = ?

    // 1) JPQL로 표현하는 법:
    @Modifying // update, delete, insert를 할 때는 @Modifying 어노테이션을 붙여줘야 한다
    @Query(value = "update BoardEntity b set b.boardHits=b.boardHits+1 where b.id=:id")

    // 2) native query로 표현하는 법:
    // @Query(value = "update board_table set board_hits = board_hits+1 where id = ?", nativeQuery = true)

    void updateHits(@Param("id") Long id);
}

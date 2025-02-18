package com.map_study.repository;

import com.map_study.entity.Board;
import com.map_study.entity.BoardCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepository extends JpaRepository<Board, Integer> {

    Page<Board> findByTitleContaining(String searchKeyword, Pageable pageable);

    // 카테고리별 게시글 조회
    Page<Board> findByCategory(BoardCategory category, Pageable pageable);

    Page<Board> findByTitleContainingAndCategory(String searchKeyword, BoardCategory category, Pageable pageable);

}


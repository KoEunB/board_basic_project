package com.map_study.repository;

import com.map_study.entity.Board;
import com.map_study.entity.SecretBoard;
import com.map_study.entity.SecretBoardCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SecretBoardRepository extends JpaRepository<SecretBoard, Integer> {

    Page<SecretBoard> findByTitleContaining(String searchKeyword, Pageable pageable);

    // 카테고리별 게시글 조회
    Page<SecretBoard> findBycategory(SecretBoardCategory category, Pageable pageable);

    Page<SecretBoard> findByTitleContainingAndCategory(String searchKeyword, SecretBoardCategory category, Pageable pageable);

}

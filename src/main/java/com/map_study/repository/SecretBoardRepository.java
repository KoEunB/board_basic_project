package com.map_study.repository;

import com.map_study.entity.SecretBoard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SecretBoardRepository extends JpaRepository<SecretBoard, Integer> {

    Page<SecretBoard> findByTitleContaining(String searchKeyword, Pageable pageable);
}

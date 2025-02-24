package com.map_study.repository;

import com.map_study.entity.SecretBoardLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SecretBoardLikeRepository extends JpaRepository<SecretBoardLike, Integer> {

    boolean existsByMemberIdAndBoardId(String memberId, Integer boardId);
    Optional<SecretBoardLike> findByMemberIdAndBoardId(String memberId, Integer boardId);
}


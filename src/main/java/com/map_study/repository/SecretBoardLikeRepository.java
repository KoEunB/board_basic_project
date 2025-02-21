package com.map_study.repository;

import com.map_study.entity.SecretBoardLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SecretBoardLikeRepository extends JpaRepository<SecretBoardLike, Integer> {

    boolean existsByMemberIdAndBoardId(Integer memberId, Integer boardId);
    Optional<SecretBoardLike> findByMemberIdAndBoardId(Integer memberId, Integer boardId);
}


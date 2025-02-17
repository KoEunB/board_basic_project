package com.map_study.repository;

import com.map_study.entity.SecretBoardLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SecretBoardLikeRepository extends JpaRepository<SecretBoardLike, Integer> {

    boolean existsByMemberIdAndArticleId(Integer memberId, Integer articleId);
    Optional<SecretBoardLike> findByMemberIdAndArticleId(Integer memberId, Integer articleId);
}


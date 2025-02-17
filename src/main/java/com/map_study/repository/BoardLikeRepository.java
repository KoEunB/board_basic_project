package com.map_study.repository;

import com.map_study.entity.BoardLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BoardLikeRepository extends JpaRepository<BoardLike, Integer> {

    boolean existsByMemberIdAndArticleId(Integer memberId, Integer articleId);
    Optional<BoardLike> findByMemberIdAndArticleId(Integer memberId, Integer articleId);
}


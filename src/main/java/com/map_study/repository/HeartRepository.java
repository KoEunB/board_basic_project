package com.map_study.repository;

import com.map_study.entity.Heart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HeartRepository extends JpaRepository<Heart, Integer> {

    boolean existsByMemberIdAndArticleId(Integer memberId, Integer articleId);
    Optional<Heart> findByMemberIdAndArticleId(Integer memberId, Integer articleId);
}


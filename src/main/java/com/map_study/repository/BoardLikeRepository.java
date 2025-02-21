package com.map_study.repository;

import com.map_study.entity.BoardLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BoardLikeRepository extends JpaRepository<BoardLike, Integer> {

    boolean existsByMemberIdAndBoardId(Integer memberId, Integer boardId);
    Optional<BoardLike> findByMemberIdAndBoardId(Integer memberId, Integer boardId);
}


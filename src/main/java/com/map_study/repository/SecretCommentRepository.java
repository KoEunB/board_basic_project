package com.map_study.repository;

import com.map_study.entity.SecretComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SecretCommentRepository extends JpaRepository<SecretComment, Integer> {
    List<SecretComment> findByBoardId(Integer boardId);
}

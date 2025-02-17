package com.map_study.service;

import com.map_study.entity.SecretComment;
import com.map_study.repository.SecretCommentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SecretCommentService {

    private final SecretCommentRepository secretCommentRepository;

    public SecretCommentService(SecretCommentRepository secretCommentRepository) {
        this.secretCommentRepository = secretCommentRepository;
    }

    // 댓글 저장
    public void save(SecretComment secretComment) {

        secretCommentRepository.save(secretComment);
    }

    //특정 게시글 댓글 조회
    public List<SecretComment> findAll(Integer postId) {

        return secretCommentRepository.findByPostId(postId);
    }
}

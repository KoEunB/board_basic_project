package com.map_study.service;

import com.map_study.entity.Comment;
import com.map_study.entity.SecretComment;
import com.map_study.repository.SecretCommentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
    public List<SecretComment> findAll(Integer boardId) {

        return secretCommentRepository.findByBoardId(boardId);
    }

    //댓글 삭제
    public boolean deleteSecretComment(Integer commentId, String memberId) {
        Optional<SecretComment> optionalSecretComment = secretCommentRepository.findById(commentId);
        if (optionalSecretComment.isPresent()) {
            SecretComment secretComment = optionalSecretComment.get();
            if (secretComment.getMemberId().equals(memberId)) {
                secretCommentRepository.deleteById(commentId);
                return true;
            }
        }
        return false;
    }
}

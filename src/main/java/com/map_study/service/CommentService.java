package com.map_study.service;

import com.map_study.entity.Comment;
import com.map_study.repository.CommentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CommentService {
    private final CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    //댓글 저장
    public void save(Comment comment) {
        commentRepository.save(comment);
    }

    //특정 게시글 댓글 조회
    public List<Comment> findAll(Integer boardId) {
        return commentRepository.findByBoardId(boardId);
    }

    //댓글 삭제
    public boolean deleteComment(Integer commentId, String memberId) {
        Optional<Comment> optionalComment = commentRepository.findById(commentId);
        if (optionalComment.isPresent()) {
            Comment comment = optionalComment.get();
            if (comment.getMemberId().equals(memberId)) {
                commentRepository.deleteById(commentId);
                return true;
            }
        }
        return false;
    }
}

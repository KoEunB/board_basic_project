package com.map_study.service;

import com.map_study.entity.Comment;
import com.map_study.repository.CommentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {

    private final CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    // 댓글 저장
    public void save(Comment comment) {
        commentRepository.save(comment);
    }

    //특정 게시글 댓글 조회
    public List<Comment> findAll(Integer boardId) {
        return commentRepository.findByBoardId(boardId);
    }
}

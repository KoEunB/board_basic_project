package com.map_study.controller;

import com.map_study.entity.Comment;
import com.map_study.service.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/free-comment")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/save")
    public @ResponseBody List<Comment> save(@RequestBody Comment comment) {
        commentService.save(comment);
        return commentService.findAll(comment.getBoardId());
    }

    @DeleteMapping("/delete/{commentId}/{memberId}")
    public ResponseEntity<String> deleteComment(@PathVariable("commentId") Integer commentId,
                                                @PathVariable("memberId") String memberId) {
        boolean deleted = commentService.deleteComment(commentId, memberId);
        if (deleted) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("삭제 권한이 없습니다.");
        }
    }
}

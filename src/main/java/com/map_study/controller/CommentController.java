package com.map_study.controller;

import com.map_study.entity.Comment;
import com.map_study.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/free-comment")
@Tag(name = "자유게시판 댓글 API", description = "게시글 댓글 관리 기능 제공")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @Operation(summary = "댓글 저장", description = "게시글에 댓글을 저장합니다.")
    @PostMapping("/save")
    public @ResponseBody List<Comment> save(@RequestBody Comment comment) {
        commentService.save(comment);
        return commentService.findAll(comment.getBoardId());
    }

    @Operation(summary = "댓글 삭제", description = "댓글 ID와 회원 ID를 받아 댓글을 삭제합니다.")
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

package com.map_study.controller;

import com.map_study.entity.SecretComment;
import com.map_study.service.SecretCommentService;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/secret-comment")
public class SecretCommentController {

    private final SecretCommentService secretCommentService;

    public SecretCommentController(SecretCommentService secretCommentService) {
        this.secretCommentService = secretCommentService;
    }

    @PostMapping("/save")
    public List<SecretComment> save(@RequestBody SecretComment secretcomment) {
        secretCommentService.save(secretcomment);
        return secretCommentService.findAll(secretcomment.getBoardId());
    }

    @DeleteMapping("/delete/{commentId}/{memberId}")
    public ResponseEntity<String> deleteSecretComment(@PathVariable("commentId") Integer commentId,
                                                      @PathVariable("memberId") String memberId) {
        boolean deleted = secretCommentService.deleteSecretComment(commentId, memberId);
        if (deleted) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("삭제 권한이 없습니다.");
        }
    }
}


package com.map_study.controller;

import com.map_study.service.BoardLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/board/like")
@RequiredArgsConstructor
public class BoardLikeController {

    private final BoardLikeService boardLikeService;

    // 좋아요 추가
    @PostMapping("/{memberId}/{articleId}")
    public ResponseEntity<String> addLike(
            @PathVariable("memberId") Integer memberId,
            @PathVariable("articleId") Integer articleId) {

        boardLikeService.addLike(memberId, articleId);
        return ResponseEntity.ok("좋아요 추가됨");
    }

    //좋아요 삭제
    @DeleteMapping("/{memberId}/{articleId}")
    public ResponseEntity<String> removeLike(
            @PathVariable("memberId") Integer memberId,
            @PathVariable("articleId") Integer articleId) {

        boardLikeService.removeLike(memberId, articleId);
        return ResponseEntity.ok("좋아요 삭제됨");
    }

    //좋아요 여부 확인
    @GetMapping("/{memberId}/{articleId}")
    public ResponseEntity<Boolean> checkLike(
            @PathVariable("memberId") Integer memberId,
            @PathVariable("articleId") Integer articleId) {

        return ResponseEntity.ok(boardLikeService.isLiked(memberId, articleId));
    }
}




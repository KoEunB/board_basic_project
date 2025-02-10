package com.map_study.controller;

import com.map_study.service.HeartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/like")
@RequiredArgsConstructor
public class HeartController {

    private final HeartService heartService;

    // 좋아요 추가
    @PostMapping("/{memberId}/{articleId}")
    public ResponseEntity<String> addLike(
            @PathVariable("memberId") Integer memberId,
            @PathVariable("articleId") Integer articleId) {

        heartService.addLike(memberId, articleId);
        return ResponseEntity.ok("좋아요 추가됨");
    }

    //좋아요 삭제
    @DeleteMapping("/{memberId}/{articleId}")
    public ResponseEntity<String> removeLike(
            @PathVariable("memberId") Integer memberId,
            @PathVariable("articleId") Integer articleId) {

        heartService.removeLike(memberId, articleId);
        return ResponseEntity.ok("좋아요 삭제됨");
    }

    //좋아요 여부 확인
    @GetMapping("/{memberId}/{articleId}")
    public ResponseEntity<Boolean> checkLike(
            @PathVariable("memberId") Integer memberId,
            @PathVariable("articleId") Integer articleId) {

        return ResponseEntity.ok(heartService.isLiked(memberId, articleId));
    }
}




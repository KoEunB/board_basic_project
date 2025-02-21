package com.map_study.controller;

import com.map_study.service.SecretBoardLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/secret-board/like")
@RequiredArgsConstructor
public class SecretBoardLikeController {

    private final SecretBoardLikeService secretBoardLikeService;

    // 좋아요 추가
    @PostMapping("/{memberId}/{boardId}")
    public ResponseEntity<String> addLike(
            @PathVariable("memberId") Integer memberId,
            @PathVariable("boardId") Integer boardId) {

        secretBoardLikeService.addLike(memberId, boardId);
        return ResponseEntity.ok("좋아요 추가됨");
    }

    //좋아요 삭제
    @DeleteMapping("/{memberId}/{boardId}")
    public ResponseEntity<String> removeLike(
            @PathVariable("memberId") Integer memberId,
            @PathVariable("boardId") Integer boardId) {

        secretBoardLikeService.removeLike(memberId, boardId);
        return ResponseEntity.ok("좋아요 삭제됨");
    }

    //좋아요 여부 확인
    @GetMapping("/{memberId}/{boardId}")
    public ResponseEntity<Boolean> checkLike(
            @PathVariable("memberId") Integer memberId,
            @PathVariable("boardId") Integer boardId) {

        return ResponseEntity.ok(secretBoardLikeService.isLiked(memberId, boardId));
    }
}




package com.map_study.controller;

import com.map_study.service.BoardLikeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/free-board/like")
@RequiredArgsConstructor
@Tag(name = "자유게시판 좋아요 API", description = "게시글 좋아요 기능 제공")
public class BoardLikeController {

    private final BoardLikeService boardLikeService;

    // 좋아요 추가
    @Operation(summary = "좋아요 추가", description = "회원 ID와 게시글 ID를 받아 좋아요를 추가합니다.")
    @PostMapping("/{boardId}/{memberId}")
    public ResponseEntity<String> addLike(
            @PathVariable("memberId") String memberId,
            @PathVariable("boardId") Integer boardId) {

        boardLikeService.addLike(memberId, boardId);
        return ResponseEntity.ok("좋아요 추가됨");
    }

    //좋아요 삭제
    @Operation(summary = "좋아요 삭제", description = "회원 ID와 게시글 ID를 받아 좋아요를 삭제합니다.")
    @DeleteMapping("/{boardId}/{memberId}")
    public ResponseEntity<String> removeLike(
            @PathVariable("memberId") String memberId,
            @PathVariable("boardId") Integer boardId) {

        boardLikeService.removeLike(memberId, boardId);
        return ResponseEntity.ok("좋아요 삭제됨");
    }

    //좋아요 여부 확인
    @Operation(summary = "좋아요 여부 확인", description = "회원 ID와 게시글 ID를 받아 좋아요 여부를 확인합니다.")
    @GetMapping("/{boardId}/{memberId}")
    public ResponseEntity<Boolean> checkLike(
            @PathVariable("memberId") String memberId,
            @PathVariable("boardId") Integer boardId) {

        return ResponseEntity.ok(boardLikeService.isLiked(memberId, boardId));
    }
}
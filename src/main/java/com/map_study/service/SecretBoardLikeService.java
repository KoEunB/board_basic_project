package com.map_study.service;

import com.map_study.entity.SecretBoard;
import com.map_study.entity.SecretBoardLike;
import com.map_study.repository.SecretBoardLikeRepository;
import com.map_study.repository.SecretBoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SecretBoardLikeService {

    private final SecretBoardLikeRepository secretBoardLikeRepository;
    private final SecretBoardRepository secretBoardRepository;

    //좋아요 추가
    @Transactional
    public void addLike(Integer memberId, Integer boardId) {
        if (!secretBoardLikeRepository.existsByMemberIdAndBoardId(memberId, boardId)) {
            SecretBoardLike like = new SecretBoardLike();
            like.setMemberId(memberId);
            like.setBoardId(boardId);
            secretBoardLikeRepository.save(like);

            // 게시글의 likeCount 증가
            Optional<SecretBoard> board = secretBoardRepository.findById(boardId);
            board.ifPresent(b -> {
                b.setHeartCount(b.getHeartCount() + 1);
                secretBoardRepository.save(b);
            });
        }
    }

    //좋아요 삭제
    @Transactional
    public void removeLike(Integer memberId, Integer boardId) {
        secretBoardLikeRepository.findByMemberIdAndBoardId(memberId, boardId).ifPresent(like -> {
            secretBoardLikeRepository.delete(like);

            // 게시글의 likeCount 감소
            Optional<SecretBoard> board = secretBoardRepository.findById(boardId);
            board.ifPresent(b -> {
                b.setHeartCount(Math.max(0, b.getHeartCount() - 1)); // 최소 0 유지
                secretBoardRepository.save(b);
            });
        });
    }

    //좋아요 여부 확인
    public boolean isLiked(Integer memberId, Integer boardId) {
        return secretBoardLikeRepository.existsByMemberIdAndBoardId(memberId, boardId);
    }
}




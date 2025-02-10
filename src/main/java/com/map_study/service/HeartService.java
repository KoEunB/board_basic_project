package com.map_study.service;

import com.map_study.entity.Board;
import com.map_study.entity.Heart;
import com.map_study.repository.BoardRepository;
import com.map_study.repository.HeartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class HeartService {

    private final HeartRepository heartRepository;
    private final BoardRepository boardRepository;

    //좋아요 추가
    @Transactional
    public void addLike(Integer memberId, Integer articleId) {
        if (!heartRepository.existsByMemberIdAndArticleId(memberId, articleId)) {
            Heart like = new Heart();
            like.setMemberId(memberId);
            like.setArticleId(articleId);
            heartRepository.save(like);

            // 게시글의 likeCount 증가
            Optional<Board> board = boardRepository.findById(articleId);
            board.ifPresent(b -> {
                b.setHeartCount(b.getHeartCount() + 1);
                boardRepository.save(b);
            });
        }
    }

    //좋아요 삭제
    @Transactional
    public void removeLike(Integer memberId, Integer articleId) {
        heartRepository.findByMemberIdAndArticleId(memberId, articleId).ifPresent(like -> {
            heartRepository.delete(like);

            // 게시글의 likeCount 감소
            Optional<Board> board = boardRepository.findById(articleId);
            board.ifPresent(b -> {
                b.setHeartCount(Math.max(0, b.getHeartCount() - 1)); // 최소 0 유지
                boardRepository.save(b);
            });
        });
    }

    //좋아요 여부 확인
    public boolean isLiked(Integer memberId, Integer articleId) {
        return heartRepository.existsByMemberIdAndArticleId(memberId, articleId);
    }
}




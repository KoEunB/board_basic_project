package com.map_study.service;

import com.map_study.entity.Board;
import com.map_study.entity.BoardLike;
import com.map_study.repository.BoardRepository;
import com.map_study.repository.BoardLikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BoardLikeService {

    private final BoardLikeRepository boardLikeRepository;
    private final BoardRepository boardRepository;

    //좋아요 추가
    @Transactional
    public void addLike(String memberId, Integer boardId) {
        if (!boardLikeRepository.existsByMemberIdAndBoardId(memberId, boardId)) {
            Board board = boardRepository.findById(boardId)
                    .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));

            BoardLike like = new BoardLike();
            like.setMemberId(memberId);
            like.setBoardId(boardId);
            boardLikeRepository.save(like);

            board.setHeartCount(board.getHeartCount() + 1);
            boardRepository.save(board);
        }
    }

    //좋아요 삭제
    @Transactional
    public void removeLike(String memberId, Integer boardId) {
        boardLikeRepository.findByMemberIdAndBoardId(memberId, boardId).ifPresent(like -> {
            boardLikeRepository.delete(like);

            // 게시글의 likeCount 감소
            Optional<Board> board = boardRepository.findById(boardId);
            board.ifPresent(b -> {
                b.setHeartCount(Math.max(0, b.getHeartCount() - 1)); // 최소 0 유지
                boardRepository.save(b);
            });
        });
    }

    //좋아요 여부 확인
    public boolean isLiked(String memberId, Integer boardId) {
        return boardLikeRepository.existsByMemberIdAndBoardId(memberId, boardId);
    }
}
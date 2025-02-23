package com.map_study.service;

import com.map_study.entity.Board;
import com.map_study.repository.BoardRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.map_study.entity.BoardCategory; // 패키지 경로에 맞게 수정


import java.io.File;
import java.util.UUID;

@Service
@Slf4j
public class BoardService {

    @Autowired
    private BoardRepository boardRepository;

    //글 작성 처리
    public void boardWrite(Board board, MultipartFile file) throws Exception {
        if (!file.isEmpty()) {
            String uploadDir = System.getProperty("user.dir") + "/upload";
            File uploadFolder = new File(uploadDir);
            if (!uploadFolder.exists()) {
                uploadFolder.mkdirs();
            }

            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            File saveFile = new File(uploadDir, fileName);
            file.transferTo(saveFile);

            board.setFilename(fileName);
            board.setFilepath("/files/" + fileName);
        }

        // 임시로 memberId 지정 (ex. "testUser")
        if (board.getMemberId() == null || board.getMemberId().isEmpty()) {
            board.setMemberId("testUser"); // 나중에 로그인 기능이 생기면 변경
        }

        boardRepository.save(board);
    }


    // 카테고리별 게시글 리스트 처리
    public Page<Board> boardList(Pageable pageable, BoardCategory category) {
        if (category == null || category == BoardCategory.ALL) {
            return boardRepository.findAll(pageable);
        } else {
            return boardRepository.findByCategory(category, pageable);
        }
    }

    public Page<Board> boardSearchList(String searchKeyword, Pageable pageable) {

        return boardRepository.findByTitleContaining(searchKeyword, pageable);
    }

    public Page<Board> boardSearchListByCategory(String searchKeyword, BoardCategory category, Pageable pageable) {
        return boardRepository.findByTitleContainingAndCategory(searchKeyword, category, pageable);
    }


    //특정 게시글 불러오기
    @Transactional
    public Board boardView(Integer boardId) {

        Board board = boardRepository.findById(boardId).get();

        //조회수
        board.setViewCount(board.getViewCount() + 1);

        return board;
    }

    //특정 게시글 삭제
    public void boardDelete(Integer boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));

        boardRepository.delete(board);
    }

}

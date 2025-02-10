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

import java.beans.Transient;
import java.io.File;
import java.util.UUID;

@Service
@Slf4j
public class BoardService {

    @Autowired
    private BoardRepository boardRepository;

    //글 작성 처리
    public void boardWrite(Board board, MultipartFile file) throws Exception {

        String projectPath = System.getProperty("user.dir") + "\\src\\main\\resources\\static\\files";

        UUID uuid = UUID.randomUUID();

        String fileName = uuid.toString() + "_" + file.getOriginalFilename();

        File saveFile = new File(projectPath, "fileName");

        file.transferTo(saveFile);

        //db에 저장
        board.setFilename(fileName);
        board.setFilepath("/files/" + fileName);

        boardRepository.save(board);

    }

    //게시글 리스트 처리
    public Page<Board> boardList(Pageable pageable) {

        return boardRepository.findAll(pageable);
    }

    public Page<Board> boardSearchList(String searchKeyword, Pageable pageable) {

        return boardRepository.findByTitleContaining(searchKeyword, pageable);
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

        boardRepository.deleteById(boardId);
    }

}

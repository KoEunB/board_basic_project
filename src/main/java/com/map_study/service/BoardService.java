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
            // upload 폴더에 저장 (static이 아닌 외부 접근 가능한 경로로)
            String uploadDir = System.getProperty("user.dir") + "/upload";
            File uploadFolder = new File(uploadDir);
            if (!uploadFolder.exists()) {
                uploadFolder.mkdirs(); // 폴더 없으면 생성
            }

            // 파일 이름에 UUID로 중복 방지
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            File saveFile = new File(uploadDir, fileName);
            file.transferTo(saveFile);

            // 파일 정보 DB 저장 (정적 리소스 접근 경로 설정)
            board.setFilename(fileName);
            board.setFilepath("/files/" + fileName);
        }

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

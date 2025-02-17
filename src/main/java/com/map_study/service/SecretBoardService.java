package com.map_study.service;

import com.map_study.entity.SecretBoard;
import com.map_study.repository.SecretBoardRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.UUID;

@Service
public class SecretBoardService {

    @Autowired
    private SecretBoardRepository secretBoardRepository;

    //글 작성 처리
    public void secretboardWrite(SecretBoard secretBoard, MultipartFile file) throws Exception {
        if (!file.isEmpty()) {
            // secretupload 폴더에 저장
            String uploadDir = System.getProperty("user.dir") + "/secretupload";
            File uploadFolder = new File(uploadDir);
            if (!uploadFolder.exists()) {
                uploadFolder.mkdirs(); // 폴더 없으면 생성
            }

            // 파일 이름에 UUID로 중복 방지
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            File saveFile = new File(uploadDir, fileName);
            file.transferTo(saveFile);

            // 파일 정보 DB 저장 (정적 리소스 접근 경로 설정)
            secretBoard.setFilename(fileName);
            secretBoard.setFilepath("/secretfiles/" + fileName);
        }

        secretBoardRepository.save(secretBoard);
    }

    //게시글 리스트 처리
    public Page<SecretBoard> secretboardList(Pageable pageable) {

        return secretBoardRepository.findAll(pageable);
    }

    public Page<SecretBoard> secretboardSearchList(String searchKeyword, Pageable pageable) {

        return secretBoardRepository.findByTitleContaining(searchKeyword, pageable);
    }

    //특정 게시글 불러오기
    @Transactional
    public SecretBoard secretboardView(Integer boardId) {

        SecretBoard secretBoard = secretBoardRepository.findById(boardId).get();

        //조회수
        secretBoard.setViewCount(secretBoard.getViewCount() + 1);

        return secretBoard;
    }

    //특정 게시글 삭제
    public void secretboardDelete(Integer boardId) {

        secretBoardRepository.deleteById(boardId);
    }

    // 게시글 수정
    public void updatesecretBoard(SecretBoard secretBoard) {

        secretBoardRepository.save(secretBoard); // 기존 데이터 수정
    }
}

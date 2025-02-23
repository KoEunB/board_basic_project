package com.map_study.service;

import com.map_study.entity.Board;
import com.map_study.entity.BoardCategory;
import com.map_study.entity.SecretBoard;
import com.map_study.entity.SecretBoardCategory;
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

        // 임시로 memberId 지정 (ex. "testUser")
        if (secretBoard.getMemberId() == null || secretBoard.getMemberId().isEmpty()) {
            secretBoard.setMemberId("testUser"); // 나중에 로그인 기능이 생기면 변경
        }

        secretBoardRepository.save(secretBoard);
    }

    //카테고리별 게시글 리스트 처리
    public Page<SecretBoard> secretboardList(Pageable pageable, SecretBoardCategory category) {
        if (category == null) { //카테고리가 없으면 기본값을 족보로 설정
            category = SecretBoardCategory.PAST_EXAMS;
        }
        return secretBoardRepository.findBycategory(category, pageable);
    }


    public Page<SecretBoard> secretboardSearchList(String searchKeyword, Pageable pageable) {

        return secretBoardRepository.findByTitleContaining(searchKeyword, pageable);
    }

    public Page<SecretBoard> secretboardSearchListByCategory(String searchKeyword, SecretBoardCategory category, Pageable pageable) {
        return secretBoardRepository.findByTitleContainingAndCategory(searchKeyword, category, pageable);
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
        SecretBoard secretBoard = secretBoardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));

        secretBoardRepository.delete(secretBoard);
    }

}
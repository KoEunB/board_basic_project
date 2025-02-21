package com.map_study.controller;
import com.map_study.entity.*;
import com.map_study.service.SecretBoardService;
import com.map_study.service.SecretCommentService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@RequestMapping("/secret-board")
public class SecretBoardController {

    private final SecretBoardService secretBoardService;
    private final SecretCommentService secretCommentService;

    public SecretBoardController(SecretBoardService secretBoardService, SecretCommentService secretCommentService) {
        this.secretBoardService = secretBoardService;
        this.secretCommentService = secretCommentService;
    }

    //비밀번호 입력
    @GetMapping("/enter")
    public String secretBoardEnter() {
        return "secretLogin"; // 비밀번호 입력 페이지
    }

    // 🔑 비밀번호 확인 후 세션에 저장
    @PostMapping("/enter")
    public String verifySecretBoard(@RequestParam("password") String password, Model model) {
        String correctPassword = "1234"; // 관리자가 설정한 비밀번호 (DB에서 가져와도 됨)
        if (password.equals(correctPassword)) {
            model.addAttribute("secretBoardPassword", password);
            return "redirect:/secret-board/list";
        }
        model.addAttribute("message", "비밀번호가 틀렸습니다.");
        return "secretLogin";
    }

    @GetMapping("/write")
    public String secretboardWriteForm() {

        return "secretboardwrite";
    }

    @PostMapping("/writepro")
    public String secretboardWritePro(@ModelAttribute SecretBoard secretBoard,
                                      @RequestParam("category") SecretBoardCategory category,
                                      @RequestParam(name = "file", required = false) MultipartFile file,
                                      Model model) throws Exception {

        secretBoard.setCategory(category); //카테고리 설정
        secretBoardService.secretboardWrite(secretBoard, file);

        model.addAttribute("message", "글 작성이 완료되었습니다.");
        model.addAttribute("searchUrl", "/secret-board/list?category=" + category);

        return "message";
    }

    // 카테고리별 게시글 조회
    @GetMapping("/list")
    public String secretboardList(Model model,
                            @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
                            @RequestParam(name = "searchKeyword", defaultValue = "") String searchKeyword,
                            @RequestParam(name = "category", required = false) SecretBoardCategory category) {

        Page<SecretBoard> list;

        if (!searchKeyword.isEmpty() && category != null) {
            // 카테고리와 키워드가 모두 있는 경우
            list = secretBoardService.secretboardSearchListByCategory(searchKeyword, category, pageable);
        } else if (!searchKeyword.isEmpty()) {
            // 검색 키워드만 있는 경우
            list = secretBoardService.secretboardSearchList(searchKeyword, pageable);
        } else if (category != null) {
            // 카테고리만 있는 경우
            list = secretBoardService.secretboardList(pageable, category);
        } else {
            // 아무 필터도 없는 경우 전체 조회
            list = secretBoardService.secretboardList(pageable, null);
        }

        int nowPage = list.getPageable().getPageNumber() + 1;
        int startPage = Math.max(nowPage - 4, 1);
        int endPage = Math.min(nowPage + 5, list.getTotalPages());

        model.addAttribute("list", list);
        model.addAttribute("nowPage", nowPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("selectedCategory", category); // 선택된 카테고리 유지
        model.addAttribute("searchKeyword", searchKeyword); // 검색 키워드 유지

        return "secretboardlist";
    }

    @GetMapping("/view")
    public String secretboardView(Model model, @RequestParam("boardId") Integer boardId) {

        model.addAttribute("secretboard", secretBoardService.secretboardView(boardId));

        //댓글
        List<SecretComment> commentList = secretCommentService.findAll(boardId);
        model.addAttribute("secretCommentList", commentList);

        return "secretboardview";
    }

    @GetMapping("/delete")
    public String secretboardDelete(Model model, @RequestParam("boardId") Integer boardId) {

        secretBoardService.secretboardDelete(boardId);
        return "redirect:/secret-board/list";
    }

    @GetMapping("/modify/{boardId}")
    public String secretboardModify(Model model, @PathVariable("boardId") Integer boardId) {

        model.addAttribute("secretboard", secretBoardService.secretboardView(boardId));

        return "secretboardmodify";
    }

    @PostMapping("/update/{boardId}")
    public String boardUpdate(@PathVariable("boardId") Integer boardId,
                              SecretBoard secretBoard,
                              Model model,
                              @RequestParam(name = "file", required = false) MultipartFile file,
                              @RequestParam(name = "deleteImage", required = false) String deleteImage) throws Exception {

        SecretBoard boardTemp = secretBoardService.secretboardView(boardId);
        boardTemp.setTitle(secretBoard.getTitle());
        boardTemp.setContent(secretBoard.getContent());

        // 이미지 삭제 체크되었을 경우 기존 이미지 제거
        if (deleteImage != null) {
            boardTemp.setFilename(null);
            boardTemp.setFilepath(null);
        }

        // 새로운 파일 업로드
        secretBoardService.secretboardWrite(boardTemp, file);

        model.addAttribute("message", "글 수정이 완료되었습니다.");
        model.addAttribute("searchUrl", "/secret-board/list?category=" + boardTemp.getCategory());

        return "message";
    }
}

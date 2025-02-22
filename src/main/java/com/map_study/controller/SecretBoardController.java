package com.map_study.controller;
import com.map_study.entity.*;
import com.map_study.service.SecretBoardService;
import com.map_study.service.SecretCommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "비밀게시판 API", description = "게시글 작성, 조회, 수정, 삭제 기능 제공")
public class SecretBoardController {

    private final SecretBoardService secretBoardService;
    private final SecretCommentService secretCommentService;

    public SecretBoardController(SecretBoardService secretBoardService, SecretCommentService secretCommentService) {
        this.secretBoardService = secretBoardService;
        this.secretCommentService = secretCommentService;
    }

    //비밀번호 입력
    @Operation(summary = "비밀번호 입력 페이지", description = "비밀게시판 접근을 위한 비밀번호 입력 페이지를 반환합니다.")
    @GetMapping("/enter")
    public String secretBoardEnter() {
        return "secretLogin"; // 비밀번호 입력 페이지
    }

    // 🔑 비밀번호 확인 후 세션에 저장
    @Operation(summary = "비밀번호 확인", description = "입력된 비밀번호를 확인 후 세션에 저장합니다.")
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


    @Operation(summary = "게시글 작성 페이지", description = "비밀게시판 글 작성 페이지를 반환합니다.")
    @GetMapping("/write")
    public String secretboardWriteForm() {

        return "secretboardwrite";
    }

    @Operation(summary = "게시글 작성", description = "새로운 비밀게시판 글을 작성합니다.")
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
    @Operation(summary = "게시글 목록 조회", description = "카테고리 및 검색 키워드에 따라 비밀게시판의 게시글을 조회합니다.")
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

    @Operation(summary = "게시글 조회", description = "게시글 ID를 이용하여 비밀게시판 게시글을 조회합니다.")
    @GetMapping("/view")
    public String secretboardView(Model model, @RequestParam("boardId") Integer boardId) {

        model.addAttribute("secretboard", secretBoardService.secretboardView(boardId));

        //댓글
        List<SecretComment> commentList = secretCommentService.findAll(boardId);
        model.addAttribute("secretCommentList", commentList);

        return "secretboardview";
    }

    @Operation(summary = "게시글 삭제", description = "게시글 ID를 이용하여 비밀게시판 게시글을 삭제합니다.")
    @GetMapping("/delete")
    public String secretboardDelete(Model model, @RequestParam("boardId") Integer boardId) {

        secretBoardService.secretboardDelete(boardId);
        return "redirect:/secret-board/list";
    }

    @Operation(summary = "게시글 수정 페이지 이동", description = "게시글 ID를 기반으로 수정 페이지로 이동합니다.")
    @GetMapping("/modify/{boardId}")
    public String secretboardModify(Model model, @PathVariable("boardId") Integer boardId) {

        model.addAttribute("secretboard", secretBoardService.secretboardView(boardId));

        return "secretboardmodify";
    }

    @Operation(summary = "게시글 수정", description = "게시글을 수정하고 저장합니다.")
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

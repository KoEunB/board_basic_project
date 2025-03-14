package com.map_study.controller;

import com.map_study.entity.Board;
import com.map_study.entity.BoardCategory;
import com.map_study.entity.Comment;
import com.map_study.service.BoardService;
import com.map_study.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@RestController
@Tag(name = "자유게시판 API", description = "게시글 작성, 조회, 수정, 삭제 기능 제공")
public class BoardController {

    private final BoardService boardService;
    private final CommentService commentService;

    public BoardController(BoardService boardService, CommentService commentService) {
        this.boardService = boardService;
        this.commentService = commentService;
    }

    @Operation(summary = "게시판 메인 페이지")
    @GetMapping("/")
    public String boardMain() {

        return "boardmain";
    }

    @Operation(summary = "게시글 작성 페이지 이동")
    @GetMapping("/free-board/write")
    public String boardWriteForm() {

        return "boardwrite";
    }

    @Operation(summary = "게시글 작성", description = "새로운 게시글을 작성합니다.")
    @PostMapping("/free-board/writepro")
    public String boardWritePro(@ModelAttribute Board board,
                                @RequestParam(name = "file", required = false) MultipartFile file,
                                Model model) throws Exception {

        boardService.boardWrite(board, file);

        model.addAttribute("message", "글 작성이 완료되었습니다.");
        model.addAttribute("searchUrl", "/free-board/list?category=" + board.getCategory());

        return "message";
    }

    @Operation(summary = "게시글 목록 조회", description = "카테고리와 검색 키워드에 따라 게시글 목록을 조회합니다.")
    // 카테고리별 게시글 조회
    @GetMapping("/free-board/list")
    public ResponseEntity<Page<Board>> boardList(
            @PageableDefault(page = 0, size = 12, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam(name = "searchKeyword", defaultValue = "") String searchKeyword,
            @RequestParam(name = "category", required = false) BoardCategory category) {

        Page<Board> list;

        if (!searchKeyword.isEmpty() && category != null) {
            list = boardService.boardSearchListByCategory(searchKeyword, category, pageable);
        } else if (!searchKeyword.isEmpty()) {
            list = boardService.boardSearchList(searchKeyword, pageable);
        } else if (category != null) {
            list = boardService.boardList(pageable, category);
        } else {
            list = boardService.boardList(pageable, null);
        }

        return ResponseEntity.ok(list);
    }


    @Operation(summary = "게시글 상세 조회", description = "게시글 ID를 통해 상세 정보를 조회합니다.")
    @GetMapping("/free-board/view")
    public String boardView(Model model, @RequestParam("boardId") Integer boardId) {

        model.addAttribute("board", boardService.boardView(boardId));

        //댓글
        List<Comment> commentList = commentService.findAll(boardId);
        model.addAttribute("commentList", commentList);

        return "boardview";
    }

    @Operation(summary = "게시글 삭제", description = "게시글 ID와 작성자 ID를 입력받아 게시글을 삭제합니다.")
    @GetMapping("/free-board/delete/{boardId}/{memberId}")
    public String boardDelete(Model model,
                              @PathVariable("boardId") Integer boardId,
                              @PathVariable("memberId") String memberId) {

        Board board = boardService.boardView(boardId);

        // 게시글이 존재하지 않을 경우 예외 처리
        if (board == null) {
            model.addAttribute("message", "존재하지 않는 게시글입니다.");
            model.addAttribute("searchUrl", "/free-board/list");
            return "message";
        }

        // URL의 memberId와 실제 작성자 ID가 다르면 삭제 불가
        if (!board.getMemberId().equals(memberId)) {
            model.addAttribute("message", "삭제할 권한이 없습니다.");
            model.addAttribute("searchUrl", "/free-board/list");
            return "message";
        }

        boardService.boardDelete(boardId);
        return "redirect:/free-board/list";
    }

    @Operation(summary = "게시글 수정 페이지 이동", description = "게시글 ID를 기반으로 수정 페이지로 이동합니다.")
    @GetMapping("/free-board/modify/{boardId}/{memberId}")
    public String boardModify(Model model,
                              @PathVariable("boardId") Integer boardId,
                              @PathVariable("memberId") String memberId) {

        Board board = boardService.boardView(boardId);

        // 작성자가 아닌 경우 접근 제한
        if (!board.getMemberId().equals(memberId)) {
            model.addAttribute("message", "수정할 권한이 없습니다.");
            model.addAttribute("searchUrl", "/free-board/list");
            return "message";
        }

        model.addAttribute("board", board);
        return "boardmodify";  // 템플릿 이름 유지
    }



    @Operation(summary = "게시글 수정", description = "게시글을 수정하고 저장합니다.")
    @PostMapping("/free-board/update/{boardId}/{memberId}")
    public String boardUpdate(@PathVariable("boardId") Integer boardId,
                              @PathVariable("memberId") String memberId, // 요청한 사용자
                              Board board,
                              Model model,
                              @RequestParam(name = "file", required = false) MultipartFile file,
                              @RequestParam(name = "deleteImage", required = false) String deleteImage) throws Exception {

        Board boardTemp = boardService.boardView(boardId);

        // 작성자가 아닌 경우 수정 불가
        if (!boardTemp.getMemberId().equals(memberId)) {
            model.addAttribute("message", "수정할 권한이 없습니다.");
            model.addAttribute("searchUrl", "/free-board/list");
            return "message";
        }

        boardTemp.setTitle(board.getTitle());
        boardTemp.setContent(board.getContent());

        // 이미지 삭제 체크되었을 경우 기존 이미지 제거
        if (deleteImage != null) {
            boardTemp.setFilename(null);
            boardTemp.setFilepath(null);
        }

        // 새로운 파일 업로드
        boardService.boardWrite(boardTemp, file);

        model.addAttribute("message", "글 수정이 완료되었습니다.");
        model.addAttribute("searchUrl", "/free-board/list?category=" + boardTemp.getCategory());

        return "message";
    }
}
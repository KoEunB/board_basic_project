package com.map_study.controller;

import com.map_study.entity.Board;
import com.map_study.entity.Comment;
import com.map_study.service.BoardService;
import com.map_study.service.CommentService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.io.File;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/board")
public class BoardController {

    private final BoardService boardService;
    private final CommentService commentService;

    public BoardController(BoardService boardService, CommentService commentService) {
        this.boardService = boardService;
        this.commentService = commentService;
    }

    @GetMapping("/write")
    public String boardWriteForm() {

        return "boardwrite";
    }

    @PostMapping("/writepro")
    public String boardWritePro(Board board, Model model, @RequestParam(name = "file", required = false) MultipartFile file) throws Exception {

        boardService.boardWrite(board, file);

        model.addAttribute("message", "글 작성이 완료되었습니다.");
        model.addAttribute("searchUrl", "/board/list");

        return "message";
    }

    @GetMapping("/list")
    public String boardList(Model model,
                            @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
                            @RequestParam(name = "searchKeyword", defaultValue = "") String searchKeyword) {

        Page<Board> list = null;

        if (searchKeyword == null) {
            list = boardService.boardList(pageable);
        } else {
            list = boardService.boardSearchList(searchKeyword, pageable);
        }

        int nowPage = list.getPageable().getPageNumber() + 1;
        int startPage = Math.max(nowPage - 4, 1);
        int endPage = Math.min(nowPage + 5, list.getTotalPages());

        model.addAttribute("list", list);
        model.addAttribute("nowPage", nowPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        return "boardlist";
    }

    @GetMapping("/view")
    public String boardView(Model model, @RequestParam("boardId") Integer boardId) {

        model.addAttribute("board", boardService.boardView(boardId));

        //댓글
        List<Comment> commentList = commentService.findAll(boardId);
        model.addAttribute("commentList", commentList);

        return "boardview";
    }

    @GetMapping("/delete")
    public String boardDelete(Model model, @RequestParam("boardId") Integer boardId) {

        boardService.boardDelete(boardId);
        return "redirect:/board/list";
    }

    @GetMapping("/modify/{boardId}")
    public String boardModify(Model model, @PathVariable("boardId") Integer boardId) {

        model.addAttribute("board", boardService.boardView(boardId));

        return "boardmodify";
    }

    @PostMapping("/update/{boardId}")
    public String boardUpdate(@PathVariable("boardId") Integer boardId,
                              Board board,
                              Model model,
                              @RequestParam(name = "file", required = false) MultipartFile file,
                              @RequestParam(name = "deleteImage", required = false) String deleteImage) throws Exception {

        Board boardTemp = boardService.boardView(boardId);
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
        model.addAttribute("searchUrl", "/board/list");

        return "message";
    }


}
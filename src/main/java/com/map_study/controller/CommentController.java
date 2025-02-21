package com.map_study.controller;

import com.map_study.entity.Comment;
import com.map_study.service.CommentService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/free-comment")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/save")
    public @ResponseBody List<Comment> save(@RequestBody Comment comment) {
        System.out.println("댓글 = " + comment );
        commentService.save(comment);

        //해당 게시글에 작성된 댓글 리스트를 가져옴
        List<Comment> commentsList = commentService.findAll(comment.getBoardId());
        return commentsList;
    }
}

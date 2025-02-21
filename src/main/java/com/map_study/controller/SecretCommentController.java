package com.map_study.controller;

import com.map_study.entity.SecretComment;
import com.map_study.service.SecretCommentService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/secret-comment")
public class SecretCommentController {

    private final SecretCommentService secretCommentService;

    public SecretCommentController(SecretCommentService secretCommentService) {
        this.secretCommentService = secretCommentService;
    }

    @PostMapping("/save")
    public @ResponseBody List<SecretComment> save(@RequestBody SecretComment secretcomment) {
        System.out.println("댓글 = " + secretcomment );
        secretCommentService.save(secretcomment);

        //해당 게시글에 작성된 댓글 리스트를 가져옴
        List<SecretComment> secretCommentList = secretCommentService.findAll(secretcomment.getBoardId());
        return secretCommentList;
    }
}

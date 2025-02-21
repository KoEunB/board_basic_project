package com.map_study.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class SecretBoardLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer heartId; //좋아요 식별 데이터
    private Integer boardId; //좋아요가 체크된 게시물 번호
    private Integer memberId; //좋아요를 체크한 회원 번호
}

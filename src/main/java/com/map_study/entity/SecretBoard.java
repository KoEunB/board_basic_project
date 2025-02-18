package com.map_study.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@Entity
@Data
public class SecretBoard {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String title;
    private String content;
    private String filename;
    private String filepath;

    @CreationTimestamp
    private Timestamp boardCreatedTime; //게시글 작성 시간

    @Column(name = "view_count", columnDefinition = "int default 0")
    private int viewCount;

    @Column(name = "heart_count", columnDefinition = "int default 0")
    private int  heartCount;

    //카테고리 추가
    @Enumerated(EnumType.STRING) // Enum을 문자열로 저장
    @Column(nullable = false)
    private SecretBoardCategory category = SecretBoardCategory.PAST_EXAMS;
}

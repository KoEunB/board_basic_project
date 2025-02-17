package com.map_study.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@Entity
@Data
public class SecretComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id; //댓글id
    private String commentWriter;
    private String commentContents;
    private Integer postId; //게시글id

    @CreationTimestamp
    private Timestamp commentCreatedTime; //댓글 작성 시간
}

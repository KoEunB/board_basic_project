package com.map_study.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String title;
    private String content;
    private String filename;
    private String filepath;

    @Column(name = "view_count", columnDefinition = "int default 0")
    private int viewCount;

    @Column(name = "heart_count", columnDefinition = "int default 0")
    private int  heartCount;

}

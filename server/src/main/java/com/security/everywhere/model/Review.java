package com.security.everywhere.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "Review")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(length = 100)
    private String contentId = "";          // 게시물 번호
    @Column(length = 100)
    private String userId = "";          // 유저 닉네임
    @Column(length = 100)
    private String date = "";               //입력 날짜
    @Column(length = 200)
    private String textcontent = "";       // 내용

    private double star = 0.0;       // 별점
    private int likecount = 0;               //좋아요 수


    public Review() {
        likecount = 0;
    }

    public Review(String contentId, double star, String date, String textcontent) {
        this.contentId = contentId;
        this.star = star;
        this.date = date;
        this.textcontent = textcontent;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getContentId() {
        return contentId;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTextcontent() {
        return textcontent;
    }

    public void setTextcontent(String textcontent) {
        this.textcontent = textcontent;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


    public double getStar() {
        return star;
    }

    public void setStar(double star) {
        this.star = star;
    }

    public int getLikecount() {
        return likecount;
    }

    public void setLikecount(int likecount) {
        this.likecount = likecount;
    }
}

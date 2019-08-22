package com.security.everywhere.model;

import com.security.everywhere.response.tourImages.ImagesItem;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "tourImages")
public class TourImages implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String contentid;       // 콘텐츠ID
    private String imgname;         // 이미지명
    private String originimgurl;    // 원본 이미지, 약 500*333 size
    private String serialnum;       // 이미지 일련번호
    private String smallimageurl;   // 썸네일 이미지, 약 160*100 size

    protected TourImages() {
    }

    public TourImages(ImagesItem item) {
        this.contentid = item.getContentid();
        this.imgname = item.getImgname();
        this.originimgurl = item.getOriginimgurl();
        this.serialnum = item.getSerialnum();
        this.smallimageurl = item.getSmallimageurl();
    }

    public TourImages(String contentid, String imgname, String originimgurl, String serialnum, String smallimageurl) {
        this.contentid = contentid;
        this.imgname = imgname;
        this.originimgurl = originimgurl;
        this.serialnum = serialnum;
        this.smallimageurl = smallimageurl;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getImgname() {
        return imgname;
    }

    public void setImgname(String imgname) {
        this.imgname = imgname;
    }

    public String getContentid() {
        return contentid;
    }

    public void setContentid(String contentid) {
        this.contentid = contentid;
    }

    public String getOriginimgurl() {
        return originimgurl;
    }

    public void setOriginimgurl(String originimgurl) {
        this.originimgurl = originimgurl;
    }

    public String getSerialnum() {
        return serialnum;
    }

    public void setSerialnum(String serialnum) {
        this.serialnum = serialnum;
    }

    public String getSmallimageurl() {
        return smallimageurl;
    }

    public void setSmallimageurl(String smallimageurl) {
        this.smallimageurl = smallimageurl;
    }
}

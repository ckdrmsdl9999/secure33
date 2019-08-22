package com.security.everywhere.response.tourImages;

public class ImagesItem {
    private String contentid = "default";       // 콘텐츠ID
    private String imgname = "default";         // 이미지명
    private String originimgurl = "default";    // 원본 이미지, 약 500*333 size
    private String serialnum = "default";       // 이미지 일련번호
    private String smallimageurl = "default";   // 썸네일 이미지, 약 160*100 size

    public ImagesItem(){}
    public String getContentid() {
        return contentid;
    }

    public void setContentid(String contentid) {
        this.contentid = contentid;
    }

    public String getImgname() {
        return imgname;
    }

    public void setImgname(String imgname) {
        this.imgname = imgname;
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

    @Override
    public String toString()
    {
        return originimgurl+"-originimgurl"+smallimageurl+"small image url";
    }
}

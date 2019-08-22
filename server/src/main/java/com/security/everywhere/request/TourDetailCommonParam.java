package com.security.everywhere.request;

/*
 * contentId만 필수, 나머지 옵션
 * */
public class TourDetailCommonParam {
    private String contentId;               // 콘텐츠 ID
    private String contentTypeId = "12";    // 관광타입(관광지, 숙박 등) ID

    public String getContentId() {
        return contentId;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

    public String getContentTypeId() {
        return contentTypeId;
    }

    public void setContentTypeId(String contentTypeId) {
        this.contentTypeId = contentTypeId;
    }
}

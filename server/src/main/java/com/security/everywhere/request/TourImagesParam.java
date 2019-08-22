package com.security.everywhere.request;

/*
* contentId만 필수, 나머지 옵션
* */
public class TourImagesParam {
    private String numOfRows = "10";        // 한 페이지 결과 수
    private String pageNo = "1";            // 현재 페이지 번호
    private String MobileOS = "ETC";        // IOS(아이폰),AND(안드로이드),WIN(원도우폰),ETC
    private String MobileApp = "AppTest";   // 서비스명=어플명
    private String contentId;               // 콘텐츠 ID
    private String imageYN = "Y";           // Y=콘텐츠 이미지 조회, N='음식점'타입의 음식메뉴 이미지
    private String subImageYN = "Y";        // Y=원본,썸네일 이미지 조회 N=Null

    public String getNumOfRows() {
        return numOfRows;
    }

    public void setNumOfRows(String numOfRows) {
        this.numOfRows = numOfRows;
    }

    public String getPageNo() {
        return pageNo;
    }

    public void setPageNo(String pageNo) {
        this.pageNo = pageNo;
    }

    public String getMobileOS() {
        return MobileOS;
    }

    public void setMobileOS(String mobileOS) {
        MobileOS = mobileOS;
    }

    public String getMobileApp() {
        return MobileApp;
    }

    public void setMobileApp(String mobileApp) {
        MobileApp = mobileApp;
    }

    public String getContentId() {
        return contentId;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

    public String getImageYN() {
        return imageYN;
    }

    public void setImageYN(String imageYN) {
        this.imageYN = imageYN;
    }

    public String getSubImageYN() {
        return subImageYN;
    }

    public void setSubImageYN(String subImageYN) {
        this.subImageYN = subImageYN;
    }
}

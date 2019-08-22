package com.security.everywhere.response.tourCommonInfo;

public class ComInfoItem {
    private String contentid;       // 콘텐츠ID
    private String contenttypeid;   // 콘텐츠타입ID
    private String booktour;        // 교과서 속 여행지 여부
    private String createdtime;     // 콘텐츠 최초 등록일
    private String homepage;        // 홈페이지 주소
    private String modifiedtime;    // 콘텐츠 수정일
    private String overview;        // 콘텐츠 개요 조회
    private String tel;             // 전화번호
    private String telname;         // 전화번호명
    private String title;           // 콘텐츠명(제목)
    private String mapx;
    private String mapy;
    private String addr1;
    private String firstimage;
    private String firstimage2;


    public String getContentid() {
        return contentid;
    }

    public void setContentid(String contentid) {
        this.contentid = contentid;
    }

    public String getBooktour() {
        return booktour;
    }

    public void setBooktour(String booktour) {
        this.booktour = booktour;
    }

    public String getContenttypeid() {
        return contenttypeid;
    }

    public void setContenttypeid(String contenttypeid) {
        this.contenttypeid = contenttypeid;
    }

    public String getCreatedtime() {
        return createdtime;
    }

    public void setCreatedtime(String createdtime) {
        this.createdtime = createdtime;
    }

    public String getHomepage() {
        return homepage;
    }

    public void setHomepage(String homepage) {
        this.homepage = homepage;
    }

    public String getModifiedtime() {
        return modifiedtime;
    }

    public void setModifiedtime(String modifiedtime) {
        this.modifiedtime = modifiedtime;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getTelname() {
        return telname;
    }

    public void setTelname(String telname) {
        this.telname = telname;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAddr1() { return addr1; }

    public void setAddr1(String addr1) { this.addr1 = addr1; }

    public String getMapx() { return mapx; }

    public void setMapx(String mapx) { this.mapx = mapx; }

    public String getMapy() { return mapy; }

    public void setMapy(String mapy) { this.mapy = mapy;}

    public String getFirstimage() { return firstimage; }

    public void setFirstimage(String mapy) { this.firstimage = firstimage;}

    public String getFirstimage2() { return firstimage2; }

    public void setFirstimage2(String mapy) { this.firstimage2 = firstimage2;}

    @Override
    public String toString(){
        return title+" "+addr1+" 좌표-"+mapx+" " +mapy+"return값(toString of cominfoitem)";
    }
}

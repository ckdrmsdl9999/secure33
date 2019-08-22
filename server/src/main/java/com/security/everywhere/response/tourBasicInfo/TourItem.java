package com.security.everywhere.response.tourBasicInfo;

public class TourItem {
    private String addr1 = "";          // 주소
    private String addr2 = "";          // 상세주소
    private String areacode = "";       // 지역코드
    private String booktour = "";       // 교과서 속 여행지 여부
    private String cat1 = "";           // 대분류 코드
    private String cat2 = "";           // 중분류 코드
    private String cat3 = "";           // 소분류 코드
    private String contentid = "";      // 콘텐츠ID
    private String contenttypeid = "";  // 관광타입(관광지, 숙박 등) ID
    private String createdtime = "";    // 콘텐츠 최초 등록일
    private String dist = "";           // 중심 좌표로부터 거리 (단위:m)
    private String firstimage= "";      // 원본 대표이미지 약 500x333 size
    private String firstimage2= "";     // 썸네일 대표이미지 약 150x100 size
    private String mapx= "";            // 경도
    private String mapy= "";            // 위도
    private String mlevel= "";          // map level 응답
    private String modifiedtime = "";   // 콘텐츠 수정일
    private String readcount= "";       // 콘텐츠 조회수
    private String sigungucode= "";     // 시군구 코드
    private String tel= "";             // 전화번호
    private String title= "";           // 콘텐츠 제목
    private String eventstartdate = ""; // 행사 시작일
    private String eventenddate = "";   // 행사 종료일

    public String getAddr1() {
        return addr1;
    }

    public void setAddr1(String addr1) {
        this.addr1 = addr1;
    }

    public String getAddr2() {
        return addr2;
    }

    public void setAddr2(String addr2) {
        this.addr2 = addr2;
    }

    public String getAreacode() {
        return areacode;
    }

    public void setAreacode(String areacode) {
        this.areacode = areacode;
    }

    public String getBooktour() {
        return booktour;
    }

    public void setBooktour(String booktour) {
        this.booktour = booktour;
    }

    public String getCat1() {
        return cat1;
    }

    public void setCat1(String cat1) {
        this.cat1 = cat1;
    }

    public String getCat2() {
        return cat2;
    }

    public void setCat2(String cat2) {
        this.cat2 = cat2;
    }

    public String getCat3() {
        return cat3;
    }

    public void setCat3(String cat3) {
        this.cat3 = cat3;
    }

    public String getContentid() {
        return contentid;
    }

    public void setContentid(String contentid) {
        this.contentid = contentid;
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

    public String getDist() {
        return dist;
    }

    public void setDist(String dist) {
        this.dist = dist;
    }

    public String getFirstimage() {
        return firstimage;
    }

    public void setFirstimage(String firstimage) {
        this.firstimage = firstimage;
    }

    public String getFirstimage2() {
        return firstimage2;
    }

    public void setFirstimage2(String firstimage2) {
        this.firstimage2 = firstimage2;
    }

    public String getMapx() {
        return mapx;
    }

    public void setMapx(String mapx) {
        this.mapx = mapx;
    }

    public String getMapy() {
        return mapy;
    }

    public void setMapy(String mapy) {
        this.mapy = mapy;
    }

    public String getMlevel() {
        return mlevel;
    }

    public void setMlevel(String mlevel) {
        this.mlevel = mlevel;
    }

    public String getModifiedtime() {
        return modifiedtime;
    }

    public void setModifiedtime(String modifiedtime) {
        this.modifiedtime = modifiedtime;
    }

    public String getReadcount() {
        return readcount;
    }

    public void setReadcount(String readcount) {
        this.readcount = readcount;
    }

    public String getSigungucode() {
        return sigungucode;
    }

    public void setSigungucode(String sigungucode) {
        this.sigungucode = sigungucode;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getEventstartdate() {
        return eventstartdate;
    }

    public void setEventstartdate(String eventstartdate) {
        this.eventstartdate = eventstartdate;
    }

    public String getEventenddate() {
        return eventenddate;
    }

    public void setEventenddate(String eventenddate) {
        this.eventenddate = eventenddate;
    }

    @Override
    public String toString(){
        return title+" "+addr1+"확인용리턴값";
    }

}

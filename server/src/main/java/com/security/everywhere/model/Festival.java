package com.security.everywhere.model;

import com.security.everywhere.response.tourBasicInfo.TourItem;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "festival")
public class Festival implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String addr1 = "";          // 주소
    private String addr2 = "";          // 상세주소
    private String areaCode = "";       // 지역코드
    private String bookTour = "";       // 교과서 속 여행지 여부
    private String cat1 = "";           // 대분류 코드
    private String cat2 = "";           // 중분류 코드
    private String cat3 = "";           // 소분류 코드
    private String contentId = "";      // 콘텐츠ID
    private String contentTypeId = "";  // 관광타입(관광지, 숙박 등) ID
    private String createdTime = "";    // 콘텐츠 최초 등록일
    private String firstImage= "";      // 원본 대표이미지 약 500x333 size
    private String firstImage2= "";     // 썸네일 대표이미지 약 150x100 size
    private String mapX= "0";            // 경도
    private String mapY= "0";            // 위도
    private String mLevel= "";          // map level 응답
    private String modifiedTime = "";   // 콘텐츠 수정일
    private String readCount= "";       // 콘텐츠 조회수
    private String sigunguCode= "";     // 시군구 코드
    private String tel= "";             // 전화번호
    private String title= "";           // 콘텐츠 제목
    private String eventStartDate = ""; // 행사 시작일
    private String eventEndDate = "";   // 행사 종료일
    @Lob
    private String homepage;        // 홈페이지 주소
    @Lob
    private String overview;        // 콘텐츠 개요 조회

    public Festival() {}

    public Festival(TourItem item, String homepage, String overview) {
        this.addr1 = item.getAddr1();
        this.addr2 = item.getAddr2();
        this.areaCode = item.getAreacode();
        this.bookTour = item.getBooktour();
        this.cat1 = item.getCat1();
        this.cat2 = item.getCat2();
        this.cat3 = item.getCat3();
        this.contentId = item.getContentid();
        this.contentTypeId = item.getContenttypeid();
        this.createdTime = item.getCreatedtime();
        this.firstImage = item.getFirstimage();
        this.firstImage2 = item.getFirstimage2();
        this.mapX = item.getMapx();
        this.mapY = item.getMapy();
        this.mLevel = item.getMlevel();
        this.modifiedTime = item.getModifiedtime();
        this.readCount = item.getReadcount();
        this.sigunguCode = item.getSigungucode();
        this.tel = item.getTel();
        this.title = item.getTitle();
        this.eventStartDate = item.getEventstartdate();
        this.eventEndDate = item.getEventenddate();
        this.homepage = homepage;
        this.overview = overview;
    }

    public String getHomepage() {
        return homepage;
    }

    public void setHomepage(String homepage) {
        this.homepage = homepage;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

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

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getBookTour() {
        return bookTour;
    }

    public void setBookTour(String bookTour) {
        this.bookTour = bookTour;
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

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    public String getFirstImage() {
        return firstImage;
    }

    public void setFirstImage(String firstImage) {
        this.firstImage = firstImage;
    }

    public String getFirstImage2() {
        return firstImage2;
    }

    public void setFirstImage2(String firstImage2) {
        this.firstImage2 = firstImage2;
    }

    public String getMapX() {
        return mapX;
    }

    public void setMapX(String mapX) {
        this.mapX = mapX;
    }

    public String getMapY() {
        return mapY;
    }

    public void setMapY(String mapY) {
        this.mapY = mapY;
    }

    public String getmLevel() {
        return mLevel;
    }

    public void setmLevel(String mLevel) {
        this.mLevel = mLevel;
    }

    public String getModifiedTime() {
        return modifiedTime;
    }

    public void setModifiedTime(String modifiedTime) {
        this.modifiedTime = modifiedTime;
    }

    public String getReadCount() {
        return readCount;
    }

    public void setReadCount(String readCount) {
        this.readCount = readCount;
    }

    public String getSigunguCode() {
        return sigunguCode;
    }

    public void setSigunguCode(String sigunguCode) {
        this.sigunguCode = sigunguCode;
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

    public String getEventStartDate() {
        return eventStartDate;
    }

    public void setEventStartDate(String eventStartDate) {
        this.eventStartDate = eventStartDate;
    }

    public String getEventEndDate() {
        return eventEndDate;
    }

    public void setEventEndDate(String eventEndDate) {
        this.eventEndDate = eventEndDate;
    }

    @Override
    public String toString() {
        return "Festival{" +
                "id=" + id +
                ", addr1='" + addr1 + '\'' +
                ", addr2='" + addr2 + '\'' +
                ", areaCode='" + areaCode + '\'' +
                ", bookTour='" + bookTour + '\'' +
                ", cat1='" + cat1 + '\'' +
                ", cat2='" + cat2 + '\'' +
                ", cat3='" + cat3 + '\'' +
                ", contentId='" + contentId + '\'' +
                ", contentTypeId='" + contentTypeId + '\'' +
                ", createdTime='" + createdTime + '\'' +
                ", firstImage='" + firstImage + '\'' +
                ", firstImage2='" + firstImage2 + '\'' +
                ", mapX='" + mapX + '\'' +
                ", mapY='" + mapY + '\'' +
                ", mLevel='" + mLevel + '\'' +
                ", modifiedTime='" + modifiedTime + '\'' +
                ", readCount='" + readCount + '\'' +
                ", sigunguCode='" + sigunguCode + '\'' +
                ", tel='" + tel + '\'' +
                ", title='" + title + '\'' +
                ", eventStartDate='" + eventStartDate + '\'' +
                ", eventEndDate='" + eventEndDate + '\'' +
                '}';
    }
}

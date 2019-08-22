package com.security.everywhere.request;

/*
 * 2개의 값 모두 필수
 * */
public class ObservatoryParam {
    private String mapx = "";
    private String mapy = "";
    private String contentid; //관광지 id 추가

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

    public String getContentid() {
        return contentid;
    }

    public void setContentid(String contentid) {
        this.contentid = contentid;}

}

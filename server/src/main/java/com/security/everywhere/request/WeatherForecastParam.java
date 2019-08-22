package com.security.everywhere.request;


/*
* 3개의 값 모두 필수
* */
public class WeatherForecastParam {
    private String addr;    // 축제 or 관광지 주소
    private String mapX="0";    // x 좌표
    private String mapY="0";    // y 좌표
    private String contentid; //관광지 id 추가

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
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

    public String getContentid() {
        return contentid;
    }

    public void setContentid(String contentid) {
        this.contentid = contentid;
    }
}

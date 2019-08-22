package com.security.everywhere.response.observatory;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="item")
@XmlAccessorType(XmlAccessType.FIELD)
public class ObservatoryItem {
    private String stationName; // 측정소 이름
    private String addr;        // 측정소가 위치한 주소
    private String tm;          // 거리(km)
    private String information;//구분자



    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getTm() {
        return tm;
    }

    public void setTm(String tm) {
        this.tm = tm;
    }

    public String getInformation() { return information; }

    public void setInformation(String information) { this.information = information;}
}

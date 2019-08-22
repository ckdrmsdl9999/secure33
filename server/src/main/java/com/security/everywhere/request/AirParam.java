package com.security.everywhere.request;

public class AirParam {
    private String stationName = "";
    private String dataTerm = "month";
    private String pageNo = "1";
    private String numOfRows = "1";
    private String ver = "1.3";

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public String getDataTerm() {
        return dataTerm;
    }

    public void setDataTerm(String dataTerm) {
        this.dataTerm = dataTerm;
    }

    public String getPageNo() {
        return pageNo;
    }

    public void setPageNo(String pageNo) {
        this.pageNo = pageNo;
    }

    public String getNumOfRows() {
        return numOfRows;
    }

    public void setNumOfRows(String numOfRows) {
        this.numOfRows = numOfRows;
    }

    public String getVer() {
        return ver;
    }

    public void setVer(String ver) {
        this.ver = ver;
    }
}

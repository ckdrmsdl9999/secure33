package com.security.everywhere.response.weatherMiddleTerm;

public class MiddleTermWeatherBody {
    private MiddleTermWeatherItems items;
    private String numOfRows;
    private String pageNo;
    private String totalCount;

    public MiddleTermWeatherItems getItems() {
        return items;
    }

    public void setItems(MiddleTermWeatherItems items) {
        this.items = items;
    }

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

    public String getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(String totalCount) {
        this.totalCount = totalCount;
    }
}

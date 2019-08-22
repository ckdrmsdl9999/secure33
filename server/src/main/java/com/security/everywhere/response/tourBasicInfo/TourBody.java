package com.security.everywhere.response.tourBasicInfo;

public class TourBody {
    private TourItems items;
    private String numOfRows;
    private String pageNo;
    private String totalCount;

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

    public TourItems getItems() {
        return items;
    }

    public void setItems(TourItems items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return "FestivalResponseBody{" +
                "numOfRows='" + numOfRows + '\'' +
                ", pageNo='" + pageNo + '\'' +
                ", totalCount='" + totalCount + '\'' +
                '}';
    }
}

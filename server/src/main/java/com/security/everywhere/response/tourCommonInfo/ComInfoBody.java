package com.security.everywhere.response.tourCommonInfo;

public class ComInfoBody {
    private ComInfoItems items;
    private String numOfRows;
    private String pageNo;
    private String totalCount;

    public ComInfoItems getItems() {
        return items;
    }

    public void setItems(ComInfoItems items) {
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

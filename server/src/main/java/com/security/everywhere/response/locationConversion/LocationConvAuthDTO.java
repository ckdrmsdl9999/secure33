package com.security.everywhere.response.locationConversion;

public class LocationConvAuthDTO {
    private String id;
    private LocationConvAuthItem result;
    private String errMsg;
    private String errCd;
    private String trId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocationConvAuthItem getResult() {
        return result;
    }

    public void setResult(LocationConvAuthItem result) {
        this.result = result;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public String getErrCd() {
        return errCd;
    }

    public void setErrCd(String errCd) {
        this.errCd = errCd;
    }

    public String getTrId() {
        return trId;
    }

    public void setTrId(String trId) {
        this.trId = trId;
    }
}

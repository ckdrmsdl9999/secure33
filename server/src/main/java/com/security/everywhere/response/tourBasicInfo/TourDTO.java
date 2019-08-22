package com.security.everywhere.response.tourBasicInfo;

public class TourDTO {
    private TourHeader header;
    private TourBody body = null;

    public TourHeader getHeader() {
        return header;
    }

    public void setHeader(TourHeader header) {
        this.header = header;
    }

    public TourBody getBody() {
        return body;
    }

    public void setBody(TourBody body) {
        this.body = body;
    }
}

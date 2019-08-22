package com.security.everywhere.response.tourImages;

public class ImagesDTO {
    private ImagesHeader header;
    private ImagesBody body;

    public ImagesHeader getHeader() {
        return header;
    }

    public void setHeader(ImagesHeader header) {
        this.header = header;
    }

    public ImagesBody getBody() {
        return body;
    }

    public void setBody(ImagesBody body) {
        this.body = body;
    }
}

package com.security.everywhere.response.observatory;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "response")
public class ObservatoryDTO {
    private ObservatoryHeader header;
    private ObservatoryBody body;

    public ObservatoryHeader getHeader() {
        return header;
    }

    public void setHeader(ObservatoryHeader header) {
        this.header = header;
    }

    public ObservatoryBody getBody() {
        return body;
    }

    public void setBody(ObservatoryBody body) {
        this.body = body;
    }
}

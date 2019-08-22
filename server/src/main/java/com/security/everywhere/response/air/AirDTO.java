package com.security.everywhere.response.air;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "response")
public class AirDTO {
    private AirHeader header;
    private AirBody body;

    public AirHeader getHeader() {
        return header;
    }

    public void setHeader(AirHeader header) {
        this.header = header;
    }

    public AirBody getBody() {
        return body;
    }

    public void setBody(AirBody body) {
        this.body = body;
    }
}

package com.security.everywhere.response.weatherMiddleTerm;

public class MiddleTermWeatherDTO {
    private MiddleTermWeatherHeader header;
    private MiddleTermWeatherBody body;

    public MiddleTermWeatherHeader getHeader() {
        return header;
    }

    public void setHeader(MiddleTermWeatherHeader header) {
        this.header = header;
    }

    public MiddleTermWeatherBody getBody() {
        return body;
    }

    public void setBody(MiddleTermWeatherBody body) {
        this.body = body;
    }
}

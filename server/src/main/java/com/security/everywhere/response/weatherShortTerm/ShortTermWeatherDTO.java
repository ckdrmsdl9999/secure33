package com.security.everywhere.response.weatherShortTerm;

public class ShortTermWeatherDTO {
    private ShortTermWeatherHeader header;
    private ShortTermWeatherBody body;

    public ShortTermWeatherHeader getHeader() {
        return header;
    }

    public void setHeader(ShortTermWeatherHeader header) {
        this.header = header;
    }

    public ShortTermWeatherBody getBody() {
        return body;
    }

    public void setBody(ShortTermWeatherBody body) {
        this.body = body;
    }
}

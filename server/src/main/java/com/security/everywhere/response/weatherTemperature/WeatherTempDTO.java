package com.security.everywhere.response.weatherTemperature;

public class WeatherTempDTO {
    private WeatherTempHeader header;
    private WeatherTempBody body;

    public WeatherTempHeader getHeader() {
        return header;
    }

    public void setHeader(WeatherTempHeader header) {
        this.header = header;
    }

    public WeatherTempBody getBody() {
        return body;
    }

    public void setBody(WeatherTempBody body) {
        this.body = body;
    }
}

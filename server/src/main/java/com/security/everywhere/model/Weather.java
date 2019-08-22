package com.security.everywhere.model;

public class Weather {
    private String state = "";          // 날씨 상태 예: 구름 많음
    private String minTemp = "";        // 최소 기온
    private String maxTemp = "";        // 최대 기온
    private String dayOfTheWeek = "";    // 요일

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getMinTemp() {
        return minTemp;
    }

    public void setMinTemp(String minTemp) {
        this.minTemp = minTemp;
    }

    public String getMaxTemp() {
        return maxTemp;
    }

    public void setMaxTemp(String maxTemp) {
        this.maxTemp = maxTemp;
    }

    public String getDayOfTheWeek() {
        return dayOfTheWeek;
    }

    public void setDayOfTheWeek(String dayOfTheWeek) {
        this.dayOfTheWeek = dayOfTheWeek;
    }
}

package com.security.everywhere.response.air;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="item")
@XmlAccessorType(XmlAccessType.FIELD)
public class AirItem {
    private String dataTime;        // 측정일
    private String mangName;        // 측정망 정보
    private String so2Value;        // 아황산가스 농도, 단위: ppm
    private String coValue;         // 일산화탄소 농도, 단위: ppm
    private String o3Value;         // 오존 농도, 단위: ppm
    private String no2Value;        // 이산화질소 농도, 단위: ppm
    private String pm10Value;       // 미세먼지(pm10) 농도, 단위: mg/m2
    private String pm10Value24;     // 미세먼지(pm10) 24시간 예측 이동 농도
    private String pm25Value;       // 미세먼지(pm25) 농도
    private String pm25Value24;     // 미세먼지(pm25 24시간 예측 이동 농도
    private String khaiValue;       // 통합대기 환경수치
    private String khaiGrade;       // 통합대기 환경지수    Grade 값 1:좋음, 2:보통, 3:나쁨, 4:매우나쁨
    private String so2Grade;        // 아황산가스 지수
    private String coGrade;         // 일산화탄소 지수
    private String o3Grade;         // 오존 지수
    private String no2Grade;        // 이산화질소 지수
    private String pm10Grade;       // 미세먼지(pm10) 24시간 등급
    private String pm25Grade;       // 미세먼지(pm25) 24시간 등급
    private String pm10Grade1h;     // 미세먼지(pm10) 1시간 등급
    private String pm25Grade1h;     // 미세먼지(pm25) 1시간 등급

    public String getDataTime() {
        return dataTime;
    }

    public void setDataTime(String dataTime) {
        this.dataTime = dataTime;
    }

    public String getMangName() {
        return mangName;
    }

    public void setMangName(String mangName) {
        this.mangName = mangName;
    }

    public String getSo2Value() {
        return so2Value;
    }

    public void setSo2Value(String so2Value) {
        this.so2Value = so2Value;
    }

    public String getCoValue() {
        return coValue;
    }

    public void setCoValue(String coValue) {
        this.coValue = coValue;
    }

    public String getO3Value() {
        return o3Value;
    }

    public void setO3Value(String o3Value) {
        this.o3Value = o3Value;
    }

    public String getNo2Value() {
        return no2Value;
    }

    public void setNo2Value(String no2Value) {
        this.no2Value = no2Value;
    }

    public String getPm10Value() {
        return pm10Value;
    }

    public void setPm10Value(String pm10Value) {
        this.pm10Value = pm10Value;
    }

    public String getPm10Value24() {
        return pm10Value24;
    }

    public void setPm10Value24(String pm10Value24) {
        this.pm10Value24 = pm10Value24;
    }

    public String getPm25Value() {
        return pm25Value;
    }

    public void setPm25Value(String pm25Value) {
        this.pm25Value = pm25Value;
    }

    public String getPm25Value24() {
        return pm25Value24;
    }

    public void setPm25Value24(String pm25Value24) {
        this.pm25Value24 = pm25Value24;
    }

    public String getKhaiValue() {
        return khaiValue;
    }

    public void setKhaiValue(String khaiValue) {
        this.khaiValue = khaiValue;
    }

    public String getKhaiGrade() {
        return khaiGrade;
    }

    public void setKhaiGrade(String khaiGrade) {
        this.khaiGrade = khaiGrade;
    }

    public String getSo2Grade() {
        return so2Grade;
    }

    public void setSo2Grade(String so2Grade) {
        this.so2Grade = so2Grade;
    }

    public String getCoGrade() {
        return coGrade;
    }

    public void setCoGrade(String coGrade) {
        this.coGrade = coGrade;
    }

    public String getO3Grade() {
        return o3Grade;
    }

    public void setO3Grade(String o3Grade) {
        this.o3Grade = o3Grade;
    }

    public String getNo2Grade() {
        return no2Grade;
    }

    public void setNo2Grade(String no2Grade) {
        this.no2Grade = no2Grade;
    }

    public String getPm10Grade() {
        return pm10Grade;
    }

    public void setPm10Grade(String pm10Grade) {
        this.pm10Grade = pm10Grade;
    }

    public String getPm25Grade() {
        return pm25Grade;
    }

    public void setPm25Grade(String pm25Grade) {
        this.pm25Grade = pm25Grade;
    }

    public String getPm10Grade1h() {
        return pm10Grade1h;
    }

    public void setPm10Grade1h(String pm10Grade1h) {
        this.pm10Grade1h = pm10Grade1h;
    }

    public String getPm25Grade1h() {
        return pm25Grade1h;
    }

    public void setPm25Grade1h(String pm25Grade1h) {
        this.pm25Grade1h = pm25Grade1h;
    }
}

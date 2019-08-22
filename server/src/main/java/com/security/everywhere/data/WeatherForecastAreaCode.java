package com.security.everywhere.data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class WeatherForecastAreaCode {
    private static Logger logger = LoggerFactory.getLogger(WeatherForecastAreaCode.class);
    private Map<String, String> areaList;

    public WeatherForecastAreaCode() {
        this.areaList = new HashMap<>();
        areaList.put("서울", "11B00000");
        areaList.put("인천", "11B00000");
        areaList.put("경기", "11B00000");
        areaList.put("강원", "11D10000");    // 영서와 영동으로 나눌 수 있는 방법이 없어서 영서를 기본값으로 함
//        areaList.put("강원도영서", "11D10000");
//        areaList.put("강원도영동", "11D20000");
        areaList.put("대전", "11C20000");
        areaList.put("세종", "11C20000");
        areaList.put("충청남도", "11C20000");
        areaList.put("충청북도", "11C10000");
        areaList.put("광주", "  11F20000");
        areaList.put("전라남도", "  11F20000");
        areaList.put("전라북도", "11F10000");
        areaList.put("대구", "11H10000");
        areaList.put("경상북도", "11H10000");
        areaList.put("부산", "11H20000");
        areaList.put("울산", "11H20000");
        areaList.put("경상남도", "11H20000");
        areaList.put("제주", "11G00000");
    }

    public Map<String, String> getAreaList() {
        return areaList;
    }
}

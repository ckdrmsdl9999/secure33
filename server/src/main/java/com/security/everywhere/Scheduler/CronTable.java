package com.security.everywhere.Scheduler;

import com.security.everywhere.configuration.GlobalPropertySource;
import com.security.everywhere.repository.FestivalRepository;
import com.security.everywhere.repository.TourImagesRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CronTable {

    private final FestivalRepository festivalRepository;
    private final TourImagesRepository tourImagesRepository;

    private final String apiServiceKey;

    public CronTable(FestivalRepository festivalRepository
            , TourImagesRepository tourImagesRepository
            , GlobalPropertySource globalPropertySource) {
        this.festivalRepository = festivalRepository;
        this.tourImagesRepository = tourImagesRepository;
        this.apiServiceKey = globalPropertySource.getApiServiceKey();
    }


//        // 매일 21시 30분 0초에 실행
//    @Scheduled(cron = "0 30 21 * * *")
//    public void dayJob() {
//
//    }
//
//    // 매월 1일 0시 0분 0초에 실행
//    @Scheduled(cron = "0 0 0 1 * *")
//    public void monthJob() {
//
//    }
//
//
//    //서버 시작하고 10초후에 실행 후 30분마다 실행끝
//    @Scheduled(initialDelay = 10000, fixedDelay = 1800000)
//    public void Job() throws IOException {
//        System.out.println("시작");
//        //축제 정보 가져와서 디비에 저장 *현재 실행 시키면 안됨*
//        GetFestivalInfo getFestivalInfo = new GetFestivalInfo(festivalRepository, tourImagesRepository, apiServiceKey);
//        getFestivalInfo.run();
//        System.out.println("끝");
//    }

}

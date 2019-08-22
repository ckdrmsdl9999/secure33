package com.security.everywhere.Scheduler;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.security.everywhere.model.Festival;
import com.security.everywhere.model.TourImages;
import com.security.everywhere.repository.TourImagesRepository;
import com.security.everywhere.repository.FestivalRepository;
import com.security.everywhere.response.tourBasicInfo.TourItem;
import com.security.everywhere.response.tourBasicInfo.TourResponse;
import com.security.everywhere.response.tourCommonInfo.ComInfoItem;
import com.security.everywhere.response.tourCommonInfo.ComInfoResponse;
import com.security.everywhere.response.tourImages.ImagesItem;
import com.security.everywhere.response.tourImages.ImagesResponse;

import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class GetFestivalInfo {

    private final FestivalRepository festivalRepository;
    private final TourImagesRepository tourImagesRepository;
    private final String apiServiceKey;
    private StringBuilder urlBuilder;
    private URL url;
    private final ObjectMapper mapper;

    public GetFestivalInfo(FestivalRepository festivalRepository
            , TourImagesRepository tourImagesRepository
            , String apiServiceKey) {
        this.festivalRepository = festivalRepository;
        this.tourImagesRepository = tourImagesRepository;
        this.apiServiceKey = apiServiceKey;
        this.mapper = new ObjectMapper();

        // 모르는 property에 대해 무시하고 넘어간다. DTO의 하위 호환성 보장에 필요하다
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        // ENUM 값이 존재하지 않으면 null로 설정한다. Enum 항목이 추가되어도 무시하고 넘어가게 할 때 필요하다.
        mapper.configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL, true);
    }

    public void run() throws IOException {
        urlBuilder = new StringBuilder("http://api.visitkorea.or.kr/openapi/service/rest/KorService/searchFestival"); /*URL*/
        urlBuilder.append("?")
                .append(URLEncoder.encode("ServiceKey", StandardCharsets.UTF_8))
                .append("=")
                .append(apiServiceKey); /*공공데이터포털에서 발급받은 인증키*/
        urlBuilder.append("&")
                .append(URLEncoder.encode("numOfRows", StandardCharsets.UTF_8))
                .append("=")
                .append(URLEncoder.encode("1811", StandardCharsets.UTF_8)); /*한 페이지 결과 수*/
        urlBuilder.append("&")
                .append(URLEncoder.encode("pageNo", StandardCharsets.UTF_8))
                .append("=")
                .append(URLEncoder.encode("1", StandardCharsets.UTF_8)); /*현재 페이지 번호*/
        urlBuilder.append("&")
                .append(URLEncoder.encode("MobileOS", StandardCharsets.UTF_8))
                .append("=")
                .append(URLEncoder.encode("ETC", StandardCharsets.UTF_8)); /*IOS (아이폰), AND (안드로이드), WIN (원도우폰),ETC*/
        urlBuilder.append("&")
                .append(URLEncoder.encode("MobileApp", StandardCharsets.UTF_8))
                .append("=")
                .append(URLEncoder.encode("AppTest", StandardCharsets.UTF_8)); /*서비스명=어플명*/
        urlBuilder.append("&")
                .append(URLEncoder.encode("arrange", StandardCharsets.UTF_8))
                .append("=")
                .append(URLEncoder.encode("A", StandardCharsets.UTF_8)); /*(A=제목순, B=조회순, C=수정순, D=생성일순) 대표이미지가 반드시 있는 정렬 (O=제목순, P=조회순, Q=수정일순, R=생성일순)*/
        urlBuilder.append("&")
                .append(URLEncoder.encode("listYN", StandardCharsets.UTF_8))
                .append("=")
                .append(URLEncoder.encode("Y", StandardCharsets.UTF_8)); /*목록 구분(Y=목록, N=개수)*/
        urlBuilder.append("&")
                .append(URLEncoder.encode("areaCode", StandardCharsets.UTF_8))
                .append("=")
                .append(URLEncoder.encode("", StandardCharsets.UTF_8)); /*지역코드*/
        urlBuilder.append("&")
                .append(URLEncoder.encode("sigunguCode", StandardCharsets.UTF_8))
                .append("=")
                .append(URLEncoder.encode("", StandardCharsets.UTF_8)); /*시군구코드(areaCode 필수)*/
        urlBuilder.append("&")
                .append(URLEncoder.encode("eventStartDate", StandardCharsets.UTF_8))
                .append("=")
                .append(URLEncoder.encode("20190101", StandardCharsets.UTF_8)); /*행사 시작일(형식:YYYYMMDD)*/
        urlBuilder.append("&")
                .append(URLEncoder.encode("eventEndDate", StandardCharsets.UTF_8))
                .append("=")
                .append(URLEncoder.encode("", StandardCharsets.UTF_8)); /*행사 종료일(형식:YYYYMMDD)*/
        urlBuilder.append("&")
                .append(URLEncoder.encode("_type", StandardCharsets.UTF_8))
                .append("=")
                .append(URLEncoder.encode("json", StandardCharsets.UTF_8)); /*행사 종료일(형식:YYYYMMDD)*/
        url = new URL(urlBuilder.toString());

        TourResponse responseResult = mapper.readValue(url, TourResponse.class);

        List<TourItem> festivals = responseResult.getResponse().getBody().getItems().getItem();


        List<ImagesItem> imagesItems;
        ComInfoItem comInfoItem;
        String homePage;
        String overView;
        int count = 0;
        for (TourItem item: festivals) {

            try {
                // 홈페이지와 개요 정보 가져옴
                comInfoItem = getFestivalComInfo(item);
                homePage = comInfoItem.getHomepage();
                overView = comInfoItem.getOverview();
            } catch (NullPointerException e) {
                homePage = "";
                overView = "";
            }

            // 축제 정보 DB에 저장
            festivalRepository.save(new Festival(item
                    , homePage
                    , overView));

            // 여러장의 이미지를 가져와 DB에 저장
            try {
                imagesItems = getImages(item);

                for (ImagesItem imagesItem: imagesItems) {
                    tourImagesRepository.save(new TourImages(imagesItem));
                }
            } catch (NullPointerException e) {
            }
            count += 1;
        }
    }

    private ComInfoItem getFestivalComInfo(TourItem tourItem) throws IOException {
        urlBuilder = new StringBuilder("http://api.visitkorea.or.kr/openapi/service/rest/KorService/detailCommon"); /*URL*/
        urlBuilder.append("?")
                .append(URLEncoder.encode("ServiceKey", StandardCharsets.UTF_8))
                .append("=")
                .append(apiServiceKey); /*공공데이터포털에서 발급받은 인증키*/
        urlBuilder.append("&")
                .append(URLEncoder.encode("numOfRows", StandardCharsets.UTF_8))
                .append("=")
                .append(URLEncoder.encode("10", StandardCharsets.UTF_8)); /*한 페이지 결과 수*/
        urlBuilder.append("&")
                .append(URLEncoder.encode("pageNo", StandardCharsets.UTF_8))
                .append("=")
                .append(URLEncoder.encode("1", StandardCharsets.UTF_8)); /*현재 페이지 번호*/
        urlBuilder.append("&")
                .append(URLEncoder.encode("MobileOS", StandardCharsets.UTF_8))
                .append("=")
                .append(URLEncoder.encode("ETC", StandardCharsets.UTF_8)); /*IOS (아이폰), AND (안드로이드), WIN (원도우폰),ETC*/
        urlBuilder.append("&")
                .append(URLEncoder.encode("MobileApp", StandardCharsets.UTF_8))
                .append("=")
                .append(URLEncoder.encode("AppTest", StandardCharsets.UTF_8)); /*서비스명=어플명*/
        urlBuilder.append("&")
                .append(URLEncoder.encode("contentId", StandardCharsets.UTF_8))
                .append("=")
                .append(URLEncoder.encode(tourItem.getContentid(), StandardCharsets.UTF_8));    // 콘텐츠 ID
        urlBuilder.append("&")
                .append(URLEncoder.encode("contentTypeId", StandardCharsets.UTF_8))
                .append("=")
                .append(URLEncoder.encode("15", StandardCharsets.UTF_8));    // 관광타입(관광지, 숙박 등) ID
        urlBuilder.append("&")
                .append(URLEncoder.encode("defaultYN", StandardCharsets.UTF_8))
                .append("=")
                .append(URLEncoder.encode("Y", StandardCharsets.UTF_8));    // 기본정보 조회여부
        urlBuilder.append("&")
                .append(URLEncoder.encode("firstImageYN", StandardCharsets.UTF_8))
                .append("=")
                .append(URLEncoder.encode("N", StandardCharsets.UTF_8));    // 원본, 썸네일 대표이미지 조회여부
        urlBuilder.append("&")
                .append(URLEncoder.encode("areacodeYN", StandardCharsets.UTF_8))
                .append("=")
                .append(URLEncoder.encode("N", StandardCharsets.UTF_8));    // 지역코드, 시군구코드 조회여부
        urlBuilder.append("&")
                .append(URLEncoder.encode("catcodeYN", StandardCharsets.UTF_8))
                .append("=")
                .append(URLEncoder.encode("N", StandardCharsets.UTF_8));    // 대,중,소분류코드 조회여부
        urlBuilder.append("&")
                .append(URLEncoder.encode("addrinfoYN", StandardCharsets.UTF_8))
                .append("=")
                .append(URLEncoder.encode("N", StandardCharsets.UTF_8));    // 주소, 상세주소 조회여부
        urlBuilder.append("&")
                .append(URLEncoder.encode("mapinfoYN", StandardCharsets.UTF_8))
                .append("=")
                .append(URLEncoder.encode("N", StandardCharsets.UTF_8));    // 좌표 X,Y 조회여부
        urlBuilder.append("&")
                .append(URLEncoder.encode("overviewYN", StandardCharsets.UTF_8))
                .append("=")
                .append(URLEncoder.encode("Y", StandardCharsets.UTF_8));    // 콘텐츠 개요 조회여부
        urlBuilder.append("&")
                .append(URLEncoder.encode("_type", StandardCharsets.UTF_8))
                .append("=")
                .append(URLEncoder.encode("json", StandardCharsets.UTF_8));
        url = new URL(urlBuilder.toString());

        ComInfoResponse responseResult = mapper.readValue(url, ComInfoResponse.class);

        return responseResult.getResponse().getBody().getItems().getItem();
    }

    private List<ImagesItem> getImages(TourItem tourItem) throws IOException {
        urlBuilder = new StringBuilder("http://api.visitkorea.or.kr/openapi/service/rest/KorService/detailImage"); /*URL*/
        urlBuilder.append("?")
                .append(URLEncoder.encode("ServiceKey", StandardCharsets.UTF_8))
                .append("=")
                .append(apiServiceKey); /*공공데이터포털에서 발급받은 인증키*/
        urlBuilder.append("&")
                .append(URLEncoder.encode("numOfRows", StandardCharsets.UTF_8))
                .append("=")
                .append(URLEncoder.encode("10", StandardCharsets.UTF_8)); /*한 페이지 결과 수*/
        urlBuilder.append("&")
                .append(URLEncoder.encode("pageNo", StandardCharsets.UTF_8))
                .append("=")
                .append(URLEncoder.encode("1", StandardCharsets.UTF_8)); /*현재 페이지 번호*/
        urlBuilder.append("&")
                .append(URLEncoder.encode("MobileOS", StandardCharsets.UTF_8))
                .append("=")
                .append(URLEncoder.encode("ETC", StandardCharsets.UTF_8)); /*IOS (아이폰), AND (안드로이드), WIN (원도우폰),ETC*/
        urlBuilder.append("&")
                .append(URLEncoder.encode("MobileApp", StandardCharsets.UTF_8))
                .append("=")
                .append(URLEncoder.encode("AppTest", StandardCharsets.UTF_8)); /*서비스명=어플명*/
        urlBuilder.append("&")
                .append(URLEncoder.encode("contentId", StandardCharsets.UTF_8))
                .append("=")
                .append(URLEncoder.encode(tourItem.getContentid(), StandardCharsets.UTF_8));    // 콘텐츠 ID
        urlBuilder.append("&")
                .append(URLEncoder.encode("imageYN", StandardCharsets.UTF_8))
                .append("=")
                .append(URLEncoder.encode("Y", StandardCharsets.UTF_8));    // Y=콘텐츠 이미지 조회, N='음식점'타입의 음식메뉴 이미지
        urlBuilder.append("&")
                .append(URLEncoder.encode("subImageYN", StandardCharsets.UTF_8))
                .append("=")
                .append(URLEncoder.encode("Y", StandardCharsets.UTF_8));    // Y=원본,썸네일 이미지 조회 N=Null
        urlBuilder.append("&")
                .append(URLEncoder.encode("_type", StandardCharsets.UTF_8))
                .append("=")
                .append(URLEncoder.encode("json", StandardCharsets.UTF_8));    // 콘텐츠 개요 조회여부
        url = new URL(urlBuilder.toString());

        ImagesResponse responseResult = null;
        try {
            responseResult = mapper.readValue(url, ImagesResponse.class);
        } catch (MismatchedInputException e) {
            return null;
        }

        return responseResult.getResponse().getBody().getItems().getItem();
    }
}

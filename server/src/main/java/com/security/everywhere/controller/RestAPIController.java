package com.security.everywhere.controller;

/* 뒤에 DB 안붙은 api는 실시간으로 이용해서 리턴해주는 메소드입니다.
* /api/festivalInfo      <- 여러가지 축제 정보 리턴 (DB)
* /api/festivalContent   <- 요청으로 들어온 컨텐츠ID에 해당되는 축제 정보 하나 리턴 (DB)
* /api/festivalImages    <- 요청으로 들어온 컨텐츠 ID에 해당되는 축제 이미지들 리턴 (DB)
* /api/nearbyTour        <- 공공 api를 통해  x, y축을 가지고 주변 관광지 정보 가져오기, 요청 보낼때 파라미터로 정렬과 거리를 조절할 수 있음
* /api/tourImages        <- 관광지 이미지 추가로 가져오기 (관광지만 가능)
* /api/detailIntro/tour  <- 공공 api를 통해 관광지의 상세정보 가져와 리턴 (축제랑 form이 달라서 꼭 관광지만으로 사용해야함), 휴무일, 개장시간, 주차시설등 (관광지만 가능)
* /api/detailCommon/tour <- 개요, 홈페이지 정보 (관광지만 가능, 공통정보 조회 api)
* /api/airInfo           <- 공공 api를 통해 현재 대기상태 정보를 가져와 리턴 (축제, 관광지 둘 다 가능)
* /api//weatherInfo      <- 공공 api를 통해 1~7일 날씨 정보를 가져와 리턴 (축제, 관광지 둘 다 가능)
* */
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.security.everywhere.configuration.GlobalPropertySource;
import com.security.everywhere.data.TempForecastAreaCode;
import com.security.everywhere.data.WeatherForecastAreaCode;
import com.security.everywhere.model.Festival;
import com.security.everywhere.model.Review;
import com.security.everywhere.model.TourImages;
import com.security.everywhere.model.Weather;
import com.security.everywhere.repository.ReviewRepository;
import com.security.everywhere.repository.TourImagesRepository;
import com.security.everywhere.repository.FestivalRepository;
import com.security.everywhere.request.*;
import com.security.everywhere.response.air.AirDTO;
import com.security.everywhere.response.air.AirItem;
import com.security.everywhere.response.locationConversion.LocationConvAuthDTO;
import com.security.everywhere.response.locationConversion.LocationConvDTO;
import com.security.everywhere.response.observatory.ObservatoryDTO;
import com.security.everywhere.response.tourBasicInfo.TourItem;
import com.security.everywhere.response.tourBasicInfo.TourResponse;
import com.security.everywhere.response.tourCommonInfo.ComInfoItem;
import com.security.everywhere.response.tourCommonInfo.ComInfoResponse;
import com.security.everywhere.response.tourDetailIntro.DetailIntroResponse;
import com.security.everywhere.response.tourDetailIntro.DetailIntroitem;
import com.security.everywhere.response.tourImages.ImagesItem;
import com.security.everywhere.response.tourImages.ImagesResponse;
import com.security.everywhere.response.weatherMiddleTerm.MiddleTermWeatherResponse;
import com.security.everywhere.response.weatherShortTerm.ShortTermWeatherItem;
import com.security.everywhere.response.weatherShortTerm.ShortTermWeatherResponse;
import com.security.everywhere.response.weatherTemperature.WeatherTempResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/api")
public class RestAPIController {


    private final FestivalRepository festivalRepository;
    private final TourImagesRepository tourImagesRepository;
    private final TempForecastAreaCode tempForecastAreaCode;
    private final WeatherForecastAreaCode weatherForecastAreaCode;
    private final ReviewRepository reviewRepository;
    private static Logger logger = LoggerFactory.getLogger(GlobalPropertySource.class);

        private final ObjectMapper mapper;
        private final RestTemplate restTemplate;

        private final String apiServiceKey;
        private final String consumerKey;
        private final String consumerSecret;


    public RestAPIController(FestivalRepository festivalRepository
            , TempForecastAreaCode tempForecastAreaCode
            , WeatherForecastAreaCode weatherForecastAreaCode
            , TourImagesRepository tourImagesRepository
            , GlobalPropertySource globalPropertySource
            , ReviewRepository reviewRepository) {

        this.festivalRepository = festivalRepository;
        this.tempForecastAreaCode = tempForecastAreaCode;
        this.weatherForecastAreaCode = weatherForecastAreaCode;
        this.tourImagesRepository = tourImagesRepository;
        this.reviewRepository = reviewRepository;
        this.mapper = new ObjectMapper();
        this.restTemplate = new RestTemplate();
        this.apiServiceKey = globalPropertySource.getApiServiceKey();
        this.consumerKey = globalPropertySource.getConsumerKey();
        this.consumerSecret = globalPropertySource.getConsumerSecret();

        // 모르는 property에 대해 무시하고 넘어간다. DTO의 하위 호환성 보장에 필요하다
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        // ENUM 값이 존재하지 않으면 null로 설정한다. Enum 항목이 추가되어도 무시하고 넘어가게 할 때 필요하다.
        mapper.configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL, true);
        }


        @PostMapping("/festivalInfo")
        public List<Festival> festivalInfo(@RequestBody FestivalParam requestParam) {
                int pageNo = Integer.parseInt(requestParam.getPageNo());
                int numOfRows = Integer.parseInt(requestParam.getNumOfRows());
                String eventStartDate = requestParam.getEventStartDate();

                Pageable pageElements = PageRequest.of(pageNo, numOfRows, Sort.by("eventStartDate"));

                List<Festival> festivals = new ArrayList<>();

                if("con".equals(requestParam.getCategory())) {
                        festivals = festivalRepository.findAllByEventStartDateGreaterThanEqualAndEventEndDateLessThanEqualAndAddr1Containing
                        (eventStartDate, requestParam.getEventEndDate(), pageElements,requestParam.getAddress());
                }
                else if("search".equals(requestParam.getCategory())){
                        festivals = festivalRepository.findByTitleContaining(requestParam.getTitle());//jpa쿼리
                }
                else if("main".equals(requestParam.getCategory())) {
                        festivals = festivalRepository.findAllByEventStartDateGreaterThanEqual(eventStartDate, pageElements);
                }

        return festivals;
    }

    @PostMapping("/likeButton")
    public void likeButton(@RequestBody String reviewid) {
        reviewRepository.pluslikecount( Long.parseLong(reviewid));
    }

    @PostMapping("/popularReviewList_pos")
    public List<Review> popularReviewList_pos(@RequestBody String contentid) {
        return reviewRepository.findTopByContentIdAndStarGreaterThanEqualAndLikecountGreaterThanOrderByLikecountDesc(contentid,3.0, 0);
    }

    @PostMapping("/popularReviewList_nav")
    public List<Review> popularReviewList_nav(@RequestBody String contentid) {
        return reviewRepository.findTopByContentIdAndStarLessThanAndLikecountGreaterThanOrderByLikecountDesc(contentid,3.0, 0);
    }



    @PostMapping("/reviewList")
    public List<Review> review_list(@RequestBody String contentid) {
        return reviewRepository.findAllByContentId(contentid);
    }


    @PostMapping("/writeReview")
    public void writeReview(@RequestBody Review review) {
        reviewRepository.save(review);
    }

        @PostMapping("/festivalContent")
        public Festival festivalContent(@RequestBody String contentid) {
        Festival festival;
        festival = festivalRepository.findByContentId(contentid);

        String result = festival.getOverview().replaceAll("<br>","");
        result = result.replaceAll("<br />","");
        festival.setOverview(result);

        return festival;
        }


        // 축제 이미지 추가로 가져오기
        @PostMapping("/festivalImages")
        public List<TourImages> festivalImages(@RequestBody String contentid) {
                return tourImagesRepository.findByContentid(contentid);
        }


        // 관광지 이미지 추가로 가져오기
        @PostMapping("/tourImages")
        public List <ImagesItem> tourImages(@RequestBody String contentid) throws IOException {
            System.out.println("/tourImages의contentid값" + contentid);

            StringBuilder urlBuilder = new StringBuilder("http://api.visitkorea.or.kr/openapi/service/rest/KorService/detailImage"); /*URL*/
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
                    .append(URLEncoder.encode(contentid, StandardCharsets.UTF_8));    // 콘텐츠 ID
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
            URL url = new URL(urlBuilder.toString());
            ImagesResponse imagesResponse = null;
            //   ImagesResponse imagesResponse = mapper.readValue(url, ImagesResponse.class);
            List<ImagesItem> a = new ArrayList<>();
            try {
                imagesResponse = mapper.readValue(url, ImagesResponse.class);

            } catch (Exception e) {
                System.out.println("ㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇ에러ㅓㅓㅓㅓㅓㅓㅓㅓㅓ");
                return a;//사진있는경우
            }
            return imagesResponse.getResponse().getBody().getItems().getItem();
        }
//x, y축을 가지고 주변 관광지 정보 가져오기-축제
        @PostMapping("/nearbyTour")
        public List<TourItem> nearbyTour(@RequestBody NearbyTourParam nearbyTourParam) throws IOException {
        var contentIdNear=nearbyTourParam.getContentid();
        Festival festival;
        festival = festivalRepository.findByContentId(contentIdNear);
        nearbyTourParam.setMapX(festival.getMapX());
        nearbyTourParam.setMapY(festival.getMapY());
        if(nearbyTourParam.getMapX().length()==0)//예외처리 contentid 2602871
        {
            nearbyTourParam.setMapX("126.9787932340");
            nearbyTourParam.setMapY("37.5659098804");
        }

        //  System.out.println(nearbyTourParam.getArrange()+"포착완료");
        System.out.println("/nearbytour에서 관광지를 찾기위한 contentid값과x,y값"+contentIdNear+" "+nearbyTourParam.getMapX()+" "+nearbyTourParam.getMapY());
        StringBuilder urlBuilder = new StringBuilder("http://api.visitkorea.or.kr/openapi/service/rest/KorService/locationBasedList"); /*URL*/
        urlBuilder.append("?")
        .append(URLEncoder.encode("serviceKey", StandardCharsets.UTF_8))
        .append("=")
        .append(apiServiceKey); /*공공데이터포털에서 발급받은 인증키*/
        urlBuilder.append("&")
        .append(URLEncoder.encode("numOfRows", StandardCharsets.UTF_8))
        .append("=")
        .append(URLEncoder.encode(nearbyTourParam.getNumOfRows(), StandardCharsets.UTF_8)); /*한 페이지 결과 수*/
        urlBuilder.append("&")
        .append(URLEncoder.encode("pageNo", StandardCharsets.UTF_8))
        .append("=")
        .append(URLEncoder.encode(nearbyTourParam.getPageNo(), StandardCharsets.UTF_8)); /*현재 페이지 번호*/
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
        .append(URLEncoder.encode(nearbyTourParam.getArrange(), StandardCharsets.UTF_8)); /*(A=제목순, B=조회순, C=수정순, D=생성일순) 대표이미지가 반드시 있는 정렬 (O=제목순, P=조회순, Q=수정일순, R=생성일순)*/
        urlBuilder.append("&")
        .append(URLEncoder.encode("contentTypeId", StandardCharsets.UTF_8))
        .append("=")
        .append(URLEncoder.encode("12", StandardCharsets.UTF_8));       // 관광타입(관광지, 숙박 등) ID 관광지:12, 축제:15
        urlBuilder.append("&")
        .append(URLEncoder.encode("mapX", StandardCharsets.UTF_8))
        .append("=")
        .append(URLEncoder.encode(nearbyTourParam.getMapX(), StandardCharsets.UTF_8));  // GPS X좌표(WGS84 경도 좌표)
        urlBuilder.append("&")
        .append(URLEncoder.encode("mapY", StandardCharsets.UTF_8))
        .append("=")
        .append(URLEncoder.encode(nearbyTourParam.getMapY(), StandardCharsets.UTF_8));  // GPS Y좌표(WGS84 위도 좌표)
        urlBuilder.append("&")
        .append(URLEncoder.encode("radius", StandardCharsets.UTF_8))
        .append("=")
        .append(URLEncoder.encode(nearbyTourParam.getRadius(), StandardCharsets.UTF_8));    // 거리 반경(단위m), Max값 20000m=20Km
        urlBuilder.append("&")
        .append(URLEncoder.encode("listYN", StandardCharsets.UTF_8))
        .append("=")
        .append(URLEncoder.encode("Y", StandardCharsets.UTF_8)); /*목록 구분(Y=목록, N=개수)*/
        urlBuilder.append("&")
        .append(URLEncoder.encode("_type", StandardCharsets.UTF_8))
        .append("=")
        .append(URLEncoder.encode("json", StandardCharsets.UTF_8));
        URL url = new URL(urlBuilder.toString());
            TourResponse tourResponse=null;
            List<TourItem> a = new ArrayList<>();
            try {
                tourResponse = mapper.readValue(url, TourResponse.class);
            } catch (Exception e) {
                System.out.println("에러가 발생했어요!!");
                return a;//
            }
            return tourResponse.getResponse().getBody().getItems().getItem();

        }

    //x, y축을 가지고 주변 관광지 정보 가져오기-관광지
    @PostMapping("/nearbyTour2")
    public List<TourItem> nearbyTour2(@RequestBody NearbyTourParam nearbyTourParam) throws IOException {
        var contentIdNear=nearbyTourParam.getContentid();
        System.out.println("/nearbyTour2의"+contentIdNear+"입니다");



        System.out.println(nearbyTourParam.getMapX()+" "+nearbyTourParam.getMapY()+"이랑임");
        System.out.println("/nearbytour2에서 관광지를 찾기위한 x,y값"+nearbyTourParam.getMapX()+" "+nearbyTourParam.getMapY()+nearbyTourParam.getAddr1()+
        nearbyTourParam.getNumOfRows()+" "+nearbyTourParam.getArrange()+" "+nearbyTourParam.getRadius());
        StringBuilder urlBuilder = new StringBuilder("http://api.visitkorea.or.kr/openapi/service/rest/KorService/locationBasedList"); /*URL*/
        urlBuilder.append("?")
        .append(URLEncoder.encode("serviceKey", StandardCharsets.UTF_8))
        .append("=")
        .append(apiServiceKey); /*공공데이터포털에서 발급받은 인증키*/
        urlBuilder.append("&")
        .append(URLEncoder.encode("numOfRows", StandardCharsets.UTF_8))
        .append("=")
        .append(URLEncoder.encode(nearbyTourParam.getNumOfRows(), StandardCharsets.UTF_8)); /*한 페이지 결과 수*/
        urlBuilder.append("&")
        .append(URLEncoder.encode("pageNo", StandardCharsets.UTF_8))
        .append("=")
        .append(URLEncoder.encode(nearbyTourParam.getPageNo(), StandardCharsets.UTF_8)); /*현재 페이지 번호*/
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
        .append(URLEncoder.encode(nearbyTourParam.getArrange(), StandardCharsets.UTF_8)); /*(A=제목순, B=조회순, C=수정순, D=생성일순) 대표이미지가 반드시 있는 정렬 (O=제목순, P=조회순, Q=수정일순, R=생성일순)*/
        urlBuilder.append("&")
        .append(URLEncoder.encode("contentTypeId", StandardCharsets.UTF_8))
        .append("=")
        .append(URLEncoder.encode("12", StandardCharsets.UTF_8));       // 관광타입(관광지, 숙박 등) ID 관광지:12, 축제:15
        urlBuilder.append("&")
        .append(URLEncoder.encode("mapX", StandardCharsets.UTF_8))
        .append("=")
        .append(URLEncoder.encode(nearbyTourParam.getMapX(), StandardCharsets.UTF_8));  // GPS X좌표(WGS84 경도 좌표)
        urlBuilder.append("&")
        .append(URLEncoder.encode("mapY", StandardCharsets.UTF_8))
        .append("=")
        .append(URLEncoder.encode(nearbyTourParam.getMapY(), StandardCharsets.UTF_8));  // GPS Y좌표(WGS84 위도 좌표)
        urlBuilder.append("&")
        .append(URLEncoder.encode("radius", StandardCharsets.UTF_8))
        .append("=")
        .append(URLEncoder.encode(nearbyTourParam.getRadius(), StandardCharsets.UTF_8));    // 거리 반경(단위m), Max값 20000m=20Km
        urlBuilder.append("&")
        .append(URLEncoder.encode("listYN", StandardCharsets.UTF_8))
        .append("=")
        .append(URLEncoder.encode("Y", StandardCharsets.UTF_8)); /*목록 구분(Y=목록, N=개수)*/
        urlBuilder.append("&")
        .append(URLEncoder.encode("_type", StandardCharsets.UTF_8))
        .append("=")
        .append(URLEncoder.encode("json", StandardCharsets.UTF_8));
        URL url = new URL(urlBuilder.toString());

        TourResponse tourResponse=null;
        List<TourItem> a = new ArrayList<>();
        try {
            tourResponse = mapper.readValue(url, TourResponse.class);
        } catch (Exception e) {
            System.out.println("에러가 발생했어요!!");
            return a;//
        }
        return tourResponse.getResponse().getBody().getItems().getItem();

        }

// 관광지의 상세정보
    @PostMapping("/detailIntro/tour")
    public DetailIntroitem tourDetailIntro(@RequestBody TourDetailIntroParam detailIntroParam) throws IOException {
        StringBuilder urlBuilder = new StringBuilder("http://api.visitkorea.or.kr/openapi/service/rest/KorService/detailIntro");
        urlBuilder.append("?")
        .append(URLEncoder.encode("ServiceKey", StandardCharsets.UTF_8))
        .append("=")
        .append(apiServiceKey);
        urlBuilder.append("&")
        .append(URLEncoder.encode("numOfRows", StandardCharsets.UTF_8))
        .append("=")
        .append(URLEncoder.encode(detailIntroParam.getNumOfRows(), StandardCharsets.UTF_8));
        urlBuilder.append("&")
        .append(URLEncoder.encode("pageNo", StandardCharsets.UTF_8))
        .append("=")
        .append(URLEncoder.encode(detailIntroParam.getPageNo(), StandardCharsets.UTF_8));
        urlBuilder.append("&")
        .append(URLEncoder.encode("MobileOS", StandardCharsets.UTF_8))
        .append("=")
        .append(URLEncoder.encode("ETC", StandardCharsets.UTF_8));
        urlBuilder.append("&")
        .append(URLEncoder.encode("MobileApp", StandardCharsets.UTF_8))
        .append("=")
        .append(URLEncoder.encode("AppTest", StandardCharsets.UTF_8));
        urlBuilder.append("&")
        .append(URLEncoder.encode("contentId", StandardCharsets.UTF_8))
        .append("=")
        .append(URLEncoder.encode(detailIntroParam.getContentId(), StandardCharsets.UTF_8));
        urlBuilder.append("&")
        .append(URLEncoder.encode("contentTypeId", StandardCharsets.UTF_8))
        .append("=")
        .append(URLEncoder.encode(detailIntroParam.getContentTypeId(), StandardCharsets.UTF_8));
        urlBuilder.append("&")
        .append(URLEncoder.encode("_type", StandardCharsets.UTF_8))
        .append("=")
        .append(URLEncoder.encode("json", StandardCharsets.UTF_8));
        URL url = new URL(urlBuilder.toString());

        DetailIntroResponse detailIntroResponse = mapper.readValue(url, DetailIntroResponse.class);

        return detailIntroResponse.getResponse().getBody().getItems().getItem();
        }


    // 개요, 홈페이지 정보 (관광지 공통정보 조회 api)
    @PostMapping("/detailCommon/tour")
    public ComInfoItem tourDetailCommon (@RequestBody String tourItem) throws IOException {
        System.out.println("detailtour의 contentid값"+tourItem);
        StringBuilder urlBuilder = new StringBuilder("http://api.visitkorea.or.kr/openapi/service/rest/KorService/detailCommon"); /*URL*/
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
        .append(URLEncoder.encode(tourItem, StandardCharsets.UTF_8));    // 콘텐츠 ID
        urlBuilder.append("&")
        .append(URLEncoder.encode("contentTypeId", StandardCharsets.UTF_8))
        .append("=")
        .append(URLEncoder.encode("12", StandardCharsets.UTF_8));    // 관광타입(관광지, 숙박 등) ID
        urlBuilder.append("&")//tourItem.getContentTypeId()->12로변경
        .append(URLEncoder.encode("defaultYN", StandardCharsets.UTF_8))
        .append("=")
        .append(URLEncoder.encode("Y", StandardCharsets.UTF_8));    // 기본정보 조회여부
        urlBuilder.append("&")
        .append(URLEncoder.encode("firstImageYN", StandardCharsets.UTF_8))
        .append("=")
        .append(URLEncoder.encode("Y", StandardCharsets.UTF_8));    // 원본, 썸네일 대표이미지 조회여부
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
        .append(URLEncoder.encode("Y", StandardCharsets.UTF_8));    // 주소, 상세주소 조회여부
        urlBuilder.append("&")
        .append(URLEncoder.encode("mapinfoYN", StandardCharsets.UTF_8))
        .append("=")
        .append(URLEncoder.encode("Y", StandardCharsets.UTF_8));    // 좌표 X,Y 조회여부
        urlBuilder.append("&")
        .append(URLEncoder.encode("overviewYN", StandardCharsets.UTF_8))
        .append("=")
        .append(URLEncoder.encode("Y", StandardCharsets.UTF_8));    // 콘텐츠 개요 조회여부
        urlBuilder.append("&")
        .append(URLEncoder.encode("_type", StandardCharsets.UTF_8))
        .append("=")
        .append(URLEncoder.encode("json", StandardCharsets.UTF_8));
        URL url = new URL(urlBuilder.toString());

            ComInfoResponse responseResult = mapper.readValue(url, ComInfoResponse.class);
            System.out.println("/detailcmmontour에서 image값확인"+responseResult.getResponse().getBody().getItems().getItem().getFirstimage()+"이야"+responseResult.getResponse().getBody().getItems().getItem().getFirstimage2());

        String result ="";
        result = responseResult.getResponse().getBody().getItems().getItem().getOverview().replaceAll("</br>","");
        result = responseResult.getResponse().getBody().getItems().getItem().getOverview().replaceAll("<br>","");
        result = result.replaceAll("<br />","");
        responseResult.getResponse().getBody().getItems().getItem().setOverview(result);
            return responseResult.getResponse().getBody().getItems().getItem();
        }

    @PostMapping("/airInfo2")//대기정보-관광지
    public List<AirItem> observatoryInfo2(@RequestBody ObservatoryParam requestParam) throws IOException {
        //  System.out.println("airinfo2의 contentid 파라미터값체크"+requestParam.getContentid());
        var aaa=requestParam.getContentid();
        //     System.out.println("airinfo2의 aaa 파라미터값체크"+aaa);

        StringBuilder urlBuilder = new StringBuilder("http://api.visitkorea.or.kr/openapi/service/rest/KorService/detailCommon"); /*URL*/
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
        .append(URLEncoder.encode(aaa, StandardCharsets.UTF_8));    // 콘텐츠 ID
        urlBuilder.append("&")
        .append(URLEncoder.encode("contentTypeId", StandardCharsets.UTF_8))
        .append("=")
        .append(URLEncoder.encode("12", StandardCharsets.UTF_8));    // 관광타입(관광지, 숙박 등) ID
        urlBuilder.append("&")//tourItem.getContentTypeId()->12로변경
        .append(URLEncoder.encode("defaultYN", StandardCharsets.UTF_8))
        .append("=")
        .append(URLEncoder.encode("Y", StandardCharsets.UTF_8));    // 기본정보 조회여부
        urlBuilder.append("&")
        .append(URLEncoder.encode("firstImageYN", StandardCharsets.UTF_8))
        .append("=")
        .append(URLEncoder.encode("Y", StandardCharsets.UTF_8));    // 원본, 썸네일 대표이미지 조회여부
        urlBuilder.append("&")
        .append(URLEncoder.encode("areacodeYN", StandardCharsets.UTF_8))
        .append("=")
        .append(URLEncoder.encode("Y", StandardCharsets.UTF_8));    // 지역코드, 시군구코드 조회여부
        urlBuilder.append("&")
        .append(URLEncoder.encode("catcodeYN", StandardCharsets.UTF_8))
        .append("=")
        .append(URLEncoder.encode("Y", StandardCharsets.UTF_8));    // 대,중,소분류코드 조회여부
        urlBuilder.append("&")
        .append(URLEncoder.encode("addrinfoYN", StandardCharsets.UTF_8))
        .append("=")
        .append(URLEncoder.encode("Y", StandardCharsets.UTF_8));    // 주소, 상세주소 조회여부
        urlBuilder.append("&")
        .append(URLEncoder.encode("mapinfoYN", StandardCharsets.UTF_8))
        .append("=")
        .append(URLEncoder.encode("Y", StandardCharsets.UTF_8));    // 좌표 X,Y 조회여부
        urlBuilder.append("&")
        .append(URLEncoder.encode("overviewYN", StandardCharsets.UTF_8))
        .append("=")
        .append(URLEncoder.encode("Y", StandardCharsets.UTF_8));    // 콘텐츠 개요 조회여부
        urlBuilder.append("&")
        .append(URLEncoder.encode("_type", StandardCharsets.UTF_8))
        .append("=")
        .append(URLEncoder.encode("json", StandardCharsets.UTF_8));
        URL url = new URL(urlBuilder.toString());

        ComInfoResponse responseResult2 = mapper.readValue(url, ComInfoResponse.class);
        //    System.out.println("airinfo첫번째api값체크"+responseResult2.getResponse().getBody().getItems().getItem());
        //    System.out.println("airinfo2의 getmapx값체크요-"+responseResult2.getResponse().getBody().getItems().getItem().getMapx()+" "+responseResult2.getResponse().getBody().getItems().getItem().getMapy());
        //   requestParam.setMapx( responseResult2.getResponse().getBody().getItems().getItem().getMapX());
        //   requestParam.setMapy( responseResult2.getResponse().getBody().getItems().getItem().getMapY());

        // 좌표 변환해주는 api 사용하기 전에 키를 받아야함
        urlBuilder = new StringBuilder("https://sgisapi.kostat.go.kr/OpenAPI3/auth/authentication.json");
        urlBuilder.append("?")
        .append(URLEncoder.encode("consumer_key", StandardCharsets.UTF_8))
        .append("=")
        .append(URLEncoder.encode(consumerKey, StandardCharsets.UTF_8));
        urlBuilder.append("&")
        .append(URLEncoder.encode("consumer_secret", StandardCharsets.UTF_8))
        .append("=")
        .append(URLEncoder.encode(consumerSecret, StandardCharsets.UTF_8));
        url = new URL(urlBuilder.toString());
        LocationConvAuthDTO locationConvAuthDTO = mapper.readValue(url, LocationConvAuthDTO.class);

        // WGS84 경/위도를 TM좌표 중부원점(GRS80)으로 변환
        urlBuilder = new StringBuilder("https://sgisapi.kostat.go.kr/OpenAPI3/transformation/transcoord.json");
        urlBuilder.append("?")
        .append(URLEncoder.encode("accessToken", StandardCharsets.UTF_8))
        .append("=")
        .append(URLEncoder.encode(locationConvAuthDTO.getResult().getAccessToken(), StandardCharsets.UTF_8));
        urlBuilder.append("&")
        .append(URLEncoder.encode("src", StandardCharsets.UTF_8))
        .append("=")
        .append(URLEncoder.encode("4326", StandardCharsets.UTF_8));
        urlBuilder.append("&")
        .append(URLEncoder.encode("dst", StandardCharsets.UTF_8))
        .append("=")
        .append(URLEncoder.encode("5181", StandardCharsets.UTF_8));
        urlBuilder.append("&")
        .append(URLEncoder.encode("posX", StandardCharsets.UTF_8))
        .append("=")
        .append(URLEncoder.encode(responseResult2.getResponse().getBody().getItems().getItem().getMapx(), StandardCharsets.UTF_8));
        urlBuilder.append("&")
        .append(URLEncoder.encode("posY", StandardCharsets.UTF_8))
        .append("=")
        .append(URLEncoder.encode(responseResult2.getResponse().getBody().getItems().getItem().getMapy(), StandardCharsets.UTF_8));
        url = new URL(urlBuilder.toString());

        LocationConvDTO locationConvDTO = mapper.readValue(url, LocationConvDTO.class);
        //  System.out.println("airinpo2의pos값확인:"+locationConvDTO.getResult().getPosX()+"  "+locationConvDTO.getResult().getPosY());
        // 좌표기준 근접 측정소 정보 가져오기
        urlBuilder = new StringBuilder("http://openapi.airkorea.or.kr/openapi/services/rest/MsrstnInfoInqireSvc/getNearbyMsrstnList");
        urlBuilder.append("?")
        .append(URLEncoder.encode("tmX", StandardCharsets.UTF_8))
        .append("=")
        .append(URLEncoder.encode(locationConvDTO.getResult().getPosX(), StandardCharsets.UTF_8));
        urlBuilder.append("&")
        .append(URLEncoder.encode("tmY", StandardCharsets.UTF_8))
        .append("=")
        .append(URLEncoder.encode(locationConvDTO.getResult().getPosY(), StandardCharsets.UTF_8));
        urlBuilder.append("&")
        .append(URLEncoder.encode("ServiceKey", StandardCharsets.UTF_8))
        .append("=")
        .append(apiServiceKey);
        url = new URL(urlBuilder.toString());

        ObservatoryDTO observatoryDTO = null;

        try {
        observatoryDTO = restTemplate.getForObject(url.toURI(), ObservatoryDTO.class);
        } catch (URISyntaxException e) {
        e.printStackTrace();
        }

        // 측정소 이름으로 대기정보 가져오기
        urlBuilder = new StringBuilder("http://openapi.airkorea.or.kr/openapi/services/rest/ArpltnInforInqireSvc/getMsrstnAcctoRltmMesureDnsty");
        urlBuilder.append("?")
        .append(URLEncoder.encode("stationName", StandardCharsets.UTF_8))
        .append("=")
        .append(URLEncoder.encode(observatoryDTO.getBody().getItems().get(0).getStationName(), StandardCharsets.UTF_8));
        urlBuilder.append("&")
        .append(URLEncoder.encode("dataTerm", StandardCharsets.UTF_8))
        .append("=")
        .append(URLEncoder.encode("DAILY", StandardCharsets.UTF_8));
        urlBuilder.append("&").append(URLEncoder.encode("pageNo", StandardCharsets.UTF_8))
        .append("=")
        .append(URLEncoder.encode("1", StandardCharsets.UTF_8));
        urlBuilder.append("&")
        .append(URLEncoder.encode("numOfRows", StandardCharsets.UTF_8))
        .append("=")
        .append(URLEncoder.encode("10", StandardCharsets.UTF_8));
        urlBuilder.append("&")
        .append(URLEncoder.encode("ServiceKey", StandardCharsets.UTF_8))
        .append("=")
        .append(apiServiceKey);
        urlBuilder.append("&")
        .append(URLEncoder.encode("ver", StandardCharsets.UTF_8))
        .append("=")
        .append(URLEncoder.encode("1.3", StandardCharsets.UTF_8));
        url = new URL(urlBuilder.toString());
        //   System.out.println("stationname확인-"+observatoryDTO.getBody().getItems().get(0).getStationName());
        AirDTO airDTO = null;

        try {
        airDTO = restTemplate.getForObject(url.toURI(), AirDTO.class);
        } catch (URISyntaxException e) {
        e.printStackTrace();
        }
        // System.out.println(airDTO.getBody().getItems().get(0).getKhaiGrade()+"농도이다");
    /*    for(int i=0; i<10;i++) {
            System.out.println("시간"+airDTO.getBody().getItems().get(i).getDataTime()+"  "+airDTO.getBody().getItems().get(i).getKhaiGrade() + "농도이다");
        }*/
            return airDTO.getBody().getItems();
        }

    // 대기정보-축제
    @PostMapping("/airInfo")
    public List<AirItem> observatoryInfo(@RequestBody ObservatoryParam requestParam) throws IOException {
        Festival festivals3 = new Festival();//content값
        festivals3=festivalRepository.findByContentId(requestParam.getContentid());
        requestParam.setMapx(festivals3.getMapX());
        requestParam.setMapy(festivals3.getMapY());
          System.out.println("airinfo의 getmapx값체크요-"+requestParam.getContentid()+"이고"+festivals3.getMapX()+" "+festivals3.getMapY());

          if(requestParam.getMapx().length()==0)//예외처리
          {
              requestParam.setMapx("126.9787932340");
              requestParam.setMapy("37.5659098804");
          }

        // 좌표 변환해주는 api 사용하기 전에 키를 받아야함
        StringBuilder urlBuilder = new StringBuilder("https://sgisapi.kostat.go.kr/OpenAPI3/auth/authentication.json");
        urlBuilder.append("?")
        .append(URLEncoder.encode("consumer_key", StandardCharsets.UTF_8))
        .append("=")
        .append(URLEncoder.encode(consumerKey, StandardCharsets.UTF_8));
        urlBuilder.append("&")
        .append(URLEncoder.encode("consumer_secret", StandardCharsets.UTF_8))
        .append("=")
        .append(URLEncoder.encode(consumerSecret, StandardCharsets.UTF_8));
        URL url = new URL(urlBuilder.toString());
        LocationConvAuthDTO locationConvAuthDTO = mapper.readValue(url, LocationConvAuthDTO.class);

        // WGS84 경/위도를 TM좌표 중부원점(GRS80)으로 변환
        urlBuilder = new StringBuilder("https://sgisapi.kostat.go.kr/OpenAPI3/transformation/transcoord.json");
        urlBuilder.append("?")
        .append(URLEncoder.encode("accessToken", StandardCharsets.UTF_8))
        .append("=")
        .append(URLEncoder.encode(locationConvAuthDTO.getResult().getAccessToken(), StandardCharsets.UTF_8));
        urlBuilder.append("&")
        .append(URLEncoder.encode("src", StandardCharsets.UTF_8))
        .append("=")
        .append(URLEncoder.encode("4326", StandardCharsets.UTF_8));
        urlBuilder.append("&")
        .append(URLEncoder.encode("dst", StandardCharsets.UTF_8))
        .append("=")
        .append(URLEncoder.encode("5181", StandardCharsets.UTF_8));
        urlBuilder.append("&")
        .append(URLEncoder.encode("posX", StandardCharsets.UTF_8))
        .append("=")
        .append(URLEncoder.encode(requestParam.getMapx(), StandardCharsets.UTF_8));
        urlBuilder.append("&")
        .append(URLEncoder.encode("posY", StandardCharsets.UTF_8))
        .append("=")
        .append(URLEncoder.encode(requestParam.getMapy(), StandardCharsets.UTF_8));
        url = new URL(urlBuilder.toString());

        LocationConvDTO locationConvDTO = mapper.readValue(url, LocationConvDTO.class);

        // 좌표기준 근접 측정소 정보 가져오기
        urlBuilder = new StringBuilder("http://openapi.airkorea.or.kr/openapi/services/rest/MsrstnInfoInqireSvc/getNearbyMsrstnList");
        urlBuilder.append("?")
        .append(URLEncoder.encode("tmX", StandardCharsets.UTF_8))
        .append("=")
        .append(URLEncoder.encode(locationConvDTO.getResult().getPosX(), StandardCharsets.UTF_8));
        urlBuilder.append("&")
        .append(URLEncoder.encode("tmY", StandardCharsets.UTF_8))
        .append("=")
        .append(URLEncoder.encode(locationConvDTO.getResult().getPosY(), StandardCharsets.UTF_8));
        urlBuilder.append("&")
        .append(URLEncoder.encode("ServiceKey", StandardCharsets.UTF_8))
        .append("=")
        .append(apiServiceKey);
        url = new URL(urlBuilder.toString());

        ObservatoryDTO observatoryDTO = null;
        // System.out.println("pos값확인:"+locationConvDTO.getResult().getPosX()+"  "+locationConvDTO.getResult().getPosY());
        try {
        observatoryDTO = restTemplate.getForObject(url.toURI(), ObservatoryDTO.class);
        } catch (URISyntaxException e) {
        e.printStackTrace();
        }

        // 측정소 이름으로 대기정보 가져오기
        urlBuilder = new StringBuilder("http://openapi.airkorea.or.kr/openapi/services/rest/ArpltnInforInqireSvc/getMsrstnAcctoRltmMesureDnsty");
        urlBuilder.append("?")
        .append(URLEncoder.encode("stationName", StandardCharsets.UTF_8))
        .append("=")
        .append(URLEncoder.encode(observatoryDTO.getBody().getItems().get(0).getStationName(), StandardCharsets.UTF_8));
        urlBuilder.append("&")
        .append(URLEncoder.encode("dataTerm", StandardCharsets.UTF_8))
        .append("=")
        .append(URLEncoder.encode("DAILY", StandardCharsets.UTF_8));
        urlBuilder.append("&").append(URLEncoder.encode("pageNo", StandardCharsets.UTF_8))
        .append("=")
        .append(URLEncoder.encode("1", StandardCharsets.UTF_8));
        urlBuilder.append("&")
        .append(URLEncoder.encode("numOfRows", StandardCharsets.UTF_8))
        .append("=")
        .append(URLEncoder.encode("10", StandardCharsets.UTF_8));
        urlBuilder.append("&")
        .append(URLEncoder.encode("ServiceKey", StandardCharsets.UTF_8))
        .append("=")
        .append(apiServiceKey);
        urlBuilder.append("&")
        .append(URLEncoder.encode("ver", StandardCharsets.UTF_8))
        .append("=")
        .append(URLEncoder.encode("1.3", StandardCharsets.UTF_8));
        url = new URL(urlBuilder.toString());
        // System.out.println("stationname확인-"+observatoryDTO.getBody().getItems().get(0).getStationName());
        AirDTO airDTO = null;

        try {
        airDTO = restTemplate.getForObject(url.toURI(), AirDTO.class);
        } catch (URISyntaxException e) {
        e.printStackTrace();
        }
        //  System.out.println(airDTO.getBody().getItems().get(0).getKhaiGrade()+"농도이다");
    /*    for(int i=0; i<10;i++) {
            System.out.println("시간"+airDTO.getBody().getItems().get(i).getDataTime()+"  "+airDTO.getBody().getItems().get(i).getKhaiGrade() + "농도이다");
        }*/
        return airDTO.getBody().getItems();
        }


    @PostMapping("/weatherInfo")//기상정보-축제
    public List<Weather> weatherInfo(@RequestBody WeatherForecastParam weatherForecastParam) throws ParseException, IOException {
        Calendar calendar = new GregorianCalendar(Locale.KOREA);
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd", Locale.KOREA);
        SimpleDateFormat currentTimeFormat = new SimpleDateFormat("yyyyMMddHHmm", Locale.KOREA);

        String today = format.format(calendar.getTime());     // 현재 날짜
        long currentMillis = calendar.getTimeInMillis();    // 현재 시간을 초로
        Date standardDate = currentTimeFormat.parse(today+"0600");    // api가 아침 6시를 기준으로 데이터가 갱신되므로
        long standardMillis = standardDate.getTime();       // 기준 시간을 초로

       // System.out.println("넘어온contentid이거다"+weatherForecastParam.getContentid());
        Festival festivals2 = new Festival();
        festivals2.setMapX("0");
        festivals2.setMapY("0");
        System.out.println("/weatherinfo에서db값체크1"+festivals2.getAddr1()+"*"+festivals2.getMapX()+"*"+festivals2.getMapY());
        festivals2=festivalRepository.findByContentId(weatherForecastParam.getContentid());
        System.out.println("/weatherinfo에서값체크"+weatherForecastParam.getAddr()+" "+weatherForecastParam.getMapX()+" "+weatherForecastParam.getMapY());
        weatherForecastParam.setAddr(festivals2.getAddr1());
        weatherForecastParam.setMapX(festivals2.getMapX());
        weatherForecastParam.setMapY(festivals2.getMapY());
        System.out.println("/weatherinfo에서db값체크2"+festivals2.getAddr1()+"*"+festivals2.getMapX().length()+"*"+festivals2.getMapY());


        if(festivals2.getMapX().length()==0)//예외처리
        {
            System.out.println("db에 x,y값이 존재하지않는 축제입니다kkkkkkkkkkkkkkkkkkk");
            weatherForecastParam.setMapX("126.9787932340");
            weatherForecastParam.setMapY("37.5659098804");


        }
        else {System.out.println("몰라영");
        }
        // 새벽 6시 이전이면 하루 전 데이터 가져옴
        String currentTime;
        if (standardMillis > currentMillis) {
        calendar.add(Calendar.DATE, -1);
        }
        currentTime = format.format(calendar.getTime()) + "0600";

        String addr = weatherForecastParam.getAddr();
        String regId = null;
        Map<String, String> weatherAreaCode = weatherForecastAreaCode.getAreaList();

        for (var key : weatherAreaCode.entrySet()) {
        if (addr.contains(key.getKey())) {
        regId = weatherAreaCode.get(key.getKey());
        break;
        }
        }

        // 중기예보정보 3~10일의 데이터가 들어있음
        StringBuilder urlBuilder = new StringBuilder("http://newsky2.kma.go.kr/service/MiddleFrcstInfoService/getMiddleLandWeather");
        urlBuilder.append("?")
        .append(URLEncoder.encode("ServiceKey", StandardCharsets.UTF_8))
        .append("=")
        .append(apiServiceKey);
        urlBuilder.append("&")
        .append(URLEncoder.encode("regId", StandardCharsets.UTF_8))
        .append("=")
        .append(URLEncoder.encode(regId, StandardCharsets.UTF_8));
        urlBuilder.append("&")
        .append(URLEncoder.encode("tmFc", StandardCharsets.UTF_8))
        .append("=")
        .append(URLEncoder.encode(currentTime, StandardCharsets.UTF_8));
        urlBuilder.append("&")
        .append(URLEncoder.encode("numOfRows", StandardCharsets.UTF_8))
        .append("=")
        .append(URLEncoder.encode("10", StandardCharsets.UTF_8));
        urlBuilder.append("&")
        .append(URLEncoder.encode("pageNo", StandardCharsets.UTF_8))
        .append("=")
        .append(URLEncoder.encode("1", StandardCharsets.UTF_8));
        urlBuilder.append("&")
        .append(URLEncoder.encode("_type", StandardCharsets.UTF_8))
        .append("=")
        .append(URLEncoder.encode("json", StandardCharsets.UTF_8));
        URL url = new URL(urlBuilder.toString());

        MiddleTermWeatherResponse middleTermWeatherResponse = mapper.readValue(url, MiddleTermWeatherResponse.class);

        Map<String, String> tempAreaCode = tempForecastAreaCode.getAreaList();

        for (var key : tempAreaCode.entrySet()) {
        if (addr.contains(key.getKey())) {
        regId = tempAreaCode.get(key.getKey());
        break;
        }
        }

        // 3~7일 기온의 정보가 있음
        urlBuilder = new StringBuilder("http://newsky2.kma.go.kr/service/MiddleFrcstInfoService/getMiddleTemperature");
        urlBuilder.append("?")
        .append(URLEncoder.encode("ServiceKey", StandardCharsets.UTF_8))
        .append("=")
        .append(apiServiceKey);
        urlBuilder.append("&")
        .append(URLEncoder.encode("regId", StandardCharsets.UTF_8))
        .append("=")
        .append(URLEncoder.encode(regId, StandardCharsets.UTF_8));
        urlBuilder.append("&")
        .append(URLEncoder.encode("tmFc", StandardCharsets.UTF_8))
        .append("=")
        .append(URLEncoder.encode(currentTime, StandardCharsets.UTF_8));
        urlBuilder.append("&")
        .append(URLEncoder.encode("numOfRows", StandardCharsets.UTF_8))
        .append("=")
        .append(URLEncoder.encode("10", StandardCharsets.UTF_8));
        urlBuilder.append("&")
        .append(URLEncoder.encode("pageNo", StandardCharsets.UTF_8))
        .append("=")
        .append(URLEncoder.encode("1", StandardCharsets.UTF_8));
        urlBuilder.append("&")
        .append(URLEncoder.encode("_type", StandardCharsets.UTF_8))
        .append("=")
        .append(URLEncoder.encode("json", StandardCharsets.UTF_8));
        url = new URL(urlBuilder.toString());

        WeatherTempResponse weatherTempResponse = mapper.readValue(url, WeatherTempResponse.class);

        double x = Double.parseDouble(weatherForecastParam.getMapX());
        double y = Double.parseDouble(weatherForecastParam.getMapY());
        Map<String, Object> xy = getGridxy(y, x);   // 동네예보 전용 좌표로

        calendar = new GregorianCalendar(Locale.KOREA);

        standardDate = currentTimeFormat.parse(today+"0210");
        standardMillis = standardDate.getTime();

        String base_date;
        String base_time = "0210";
        // 새벽 2시 10분 이전이면 하루 전 데이터 가져옴
        if (standardMillis > currentMillis) {
        calendar.add(Calendar.DATE, -1);
        base_time = "0800";
        }
        base_date = format.format(calendar.getTime());

        String nx = String.valueOf(xy.get("x"));
        String ny = String.valueOf(xy.get("y"));
        nx = nx.substring(0, nx.length()-2);    // 문자열에서 .0 제거
        ny = ny.substring(0, ny.length()-2);

        // 3일치 기후 정보 가져오기
        urlBuilder = new StringBuilder("http://newsky2.kma.go.kr/service/SecndSrtpdFrcstInfoService2/ForecastSpaceData");
        urlBuilder.append("?")
        .append(URLEncoder.encode("serviceKey", StandardCharsets.UTF_8))
        .append("=")
        .append(apiServiceKey);
        urlBuilder.append("&")
        .append(URLEncoder.encode("base_date", StandardCharsets.UTF_8))
        .append("=")
        .append(URLEncoder.encode(base_date, StandardCharsets.UTF_8));
        urlBuilder.append("&")
        .append(URLEncoder.encode("base_time", StandardCharsets.UTF_8))
        .append("=")
        .append(URLEncoder.encode(base_time, StandardCharsets.UTF_8));
        urlBuilder.append("&")
        .append(URLEncoder.encode("nx", StandardCharsets.UTF_8))
        .append("=")
        .append(URLEncoder.encode(nx, StandardCharsets.UTF_8));
        urlBuilder.append("&")
        .append(URLEncoder.encode("ny", StandardCharsets.UTF_8))
        .append("=")
        .append(URLEncoder.encode(ny, StandardCharsets.UTF_8));
        urlBuilder.append("&")
        .append(URLEncoder.encode("numOfRows", StandardCharsets.UTF_8))
        .append("=")
        .append(URLEncoder.encode("255", StandardCharsets.UTF_8));
        urlBuilder.append("&")
        .append(URLEncoder.encode("pageNo", StandardCharsets.UTF_8))
        .append("=")
        .append(URLEncoder.encode("1", StandardCharsets.UTF_8));
        urlBuilder.append("&")
        .append(URLEncoder.encode("_type", StandardCharsets.UTF_8))
        .append("=")
        .append(URLEncoder.encode("json", StandardCharsets.UTF_8));
        url = new URL(urlBuilder.toString());

        ShortTermWeatherResponse shortTermWeatherResponse = mapper.readValue(url, ShortTermWeatherResponse.class);

        List<ShortTermWeatherItem> shortTermWeatherItems = shortTermWeatherResponse.getResponse().getBody().getItems().getItem();

        List<Weather> weatherList = new ArrayList<>();

        // 다음날 날짜 가져옴
        calendar = new GregorianCalendar(Locale.KOREA);
        calendar.add(Calendar.DATE, 1);
        String tomorrow = format.format(calendar.getTime());

        // 위에서 다음날로 해놨기 때문에 하루 뒤로 하고 요일 정보 가져옴
        calendar.add(Calendar.DATE, -1);
        int dayOfWeekCode = calendar.get(Calendar.DAY_OF_WEEK);

        // 형식이 달라서 1, 2일만 따로 설정
        setDay12Info(shortTermWeatherItems, weatherList, today, dayOfWeekCode);
        setDay12Info(shortTermWeatherItems, weatherList, tomorrow, dayOfWeekCode+1);

        Weather weatherInfo;

        weatherInfo = new Weather();
        weatherInfo.setMinTemp( weatherTempResponse.getResponse().getBody().getItems().getItem().getTaMin3() );
        weatherInfo.setMaxTemp( weatherTempResponse.getResponse().getBody().getItems().getItem().getTaMax3() );
        weatherInfo.setState( middleTermWeatherResponse.getResponse().getBody().getItems().getItem().getWf3Pm() );
        weatherInfo.setDayOfTheWeek( setDayOfWeek(dayOfWeekCode+2) );
        weatherList.add(weatherInfo);
        weatherInfo = new Weather();
        weatherInfo.setMinTemp( weatherTempResponse.getResponse().getBody().getItems().getItem().getTaMin4() );
        weatherInfo.setMaxTemp( weatherTempResponse.getResponse().getBody().getItems().getItem().getTaMax4() );
        weatherInfo.setState( middleTermWeatherResponse.getResponse().getBody().getItems().getItem().getWf4Pm() );
        weatherInfo.setDayOfTheWeek( setDayOfWeek(dayOfWeekCode+3) );
        weatherList.add(weatherInfo);
        weatherInfo = new Weather();
        weatherInfo.setMinTemp( weatherTempResponse.getResponse().getBody().getItems().getItem().getTaMin5() );
        weatherInfo.setMaxTemp( weatherTempResponse.getResponse().getBody().getItems().getItem().getTaMax5() );
        weatherInfo.setState( middleTermWeatherResponse.getResponse().getBody().getItems().getItem().getWf5Pm() );
        weatherInfo.setDayOfTheWeek( setDayOfWeek(dayOfWeekCode+4) );
        weatherList.add(weatherInfo);
        weatherInfo = new Weather();
        weatherInfo.setMinTemp( weatherTempResponse.getResponse().getBody().getItems().getItem().getTaMin6() );
        weatherInfo.setMaxTemp( weatherTempResponse.getResponse().getBody().getItems().getItem().getTaMax6() );
        weatherInfo.setState( middleTermWeatherResponse.getResponse().getBody().getItems().getItem().getWf6Pm() );
        weatherInfo.setDayOfTheWeek( setDayOfWeek(dayOfWeekCode+5) );
        weatherList.add(weatherInfo);
        weatherInfo = new Weather();
        weatherInfo.setMinTemp( weatherTempResponse.getResponse().getBody().getItems().getItem().getTaMin7() );
        weatherInfo.setMaxTemp( weatherTempResponse.getResponse().getBody().getItems().getItem().getTaMax7() );
        weatherInfo.setState( middleTermWeatherResponse.getResponse().getBody().getItems().getItem().getWf7Pm() );
        weatherInfo.setDayOfTheWeek( setDayOfWeek(dayOfWeekCode+6) );
        weatherList.add(weatherInfo);

            return weatherList;
        }

        @PostMapping("/weatherInfo2")//기상정보-관광지
        public List<Weather> weatherInfo2(@RequestBody WeatherForecastParam weatherForecastParam) throws ParseException, IOException {
        Calendar calendar = new GregorianCalendar(Locale.KOREA);
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd", Locale.KOREA);
        SimpleDateFormat currentTimeFormat = new SimpleDateFormat("yyyyMMddHHmm", Locale.KOREA);

        System.out.println("실행되나ㅏㅏㅏㅏㅏㅏㅏㅏㅏㅏㅏㅏㅏㅏㅏㅏ");
        String today = format.format(calendar.getTime());     // 현재 날짜
        long currentMillis = calendar.getTimeInMillis();    // 현재 시간을 초로
        Date standardDate = currentTimeFormat.parse(today+"0600");    // api가 아침 6시를 기준으로 데이터가 갱신되므로
        long standardMillis = standardDate.getTime();       // 기준 시간을 초로

//       // System.out.println("넘어온contentid이거다"+weatherForecastParam.getContentid());
        //     System.out.println("airinfo2의 contentid 파라미터값체크"+weatherForecastParam.getContentid());
        var aaa=weatherForecastParam.getContentid();
        //     System.out.println("airinfo2의 aaa 파라미터값체크"+aaa);

        StringBuilder urlBuilder = new StringBuilder("http://api.visitkorea.or.kr/openapi/service/rest/KorService/detailCommon"); /*URL*/
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
        .append(URLEncoder.encode(aaa, StandardCharsets.UTF_8));    // 콘텐츠 ID
        urlBuilder.append("&")
        .append(URLEncoder.encode("contentTypeId", StandardCharsets.UTF_8))
        .append("=")
        .append(URLEncoder.encode("12", StandardCharsets.UTF_8));    // 관광타입(관광지, 숙박 등) ID
        urlBuilder.append("&")//tourItem.getContentTypeId()->12로변경
        .append(URLEncoder.encode("defaultYN", StandardCharsets.UTF_8))
        .append("=")
        .append(URLEncoder.encode("Y", StandardCharsets.UTF_8));    // 기본정보 조회여부
        urlBuilder.append("&")
        .append(URLEncoder.encode("firstImageYN", StandardCharsets.UTF_8))
        .append("=")
        .append(URLEncoder.encode("Y", StandardCharsets.UTF_8));    // 원본, 썸네일 대표이미지 조회여부
        urlBuilder.append("&")
        .append(URLEncoder.encode("areacodeYN", StandardCharsets.UTF_8))
        .append("=")
        .append(URLEncoder.encode("Y", StandardCharsets.UTF_8));    // 지역코드, 시군구코드 조회여부
        urlBuilder.append("&")
        .append(URLEncoder.encode("catcodeYN", StandardCharsets.UTF_8))
        .append("=")
        .append(URLEncoder.encode("Y", StandardCharsets.UTF_8));    // 대,중,소분류코드 조회여부
        urlBuilder.append("&")
        .append(URLEncoder.encode("addrinfoYN", StandardCharsets.UTF_8))
        .append("=")
        .append(URLEncoder.encode("Y", StandardCharsets.UTF_8));    // 주소, 상세주소 조회여부
        urlBuilder.append("&")
        .append(URLEncoder.encode("mapinfoYN", StandardCharsets.UTF_8))
        .append("=")
        .append(URLEncoder.encode("Y", StandardCharsets.UTF_8));    // 좌표 X,Y 조회여부
        urlBuilder.append("&")
        .append(URLEncoder.encode("overviewYN", StandardCharsets.UTF_8))
        .append("=")
        .append(URLEncoder.encode("Y", StandardCharsets.UTF_8));    // 콘텐츠 개요 조회여부
        urlBuilder.append("&")
        .append(URLEncoder.encode("_type", StandardCharsets.UTF_8))
        .append("=")
        .append(URLEncoder.encode("json", StandardCharsets.UTF_8));
        URL url = new URL(urlBuilder.toString());

        ComInfoResponse responseResult2 = mapper.readValue(url, ComInfoResponse.class);
        //   System.out.println("airinfo첫번째api값체크"+responseResult2.getResponse().getBody().getItems().getItem());
        //  System.out.println("airinfo2의 getmapx값체크요-"+responseResult2.getResponse().getBody().getItems().getItem().getMapx()+" "+responseResult2.getResponse().getBody().getItems().getItem().getMapy());
        System.out.println(weatherForecastParam.getMapX()+"x좌표");

        //   weatherForecastParam.setAddr(responseResult2.getResponse().getBody().getItems().getItem().getAddr1());
        //  weatherForecastParam.setMapX(responseResult2.getResponse().getBody().getItems().getItem().getMapx());
        //  weatherForecastParam.setMapY(responseResult2.getResponse().getBody().getItems().getItem().getMapx());
//      System.out.println("기상정보값관련값체크"+responseResult2.getResponse().getBody().getItems().getItem().getAddr1());


        // 새벽 6시 이전이면 하루 전 데이터 가져옴
        String currentTime;
        if (standardMillis > currentMillis) {
        calendar.add(Calendar.DATE, -1);
        }
        currentTime = format.format(calendar.getTime()) + "0600";

        String addr = responseResult2.getResponse().getBody().getItems().getItem().getAddr1();
        String regId = null;
        Map<String, String> weatherAreaCode = weatherForecastAreaCode.getAreaList();

        for (var key : weatherAreaCode.entrySet()) {
        if (addr.contains(key.getKey())) {
        regId = weatherAreaCode.get(key.getKey());
        break;
        }
        }

        // 중기예보정보 3~10일의 데이터가 들어있음
        urlBuilder = new StringBuilder("http://newsky2.kma.go.kr/service/MiddleFrcstInfoService/getMiddleLandWeather");
        urlBuilder.append("?")
        .append(URLEncoder.encode("ServiceKey", StandardCharsets.UTF_8))
        .append("=")
        .append(apiServiceKey);
        urlBuilder.append("&")
        .append(URLEncoder.encode("regId", StandardCharsets.UTF_8))
        .append("=")
        .append(URLEncoder.encode(regId, StandardCharsets.UTF_8));
        urlBuilder.append("&")
        .append(URLEncoder.encode("tmFc", StandardCharsets.UTF_8))
        .append("=")
        .append(URLEncoder.encode(currentTime, StandardCharsets.UTF_8));
        urlBuilder.append("&")
        .append(URLEncoder.encode("numOfRows", StandardCharsets.UTF_8))
        .append("=")
        .append(URLEncoder.encode("10", StandardCharsets.UTF_8));
        urlBuilder.append("&")
        .append(URLEncoder.encode("pageNo", StandardCharsets.UTF_8))
        .append("=")
        .append(URLEncoder.encode("1", StandardCharsets.UTF_8));
        urlBuilder.append("&")
        .append(URLEncoder.encode("_type", StandardCharsets.UTF_8))
        .append("=")
        .append(URLEncoder.encode("json", StandardCharsets.UTF_8));
        url = new URL(urlBuilder.toString());

        MiddleTermWeatherResponse middleTermWeatherResponse = mapper.readValue(url, MiddleTermWeatherResponse.class);

        Map<String, String> tempAreaCode = tempForecastAreaCode.getAreaList();

        for (var key : tempAreaCode.entrySet()) {
        if (addr.contains(key.getKey())) {
        regId = tempAreaCode.get(key.getKey());
        break;
        }
        }

        // 3~7일 기온의 정보가 있음
        urlBuilder = new StringBuilder("http://newsky2.kma.go.kr/service/MiddleFrcstInfoService/getMiddleTemperature");
        urlBuilder.append("?")
        .append(URLEncoder.encode("ServiceKey", StandardCharsets.UTF_8))
        .append("=")
        .append(apiServiceKey);
        urlBuilder.append("&")
        .append(URLEncoder.encode("regId", StandardCharsets.UTF_8))
        .append("=")
        .append(URLEncoder.encode(regId, StandardCharsets.UTF_8));
        urlBuilder.append("&")
        .append(URLEncoder.encode("tmFc", StandardCharsets.UTF_8))
        .append("=")
        .append(URLEncoder.encode(currentTime, StandardCharsets.UTF_8));
        urlBuilder.append("&")
        .append(URLEncoder.encode("numOfRows", StandardCharsets.UTF_8))
        .append("=")
        .append(URLEncoder.encode("10", StandardCharsets.UTF_8));
        urlBuilder.append("&")
        .append(URLEncoder.encode("pageNo", StandardCharsets.UTF_8))
        .append("=")
        .append(URLEncoder.encode("1", StandardCharsets.UTF_8));
        urlBuilder.append("&")
        .append(URLEncoder.encode("_type", StandardCharsets.UTF_8))
        .append("=")
        .append(URLEncoder.encode("json", StandardCharsets.UTF_8));
        url = new URL(urlBuilder.toString());

        WeatherTempResponse weatherTempResponse = mapper.readValue(url, WeatherTempResponse.class);

        double x = Double.parseDouble(responseResult2.getResponse().getBody().getItems().getItem().getMapx());
        double y = Double.parseDouble(responseResult2.getResponse().getBody().getItems().getItem().getMapy());
        Map<String, Object> xy = getGridxy(y, x);   // 동네예보 전용 좌표로

        calendar = new GregorianCalendar(Locale.KOREA);

        standardDate = currentTimeFormat.parse(today+"0210");
        standardMillis = standardDate.getTime();

        String base_date;
        String base_time = "0210";
        // 새벽 2시 10분 이전이면 하루 전 데이터 가져옴
        if (standardMillis > currentMillis) {
        calendar.add(Calendar.DATE, -1);
        base_time = "0800";
        }
        base_date = format.format(calendar.getTime());

        String nx = String.valueOf(xy.get("x"));
        String ny = String.valueOf(xy.get("y"));
        nx = nx.substring(0, nx.length()-2);    // 문자열에서 .0 제거
        ny = ny.substring(0, ny.length()-2);

        // 3일치 기후 정보 가져오기
        urlBuilder = new StringBuilder("http://newsky2.kma.go.kr/service/SecndSrtpdFrcstInfoService2/ForecastSpaceData");
        urlBuilder.append("?")
        .append(URLEncoder.encode("serviceKey", StandardCharsets.UTF_8))
        .append("=")
        .append(apiServiceKey);
        urlBuilder.append("&")
        .append(URLEncoder.encode("base_date", StandardCharsets.UTF_8))
        .append("=")
        .append(URLEncoder.encode(base_date, StandardCharsets.UTF_8));
        urlBuilder.append("&")
        .append(URLEncoder.encode("base_time", StandardCharsets.UTF_8))
        .append("=")
        .append(URLEncoder.encode(base_time, StandardCharsets.UTF_8));
        urlBuilder.append("&")
        .append(URLEncoder.encode("nx", StandardCharsets.UTF_8))
        .append("=")
        .append(URLEncoder.encode(nx, StandardCharsets.UTF_8));
        urlBuilder.append("&")
        .append(URLEncoder.encode("ny", StandardCharsets.UTF_8))
        .append("=")
        .append(URLEncoder.encode(ny, StandardCharsets.UTF_8));
        urlBuilder.append("&")
        .append(URLEncoder.encode("numOfRows", StandardCharsets.UTF_8))
        .append("=")
        .append(URLEncoder.encode("255", StandardCharsets.UTF_8));
        urlBuilder.append("&")
        .append(URLEncoder.encode("pageNo", StandardCharsets.UTF_8))
        .append("=")
        .append(URLEncoder.encode("1", StandardCharsets.UTF_8));
        urlBuilder.append("&")
        .append(URLEncoder.encode("_type", StandardCharsets.UTF_8))
        .append("=")
        .append(URLEncoder.encode("json", StandardCharsets.UTF_8));
        url = new URL(urlBuilder.toString());

        ShortTermWeatherResponse shortTermWeatherResponse = mapper.readValue(url, ShortTermWeatherResponse.class);

        List<ShortTermWeatherItem> shortTermWeatherItems = shortTermWeatherResponse.getResponse().getBody().getItems().getItem();

        List<Weather> weatherList = new ArrayList<>();

        // 다음날 날짜 가져옴
        calendar = new GregorianCalendar(Locale.KOREA);
        calendar.add(Calendar.DATE, 1);
        String tomorrow = format.format(calendar.getTime());

        // 위에서 다음날로 해놨기 때문에 하루 뒤로 하고 요일 정보 가져옴
        calendar.add(Calendar.DATE, -1);
        int dayOfWeekCode = calendar.get(Calendar.DAY_OF_WEEK);

        // 형식이 달라서 1, 2일만 따로 설정
        setDay12Info(shortTermWeatherItems, weatherList, today, dayOfWeekCode);
        setDay12Info(shortTermWeatherItems, weatherList, tomorrow, dayOfWeekCode+1);

        Weather weatherInfo;

        weatherInfo = new Weather();
        weatherInfo.setMinTemp( weatherTempResponse.getResponse().getBody().getItems().getItem().getTaMin3() );
        weatherInfo.setMaxTemp( weatherTempResponse.getResponse().getBody().getItems().getItem().getTaMax3() );
        weatherInfo.setState( middleTermWeatherResponse.getResponse().getBody().getItems().getItem().getWf3Pm() );
        weatherInfo.setDayOfTheWeek( setDayOfWeek(dayOfWeekCode+2) );
        weatherList.add(weatherInfo);
        weatherInfo = new Weather();
        weatherInfo.setMinTemp( weatherTempResponse.getResponse().getBody().getItems().getItem().getTaMin4() );
        weatherInfo.setMaxTemp( weatherTempResponse.getResponse().getBody().getItems().getItem().getTaMax4() );
        weatherInfo.setState( middleTermWeatherResponse.getResponse().getBody().getItems().getItem().getWf4Pm() );
        weatherInfo.setDayOfTheWeek( setDayOfWeek(dayOfWeekCode+3) );
        weatherList.add(weatherInfo);
        weatherInfo = new Weather();
        weatherInfo.setMinTemp( weatherTempResponse.getResponse().getBody().getItems().getItem().getTaMin5() );
        weatherInfo.setMaxTemp( weatherTempResponse.getResponse().getBody().getItems().getItem().getTaMax5() );
        weatherInfo.setState( middleTermWeatherResponse.getResponse().getBody().getItems().getItem().getWf5Pm() );
        weatherInfo.setDayOfTheWeek( setDayOfWeek(dayOfWeekCode+4) );
        weatherList.add(weatherInfo);
        weatherInfo = new Weather();
        weatherInfo.setMinTemp( weatherTempResponse.getResponse().getBody().getItems().getItem().getTaMin6() );
        weatherInfo.setMaxTemp( weatherTempResponse.getResponse().getBody().getItems().getItem().getTaMax6() );
        weatherInfo.setState( middleTermWeatherResponse.getResponse().getBody().getItems().getItem().getWf6Pm() );
        weatherInfo.setDayOfTheWeek( setDayOfWeek(dayOfWeekCode+5) );
        weatherList.add(weatherInfo);
        weatherInfo = new Weather();
        weatherInfo.setMinTemp( weatherTempResponse.getResponse().getBody().getItems().getItem().getTaMin7() );
        weatherInfo.setMaxTemp( weatherTempResponse.getResponse().getBody().getItems().getItem().getTaMax7() );
        weatherInfo.setState( middleTermWeatherResponse.getResponse().getBody().getItems().getItem().getWf7Pm() );
        weatherInfo.setDayOfTheWeek( setDayOfWeek(dayOfWeekCode+6) );
        weatherList.add(weatherInfo);

        return weatherList;
        }


    // 요일 설정
    private String setDayOfWeek(int dayOfWeekCode) {
        String dayOfWeek = null;
        dayOfWeekCode--;
        dayOfWeekCode = dayOfWeekCode % 7;

        switch (dayOfWeekCode) {
        case 0:
        dayOfWeek = "일";
        break;
        case 1:
        dayOfWeek = "월";
        break;
        case 2:
        dayOfWeek = "화";
        break;
        case 3:
        dayOfWeek = "수";
        break;
        case 4:
        dayOfWeek = "목";
        break;
        case 5:
        dayOfWeek = "금";
        break;
        case 6:
        dayOfWeek = "토";
        break;
        }

        return dayOfWeek;
        }


    // 1,2일의 날씨 정보는 3일 이후의 데이터와 형식이 달라서 따로 설정
    private void setDay12Info(List<ShortTermWeatherItem> shortTermWeatherItems, List<Weather> weatherList, String day, int dayOfWeekCode) {
        Weather weatherInfo = new Weather();

        int skyStateCode;
        String skyState = null;
        for (var item: shortTermWeatherItems) {
        if (item.getFcstDate().equals(day)) {
        if (item.getCategory().equals("SKY")) {
        skyStateCode = Integer.parseInt(item.getFcstValue());
        if (skyStateCode == 1)
        skyState = "맑음";
        if (skyStateCode == 3)
        skyState = "구름 많음";
        if (skyStateCode == 4)
        skyState = "흐림";
        if (weatherInfo.getState().isEmpty())
        weatherInfo.setState(skyState);
        }
        if (item.getCategory().equals("TMN")) {
        weatherInfo.setMinTemp(item.getFcstValue());
        }
        if (item.getCategory().equals("TMX")) {
        weatherInfo.setMaxTemp(item.getFcstValue());
        }
        }
        }
        weatherInfo.setDayOfTheWeek( setDayOfWeek(dayOfWeekCode) );

        weatherList.add(weatherInfo);
        }


    // 동네예보 좌표 구하는 메소드
    private Map<String, Object> getGridxy(double orgLat, double orgLon) {

        double RE = 6371.00877; // 지구 반경(km)
        double GRID = 5.0; // 격자 간격(km)
        double SLAT1 = 30.0; // 투영 위도1(degree)
        double SLAT2 = 60.0; // 투영 위도2(degree)
        double OLON = 126.0; // 기준점 경도(degree)
        double OLAT = 38.0; // 기준점 위도(degree)
        double XO = 43; // 기준점 X좌표(GRID)
        double YO = 136; // 기1준점 Y좌표(GRID)

        double DEGRAD = Math.PI / 180.0;
        // double RADDEG = 180.0 / Math.PI;

        double re = RE / GRID;
        double slat1 = SLAT1 * DEGRAD;
        double slat2 = SLAT2 * DEGRAD;
        double olon = OLON * DEGRAD;
        double olat = OLAT * DEGRAD;

        double sn = Math.tan(Math.PI * 0.25 + slat2 * 0.5) / Math.tan(Math.PI * 0.25 + slat1 * 0.5);
        sn = Math.log(Math.cos(slat1) / Math.cos(slat2)) / Math.log(sn);
        double sf = Math.tan(Math.PI * 0.25 + slat1 * 0.5);
        sf = Math.pow(sf, sn) * Math.cos(slat1) / sn;
        double ro = Math.tan(Math.PI * 0.25 + olat * 0.5);
        ro = re * sf / Math.pow(ro, sn);
        Map<String, Object> map = new HashMap<>();
        map.put("lat", orgLat);
        map.put("lng", orgLat);
        double ra = Math.tan(Math.PI * 0.25 + (orgLat) * DEGRAD * 0.5);
        ra = re * sf / Math.pow(ra, sn);
        double theta = orgLon * DEGRAD - olon;
        if (theta > Math.PI)
        theta -= 2.0 * Math.PI;
        if (theta < -Math.PI)
        theta += 2.0 * Math.PI;
        theta *= sn;

        map.put("x", Math.floor(ra * Math.sin(theta) + XO + 0.5));
        map.put("y", Math.floor(ro - ra * Math.cos(theta) + YO + 0.5));

        return map;
        }

        }

package growthon.withtail_be.domain.animalhospital.controller;

import growthon.withtail_be.domain.animalhospital.dto.request.AnimalHospitalSearchReqDto;
import growthon.withtail_be.domain.animalhospital.dto.response.AnimalHospitalDetailResDto;
import growthon.withtail_be.domain.animalhospital.dto.response.AnimalHospitalSearchItemResDto;
import growthon.withtail_be.domain.animalhospital.dto.response.HospitalNewsResDto;
import growthon.withtail_be.domain.animalhospital.dto.response.HospitalOperatingHoursResDto;
import growthon.withtail_be.domain.animalhospital.dto.response.MedicalStaffResDto;
import growthon.withtail_be.domain.model.RegionType;
import growthon.withtail_be.domain.animalhospital.service.AnimalHospitalService;
import growthon.withtail_be.global.code.SuccessStatus;
import growthon.withtail_be.global.response.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/hospitals")
public class AnimalHospitalController {

    private final AnimalHospitalService animalHospitalService;


    // 병원 검색 (필터 미포함)
    @Operation(
            summary = "병원 검색(기본)",
            description = """
            키워드/지역 + 페이징으로 병원 목록을 조회합니다. (필터/정렬 없음)
            
            Params:
            - keyword: 병원명 검색(부분일치)
            - region: RegionType(enum)
            - page: 0부터 시작 (default 0)
            - size: 페이지 사이즈 (default 10)
            
            Example:
            - /hospitals/search?keyword=우리&region=SEOUL&page=0&size=10
            
            Response(리스트 카드용):
            - 병원명, 주소요약, 24시간/야간, 전화번호, 제공서비스, 이미지(null 가능)
            """
    )
    @GetMapping("/search")
    public BaseResponse<Page<AnimalHospitalSearchItemResDto>> searchBasic(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) RegionType region,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<AnimalHospitalSearchItemResDto> result =
                animalHospitalService.searchBasic(keyword, region, page, size);

        return BaseResponse.onSuccess(SuccessStatus.OK, result);
    }


    //병원 검색 (필터 포함 + 정렬)
    @Operation(
            summary = "병원 검색(필터+정렬) - 단일 API",
            description = """
            병원 검색 화면 메인 API (필터/정렬/페이징을 한 번에 처리)
            
            Params:
            - keyword: 병원명 검색(부분일치)
            - region: RegionType(enum)
            - specialty: 전문분야(문자열 1개) 예) 내과, 외과...
            - animalType: 동물종(문자열 1개) 예) 강아지, 고양이...
            - open24h: 24시간 여부(true/false)
            - nightCare: 야간진료 여부(true/false)
            - sortType: RATING_DESC | REVIEW_COUNT_DESC
            - page: 0부터 시작
            - size: 페이지 사이즈
            
            Example:
            - /hospitals?specialty=외과&animalType=강아지&nightCare=true&sortType=REVIEW_COUNT_DESC&page=0&size=10
            
            Response(리스트 카드용):
            - 병원명, 평점/리뷰수, 위치, 24시간/야간, 전화번호, 제공서비스, 이미지(null 가능)
            """
    )
    @GetMapping
    public BaseResponse<Page<AnimalHospitalSearchItemResDto>> searchWithFilters(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) RegionType region,
            @RequestParam(required = false) String specialty,
            @RequestParam(required = false) String animalType,
            @RequestParam(required = false) Boolean open24h,
            @RequestParam(required = false) Boolean nightCare,
            @RequestParam(required = false) String sortType,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size
    ) {
        AnimalHospitalSearchReqDto req = new AnimalHospitalSearchReqDto(
                specialty,
                animalType,
                open24h,
                nightCare,
                sortType,
                page,
                size
        );

        Page<AnimalHospitalSearchItemResDto> result =
                animalHospitalService.searchWithFilters(keyword, region, req);

        return BaseResponse.onSuccess(SuccessStatus.OK, result);
    }

    //병원 상세 (상단 공통)
    @Operation(
            summary = "병원 상세(상단 공통)",
            description = """
            병원 상세 페이지 상단 공통 영역 데이터.
            
            Path:
            - hospitalId: 병원 ID
            
            Example:
            - /hospitals/1
            
            Response(상단 공통용):
            - 병원명, 주소, 전화번호, 24시간/야간, 대표이미지(null 가능) 등
            """
    )
    @GetMapping("/{hospitalId}")
    public BaseResponse<AnimalHospitalDetailResDto> getHospitalDetailTop(
            @PathVariable Long hospitalId
    ) {
        AnimalHospitalDetailResDto result =
                animalHospitalService.getHospitalDetailTop(hospitalId);

        return BaseResponse.onSuccess(SuccessStatus.OK, result);
    }

    //병원 진료시간 목록
    @Operation(
            summary = "병원 상세 - 진료시간 목록(요일별)",
            description = """
            [사용 화면]
            - 병원 상세 페이지의 “진료시간” 영역(또는 탭)
            
            [Path Variable]
            - hospitalId (required)
              - 예) /hospitals/1/hours
            
            [응답 데이터 사용처(프론트)]
            - List<HospitalOperatingHoursResDto>
            - 요일별로 오픈/마감 시간, 점심시간, 휴무 여부 등을 표시하는 용도
              (구체 필드는 DTO 정의 기준)
            
            [프론트 요청 예시]
            - /hospitals/1/hours
            """
    )
    @GetMapping("/{hospitalId}/hours")
    public BaseResponse<List<HospitalOperatingHoursResDto>> getOperatingHours(
            @PathVariable Long hospitalId
    ) {
        List<HospitalOperatingHoursResDto> result =
                animalHospitalService.getOperatingHours(hospitalId);

        return BaseResponse.onSuccess(SuccessStatus.OK, result);
    }

    //병원 의료진 목록
    @Operation(
            summary = "병원 상세 - 의료진 목록",
            description = """
            [사용 화면]
            - 병원 상세 페이지 “의료진” 탭
            
            [Path Variable]
            - hospitalId (required)
              - 예) /hospitals/1/staff
            
            [응답 데이터 사용처(프론트)]
            - List<MedicalStaffResDto>
            - 의료진 카드/리스트 구성
              - 의료진 이름, 직책/설명 등 (DTO 기준)
              - 이미지 URL은 테스트 단계에서는 null 가능
            
            [프론트 요청 예시]
            - /hospitals/1/staff
            """
    )
    @GetMapping("/{hospitalId}/staff")
    public BaseResponse<List<MedicalStaffResDto>> getMedicalStaffList(
            @PathVariable Long hospitalId
    ) {
        List<MedicalStaffResDto> result =
                animalHospitalService.getMedicalStaffList(hospitalId);

        return BaseResponse.onSuccess(SuccessStatus.OK, result);
    }

    //병원 소식 목록
    @Operation(
            summary = "병원 상세 - 병원 소식(공지) 목록",
            description = """
            [사용 화면]
            - 병원 상세 페이지 “병원 소식” 탭
            
            [Path Variable]
            - hospitalId (required)
              - 예) /hospitals/1/news
            
            [응답 데이터 사용처(프론트)]
            - List<HospitalNewsResDto>
            - 병원 공지/이벤트/소식 리스트 렌더링
              - 제목, 내용, 날짜 등 (DTO 기준)
            
            [프론트 요청 예시]
            - /hospitals/1/news
            """
    )
    @GetMapping("/{hospitalId}/news")
    public BaseResponse<List<HospitalNewsResDto>> getHospitalNewsList(
            @PathVariable Long hospitalId
    ) {
        List<HospitalNewsResDto> result =
                animalHospitalService.getHospitalNewsList(hospitalId);

        return BaseResponse.onSuccess(SuccessStatus.OK, result);
    }
}

package growthon.withtail_be.domain.animalhospital.controller;

import growthon.withtail_be.domain.animalhospital.dto.request.AnimalHospitalSearchReqDto;
import growthon.withtail_be.domain.animalhospital.dto.response.AnimalHospitalDetailResDto;
import growthon.withtail_be.domain.animalhospital.dto.response.AnimalHospitalSearchItemResDto;
import growthon.withtail_be.domain.animalhospital.dto.response.HospitalNewsResDto;
import growthon.withtail_be.domain.animalhospital.dto.response.HospitalOperatingHoursResDto;
import growthon.withtail_be.domain.animalhospital.dto.response.MedicalStaffResDto;
import growthon.withtail_be.domain.animalhospital.entity.RegionType;
import growthon.withtail_be.domain.animalhospital.service.AnimalHospitalService;
import growthon.withtail_be.global.code.SuccessStatus;
import growthon.withtail_be.global.response.BaseResponse;
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
    @GetMapping("/{hospitalId}")
    public BaseResponse<AnimalHospitalDetailResDto> getHospitalDetailTop(
            @PathVariable Long hospitalId
    ) {
        AnimalHospitalDetailResDto result =
                animalHospitalService.getHospitalDetailTop(hospitalId);

        return BaseResponse.onSuccess(SuccessStatus.OK, result);
    }

    //병원 진료시간 목록
    @GetMapping("/{hospitalId}/hours")
    public BaseResponse<List<HospitalOperatingHoursResDto>> getOperatingHours(
            @PathVariable Long hospitalId
    ) {
        List<HospitalOperatingHoursResDto> result =
                animalHospitalService.getOperatingHours(hospitalId);

        return BaseResponse.onSuccess(SuccessStatus.OK, result);
    }

    //병원 의료진 목록
    @GetMapping("/{hospitalId}/staff")
    public BaseResponse<List<MedicalStaffResDto>> getMedicalStaffList(
            @PathVariable Long hospitalId
    ) {
        List<MedicalStaffResDto> result =
                animalHospitalService.getMedicalStaffList(hospitalId);

        return BaseResponse.onSuccess(SuccessStatus.OK, result);
    }

    //병원 소식 목록
    @GetMapping("/{hospitalId}/news")
    public BaseResponse<List<HospitalNewsResDto>> getHospitalNewsList(
            @PathVariable Long hospitalId
    ) {
        List<HospitalNewsResDto> result =
                animalHospitalService.getHospitalNewsList(hospitalId);

        return BaseResponse.onSuccess(SuccessStatus.OK, result);
    }
}

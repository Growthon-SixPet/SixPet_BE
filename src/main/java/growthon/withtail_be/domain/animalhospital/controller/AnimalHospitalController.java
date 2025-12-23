package growthon.withtail_be.domain.animalhospital.controller;

import growthon.withtail_be.domain.animalhospital.dto.AnimalHospitalAboutResponse;
import growthon.withtail_be.domain.animalhospital.dto.AnimalHospitalDetailResponse;
import growthon.withtail_be.domain.animalhospital.dto.AnimalHospitalSearchResponse;
import growthon.withtail_be.domain.animalhospital.dto.AnimalHospitalStaffResponse;
import growthon.withtail_be.domain.animalhospital.service.AnimalHospitalService;
import io.swagger.v3.oas.annotations.Parameter;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/animal-hospitals")
public class AnimalHospitalController {

    private final AnimalHospitalService animalHospitalService;

    // 병원 검색(필터) - 단일 엔드포인트
    @GetMapping
    public Page<AnimalHospitalSearchResponse> search(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String sido,
            @RequestParam(required = false) String sigungu,
            @RequestParam(required = false) Boolean open24h,
            @RequestParam(required = false) Boolean nightCare,
            @RequestParam(required = false) Boolean emergencyAvailable,
            @RequestParam(required = false) List<Long> specialtyIds,
            @RequestParam(required = false) List<Long> animalTypeIds,
            @RequestParam(required = false) List<Long> amenityIds,
            @RequestParam(required = false) List<Long> paymentMethodIds,
            @RequestParam(required = false) String sortType,
            @Parameter(description = "0부터 시작") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "기본 10") @RequestParam(defaultValue = "10") int size
    ) {
        return animalHospitalService.search(
                keyword,
                sido,
                sigungu,
                open24h,
                nightCare,
                emergencyAvailable,
                specialtyIds,
                animalTypeIds,
                amenityIds,
                paymentMethodIds,
                sortType,
                page,
                size
        );
    }

    // 상세(기본)
    @GetMapping("/{id}")
    public AnimalHospitalDetailResponse detail(@PathVariable Long id) {
        return animalHospitalService.getDetail(id);
    }

    // 상세(병원 소개)
    @GetMapping("/{id}/about")
    public AnimalHospitalAboutResponse about(@PathVariable Long id) {
        return animalHospitalService.getAbout(id);
    }

    // 상세(의료진)
    @GetMapping("/{id}/staff")
    public List<AnimalHospitalStaffResponse> staff(@PathVariable Long id) {
        return animalHospitalService.getStaff(id);
    }
}

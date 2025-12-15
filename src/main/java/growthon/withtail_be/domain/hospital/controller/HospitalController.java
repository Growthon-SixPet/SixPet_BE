package growthon.withtail_be.domain.hospital.controller;

import growthon.withtail_be.domain.hospital.dto.HospitalSearchResponse;
import growthon.withtail_be.domain.hospital.service.HospitalService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

// 병원 검색 API 진입점
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/hospitals")
public class HospitalController {


    private final HospitalService hospitalService;

    // GET /api/hospitals?keyword=&sido=&sigungu=&page=&size=
    @GetMapping
    public Page<HospitalSearchResponse> search(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String sido,
            @RequestParam(required = false) String sigungu,
            @PageableDefault(size = 10) Pageable pageable
    ) {
        return hospitalService.search(keyword, sido, sigungu, pageable);
    }
}

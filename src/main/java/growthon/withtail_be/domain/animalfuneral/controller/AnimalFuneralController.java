package growthon.withtail_be.domain.animalfuneral.controller;

import growthon.withtail_be.domain.animalfuneral.dto.AnimalFuneralBlissStoneResDto;
import growthon.withtail_be.domain.animalfuneral.dto.AnimalFuneralDetailResDto;
import growthon.withtail_be.domain.animalfuneral.dto.AnimalFuneralProcedureResDto;
import growthon.withtail_be.domain.animalfuneral.dto.AnimalFuneralSearchResDto;
import growthon.withtail_be.domain.animalfuneral.service.AnimalFuneralService;
import growthon.withtail_be.global.code.SuccessStatus;
import growthon.withtail_be.global.response.BaseResponse;
import growthon.withtail_be.domain.model.RegionType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequiredArgsConstructor
@RequestMapping("/funerals")
public class AnimalFuneralController {

    private final AnimalFuneralService animalFuneralService;

    // 장례식장 검색(목록)
    @Operation(
            summary = "장례식장 검색(목록)",
            description = """
                    장례식장 목록을 조회합니다. (단일 엔드포인트로 필터/페이징 처리)
                    - keyword: 장례식장 이름 검색(부분 일치)
                    - region: 권역(RegionType enum)
                    - minCost/maxCost: 최소비용(min_cost) 범위 필터
                    - blissStoneAvailable: 메모리얼 스톤 제공 여부
                    - page/size: 페이징(0부터 시작)
                    응답은 Page 형태이며, 각 아이템에 편의시설(amenities) 이름 리스트가 포함됩니다.
                    """
    )
    @GetMapping
    public BaseResponse<Page<AnimalFuneralSearchResDto>> search(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) RegionType region,
            @RequestParam(required = false) Integer minCost,
            @RequestParam(required = false) Integer maxCost,
            @RequestParam(required = false) Boolean blissStoneAvailable,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<AnimalFuneralSearchResDto> result =
                animalFuneralService.search(keyword, region, minCost, maxCost, blissStoneAvailable, page, size);

        return BaseResponse.onSuccess(SuccessStatus.FUNERAL_LIST_GET_SUCCESS, result);
    }

    // 장례식장 상세(공통 탭)
    // 장례식장 상세(공통 탭)
    @Operation(
            summary = "장례식장 상세(기본/공통 탭)",
            description = """
                    장례식장 상세 기본 정보를 조회합니다.
                    포함 내용:
                    - 기본 정보(id, name, address, phone, description, mainImageUrl)
                    - 리뷰 정보(reviewCount, ratingAvg)
                    - isOpenNow(현재 영업중 여부: 운영시간 기준으로 계산)
                    - amenities(편의시설 이름 리스트)
                    - costs(장례 비용/패키지 리스트)
                    - operatingHours(요일별 운영시간 리스트)
                    """
    )
    @GetMapping("/{id}")
    public BaseResponse<AnimalFuneralDetailResDto> getDetail(@PathVariable("id") Long id) {
        AnimalFuneralDetailResDto result = animalFuneralService.getDetail(id);
        return BaseResponse.onSuccess(SuccessStatus.FUNERAL_DETAIL_GET_SUCCESS, result);
    }

    // 장례 절차(서비스) 탭
    @Operation(
            summary = "장례 절차(서비스) 탭 조회",
            description = """
                    장례 절차 탭에 필요한 정보를 조회합니다.
                    현재 구현은 procedureImageUrl 기반(설명 이미지) 응답을 반환합니다.
                    """
    )
    @GetMapping("/{id}/procedures")
    public BaseResponse<AnimalFuneralProcedureResDto> getProcedures(@PathVariable("id") Long id) {
        AnimalFuneralProcedureResDto result = animalFuneralService.getProcedures(id);
        return BaseResponse.onSuccess(SuccessStatus.FUNERAL_PROCEDURE_GET_SUCCESS, result);
    }

    // 메모리얼 스톤 탭
    @Operation(
            summary = "메모리얼 스톤(블리스 스톤) 탭 조회",
            description = """
                    메모리얼 스톤(블리스 스톤) 탭에 필요한 정보를 조회합니다.
                    현재 구현은 blissStoneImageUrl 기반(설명 이미지) 응답을 반환합니다.
                    """
    )
    @GetMapping("/{id}/bliss-stone")
    public BaseResponse<AnimalFuneralBlissStoneResDto> getBlissStone(@PathVariable("id") Long id) {
        AnimalFuneralBlissStoneResDto result = animalFuneralService.getBlissStone(id);
        return BaseResponse.onSuccess(SuccessStatus.FUNERAL_BLISS_STONE_GET_SUCCESS, result);
    }
}

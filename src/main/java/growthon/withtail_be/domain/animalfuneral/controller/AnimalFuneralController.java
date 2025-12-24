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

@RestController
@RequiredArgsConstructor
@RequestMapping("/funerals")
public class AnimalFuneralController {

    private final AnimalFuneralService animalFuneralService;

    // 장례식장 검색(목록)
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
    @GetMapping("/{id}")
    public BaseResponse<AnimalFuneralDetailResDto> getDetail(@PathVariable("id") Long id) {
        AnimalFuneralDetailResDto result = animalFuneralService.getDetail(id);
        return BaseResponse.onSuccess(SuccessStatus.FUNERAL_DETAIL_GET_SUCCESS, result);
    }

    // 장례 절차(서비스) 탭
    @GetMapping("/{id}/procedures")
    public BaseResponse<AnimalFuneralProcedureResDto> getProcedures(@PathVariable("id") Long id) {
        AnimalFuneralProcedureResDto result = animalFuneralService.getProcedures(id);
        return BaseResponse.onSuccess(SuccessStatus.FUNERAL_PROCEDURE_GET_SUCCESS, result);
    }

    // 메모리얼 스톤 탭
    @GetMapping("/{id}/bliss-stone")
    public BaseResponse<AnimalFuneralBlissStoneResDto> getBlissStone(@PathVariable("id") Long id) {
        AnimalFuneralBlissStoneResDto result = animalFuneralService.getBlissStone(id);
        return BaseResponse.onSuccess(SuccessStatus.FUNERAL_BLISS_STONE_GET_SUCCESS, result);
    }
}

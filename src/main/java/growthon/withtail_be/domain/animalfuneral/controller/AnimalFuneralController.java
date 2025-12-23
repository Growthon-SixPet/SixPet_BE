package growthon.withtail_be.domain.animalfuneral.controller;

import growthon.withtail_be.domain.animalfuneral.dto.AnimalFuneralBasicResponse;
import growthon.withtail_be.domain.animalfuneral.dto.AnimalFuneralBrandResponse;
import growthon.withtail_be.domain.animalfuneral.dto.AnimalFuneralCostsResponse;
import growthon.withtail_be.domain.animalfuneral.dto.AnimalFuneralProceduresResponse;
import growthon.withtail_be.domain.animalfuneral.dto.AnimalFuneralSearchResponse;
import growthon.withtail_be.domain.animalfuneral.service.AnimalFuneralService;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/animal-funerals")
public class AnimalFuneralController {

    private final AnimalFuneralService animalFuneralService;

    public AnimalFuneralController(AnimalFuneralService animalFuneralService) {
        this.animalFuneralService = animalFuneralService;
    }

    // 검색(필터) - 하나의 엔드포인트로 리스트 한번에
    @GetMapping
    public Page<AnimalFuneralSearchResponse> search(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String sido,
            @RequestParam(required = false) String sigungu,
            @RequestParam(required = false) Integer minCost,
            @RequestParam(required = false) Integer maxCost,
            @RequestParam(required = false) Boolean blissStoneAvailable,
            @RequestParam(required = false) List<Long> amenityIds,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return animalFuneralService.search(
                keyword,
                sido,
                sigungu,
                minCost,
                maxCost,
                blissStoneAvailable,
                amenityIds,
                page,
                size
        );
    }

    // 상세(기본)
    @GetMapping("/{id}")
    public AnimalFuneralBasicResponse basic(@PathVariable Long id) {
        return animalFuneralService.getBasic(id);
    }

    // 상세(브랜드 소개)
    @GetMapping("/{id}/brand")
    public AnimalFuneralBrandResponse brand(@PathVariable Long id) {
        return animalFuneralService.getBrand(id);
    }

    // 상세(장례 절차)
    @GetMapping("/{id}/procedures")
    public AnimalFuneralProceduresResponse procedures(@PathVariable Long id) {
        return animalFuneralService.getProcedures(id);
    }

    // 상세(장례 비용)
    @GetMapping("/{id}/costs")
    public AnimalFuneralCostsResponse costs(@PathVariable Long id) {
        return animalFuneralService.getCosts(id);
    }
}

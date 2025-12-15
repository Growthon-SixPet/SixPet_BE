package growthon.withtail_be.domain.funeral.controller;

import growthon.withtail_be.domain.funeral.dto.FuneralSearchResponse;
import growthon.withtail_be.domain.funeral.service.FuneralService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/funerals")
public class FuneralController {

    private final FuneralService funeralService;

    @GetMapping
    public Page<FuneralSearchResponse> search(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String sido,
            @RequestParam(required = false) String sigungu,
            @PageableDefault(size = 10) Pageable pageable
    ) {
        return funeralService.search(keyword, sido, sigungu, pageable);
    }
}

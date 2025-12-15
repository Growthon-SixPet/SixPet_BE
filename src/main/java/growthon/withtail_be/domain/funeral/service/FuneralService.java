package growthon.withtail_be.domain.funeral.service;

import growthon.withtail_be.domain.funeral.dto.FuneralSearchResponse;
import growthon.withtail_be.domain.funeral.repository.FuneralRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FuneralService {

    private final FuneralRepository funeralRepository;

    public Page<FuneralSearchResponse> search(String keyword, String sido, String sigungu, Pageable pageable) {
        return funeralRepository.search(keyword, sido, sigungu, pageable)
                .map(FuneralSearchResponse::from);
    }
}

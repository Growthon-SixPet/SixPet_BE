package growthon.withtail_be.domain.hospital.service;

import growthon.withtail_be.domain.hospital.dto.HospitalSearchResponse;
import growthon.withtail_be.domain.hospital.repository.HospitalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

//검색 로직(조회 + DTO 변환)
@Service
@RequiredArgsConstructor
public class HospitalService {

    private final HospitalRepository hospitalRepository;

    public Page<HospitalSearchResponse> search(String keyword, String sido, String sigungu, Pageable pageable) {
        return hospitalRepository.search(keyword,sido,sigungu,pageable)
                .map(HospitalSearchResponse::from);
    }
}

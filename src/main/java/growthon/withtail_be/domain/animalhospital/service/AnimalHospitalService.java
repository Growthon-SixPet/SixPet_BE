package growthon.withtail_be.domain.animalhospital.service;

import growthon.withtail_be.domain.animalhospital.dto.AnimalHospitalAboutResponse;
import growthon.withtail_be.domain.animalhospital.dto.AnimalHospitalDetailResponse;
import growthon.withtail_be.domain.animalhospital.dto.AnimalHospitalSearchResponse;
import growthon.withtail_be.domain.animalhospital.dto.AnimalHospitalStaffResponse;
import growthon.withtail_be.domain.animalhospital.repository.AnimalHospitalRepository;
import growthon.withtail_be.domain.animalhospital.repository.AnimalHospitalSpecifications;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AnimalHospitalService {

    private final AnimalHospitalRepository animalHospitalRepository;

    @Transactional(readOnly = true)
    public Page<AnimalHospitalSearchResponse> search(
            String keyword,
            String sido,
            String sigungu,
            Boolean open24h,
            Boolean emergencyAvailable,
            List<Long> specialtyIds,
            List<Long> animalTypeIds,
            List<Long> amenityIds,
            List<Long> paymentMethodIds,
            String sort,
            Pageable pageable
    ) {
        Specification<growthon.withtail_be.domain.animalhospital.entity.AnimalHospital> spec =
                Specification.where(AnimalHospitalSpecifications.keywordLike(keyword))
                        .and(AnimalHospitalSpecifications.sidoEq(sido))
                        .and(AnimalHospitalSpecifications.sigunguEq(sigungu))
                        .and(AnimalHospitalSpecifications.open24h(open24h))
                        .and(AnimalHospitalSpecifications.emergencyAvailable(emergencyAvailable))
                        .and(AnimalHospitalSpecifications.hasSpecialtyIds(specialtyIds))
                        .and(AnimalHospitalSpecifications.hasAnimalTypeIds(animalTypeIds))
                        .and(AnimalHospitalSpecifications.hasAmenityIds(amenityIds))
                        .and(AnimalHospitalSpecifications.hasPaymentMethodIds(paymentMethodIds));

        Pageable sortedPageable = applySort(pageable, sort);

        return animalHospitalRepository.findAll(spec, sortedPageable)
                .map(AnimalHospitalSearchResponse::from);
    }

    @Transactional(readOnly = true)
    public AnimalHospitalDetailResponse getDetail(Long id) {
        var hospital = animalHospitalRepository.findWithDetailById(id)
                .orElseThrow(() -> new IllegalArgumentException("병원을 찾을 수 없습니다. id=" + id));
        return AnimalHospitalDetailResponse.from(hospital);
    }

    @Transactional(readOnly = true)
    public AnimalHospitalAboutResponse getAbout(Long id) {
        var hospital = animalHospitalRepository.findWithAboutById(id)
                .orElseThrow(() -> new IllegalArgumentException("병원을 찾을 수 없습니다. id=" + id));
        return AnimalHospitalAboutResponse.from(hospital);
    }

    @Transactional(readOnly = true)
    public List<AnimalHospitalStaffResponse> getStaff(Long id) {
        var hospital = animalHospitalRepository.findWithStaffById(id)
                .orElseThrow(() -> new IllegalArgumentException("병원을 찾을 수 없습니다. id=" + id));

        return hospital.getStaff().stream()
                .map(AnimalHospitalStaffResponse::from)
                .collect(Collectors.toList());
    }

    private Pageable applySort(Pageable pageable, String sort) {
        if (sort == null || sort.isBlank()) {
            return pageable;
        }

        Sort s;
        if ("RATING_DESC".equals(sort)) {
            s = Sort.by(Sort.Order.desc("rating"), Sort.Order.desc("reviewCount"));
        } else if ("REVIEW_COUNT_DESC".equals(sort)) {
            s = Sort.by(Sort.Order.desc("reviewCount"), Sort.Order.desc("rating"));
        } else {
            s = pageable.getSort();
        }

        return PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), s);
    }
}

package growthon.withtail_be.domain.animalhospital.service;

import growthon.withtail_be.domain.animalhospital.dto.AnimalHospitalAboutResponse;
import growthon.withtail_be.domain.animalhospital.dto.AnimalHospitalDetailResponse;
import growthon.withtail_be.domain.animalhospital.dto.AnimalHospitalSearchResponse;
import growthon.withtail_be.domain.animalhospital.dto.AnimalHospitalStaffResponse;
import growthon.withtail_be.domain.animalhospital.entity.AnimalHospital;
import growthon.withtail_be.domain.animalhospital.repository.AnimalHospitalRepository;
import growthon.withtail_be.domain.animalhospital.repository.AnimalHospitalSpecifications;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
            Boolean nightCare,
            Boolean emergencyAvailable,
            List<Long> specialtyIds,
            List<Long> animalTypeIds,
            List<Long> amenityIds,
            List<Long> paymentMethodIds,
            String sortType,
            int page,
            int size
    ) {
        Specification<AnimalHospital> spec = Specification
                .where(AnimalHospitalSpecifications.keywordLike(keyword))
                .and(AnimalHospitalSpecifications.sidoEq(sido))
                .and(AnimalHospitalSpecifications.sigunguEq(sigungu))
                .and(AnimalHospitalSpecifications.open24h(open24h))
                .and(AnimalHospitalSpecifications.nightCare(nightCare))
                .and(AnimalHospitalSpecifications.emergencyAvailable(emergencyAvailable))
                .and(AnimalHospitalSpecifications.hasSpecialtyIds(specialtyIds))
                .and(AnimalHospitalSpecifications.hasAnimalTypeIds(animalTypeIds))
                .and(AnimalHospitalSpecifications.hasAmenityIds(amenityIds))
                .and(AnimalHospitalSpecifications.hasPaymentMethodIds(paymentMethodIds));

        Sort sort = toSort(sortType);
        PageRequest pageable = PageRequest.of(page, size, sort);

        return animalHospitalRepository.findAll(spec, pageable)
                .map(AnimalHospitalSearchResponse::from);
    }

    private Sort toSort(String sortType) {
        if (sortType == null || sortType.isBlank()) {
            return Sort.unsorted();
        }
        if ("RATING_DESC".equalsIgnoreCase(sortType)) {
            return Sort.by(Sort.Order.desc("rating"), Sort.Order.desc("reviewCount"));
        }
        if ("REVIEW_COUNT_DESC".equalsIgnoreCase(sortType)) {
            return Sort.by(Sort.Order.desc("reviewCount"), Sort.Order.desc("rating"));
        }
        return Sort.unsorted();
    }

    @Transactional(readOnly = true)
    public AnimalHospitalDetailResponse getDetail(Long id) {
        // 1) 기본 엔티티 로딩
        AnimalHospital hospital = animalHospitalRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("병원을 찾을 수 없습니다. id=" + id));

        // 2) 상세(기본)에 필요한 컬렉션들을 "각각" fetch로딩 (같은 트랜잭션/영속성 컨텍스트라 병합됨)
        animalHospitalRepository.findWithOperatingHoursById(id).orElseThrow();
        animalHospitalRepository.findWithAmenitiesById(id).orElseThrow();
        animalHospitalRepository.findWithSpecialtiesById(id).orElseThrow();
        animalHospitalRepository.findWithAnimalTypesById(id).orElseThrow();
        animalHospitalRepository.findWithImagesById(id).orElseThrow();

        // 3) DTO 변환
        return AnimalHospitalDetailResponse.from(hospital);
    }

    @Transactional(readOnly = true)
    public AnimalHospitalAboutResponse getAbout(Long id) {
        AnimalHospital hospital = animalHospitalRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("병원을 찾을 수 없습니다. id=" + id));

        // 병원소개 탭에서 필요한 것만 로딩
        animalHospitalRepository.findWithAmenitiesById(id).orElseThrow();
        animalHospitalRepository.findWithPaymentMethodsById(id).orElseThrow();

        return AnimalHospitalAboutResponse.from(hospital);
    }

    @Transactional(readOnly = true)
    public List<AnimalHospitalStaffResponse> getStaff(Long id) {
        AnimalHospital hospital = animalHospitalRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("병원을 찾을 수 없습니다. id=" + id));

        animalHospitalRepository.findWithStaffById(id).orElseThrow();

        return hospital.getStaff().stream()
                .map(AnimalHospitalStaffResponse::from)
                .collect(Collectors.toList());
    }
}

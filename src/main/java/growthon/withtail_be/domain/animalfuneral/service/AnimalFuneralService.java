package growthon.withtail_be.domain.animalfuneral.service;

import growthon.withtail_be.domain.animalfuneral.dto.AnimalFuneralBasicResponse;
import growthon.withtail_be.domain.animalfuneral.dto.AnimalFuneralBrandResponse;
import growthon.withtail_be.domain.animalfuneral.dto.AnimalFuneralCostsResponse;
import growthon.withtail_be.domain.animalfuneral.dto.AnimalFuneralProceduresResponse;
import growthon.withtail_be.domain.animalfuneral.dto.AnimalFuneralSearchResponse;
import growthon.withtail_be.domain.animalfuneral.dto.OperatingHourDto;
import growthon.withtail_be.domain.animalfuneral.entity.AnimalFuneral;
import growthon.withtail_be.domain.animalfuneral.entity.FuneralAmenity;
import growthon.withtail_be.domain.animalfuneral.entity.FuneralOperatingHours;
import growthon.withtail_be.domain.animalfuneral.repository.AnimalFuneralRepository;
import growthon.withtail_be.domain.animalfuneral.repository.AnimalFuneralSpecifications;
import growthon.withtail_be.domain.animalfuneral.repository.FuneralAmenityRepository;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AnimalFuneralService {

    private final AnimalFuneralRepository animalFuneralRepository;
    private final FuneralAmenityRepository funeralAmenityRepository;

    public AnimalFuneralService(AnimalFuneralRepository animalFuneralRepository, FuneralAmenityRepository funeralAmenityRepository) {
        this.animalFuneralRepository = animalFuneralRepository;
        this.funeralAmenityRepository = funeralAmenityRepository;
    }

    @Transactional(readOnly = true)
    public Page<AnimalFuneralSearchResponse> search(
            String keyword,
            String sido,
            String sigungu,
            Integer minCost,
            Integer maxCost,
            Boolean blissStoneAvailable,
            List<Long> amenityIds,
            int page,
            int size
    ) {
        Specification<AnimalFuneral> spec = Specification
                .where(AnimalFuneralSpecifications.keywordLike(keyword))
                .and(AnimalFuneralSpecifications.sidoEq(sido))
                .and(AnimalFuneralSpecifications.sigunguEq(sigungu))
                .and(AnimalFuneralSpecifications.costBetween(minCost, maxCost))
                .and(AnimalFuneralSpecifications.blissStoneAvailable(blissStoneAvailable))
                .and(AnimalFuneralSpecifications.hasAmenityIds(amenityIds));

        PageRequest pageable = PageRequest.of(page, size);

        return animalFuneralRepository.findAll(spec, pageable)
                .map(AnimalFuneralSearchResponse::from);
    }

    // 기본 상세: operatingHours는 entityGraph로, amenities는 별도 repo로 로딩 (MultipleBagFetch 해결)
    @Transactional(readOnly = true)
    public AnimalFuneralBasicResponse getBasic(Long id) {
        AnimalFuneral funeral = animalFuneralRepository.findBasicById(id)
                .orElseThrow(() -> new IllegalArgumentException("장례식장을 찾을 수 없습니다. id=" + id));

        List<OperatingHourDto> hourDtos = new ArrayList<>();
        for (FuneralOperatingHours oh : funeral.getOperatingHours()) {
            hourDtos.add(OperatingHourDto.from(oh));
        }

        List<FuneralAmenity> links = funeralAmenityRepository.findByFuneralId(id);
        List<String> amenityNames = new ArrayList<>();
        for (FuneralAmenity link : links) {
            if (link.getAmenity() != null) {
                amenityNames.add(link.getAmenity().getName());
            }
        }

        return new AnimalFuneralBasicResponse(
                funeral.getId(),
                funeral.getName(),
                funeral.getDescription(),
                funeral.getShortAddress(),
                funeral.getPhone(),
                funeral.getReservationUrl(),
                funeral.getMainImageUrl(),
                funeral.isBlissStoneAvailable(),
                funeral.getReviewCount(),
                hourDtos,
                amenityNames
        );
    }

    @Transactional(readOnly = true)
    public AnimalFuneralBrandResponse getBrand(Long id) {
        AnimalFuneral funeral = animalFuneralRepository.findBrandById(id)
                .orElseThrow(() -> new IllegalArgumentException("장례식장을 찾을 수 없습니다. id=" + id));
        return AnimalFuneralBrandResponse.from(funeral);
    }

    @Transactional(readOnly = true)
    public AnimalFuneralProceduresResponse getProcedures(Long id) {
        AnimalFuneral funeral = animalFuneralRepository.findProceduresById(id)
                .orElseThrow(() -> new IllegalArgumentException("장례식장을 찾을 수 없습니다. id=" + id));
        return AnimalFuneralProceduresResponse.from(funeral);
    }

    @Transactional(readOnly = true)
    public AnimalFuneralCostsResponse getCosts(Long id) {
        AnimalFuneral funeral = animalFuneralRepository.findCostsById(id)
                .orElseThrow(() -> new IllegalArgumentException("장례식장을 찾을 수 없습니다. id=" + id));
        return AnimalFuneralCostsResponse.from(funeral);
    }
}

package growthon.withtail_be.domain.animalfuneral.service;

import growthon.withtail_be.domain.animalfuneral.dto.AnimalFuneralBlissStoneResDto;
import growthon.withtail_be.domain.animalfuneral.dto.AnimalFuneralDetailResDto;
import growthon.withtail_be.domain.animalfuneral.dto.AnimalFuneralProcedureResDto;
import growthon.withtail_be.domain.animalfuneral.dto.AnimalFuneralSearchResDto;
import growthon.withtail_be.domain.animalfuneral.dto.FuneralCostDto;
import growthon.withtail_be.domain.animalfuneral.dto.FuneralOperatingHourDto;
import growthon.withtail_be.domain.animalfuneral.entity.AnimalFuneral;
import growthon.withtail_be.domain.animalfuneral.entity.DayOfWeekType;
import growthon.withtail_be.domain.animalfuneral.entity.FuneralAmenity;
import growthon.withtail_be.domain.animalfuneral.entity.FuneralOperatingHours;
import growthon.withtail_be.domain.animalfuneral.repository.AnimalFuneralRepository;
import growthon.withtail_be.domain.animalfuneral.repository.AnimalFuneralSpecifications;
import growthon.withtail_be.domain.animalfuneral.repository.FuneralAmenityRepository;
import growthon.withtail_be.domain.animalfuneral.repository.FuneralCostRepository;
import growthon.withtail_be.domain.model.RegionType;
import growthon.withtail_be.global.code.ErrorStatus;
import growthon.withtail_be.global.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AnimalFuneralService {

    private final AnimalFuneralRepository animalFuneralRepository;
    private final FuneralAmenityRepository funeralAmenityRepository;
    private final FuneralCostRepository funeralCostRepository;

    // 장례식장 검색(목록)
    public Page<AnimalFuneralSearchResDto> search(
            String keyword,
            RegionType region,
            Integer minCost,
            Integer maxCost,
            Boolean blissStoneAvailable,
            int page,
            int size
    ) {
        Specification<AnimalFuneral> spec = Specification
                .where(AnimalFuneralSpecifications.keywordLike(keyword))
                .and(AnimalFuneralSpecifications.regionEq(region))
                .and(AnimalFuneralSpecifications.minCostBetween(minCost, maxCost))
                .and(AnimalFuneralSpecifications.blissStoneAvailable(blissStoneAvailable));

        Pageable pageable = PageRequest.of(page, size);
        Page<AnimalFuneral> funeralPage = animalFuneralRepository.findAll(spec, pageable);

        if (funeralPage.isEmpty()) {
            return new PageImpl<>(Collections.emptyList(), pageable, 0);
        }

        List<Long> funeralIds = funeralPage.getContent().stream()
                .map(AnimalFuneral::getId)
                .toList();

        List<FuneralAmenity> links = funeralAmenityRepository.findByFuneralIdIn(funeralIds);

        Map<Long, List<String>> amenityNamesByFuneralId = links.stream()
                .collect(Collectors.groupingBy(
                        fa -> fa.getFuneral().getId(),
                        Collectors.mapping(fa -> fa.getAmenity().getName(), Collectors.toList())
                ));

        List<AnimalFuneralSearchResDto> dtoList = funeralPage.getContent().stream()
                .map(funeral -> {
                    List<String> amenityNames = amenityNamesByFuneralId
                            .getOrDefault(funeral.getId(), Collections.emptyList());
                    return AnimalFuneralSearchResDto.from(funeral, amenityNames);
                })
                .toList();

        return new PageImpl<>(dtoList, pageable, funeralPage.getTotalElements());
    }

    // 장례식장 상세(공통 탭)
    public AnimalFuneralDetailResDto getDetail(Long funeralId) {
        AnimalFuneral funeral = animalFuneralRepository.findDetailById(funeralId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.FUNERAL_NOT_FOUND));

        List<FuneralCostDto> costs = funeralCostRepository.findByFuneralId(funeralId).stream()
                .map(FuneralCostDto::from)
                .toList();

        List<FuneralOperatingHourDto> operatingHours = funeral.getOperatingHours().stream()
                .map(FuneralOperatingHourDto::from)
                .toList();

        List<String> amenityNames = funeralAmenityRepository.findByFuneralId(funeralId).stream()
                .map(link -> link.getAmenity().getName())
                .toList();

        boolean isOpenNow = calculateIsOpenNow(funeral.getOperatingHours());

        return AnimalFuneralDetailResDto.of(funeral, isOpenNow, amenityNames, costs, operatingHours);
    }

    // 장례 절차 조회
    public AnimalFuneralProcedureResDto getProcedures(Long funeralId) {
        AnimalFuneral funeral = animalFuneralRepository.findById(funeralId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.FUNERAL_NOT_FOUND));

        return AnimalFuneralProcedureResDto.from(funeral);
    }

    // 메모리얼 스톤 설명 조회
    public AnimalFuneralBlissStoneResDto getBlissStone(Long funeralId) {
        AnimalFuneral funeral = animalFuneralRepository.findById(funeralId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.FUNERAL_NOT_FOUND));

        return AnimalFuneralBlissStoneResDto.from(funeral);
    }


    // helpers

    // 현재 운영 중인지
    public boolean calculateIsOpenNow(List<FuneralOperatingHours> operatingHours) {
        if (operatingHours == null || operatingHours.isEmpty()) {
            return false;
        }

        DayOfWeekType today = toDayOfWeekType(java.time.LocalDate.now().getDayOfWeek());
        LocalTime now = LocalTime.now();

        for (FuneralOperatingHours oh : operatingHours) {
            if (oh.getDayOfWeek() != today) continue;
            if (oh.isClosed()) return false;

            // open/close 파싱 실패하면 안전하게 false
            try {
                LocalTime open = LocalTime.parse(oh.getOpenTime());
                LocalTime close = LocalTime.parse(oh.getCloseTime());

                // 일반 케이스 (open <= close)
                if (!close.isBefore(open)) {
                    return !now.isBefore(open) && !now.isAfter(close);
                }

                // 자정 넘어가는 케이스 (예: 20:00 ~ 02:00)
                // now가 open 이후거나 close 이전이면 영업중
                return !now.isBefore(open) || !now.isAfter(close);

            } catch (Exception e) {
                return false;
            }
        }

        return false;
    }

    private DayOfWeekType toDayOfWeekType(java.time.DayOfWeek dayOfWeek) {
        return switch (dayOfWeek) {
            case MONDAY -> DayOfWeekType.MON;
            case TUESDAY -> DayOfWeekType.TUE;
            case WEDNESDAY -> DayOfWeekType.WED;
            case THURSDAY -> DayOfWeekType.THU;
            case FRIDAY -> DayOfWeekType.FRI;
            case SATURDAY -> DayOfWeekType.SAT;
            case SUNDAY -> DayOfWeekType.SUN;
        };
    }

    public boolean calculateOpenNow(AnimalFuneral funeral) {
        return calculateIsOpenNow(funeral.getOperatingHours());
    }

}

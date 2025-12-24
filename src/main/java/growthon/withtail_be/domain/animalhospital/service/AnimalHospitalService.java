package growthon.withtail_be.domain.animalhospital.service;

import growthon.withtail_be.domain.animalhospital.dto.request.AnimalHospitalSearchReqDto;
import growthon.withtail_be.domain.animalhospital.dto.response.AnimalHospitalDetailResDto;
import growthon.withtail_be.domain.animalhospital.dto.response.AnimalHospitalSearchItemResDto;
import growthon.withtail_be.domain.animalhospital.dto.response.HospitalNewsResDto;
import growthon.withtail_be.domain.animalhospital.dto.response.HospitalOperatingHoursResDto;
import growthon.withtail_be.domain.animalhospital.dto.response.MedicalStaffResDto;
import growthon.withtail_be.domain.animalhospital.entity.AnimalHospital;
import growthon.withtail_be.domain.animalhospital.entity.DayOfWeekType;
import growthon.withtail_be.domain.animalhospital.entity.HospitalOperatingHours;
import growthon.withtail_be.domain.model.RegionType;
import growthon.withtail_be.domain.animalhospital.repository.AnimalHospitalRepository;
import growthon.withtail_be.global.code.ErrorStatus;
import growthon.withtail_be.global.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AnimalHospitalService {

    private final AnimalHospitalRepository animalHospitalRepository;

    // 병원 검색 (필터 미포함)
    public Page<AnimalHospitalSearchItemResDto> searchBasic(
            String keyword,
            RegionType region,
            int page,
            int size
    ) {
        Pageable pageable = PageRequest.of(page, size);

        String trimmedKeyword = (keyword == null) ? null : keyword.trim();
        boolean hasKeyword = trimmedKeyword != null && !trimmedKeyword.isBlank();
        boolean hasRegion = region != null;

        Page<AnimalHospital> result;
        // 지역 + 키워드 둘 다 있는 경우
        if (hasRegion && hasKeyword) {
            result = animalHospitalRepository
                    .findByRegionAndNameContainingIgnoreCase(region, trimmedKeyword, pageable);
        } else if (hasRegion) { // 지역만
            result = animalHospitalRepository
                    .findByRegion(region, pageable);
        } else if (hasKeyword) { // 키워드만
            result = animalHospitalRepository
                    .findByNameContainingIgnoreCase(trimmedKeyword, pageable);
        } else { // 둘 다 없는 경우(전체)
            result = animalHospitalRepository
                    .findAll(pageable);
        }

        return result.map(h -> AnimalHospitalSearchItemResDto.from(h, isOpenNow(h)));
    }

    // 병원 검색 (필터 포함 + 정렬)
    public Page<AnimalHospitalSearchItemResDto> searchWithFilters(
            String keyword,
            RegionType region,
            AnimalHospitalSearchReqDto req
    ) {
        Sort sort = resolveSort(req.sortType());
        Pageable pageable = PageRequest.of(req.pageOrDefault(), req.sizeOrDefault(), sort);

        String trimmedKeyword = normalize(keyword);
        String specialtyName = normalize(req.specialty());
        String animalTypeName = normalize(req.animalType());

        Page<AnimalHospital> result = animalHospitalRepository.searchWithFilters(
                region,
                trimmedKeyword,
                req.open24h(),
                req.nightCare(),
                specialtyName,
                animalTypeName,
                pageable
        );

        return result.map(h -> AnimalHospitalSearchItemResDto.from(h, isOpenNow(h)));
    }

    // 병원 상세 (상단 공통)
    public AnimalHospitalDetailResDto getHospitalDetailTop(Long hospitalId) {
        AnimalHospital hospital = getHospitalOrThrow(hospitalId);

        boolean openNow = isOpenNow(hospital);

        return AnimalHospitalDetailResDto.from(hospital, openNow);
    }

    // 병원 진료시간 목록 조회
    public List<HospitalOperatingHoursResDto> getOperatingHours(Long hospitalId) {
        AnimalHospital hospital = getHospitalOrThrow(hospitalId);

        return hospital.getOperatingHours().stream()
                .map(HospitalOperatingHoursResDto::from)
                .toList();
    }

    // 병원 의료진 목록 조회
    public List<MedicalStaffResDto> getMedicalStaffList(Long hospitalId) {
        AnimalHospital hospital = animalHospitalRepository.findStaffDetailById(hospitalId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.ANIMAL_HOSPITAL_NOT_FOUND));

        return hospital.getStaffList().stream()
                .map(MedicalStaffResDto::from)
                .toList();
    }

    // 병원 소식 목록 조회
    public List<HospitalNewsResDto> getHospitalNewsList(Long hospitalId) {
        AnimalHospital hospital = animalHospitalRepository.findNewsDetailById(hospitalId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.ANIMAL_HOSPITAL_NOT_FOUND));

        return hospital.getNewsList().stream()
                .map(HospitalNewsResDto::from)
                .toList();
    }


    // helpers

    private List<String> nullIfEmpty(List<String> list) {
        return (list == null || list.isEmpty()) ? null : list;
    }

    // 정렬 기준
    private Sort resolveSort(String sortType) {
        if (sortType == null || sortType.isBlank()) {
            return Sort.by(Sort.Direction.DESC, "id");
        }

        return switch (sortType.toUpperCase(Locale.ROOT)) {
            case "RATING" -> Sort.by(Sort.Direction.DESC, "ratingAvg")
                    .and(Sort.by(Sort.Direction.DESC, "reviewCount"))
                    .and(Sort.by(Sort.Direction.DESC, "id"));
            case "REVIEW" -> Sort.by(Sort.Direction.DESC, "reviewCount")
                    .and(Sort.by(Sort.Direction.DESC, "ratingAvg"))
                    .and(Sort.by(Sort.Direction.DESC, "id"));
            case "LATEST" -> Sort.by(Sort.Direction.DESC, "id");
            default -> Sort.by(Sort.Direction.DESC, "id");
        };
    }

    // 지금 영업중인지 계산
    private boolean isOpenNow(AnimalHospital hospital) {
        if (hospital.isOpen24h()) return true;

        DayOfWeekType today = convertToDayOfWeekType(LocalDate.now().getDayOfWeek().name());
        HospitalOperatingHours todayHours = hospital.getOperatingHours().stream()
                .filter(oh -> oh.getDayOfWeek() == today)
                .findFirst()
                .orElse(null);

        if (todayHours == null) return false;
        if (todayHours.isClosed()) return false;

        // open/close 값이 비어있으면 안전하게 false 처리
        if (todayHours.getOpenTime() == null || todayHours.getCloseTime() == null) return false;

        LocalTime now = LocalTime.now();
        LocalTime open = parseTime(todayHours.getOpenTime());
        LocalTime close = parseTime(todayHours.getCloseTime());
        if (open == null || close == null) return false;

        // 일반 케이스: open <= now < close
        if (close.isAfter(open) || close.equals(open)) {
            return !now.isBefore(open) && now.isBefore(close);
        }

        // 자정 넘어가는 케이스(예: 22:00 ~ 02:00)
        return !now.isBefore(open) || now.isBefore(close);
    }

    private DayOfWeekType convertToDayOfWeekType(String javaDayOfWeekName) {
        // java.time.DayOfWeek: MONDAY~SUNDAY
        return switch (javaDayOfWeekName) {
            case "MONDAY" -> DayOfWeekType.MON;
            case "TUESDAY" -> DayOfWeekType.TUE;
            case "WEDNESDAY" -> DayOfWeekType.WED;
            case "THURSDAY" -> DayOfWeekType.THU;
            case "FRIDAY" -> DayOfWeekType.FRI;
            case "SATURDAY" -> DayOfWeekType.SAT;
            case "SUNDAY" -> DayOfWeekType.SUN;
            default -> DayOfWeekType.HOLIDAY; // 안전 fallback
        };
    }

    private LocalTime parseTime(String time) {
        try {
            return LocalTime.parse(time, DateTimeFormatter.ofPattern("H:mm"));
        } catch (Exception e) {
            return null;
        }
    }

    private String normalize(String s) {
        if (s == null) return null;
        String t = s.trim();
        return t.isBlank() ? null : t;
    }

    private AnimalHospital getHospitalOrThrow(Long hospitalId) {
        return animalHospitalRepository.findDetailById(hospitalId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.ANIMAL_HOSPITAL_NOT_FOUND));
    }


}

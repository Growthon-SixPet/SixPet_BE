package growthon.withtail_be.domain.animalhospital.dto;

import growthon.withtail_be.domain.animalhospital.entity.AnimalHospital;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;

@Getter
public class AnimalHospitalSearchResponse {

    private final Long id;
    private final String name;
    private final String shortAddress;
    private final boolean open24h;
    private final boolean emergencyAvailable;
    private final Double rating;
    private final Integer reviewCount;
    private final String mainImageUrl;

    // 카드에 표시할 뱃지(전문분야 상위 4개)
    private final List<String> badges;

    private AnimalHospitalSearchResponse(
            Long id,
            String name,
            String shortAddress,
            boolean open24h,
            boolean emergencyAvailable,
            Double rating,
            Integer reviewCount,
            String mainImageUrl,
            List<String> badges
    ) {
        this.id = id;
        this.name = name;
        this.shortAddress = shortAddress;
        this.open24h = open24h;
        this.emergencyAvailable = emergencyAvailable;
        this.rating = rating;
        this.reviewCount = reviewCount;
        this.mainImageUrl = mainImageUrl;
        this.badges = badges;
    }

    public static AnimalHospitalSearchResponse from(AnimalHospital h) {
        List<String> badges = h.getHospitalSpecialties().stream()
                .map(j -> j.getSpecialty().getName())
                .distinct()
                .limit(4)
                .collect(Collectors.toList());

        return new AnimalHospitalSearchResponse(
                h.getId(),
                h.getName(),
                h.getShortAddress(),
                h.isOpen24h(),
                h.isEmergencyAvailable(),
                h.getRating(),
                h.getReviewCount(),
                h.getMainImageUrl(),
                badges
        );
    }
}

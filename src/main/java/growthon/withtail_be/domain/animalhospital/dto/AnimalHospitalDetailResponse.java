package growthon.withtail_be.domain.animalhospital.dto;

import growthon.withtail_be.domain.animalhospital.entity.AnimalHospital;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;

@Getter
public class AnimalHospitalDetailResponse {

    private final Long id;
    private final String name;
    private final String shortAddress;
    private final String roadAddress;
    private final String phone;

    private final boolean open24h;
    private final boolean emergencyAvailable;

    private final Double rating;
    private final Integer reviewCount;

    private final String mainImageUrl;
    private final List<String> imageUrls;
    private final List<OperatingHourItem> operatingHours;
    private final List<String> badges;

    private AnimalHospitalDetailResponse(
            Long id,
            String name,
            String shortAddress,
            String roadAddress,
            String phone,
            boolean open24h,
            boolean emergencyAvailable,
            Double rating,
            Integer reviewCount,
            String mainImageUrl,
            List<String> imageUrls,
            List<OperatingHourItem> operatingHours,
            List<String> badges
    ) {
        this.id = id;
        this.name = name;
        this.shortAddress = shortAddress;
        this.roadAddress = roadAddress;
        this.phone = phone;
        this.open24h = open24h;
        this.emergencyAvailable = emergencyAvailable;
        this.rating = rating;
        this.reviewCount = reviewCount;
        this.mainImageUrl = mainImageUrl;
        this.imageUrls = imageUrls;
        this.operatingHours = operatingHours;
        this.badges = badges;
    }

    public static AnimalHospitalDetailResponse from(AnimalHospital h) {
        List<String> imageUrls = h.getImages().stream()
                .map(i -> i.getImageUrl())
                .collect(Collectors.toList());

        List<OperatingHourItem> operatingHours = h.getOperatingHours().stream()
                .map(OperatingHourItem::from)
                .collect(Collectors.toList());

        List<String> badges = h.getHospitalSpecialties().stream()
                .map(j -> j.getSpecialty().getName())
                .distinct()
                .limit(6)
                .collect(Collectors.toList());

        return new AnimalHospitalDetailResponse(
                h.getId(),
                h.getName(),
                h.getShortAddress(),
                h.getRoadAddress(),
                h.getPhone(),
                h.isOpen24h(),
                h.isEmergencyAvailable(),
                h.getRating(),
                h.getReviewCount(),
                h.getMainImageUrl(),
                imageUrls,
                operatingHours,
                badges
        );
    }

    @Getter
    public static class OperatingHourItem {
        private final String dayOfWeek;
        private final String openTime;
        private final String closeTime;
        private final boolean closed;

        private OperatingHourItem(String dayOfWeek, String openTime, String closeTime, boolean closed) {
            this.dayOfWeek = dayOfWeek;
            this.openTime = openTime;
            this.closeTime = closeTime;
            this.closed = closed;
        }

        public static OperatingHourItem from(growthon.withtail_be.domain.animalhospital.entity.HospitalOperatingHours oh) {
            String open = oh.getOpenTime() == null ? null : oh.getOpenTime().toString();
            String close = oh.getCloseTime() == null ? null : oh.getCloseTime().toString();
            return new OperatingHourItem(oh.getDayOfWeek().name(), open, close, oh.isClosed());
        }
    }
}

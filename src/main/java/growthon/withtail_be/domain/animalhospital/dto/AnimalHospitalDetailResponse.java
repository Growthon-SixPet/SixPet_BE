package growthon.withtail_be.domain.animalhospital.dto;

import growthon.withtail_be.domain.animalhospital.entity.AnimalHospital;
import growthon.withtail_be.domain.animalhospital.entity.HospitalOperatingHours;
import java.util.List;
import java.util.stream.Collectors;

public class AnimalHospitalDetailResponse {

    private final Long id;
    private final String name;
    private final double rating;
    private final int reviewCount;

    private final String shortAddress;
    private final String roadAddress;
    private final String jibunAddress;
    private final String detailAddress;

    private final String phone;

    private final boolean open24h;
    private final boolean nightCare;
    private final boolean emergencyAvailable;

    private final String mainImageUrl;

    private final List<OperatingHourItem> operatingHours;
    private final List<String> specialties;

    private AnimalHospitalDetailResponse(
            Long id,
            String name,
            double rating,
            int reviewCount,
            String shortAddress,
            String roadAddress,
            String jibunAddress,
            String detailAddress,
            String phone,
            boolean open24h,
            boolean nightCare,
            boolean emergencyAvailable,
            String mainImageUrl,
            List<OperatingHourItem> operatingHours,
            List<String> specialties
    ) {
        this.id = id;
        this.name = name;
        this.rating = rating;
        this.reviewCount = reviewCount;
        this.shortAddress = shortAddress;
        this.roadAddress = roadAddress;
        this.jibunAddress = jibunAddress;
        this.detailAddress = detailAddress;
        this.phone = phone;
        this.open24h = open24h;
        this.nightCare = nightCare;
        this.emergencyAvailable = emergencyAvailable;
        this.mainImageUrl = mainImageUrl;
        this.operatingHours = operatingHours;
        this.specialties = specialties;
    }

    public static AnimalHospitalDetailResponse from(AnimalHospital h) {
        List<OperatingHourItem> hours = h.getOperatingHours().stream()
                .map(OperatingHourItem::from)
                .collect(Collectors.toList());

        List<String> specialties = h.getSpecialties().stream()
                .map(j -> j.getSpecialty().getName())
                .collect(Collectors.toList());

        return new AnimalHospitalDetailResponse(
                h.getId(),
                h.getName(),
                h.getRating(),
                h.getReviewCount(),
                h.getShortAddress(),
                h.getRoadAddress(),
                h.getJibunAddress(),
                h.getDetailAddress(),
                h.getPhone(),
                h.isOpen24h(),
                h.isNightCare(),
                h.isEmergencyAvailable(),
                h.getMainImageUrl(),
                hours,
                specialties
        );
    }

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

        public static OperatingHourItem from(HospitalOperatingHours oh) {
            return new OperatingHourItem(
                    oh.getDayOfWeek().name(),
                    oh.getOpenTime(),
                    oh.getCloseTime(),
                    oh.isClosed()
            );
        }

        public String getDayOfWeek() { return dayOfWeek; }
        public String getOpenTime() { return openTime; }
        public String getCloseTime() { return closeTime; }
        public boolean isClosed() { return closed; }
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public double getRating() { return rating; }
    public int getReviewCount() { return reviewCount; }
    public String getShortAddress() { return shortAddress; }
    public String getRoadAddress() { return roadAddress; }
    public String getJibunAddress() { return jibunAddress; }
    public String getDetailAddress() { return detailAddress; }
    public String getPhone() { return phone; }
    public boolean isOpen24h() { return open24h; }
    public boolean isNightCare() { return nightCare; }
    public boolean isEmergencyAvailable() { return emergencyAvailable; }
    public String getMainImageUrl() { return mainImageUrl; }
    public List<OperatingHourItem> getOperatingHours() { return operatingHours; }
    public List<String> getSpecialties() { return specialties; }
}

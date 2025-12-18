package growthon.withtail_be.domain.animalhospital.dto;

import growthon.withtail_be.domain.animalhospital.entity.AnimalHospital;
import java.util.List;
import java.util.stream.Collectors;

public class AnimalHospitalSearchResponse {

    private final Long id;
    private final String name;
    private final String shortAddress;
    private final boolean open24h;
    private final boolean nightCare;
    private final boolean emergencyAvailable;
    private final double rating;
    private final int reviewCount;
    private final String mainImageUrl;
    private final String phone;
    private final List<String> services;

    private AnimalHospitalSearchResponse(
            Long id,
            String name,
            String shortAddress,
            boolean open24h,
            boolean nightCare,
            boolean emergencyAvailable,
            double rating,
            int reviewCount,
            String mainImageUrl,
            String phone,
            List<String> services
    ) {
        this.id = id;
        this.name = name;
        this.shortAddress = shortAddress;
        this.open24h = open24h;
        this.nightCare = nightCare;
        this.emergencyAvailable = emergencyAvailable;
        this.rating = rating;
        this.reviewCount = reviewCount;
        this.mainImageUrl = mainImageUrl;
        this.phone = phone;
        this.services = services;
    }

    public static AnimalHospitalSearchResponse from(AnimalHospital h) {
        List<String> services = h.getAmenities().stream()
                .map(j -> j.getAmenity().getName())
                .limit(10)
                .collect(Collectors.toList());

        return new AnimalHospitalSearchResponse(
                h.getId(),
                h.getName(),
                h.getShortAddress(),
                h.isOpen24h(),
                h.isNightCare(),
                h.isEmergencyAvailable(),
                h.getRating(),
                h.getReviewCount(),
                h.getMainImageUrl(),
                h.getPhone(),
                services
        );
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getShortAddress() { return shortAddress; }
    public boolean isOpen24h() { return open24h; }
    public boolean isNightCare() { return nightCare; }
    public boolean isEmergencyAvailable() { return emergencyAvailable; }
    public double getRating() { return rating; }
    public int getReviewCount() { return reviewCount; }
    public String getMainImageUrl() { return mainImageUrl; }
    public String getPhone() { return phone; }
    public List<String> getServices() { return services; }
}

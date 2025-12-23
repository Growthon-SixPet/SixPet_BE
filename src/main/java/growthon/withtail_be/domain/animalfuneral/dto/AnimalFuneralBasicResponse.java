package growthon.withtail_be.domain.animalfuneral.dto;

import java.util.List;

public class AnimalFuneralBasicResponse {

    private Long id;
    private String name;
    private String description;
    private String shortAddress;
    private String phone;
    private String reservationUrl;
    private String mainImageUrl;

    private boolean blissStoneAvailable;
    private int reviewCount;

    private List<OperatingHourDto> operatingHours;
    private List<String> amenities; // 서비스명 리스트

    public AnimalFuneralBasicResponse(
            Long id,
            String name,
            String description,
            String shortAddress,
            String phone,
            String reservationUrl,
            String mainImageUrl,
            boolean blissStoneAvailable,
            int reviewCount,
            List<OperatingHourDto> operatingHours,
            List<String> amenities
    ) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.shortAddress = shortAddress;
        this.phone = phone;
        this.reservationUrl = reservationUrl;
        this.mainImageUrl = mainImageUrl;
        this.blissStoneAvailable = blissStoneAvailable;
        this.reviewCount = reviewCount;
        this.operatingHours = operatingHours;
        this.amenities = amenities;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public String getShortAddress() { return shortAddress; }
    public String getPhone() { return phone; }
    public String getReservationUrl() { return reservationUrl; }
    public String getMainImageUrl() { return mainImageUrl; }
    public boolean isBlissStoneAvailable() { return blissStoneAvailable; }
    public int getReviewCount() { return reviewCount; }
    public List<OperatingHourDto> getOperatingHours() { return operatingHours; }
    public List<String> getAmenities() { return amenities; }
}

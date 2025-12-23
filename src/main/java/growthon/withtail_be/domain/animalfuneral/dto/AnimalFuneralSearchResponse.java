package growthon.withtail_be.domain.animalfuneral.dto;

import growthon.withtail_be.domain.animalfuneral.entity.AnimalFuneral;

public class AnimalFuneralSearchResponse {

    private Long id;
    private String name;
    private int reviewCount;
    private String shortAddress;
    private String phone;
    private String mainImageUrl;

    public static AnimalFuneralSearchResponse from(AnimalFuneral funeral) {
        AnimalFuneralSearchResponse dto = new AnimalFuneralSearchResponse();
        dto.id = funeral.getId();
        dto.name = funeral.getName();
        dto.reviewCount = funeral.getReviewCount();
        dto.shortAddress = funeral.getShortAddress();
        dto.phone = funeral.getPhone();
        dto.mainImageUrl = funeral.getMainImageUrl(); // 테스트: null 가능
        return dto;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public int getReviewCount() { return reviewCount; }
    public String getShortAddress() { return shortAddress; }
    public String getPhone() { return phone; }
    public String getMainImageUrl() { return mainImageUrl; }
}

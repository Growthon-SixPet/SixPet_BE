package growthon.withtail_be.domain.funeral.dto;

import growthon.withtail_be.domain.funeral.entity.Funeral;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FuneralSearchResponse {

    private Long id;
    private String name;
    private String address;
    private String phone;

    private Double rating;
    private Integer reviewCount;

    private Integer minPrice;
    private Integer maxPrice;

    public static FuneralSearchResponse from(Funeral funeral) {
        return FuneralSearchResponse.builder()
                .id(funeral.getId())
                .name(funeral.getName())
                .address(funeral.getAddress())
                .phone(funeral.getPhone())
                .rating(funeral.getRating())
                .reviewCount(funeral.getReviewCount())
                .minPrice(funeral.getMinPrice())
                .maxPrice(funeral.getMaxPrice())
                .build();
    }
}

package growthon.withtail_be.domain.animalfuneral.dto;

import growthon.withtail_be.domain.animalfuneral.entity.AnimalFuneral;

import java.util.List;

public record AnimalFuneralDetailResDto(
        Long id,
        String name,
        String address,
        String phone,
        String description,
        String mainImageUrl,

        boolean blissStoneAvailable,
        int reviewCount,
        double ratingAvg,

        boolean isOpenNow,
        List<String> amenities,
        List<FuneralCostDto> costs,
        List<FuneralOperatingHourDto> operatingHours
) {
    public static AnimalFuneralDetailResDto of(
            AnimalFuneral funeral,
            boolean isOpenNow,
            List<String> amenityNames,
            List<FuneralCostDto> costs,
            List<FuneralOperatingHourDto> operatingHours
    ) {
        return new AnimalFuneralDetailResDto(
                funeral.getId(),
                funeral.getName(),
                funeral.getAddress(),
                funeral.getPhone(),
                funeral.getDescription(),
                funeral.getMainImageUrl(),
                funeral.isBlissStoneAvailable(),
                funeral.getReviewCount(),
                funeral.getRatingAvg(),
                isOpenNow,
                amenityNames,
                costs,
                operatingHours
        );
    }
}

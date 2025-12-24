package growthon.withtail_be.domain.animalfuneral.dto;

import growthon.withtail_be.domain.animalfuneral.entity.AnimalFuneral;
import growthon.withtail_be.domain.animalfuneral.entity.FuneralAmenity;

import java.util.List;

public record AnimalFuneralSearchResDto(
        Long id,
        String name,
        String address,
        String phone,
        String mainImageUrl,
        boolean blissStoneAvailable,
        int reviewCount,
        double ratingAvg,
        List<String> amenities
) {

    public static AnimalFuneralSearchResDto from(
            AnimalFuneral funeral,
            List<String> amenityNames
    ) {
        return new AnimalFuneralSearchResDto(
                funeral.getId(),
                funeral.getName(),
                funeral.getAddress(),
                funeral.getPhone(),
                funeral.getMainImageUrl(),
                funeral.isBlissStoneAvailable(),
                funeral.getReviewCount(),
                funeral.getRatingAvg(),
                amenityNames
        );
    }
}

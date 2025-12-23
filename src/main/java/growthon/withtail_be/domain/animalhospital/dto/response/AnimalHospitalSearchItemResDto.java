package growthon.withtail_be.domain.animalhospital.dto.response;

import growthon.withtail_be.domain.animalhospital.entity.AnimalHospital;
import growthon.withtail_be.domain.animalhospital.entity.HospitalAmenity;

import java.util.List;

public record AnimalHospitalSearchItemResDto(
        Long hospitalId,
        String name,
        String address,
        String phone,
        Boolean open24h,
        Boolean nightCare,
        Boolean isOpenNow,
        Double ratingAvg,
        Integer reviewCount,
        String mainImageUrl,
        List<String> amenities
) {
    public static AnimalHospitalSearchItemResDto from(
            AnimalHospital hospital,
            boolean isOpenNow
    ) {
        List<String> amenities = hospital.getAmenities().stream()
                .map(HospitalAmenity::getAmenity)
                .map(amenity -> amenity.getName())
                .toList();

        return new AnimalHospitalSearchItemResDto(
                hospital.getId(),
                hospital.getName(),
                hospital.getAddress(),
                hospital.getPhone(),
                hospital.isOpen24h(),
                hospital.isNightCare(),
                isOpenNow,
                hospital.getRatingAvg(),
                hospital.getReviewCount(),
                hospital.getMainImageUrl(),
                amenities
        );
    }
}

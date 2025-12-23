package growthon.withtail_be.domain.animalhospital.dto.response;

import growthon.withtail_be.domain.animalhospital.entity.AnimalHospital;
import growthon.withtail_be.domain.animalhospital.entity.HospitalAmenity;
import growthon.withtail_be.domain.animalhospital.entity.HospitalAnimalType;
import growthon.withtail_be.domain.animalhospital.entity.HospitalSpecialty;

import java.util.List;

public record AnimalHospitalDetailResDto(
        Long hospitalId,
        String name,
        String address,
        String phone,
        String description,
        Boolean open24h,
        Boolean nightCare,
        Boolean isOpenNow,
        Double ratingAvg,
        Integer reviewCount,
        String mainImageUrl,
        List<String> amenities,
        List<String> animalTypes,
        List<String> specialties
) {

    public static AnimalHospitalDetailResDto from(
            AnimalHospital hospital,
            boolean isOpenNow
    ) {
        List<String> amenities = hospital.getAmenities().stream()
                .map(HospitalAmenity::getAmenity)
                .map(a -> a.getName())
                .toList();

        List<String> animalTypes = hospital.getAnimalTypes().stream()
                .map(HospitalAnimalType::getAnimalType)
                .map(a -> a.getName())
                .toList();

        List<String> specialties = hospital.getSpecialties().stream()
                .map(HospitalSpecialty::getSpecialty)
                .map(s -> s.getName())
                .toList();

        return new AnimalHospitalDetailResDto(
                hospital.getId(),
                hospital.getName(),
                hospital.getAddress(),
                hospital.getPhone(),
                hospital.getDescription(),
                hospital.isOpen24h(),
                hospital.isNightCare(),
                isOpenNow,
                hospital.getRatingAvg(),
                hospital.getReviewCount(),
                hospital.getMainImageUrl(),
                amenities,
                animalTypes,
                specialties
        );
    }
}

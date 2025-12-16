package growthon.withtail_be.domain.animalhospital.dto;

import growthon.withtail_be.domain.animalhospital.entity.AnimalHospital;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;

@Getter
public class AnimalHospitalAboutResponse {

    private final Long id;
    private final String description;
    private final List<String> amenities;
    private final List<String> paymentMethods;

    private AnimalHospitalAboutResponse(Long id, String description, List<String> amenities, List<String> paymentMethods) {
        this.id = id;
        this.description = description;
        this.amenities = amenities;
        this.paymentMethods = paymentMethods;
    }

    public static AnimalHospitalAboutResponse from(AnimalHospital h) {
        List<String> amenities = h.getHospitalAmenities().stream()
                .map(j -> j.getAmenity().getName())
                .distinct()
                .collect(Collectors.toList());

        List<String> paymentMethods = h.getHospitalPaymentMethods().stream()
                .map(j -> j.getPaymentMethod().getName())
                .distinct()
                .collect(Collectors.toList());

        return new AnimalHospitalAboutResponse(
                h.getId(),
                h.getDescription(),
                amenities,
                paymentMethods
        );
    }
}

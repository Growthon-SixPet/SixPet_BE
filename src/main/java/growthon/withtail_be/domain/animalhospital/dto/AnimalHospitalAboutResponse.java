package growthon.withtail_be.domain.animalhospital.dto;

import growthon.withtail_be.domain.animalhospital.entity.AnimalHospital;
import java.util.List;
import java.util.stream.Collectors;

public class AnimalHospitalAboutResponse {

    private final Long id;
    private final String description;
    private final List<String> services;
    private final List<String> paymentMethods;

    private AnimalHospitalAboutResponse(Long id, String description, List<String> services, List<String> paymentMethods) {
        this.id = id;
        this.description = description;
        this.services = services;
        this.paymentMethods = paymentMethods;
    }

    public static AnimalHospitalAboutResponse from(AnimalHospital h) {
        List<String> services = h.getAmenities().stream()
                .map(j -> j.getAmenity().getName())
                .collect(Collectors.toList());

        List<String> paymentMethods = h.getPaymentMethods().stream()
                .map(j -> j.getPaymentMethod().getName())
                .collect(Collectors.toList());

        return new AnimalHospitalAboutResponse(
                h.getId(),
                h.getDescription(),
                services,
                paymentMethods
        );
    }

    public Long getId() { return id; }
    public String getDescription() { return description; }
    public List<String> getServices() { return services; }
    public List<String> getPaymentMethods() { return paymentMethods; }
}

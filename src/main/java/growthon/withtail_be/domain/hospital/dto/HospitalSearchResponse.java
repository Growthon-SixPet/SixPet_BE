package growthon.withtail_be.domain.hospital.dto;

import growthon.withtail_be.domain.hospital.entity.Hospital;
import lombok.Builder;
import lombok.Getter;

// 리스트 응답용 DTO (Entity 그대로 노출하지 않기)
@Getter
@Builder
public class HospitalSearchResponse {

    private Long id;
    private String name;
    private String address;
    private String phone;

    private Double rating;
    private Integer reviewCount;
    private Boolean is24;

    // Entity -> DTO 변환
    public static HospitalSearchResponse from(Hospital hospital) {
        return HospitalSearchResponse.builder()
                .id(hospital.getId())
                .name(hospital.getName())
                .address(hospital.getAddress())
                .phone(hospital.getPhone())
                .rating(hospital.getRating())
                .reviewCount(hospital.getReviewCount())
                .is24(hospital.getIs24h())
                .build();
    }
}

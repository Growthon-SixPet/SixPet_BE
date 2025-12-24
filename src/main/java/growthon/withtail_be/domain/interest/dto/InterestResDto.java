package growthon.withtail_be.domain.interest.dto;

import growthon.withtail_be.domain.animalfuneral.entity.AnimalFuneral;
import growthon.withtail_be.domain.animalhospital.entity.AnimalHospital;
import growthon.withtail_be.domain.interest.domain.Interest;
import growthon.withtail_be.domain.interest.domain.TargetType;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class InterestResDto {

    private Long interestId;

    private TargetType targetType;
    private Long targetId;
    private String targetName;

    private String address;

    private Double ratingAvg;
    private Integer reviewCount;

    private Boolean openNow;

    private Boolean open24h;
    private Boolean nightCare;

    private String mainImageUrl;

    public static InterestResDto fromHospital(Interest interest, AnimalHospital h, boolean openNow) {
        return InterestResDto.builder()
                .interestId(interest.getInterestId())
                .targetType(TargetType.HOSPITAL)
                .targetId(h.getId())
                .targetName(h.getName())
                .address(h.getAddress())
                .ratingAvg(h.getRatingAvg())
                .reviewCount(h.getReviewCount())
                .openNow(openNow)
                .open24h(h.isOpen24h())
                .nightCare(h.isNightCare())
                .mainImageUrl(h.getMainImageUrl())
                .build();
    }

    public static InterestResDto fromFuneral(Interest interest, AnimalFuneral f, boolean openNow) {
        return InterestResDto.builder()
                .interestId(interest.getInterestId())
                .targetType(TargetType.FUNERAL)
                .targetId(f.getId())
                .targetName(f.getName())
                .address(f.getAddress())
                .ratingAvg(f.getRatingAvg())
                .reviewCount(f.getReviewCount())
                .openNow(openNow)
                .open24h(null)
                .nightCare(null)
                .mainImageUrl(f.getMainImageUrl())
                .build();
    }

}

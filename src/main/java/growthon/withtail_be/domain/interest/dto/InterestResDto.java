package growthon.withtail_be.domain.interest.dto;

import growthon.withtail_be.domain.interest.domain.Funeral;
import growthon.withtail_be.domain.interest.domain.Hospital;
import growthon.withtail_be.domain.interest.domain.Interest;
import growthon.withtail_be.domain.interest.domain.TargetType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Builder
@Getter
public class InterestResDto {

    private Long interestId;
    private TargetType targetType;
    private LocalDate interestDate;
    private Hospital hospital;
    private String hospitalName;
    private Funeral funeral;
    private String funeralName;

    public static InterestResDto from (Interest interest) {
        return InterestResDto.builder()
                    .interestId(interest.getInterestId())
                    .targetType(interest.getTargetType())
                    .interestDate(LocalDate.now())
                    .hospital(interest.getHospital())
                    .funeral(interest.getFuneral())
                    .build();
    }
}
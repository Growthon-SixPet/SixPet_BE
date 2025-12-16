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
    private Long targetId;
    private String targetName;

    public static InterestResDto from (Interest interest) {
        Long targetId = null;
        String targetName = null;

        if (interest.getTargetType() == TargetType.HOSPITAL) {
            Hospital hospital = interest.getHospital();
            targetId = hospital.getId();
            targetName = hospital.getName();
        } else if (interest.getTargetType() == TargetType.FUNERAL) {
            Funeral funeral = interest.getFuneral();
            targetId = funeral.getId();
            targetName = funeral.getName();
        }

        return InterestResDto.builder()
                    .interestId(interest.getInterestId())
                    .targetType(interest.getTargetType())
                    .targetId(targetId)
                    .targetName(targetName)
                    .build();
    }
}

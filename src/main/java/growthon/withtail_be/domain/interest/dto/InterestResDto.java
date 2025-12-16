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
    private Long userId;
    private TargetType targetType;
    private LocalDate interestDate;
    private Long targetId;
    private String targetName;

    public static InterestResDto from (Interest interest) {
        Long targetId = null;
        String targetName = null;

        if (interest.getTargetType() == TargetType.HOSPITAL) {
            Hospital hospital = interest.getHospital();
            targetId = hospital.getId();
            targetName = hospital.getName();
        }

        if (interest.getTargetType() == TargetType.FUNERAL) {
            Funeral funeral = interest.getFuneral();
            targetId = funeral.getId();
            targetName = funeral.getName();
        }

        return InterestResDto.builder()
                    .interestId(interest.getInterestId())
                    .userId(interest.getUser().getId())
                    .targetType(interest.getTargetType())
                    .interestDate(LocalDate.now())
                    .targetId(targetId)
                    .targetName(targetName)
                    .build();
    }
}
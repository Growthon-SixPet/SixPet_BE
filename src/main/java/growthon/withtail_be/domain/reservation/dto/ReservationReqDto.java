package growthon.withtail_be.domain.reservation.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import growthon.withtail_be.domain.reservation.domain.TargetType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@NoArgsConstructor
public class ReservationReqDto {
    @NotBlank(message = "보호자 이름은 필수입니다.")
    private String ownerName;

    @NotBlank(message = "연락처는 필수입니다.")
    @Pattern(
            regexp = "^010-[0-9]{4}-[0-9]{4}$",
            message = "올바른 전화번호 형식이 아닙니다."
    )
    private String phoneNumber;

    @NotBlank(message = "반려동물 이름은 필수입니다.")
    private String petName;

    @NotNull(message = "예약일은 필수입니다.")
    private LocalDate reservationDate;

    @NotNull(message = "예약시간은 필수입니다.")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private LocalTime reservationTime;

    @NotBlank(message = "방문사유를 적어주세요.")
    private String visitReason;
}

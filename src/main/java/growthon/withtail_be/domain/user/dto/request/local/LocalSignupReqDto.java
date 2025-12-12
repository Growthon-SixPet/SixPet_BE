package growthon.withtail_be.domain.user.dto.request.local;

import growthon.withtail_be.domain.user.entity.Gender;

import java.time.LocalDate;

public record LocalSignupReqDto(
        String phoneNumber,
        String password,
        String name,
        String nickname,
        LocalDate birthDate,
        Gender gender,
        String address
) {
}

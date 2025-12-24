package growthon.withtail_be.domain.animalhospital.dto.response;

import growthon.withtail_be.domain.animalhospital.entity.HospitalOperatingHours;

public record HospitalOperatingHoursResDto(
        String dayOfWeek,
        String openTime,
        String closeTime,
        Boolean closed
) {

    public static HospitalOperatingHoursResDto from(HospitalOperatingHours oh) {
        return new HospitalOperatingHoursResDto(
                oh.getDayOfWeek().name(),
                oh.getOpenTime(),
                oh.getCloseTime(),
                oh.isClosed()
        );
    }
}

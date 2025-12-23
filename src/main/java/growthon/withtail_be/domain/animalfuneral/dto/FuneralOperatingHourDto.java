package growthon.withtail_be.domain.animalfuneral.dto;

import growthon.withtail_be.domain.animalfuneral.entity.DayOfWeekType;
import growthon.withtail_be.domain.animalfuneral.entity.FuneralOperatingHours;

public record FuneralOperatingHourDto(
        DayOfWeekType dayOfWeek,
        boolean closed,
        String openTime,
        String closeTime
) {
    public static FuneralOperatingHourDto from(FuneralOperatingHours oh) {
        return new FuneralOperatingHourDto(
                oh.getDayOfWeek(),
                oh.isClosed(),
                oh.getOpenTime(),
                oh.getCloseTime()
        );
    }
}


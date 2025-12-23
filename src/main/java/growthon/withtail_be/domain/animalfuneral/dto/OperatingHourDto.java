package growthon.withtail_be.domain.animalfuneral.dto;

import growthon.withtail_be.domain.animalfuneral.entity.FuneralOperatingHours;

public class OperatingHourDto {

    private String dayOfWeek;
    private String openTime;
    private String closeTime;
    private String breakTime;
    private String note;

    public static OperatingHourDto from(FuneralOperatingHours entity) {
        OperatingHourDto dto = new OperatingHourDto();
        dto.dayOfWeek = entity.getDayOfWeek().name();
        dto.openTime = entity.getOpenTime();
        dto.closeTime = entity.getCloseTime();
        dto.breakTime = entity.getBreakTime();
        dto.note = entity.getNote();
        return dto;
    }

    public String getDayOfWeek() { return dayOfWeek; }
    public String getOpenTime() { return openTime; }
    public String getCloseTime() { return closeTime; }
    public String getBreakTime() { return breakTime; }
    public String getNote() { return note; }
}

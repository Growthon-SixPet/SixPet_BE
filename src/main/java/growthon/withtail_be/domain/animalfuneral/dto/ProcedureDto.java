package growthon.withtail_be.domain.animalfuneral.dto;

import growthon.withtail_be.domain.animalfuneral.entity.FuneralProcedure;

public class ProcedureDto {

    private Long id;
    private int stepNo;
    private String title;
    private String description;

    public static ProcedureDto from(FuneralProcedure entity) {
        ProcedureDto dto = new ProcedureDto();
        dto.id = entity.getId();
        dto.stepNo = entity.getStepNo();
        dto.title = entity.getTitle();
        dto.description = entity.getDescription();
        return dto;
    }

    public Long getId() { return id; }
    public int getStepNo() { return stepNo; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
}

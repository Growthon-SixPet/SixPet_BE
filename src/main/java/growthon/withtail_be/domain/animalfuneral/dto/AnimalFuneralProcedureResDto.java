package growthon.withtail_be.domain.animalfuneral.dto;

import growthon.withtail_be.domain.animalfuneral.entity.AnimalFuneral;

public record AnimalFuneralProcedureResDto(
        Long id,
        String name,
        String procedureImageUrl
) {

    public static AnimalFuneralProcedureResDto from(AnimalFuneral funeral) {
        return new AnimalFuneralProcedureResDto(
                funeral.getId(),
                funeral.getName(),
                funeral.getProcedureImageUrl()
        );
    }
}

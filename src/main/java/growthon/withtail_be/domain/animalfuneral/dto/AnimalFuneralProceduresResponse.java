package growthon.withtail_be.domain.animalfuneral.dto;

import growthon.withtail_be.domain.animalfuneral.entity.AnimalFuneral;
import growthon.withtail_be.domain.animalfuneral.entity.FuneralProcedure;
import java.util.ArrayList;
import java.util.List;

public class AnimalFuneralProceduresResponse {

    private Long id;
    private String name;
    private List<ProcedureDto> procedures;

    public static AnimalFuneralProceduresResponse from(AnimalFuneral funeral) {
        AnimalFuneralProceduresResponse dto = new AnimalFuneralProceduresResponse();
        dto.id = funeral.getId();
        dto.name = funeral.getName();

        List<ProcedureDto> list = new ArrayList<>();
        for (FuneralProcedure p : funeral.getProcedures()) {
            list.add(ProcedureDto.from(p));
        }
        dto.procedures = list;
        return dto;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public List<ProcedureDto> getProcedures() { return procedures; }
}

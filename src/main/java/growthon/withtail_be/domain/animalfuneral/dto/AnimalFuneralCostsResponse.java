package growthon.withtail_be.domain.animalfuneral.dto;

import growthon.withtail_be.domain.animalfuneral.entity.AnimalFuneral;
import growthon.withtail_be.domain.animalfuneral.entity.FuneralCost;
import java.util.ArrayList;
import java.util.List;

public class AnimalFuneralCostsResponse {

    private Long id;
    private String name;
    private List<CostDto> costs;

    public static AnimalFuneralCostsResponse from(AnimalFuneral funeral) {
        AnimalFuneralCostsResponse dto = new AnimalFuneralCostsResponse();
        dto.id = funeral.getId();
        dto.name = funeral.getName();

        List<CostDto> list = new ArrayList<>();
        for (FuneralCost c : funeral.getCosts()) {
            list.add(CostDto.from(c));
        }
        dto.costs = list;
        return dto;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public List<CostDto> getCosts() { return costs; }
}

package growthon.withtail_be.domain.animalfuneral.dto;

import growthon.withtail_be.domain.animalfuneral.entity.FuneralCost;

public record FuneralCostDto(
        String name,
        int price
) {
    public static FuneralCostDto from(FuneralCost cost) {
        return new FuneralCostDto(cost.getName(), cost.getPrice());
    }
}


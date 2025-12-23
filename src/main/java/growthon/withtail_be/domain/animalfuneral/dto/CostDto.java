package growthon.withtail_be.domain.animalfuneral.dto;

import growthon.withtail_be.domain.animalfuneral.entity.FuneralCost;

public class CostDto {

    private Long id;
    private String name;
    private int price;

    public static CostDto from(FuneralCost entity) {
        CostDto dto = new CostDto();
        dto.id = entity.getId();
        dto.name = entity.getName();
        dto.price = entity.getPrice();
        return dto;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public int getPrice() { return price; }
}

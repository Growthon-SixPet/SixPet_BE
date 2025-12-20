package growthon.withtail_be.domain.animalfuneral.dto;

import growthon.withtail_be.domain.animalfuneral.entity.AnimalFuneral;
import growthon.withtail_be.domain.animalfuneral.entity.FuneralBrandSection;
import java.util.ArrayList;
import java.util.List;

public class AnimalFuneralBrandResponse {

    private Long id;
    private String name;
    private List<BrandSectionDto> sections;

    public static AnimalFuneralBrandResponse from(AnimalFuneral funeral) {
        AnimalFuneralBrandResponse dto = new AnimalFuneralBrandResponse();
        dto.id = funeral.getId();
        dto.name = funeral.getName();

        List<BrandSectionDto> list = new ArrayList<>();
        for (FuneralBrandSection s : funeral.getBrandSections()) {
            list.add(BrandSectionDto.from(s));
        }
        dto.sections = list;
        return dto;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public List<BrandSectionDto> getSections() { return sections; }
}

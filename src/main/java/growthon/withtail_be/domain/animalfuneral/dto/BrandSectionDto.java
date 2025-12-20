package growthon.withtail_be.domain.animalfuneral.dto;

import growthon.withtail_be.domain.animalfuneral.entity.FuneralBrandSection;

public class BrandSectionDto {

    private Long id;
    private String title;
    private String content;

    public static BrandSectionDto from(FuneralBrandSection entity) {
        BrandSectionDto dto = new BrandSectionDto();
        dto.id = entity.getId();
        dto.title = entity.getTitle();
        dto.content = entity.getContent();
        return dto;
    }

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getContent() { return content; }
}

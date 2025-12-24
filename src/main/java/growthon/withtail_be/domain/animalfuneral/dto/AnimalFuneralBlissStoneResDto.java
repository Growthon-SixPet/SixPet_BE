package growthon.withtail_be.domain.animalfuneral.dto;

import growthon.withtail_be.domain.animalfuneral.entity.AnimalFuneral;

public record AnimalFuneralBlissStoneResDto(
        Long id,
        String name,
        String blissStoneImageUrl
) {

    public static AnimalFuneralBlissStoneResDto from(AnimalFuneral funeral) {
        return new AnimalFuneralBlissStoneResDto(
                funeral.getId(),
                funeral.getName(),
                funeral.getBlissStoneImageUrl()
        );
    }
}

package growthon.withtail_be.domain.animalhospital.dto.request;

public record AnimalHospitalSearchReqDto(

        String specialty,
        String animalType,
        Boolean open24h,
        Boolean nightCare,
        String sortType,
        Integer page,
        Integer size
) {

    public int pageOrDefault() {
        return page != null ? page : 0;
    }

    public int sizeOrDefault() {
        return size != null ? size : 10;
    }
}

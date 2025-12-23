package growthon.withtail_be.domain.animalhospital.dto.response;

import growthon.withtail_be.domain.animalhospital.entity.HospitalNews;

import java.time.LocalDate;

public record HospitalNewsResDto(
        Long newsId,
        String title,
        String content,
        String imageUrl,
        LocalDate createdAt
) {

    public static HospitalNewsResDto from(HospitalNews news) {
        return new HospitalNewsResDto(
                news.getId(),
                news.getTitle(),
                news.getContent(),
                news.getImageUrl(),
                news.getCreatedAt()
        );
    }
}

package growthon.withtail_be.domain.user.dto.response;

public record TokenDto(
        String accessToken,
        String refreshToken
) {
}

package growthon.withtail_be.domain.user.dto.token;

public record TokenDto(
        String accessToken,
        String refreshToken
) {
}

package growthon.withtail_be.domain.user.dto.response;

public record loginResDto(
        String accessToken,
        UserInfoResDto user
) {
}

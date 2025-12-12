package growthon.withtail_be.domain.user.dto.response;

public record loginHelperRes(
        UserInfoResDto userInfoResDto,
        String accessToken,
        String refreshToken
) {
}

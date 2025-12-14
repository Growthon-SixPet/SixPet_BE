package growthon.withtail_be.domain.user.dto.token;

import growthon.withtail_be.domain.user.dto.response.UserInfoResDto;

public record loginHelperRes(
        UserInfoResDto userInfoResDto,
        String accessToken,
        String refreshToken
) {
}

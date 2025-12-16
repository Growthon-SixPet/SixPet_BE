package growthon.withtail_be.domain.user.dto.response;

import growthon.withtail_be.domain.user.entity.User;

public record LoginResult(
        String accessToken,
        String refreshToken,
        User user
) {
}

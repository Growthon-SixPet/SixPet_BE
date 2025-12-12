package growthon.withtail_be.domain.user.service;

import growthon.withtail_be.domain.user.dto.response.TokenDto;
import growthon.withtail_be.domain.user.entity.RefreshToken;
import growthon.withtail_be.domain.user.entity.User;
import growthon.withtail_be.domain.user.repository.RefreshTokenRepository;
import growthon.withtail_be.domain.user.repository.UserRepository;
import growthon.withtail_be.global.code.ErrorStatus;
import growthon.withtail_be.global.exception.GeneralException;
import growthon.withtail_be.global.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

    // accesstoken 과 refreshtoken 생성
    public TokenDto generateTokens(User user) {
        String accessToken = tokenProvider.createAccessToken(user.getId());
        String refreshToken = tokenProvider.createRefreshToken(user.getId());

        return new TokenDto(accessToken, refreshToken);
    }

    // RefreshToken DB 저장 또는 업데이트
    public String saveOrUpdateRefreshToken(
            User user,
            String refreshToken,
            Boolean keepLogin
    ) {
        long days = Boolean.TRUE.equals(keepLogin) ? 14 : 7;
        LocalDateTime expiration = LocalDateTime.now().plusDays(days);

        return refreshTokenRepository.findByUser(user)
                .map(existingToken -> {
                    existingToken.updateToken(refreshToken, expiration);
                    return existingToken.getToken();
                })
                .orElseGet(() -> {
                    RefreshToken newToken = RefreshToken.builder()
                            .user(user)
                            .token(refreshToken)
                            .expiration(expiration)
                            .build();
                    refreshTokenRepository.save(newToken);
                    return newToken.getToken();
                });
    }

    // Refresh Token 유효성 검증 + AccessToken 재발급
    public String reissueAccessToken(String refreshToken) {

        if (!tokenProvider.validateToken(refreshToken)) {
            throw new GeneralException(ErrorStatus.REFRESH_TOKEN_INVALID);
        }

        Long userId = tokenProvider.getUserId(refreshToken);

        RefreshToken savedToken = refreshTokenRepository.findByUserId(userId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.REFRESH_TOKEN_NOT_FOUND));

        if (!savedToken.getToken().equals(refreshToken)) {
            throw new GeneralException(ErrorStatus.REFRESH_TOKEN_MISMATCH);
        }

        if (savedToken.isExpired()) {
            throw new GeneralException(ErrorStatus.REFRESH_TOKEN_EXPIRED);
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));

        return tokenProvider.createAccessToken(user.getId());
    }
}

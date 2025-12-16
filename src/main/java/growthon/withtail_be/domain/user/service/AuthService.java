package growthon.withtail_be.domain.user.service;

import growthon.withtail_be.domain.user.dto.request.create.local.LocalLoginReqDto;
import growthon.withtail_be.domain.user.dto.token.TokenDto;
import growthon.withtail_be.domain.user.dto.response.UserInfoResDto;
import growthon.withtail_be.domain.user.dto.token.loginHelperRes;
import growthon.withtail_be.domain.user.entity.Provider;
import growthon.withtail_be.domain.user.entity.User;
import growthon.withtail_be.domain.user.repository.UserRepository;
import growthon.withtail_be.global.code.ErrorStatus;
import growthon.withtail_be.global.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    // 자체 로그인
    public loginHelperRes localLogin(LocalLoginReqDto req) {

        User user = userRepository.findByPhoneNumber(req.phoneNumber())
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));

        // 이 유저가 다른 경로(소셜)로 가입했던 유저라면
        if (user.getProvider() != Provider.LOCAL) {
            throw new GeneralException(ErrorStatus.AUTH_PROVIDER_MISMATCH);
        }

        if (user.getPassword() == null || !passwordEncoder.matches(req.password(), user.getPassword())) {
            throw new GeneralException(ErrorStatus.INVALID_PASSWORD);
        }

        TokenDto tokens = tokenService.generateTokens(user);

        String savedRefreshToken = tokenService.saveOrUpdateRefreshToken(
                user, tokens.refreshToken(), req.keepLogin()
        );

        return new loginHelperRes(UserInfoResDto.from(user), tokens.accessToken(), savedRefreshToken);
    }
}

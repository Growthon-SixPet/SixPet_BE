package growthon.withtail_be.domain.user.service;

import growthon.withtail_be.domain.user.dto.request.google.GoogleUserInfo;
import growthon.withtail_be.domain.user.dto.request.social.SocialSignupReqDto;
import growthon.withtail_be.domain.user.dto.token.TokenDto;
import growthon.withtail_be.domain.user.dto.response.UserInfoResDto;
import growthon.withtail_be.domain.user.dto.token.loginHelperRes;
import growthon.withtail_be.domain.user.entity.Provider;
import growthon.withtail_be.domain.user.entity.User;
import growthon.withtail_be.domain.user.repository.UserRepository;
import growthon.withtail_be.global.code.ErrorStatus;
import growthon.withtail_be.global.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class GoogleOAuthService {

    private final GoogleClient googleClient;
    private final UserRepository userRepository;
    private final TokenService tokenService;

    public loginHelperRes loginOrSignUp(String code, SocialSignupReqDto req) {
        String googleAccessToken = googleClient.requestAccessToken(code);
        GoogleUserInfo googleUser = googleClient.requestUserInfo(googleAccessToken);

        User user = userRepository
                .findByProviderAndProviderId(Provider.GOOGLE, googleUser.getId())
                .orElseGet(() -> registerGoogleUser(googleUser, req));

        TokenDto tokens = tokenService.generateTokens(user);

        String savedRefreshToken = tokenService.saveOrUpdateRefreshToken(
                user, tokens.refreshToken(), false
        );

        return new loginHelperRes(UserInfoResDto.from(user), tokens.accessToken(), savedRefreshToken);
    }

    private User registerGoogleUser(GoogleUserInfo googleUser, SocialSignupReqDto req) {
        if (userRepository.existsByPhoneNumber(req.phoneNumber())) {
            throw new GeneralException(ErrorStatus.USER_PHONE_ALREADY_EXISTS);
        }

        User newUser = User.builder()
                .provider(Provider.GOOGLE)
                .providerId(googleUser.getId())
                .phoneNumber(req.phoneNumber())
                .password(null)
                .name(req.name())
                .nickname(req.nickname())
                .birthDate(req.birthDate())
                .gender(req.gender())
                .address(req.address())
                .profileImage(googleUser.getPicture())
                .build();

        return userRepository.save(newUser);
    }
}

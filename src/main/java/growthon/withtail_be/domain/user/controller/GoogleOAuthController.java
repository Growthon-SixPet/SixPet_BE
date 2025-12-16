package growthon.withtail_be.domain.user.controller;

import growthon.withtail_be.domain.user.dto.request.create.social.SocialSignupReqDto;
import growthon.withtail_be.domain.user.dto.token.loginHelperRes;
import growthon.withtail_be.domain.user.dto.response.loginResDto;
import growthon.withtail_be.domain.user.service.GoogleOAuthService;
import growthon.withtail_be.global.code.SuccessStatus;
import growthon.withtail_be.global.response.BaseResponse;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/oauth")
public class GoogleOAuthController {

    private final GoogleOAuthService googleOAuthService;

    @PostMapping("/loginOrSingUp")
    public BaseResponse<loginResDto> loginOrSingUp (
            @RequestParam("code") String code,
            @RequestBody @Valid SocialSignupReqDto req,
            HttpServletResponse response
    ) {
        loginHelperRes helperRes = googleOAuthService.loginOrSignUp(code, req);

        ResponseCookie refreshCookie = ResponseCookie.from("refresh_token", helperRes.refreshToken())
                .httpOnly(true)
                .secure(false)
                .path("/")
                .sameSite("Lax")
                .maxAge(7 * 24 * 60 * 60)
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());

        loginResDto res = new loginResDto(helperRes.accessToken(), helperRes.userInfoResDto());

        return BaseResponse.onSuccess(SuccessStatus.OK, res);
    }
}

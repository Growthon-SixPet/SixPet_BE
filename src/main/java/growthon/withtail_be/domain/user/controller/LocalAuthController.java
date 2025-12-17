package growthon.withtail_be.domain.user.controller;

import growthon.withtail_be.domain.user.dto.request.create.local.LocalLoginReqDto;
import growthon.withtail_be.domain.user.dto.token.AccessTokenDto;
import growthon.withtail_be.domain.user.dto.token.loginHelperRes;
import growthon.withtail_be.domain.user.service.AuthService;
import growthon.withtail_be.domain.user.service.TokenService;
import growthon.withtail_be.domain.user.service.UserService;
import growthon.withtail_be.global.code.ErrorStatus;
import growthon.withtail_be.global.code.SuccessStatus;
import growthon.withtail_be.global.exception.GeneralException;
import growthon.withtail_be.global.response.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class LocalAuthController {

    private final UserService userService;
    private final AuthService authService;
    private final TokenService tokenService;

    // LOCAL 로그인
    @PostMapping("/login")
    @Operation(
            summary = "자체 로그인",
            description = """
                    휴대폰 번호와 비밀번호로 로그인합니다.
                    
                    - AccessToken은 응답 바디로 반환됩니다.
                    - RefreshToken은 HttpOnly 쿠키로 저장됩니다.
                    - 쿠키 유효기간은 30일이며, 실제 유효성은 서버(DB)에서 판단합니다.
                    """
    )
    public BaseResponse<AccessTokenDto> login(
            @RequestBody @Valid LocalLoginReqDto req,
            HttpServletResponse response
    ) {
        loginHelperRes helperRes = authService.localLogin(req);

        ResponseCookie refreshCookie = ResponseCookie.from("refresh_token", helperRes.refreshToken())
                .httpOnly(true)
                .secure(false) // 운영(HTTPS)이면 true 권장
                .path("/")
                .sameSite("Lax")
                .maxAge(30L * 24L * 60L * 60L)
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());

        return BaseResponse.onSuccess(SuccessStatus.OK, new AccessTokenDto(helperRes.accessToken()));
    }

    // 액세스 토큰 재발급
    @PostMapping("/reissue")
    @Operation(
            summary = "액세스 토큰 재발급",
            description = """
                    RefreshToken을 이용해 AccessToken을 재발급합니다.
                    
                    - RefreshToken은 HttpOnly 쿠키에서 자동으로 전달됩니다.
                    - RefreshToken이 유효하지 않거나 만료된 경우 재발급에 실패합니다.
                    - 응답으로는 새로운 AccessToken만 반환됩니다.
                    """
    )
    public BaseResponse<AccessTokenDto> reissue(
            @CookieValue(value = "refresh_token", required = false) String refreshToken
    ) {
        if (refreshToken == null || refreshToken.isBlank()) {
            throw new GeneralException(ErrorStatus.REFRESH_TOKEN_INVALID); // 또는 NOT_FOUND 새로 만들어도 됨
        }

        String newTokens = tokenService.reissueAccessToken(refreshToken);

        return BaseResponse.onSuccess(SuccessStatus.OK, new AccessTokenDto(newTokens));
    }

    // 로그아웃
    @PostMapping("/logout")
    @Operation(
            summary = "로그아웃",
            description = """
                    로그아웃을 수행합니다.
                    
                    - 서버에 저장된 RefreshToken을 삭제합니다.
                    - 클라이언트의 RefreshToken 쿠키를 즉시 만료시킵니다.
                    - AccessToken은 서버에서 별도로 관리하지 않으며, 클라이언트에서 폐기합니다.
                    """
    )
    public BaseResponse<Void> logout(
            @CookieValue(value = "refresh_token", required = false) String refreshToken,
            HttpServletResponse response
    ) {
        tokenService.logout(refreshToken);

        ResponseCookie deleteCookie = ResponseCookie.from("refresh_token", "")
                .httpOnly(true)
                .secure(false) // 운영이면 true
                .path("/")
                .sameSite("Lax")
                .maxAge(0)
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, deleteCookie.toString());

        return BaseResponse.onSuccess(SuccessStatus.OK, null);
    }

}

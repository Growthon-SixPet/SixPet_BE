package growthon.withtail_be.domain.user.controller;

import growthon.withtail_be.domain.user.dto.request.create.local.LocalSignupReqDto;
import growthon.withtail_be.domain.user.dto.request.update.PasswordUpdateReqDto;
import growthon.withtail_be.domain.user.dto.request.update.UserProfileUpdateReqDto;
import growthon.withtail_be.domain.user.dto.response.UserInfoResDto;
import growthon.withtail_be.domain.user.service.UserService;
import growthon.withtail_be.global.code.SuccessStatus;
import growthon.withtail_be.global.response.BaseResponse;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    // LOCAL 회원가입
    @PostMapping("/signup")
    public BaseResponse<UserInfoResDto> signup(@RequestBody @Valid LocalSignupReqDto req) {
        return BaseResponse.onSuccess(SuccessStatus.USER_SIGNUP_SUCCESS, userService.localSignup(req));
    }

    // 내 정보 조회
    @GetMapping("/profile")
    public BaseResponse<UserInfoResDto> getMyProfile(Authentication authentication) {
        Long userId = Long.parseLong(authentication.getName());
        UserInfoResDto res = userService.getProfile(userId);
        return BaseResponse.onSuccess(SuccessStatus.USER_PROFILE_GET_SUCCESS, res);
    }

    // 내 기본정보 수정
    @PatchMapping("/profile")
    public BaseResponse<UserInfoResDto> updateMyProfile(
            Authentication authentication,
            @RequestBody @Valid UserProfileUpdateReqDto req
    ) {
        Long userId = Long.parseLong(authentication.getName());
        UserInfoResDto res = userService.updateProfile(userId, req);
        return BaseResponse.onSuccess(SuccessStatus.USER_PROFILE_UPDATE_SUCCESS, res);
    }

    // 비밀번호 변경
    @PatchMapping("/profile/password")
    public BaseResponse<Void> updatePassword(
            Authentication authentication,
            @RequestBody @Valid PasswordUpdateReqDto req
    ) {
        Long userId = Long.parseLong(authentication.getName());
        userService.updatePassword(userId, req);
        return BaseResponse.onSuccess(SuccessStatus.USER_PASSWORD_UPDATE_SUCCESS, null);
    }

    // 회원 탈퇴
    @DeleteMapping("/profile")
    public BaseResponse<Void> deleteMe(
            Authentication authentication,
            HttpServletResponse response
    ) {
        Long userId = Long.parseLong(authentication.getName());
        userService.deleteUser(userId);

        ResponseCookie deleteCookie = ResponseCookie.from("refresh_token", "")
                .httpOnly(true)
                .secure(false) // 배포 HTTPS면 true
                .path("/")
                .sameSite("Lax")
                .maxAge(0)
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, deleteCookie.toString());

        return BaseResponse.onSuccess(SuccessStatus.USER_DELETE_SUCCESS, null);
    }
}

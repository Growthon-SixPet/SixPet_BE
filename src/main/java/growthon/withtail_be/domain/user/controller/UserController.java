package growthon.withtail_be.domain.user.controller;

import growthon.withtail_be.domain.user.dto.request.create.local.LocalSignupReqDto;
import growthon.withtail_be.domain.user.dto.request.update.PasswordUpdateReqDto;
import growthon.withtail_be.domain.user.dto.request.update.UserProfileUpdateReqDto;
import growthon.withtail_be.domain.user.dto.response.UserInfoResDto;
import growthon.withtail_be.domain.user.service.UserService;
import growthon.withtail_be.global.code.SuccessStatus;
import growthon.withtail_be.global.response.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    // LOCAL 회원가입
    @PostMapping("/signup")
    @Operation(
            summary = "자체 회원가입",
            description = """
                휴대폰 번호와 비밀번호를 이용해 자체 회원가입을 진행합니다.

                - 휴대폰 인증이 사전에 완료되어 있어야 합니다.
                - 회원가입 시 토큰은 발급되지 않습니다.
                - 회원가입 완료 후, 로그인 API를 통해 다시 로그인해야 서비스를 이용할 수 있습니다.
                """
    )
    public BaseResponse<UserInfoResDto> signup(@RequestBody @Valid LocalSignupReqDto req) {
        return BaseResponse.onSuccess(SuccessStatus.USER_SIGNUP_SUCCESS, userService.localSignup(req));
    }

    // 내 정보 조회
    @GetMapping("/profile")
    @Operation(
            summary = "내 프로필 조회",
            description = """
                로그인한 사용자의 프로필 정보를 조회합니다.

                - Authorization Header에 Bearer Access Token이 존재해야 합니다.
                - 기본 회원 정보 및 프로필 이미지 URL을 반환합니다.
                """
    )
    public BaseResponse<UserInfoResDto> getMyProfile(Authentication authentication) {
        Long userId = Long.parseLong(authentication.getName());
        UserInfoResDto res = userService.getProfile(userId);
        return BaseResponse.onSuccess(SuccessStatus.USER_PROFILE_GET_SUCCESS, res);
    }

    // 내 기본정보 수정
    @PatchMapping("/profile")
    @Operation(
            summary = "내 기본정보 수정",
            description = """
                로그인한 사용자의 기본 정보를 수정합니다.

                - 수정 가능 항목: 이름, 닉네임, 생년월일, 성별, 주소
                - Authorization Header에 Bearer Access Token이 존재해야 합니다.
                """
    )
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
    @Operation(
            summary = "비밀번호 변경",
            description = """
                로그인한 사용자의 비밀번호를 변경합니다.

                - LOCAL 회원만 가능합니다.
                - Authorization Header에 Bearer Access Token이 존재해야 합니다.
                - 현재 비밀번호 검증 후 변경됩니다.
                - 새 비밀번호와 확인 비밀번호가 일치해야 합니다.
                """
    )
    public BaseResponse<Void> updatePassword(
            Authentication authentication,
            @RequestBody @Valid PasswordUpdateReqDto req
    ) {
        Long userId = Long.parseLong(authentication.getName());
        userService.updatePassword(userId, req);
        return BaseResponse.onSuccess(SuccessStatus.USER_PASSWORD_UPDATE_SUCCESS, null);
    }

    // 프로필 이미지 업로드/변경
    @PatchMapping(value = "/profile/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(
            summary = "프로필 이미지 업로드/변경",
            description = """
                로그인한 사용자의 프로필 이미지를 업로드하거나 변경합니다.

                - Authorization Header에 Bearer Access Token이 존재해야 합니다.
                - multipart/form-data 형식
                - image 필드로 파일 전송
                - 기존 이미지가 있을 경우 자동으로 교체됩니다.
                """
    )
    public BaseResponse<UserInfoResDto> updateProfileImage(
            Authentication authentication,
            @RequestPart("image") MultipartFile image
    ) {
        Long userId = Long.parseLong(authentication.getName());
        UserInfoResDto res = userService.updateProfileImage(userId, image);
        return BaseResponse.onSuccess(SuccessStatus.USER_PROFILE_IMAGE_UPDATE_SUCCESS, res);
    }

    // 프로필 이미지 삭제
    @DeleteMapping("/profile/image")
    @Operation(
            summary = "프로필 이미지 삭제",
            description = """
                로그인한 사용자의 프로필 이미지를 삭제합니다.

                - Authorization Header에 Bearer Access Token이 존재해야 합니다.
                - 기존 프로필 이미지가 없을 경우 에러가 발생합니다.
                - 삭제 후 프로필 이미지는 null 상태가 됩니다.
                """
    )
    public BaseResponse<UserInfoResDto> deleteProfileImage(Authentication authentication) {
        Long userId = Long.parseLong(authentication.getName());
        UserInfoResDto res = userService.deleteProfileImage(userId);
        return BaseResponse.onSuccess(SuccessStatus.USER_PROFILE_IMAGE_DELETE_SUCCESS, res);
    }

    // 회원 탈퇴
    @DeleteMapping("/profile")
    @Operation(
            summary = "회원 탈퇴",
            description = """
                로그인한 사용자의 회원 탈퇴를 진행합니다.

                - Authorization Header에 Bearer Access Token이 존재해야 합니다.
                - 사용자 정보 및 Refresh Token이 삭제됩니다.
                - 프로필 이미지가 존재할 경우 S3에서도 함께 삭제됩니다.
                - 탈퇴 후 재로그인은 불가능합니다.
                """
    )
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

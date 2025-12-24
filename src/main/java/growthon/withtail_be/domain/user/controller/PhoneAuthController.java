package growthon.withtail_be.domain.user.controller;

import growthon.withtail_be.domain.user.dto.request.sms.PhoneSendCodeReqDto;
import growthon.withtail_be.domain.user.dto.request.sms.PhoneVerifyCodeReqDto;
import growthon.withtail_be.domain.user.service.PhoneAuthService;
import growthon.withtail_be.global.code.SuccessStatus;
import growthon.withtail_be.global.response.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth/phone")
public class PhoneAuthController {

    private final PhoneAuthService phoneAuthService;

    // 인증번호 발송
    @PostMapping("/send")
    @Operation(
            summary = "휴대폰 인증번호 발송",
            description = "입력한 휴대폰 번호로 6자리 인증번호를 문자(SMS)로 발송합니다."
    )
    public BaseResponse<Void> sendCode(@RequestBody @Valid PhoneSendCodeReqDto req) {
        phoneAuthService.sendVerificationCode(req.phoneNumber());
        return BaseResponse.onSuccess(SuccessStatus.PHONE_CODE_SENT, null);
    }

    // 인증번호 검증
    @PostMapping("/verify")
    @Operation(
            summary = "휴대폰 인증번호 검증",
            description = "휴대폰 번호와 인증번호를 검증하여 휴대폰 인증을 완료합니다."
    )
    public BaseResponse<Void> verifyCode(@RequestBody @Valid PhoneVerifyCodeReqDto req) {
        phoneAuthService.verifyCode(req.phoneNumber(), req.code());
        return BaseResponse.onSuccess(SuccessStatus.PHONE_CODE_VERIFIED, null);
    }
}

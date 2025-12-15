package growthon.withtail_be.domain.user.controller;

import growthon.withtail_be.domain.user.dto.request.sms.PhoneSendCodeReqDto;
import growthon.withtail_be.domain.user.dto.request.sms.PhoneVerifyCodeReqDto;
import growthon.withtail_be.domain.user.service.PhoneAuthService;
import growthon.withtail_be.global.code.SuccessStatus;
import growthon.withtail_be.global.response.BaseResponse;
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
    public BaseResponse<Void> sendCode(@RequestBody @Valid PhoneSendCodeReqDto req) {
        phoneAuthService.sendVerificationCode(req.phoneNumber());
        return BaseResponse.onSuccess(SuccessStatus.PHONE_CODE_SENT, null);
    }

    // 인증번호 검증
    @PostMapping("/verify")
    public BaseResponse<Void> verifyCode(@RequestBody @Valid PhoneVerifyCodeReqDto req) {
        phoneAuthService.verifyCode(req.phoneNumber(), req.code());
        return BaseResponse.onSuccess(SuccessStatus.PHONE_CODE_VERIFIED, null);
    }
}

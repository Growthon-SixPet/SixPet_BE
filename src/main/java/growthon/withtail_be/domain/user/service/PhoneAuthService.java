package growthon.withtail_be.domain.user.service;

import growthon.withtail_be.global.code.ErrorStatus;
import growthon.withtail_be.global.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class PhoneAuthService {

    private final StringRedisTemplate redisTemplate;
    private final SmsService smsService;

    private static final long CODE_TTL_MINUTES = 3;
    private static final long VERIFIED_TTL_MINUTES = 10;

    // 인증번호 발송
    public void sendVerificationCode(String phoneNumber) {

        // 인증번호 생성 (보안 랜덤)
        String code = generateCode();

        // Redis 저장 (auth:signup:{phone})
        String codeKey = getCodeKey(phoneNumber);
        redisTemplate.opsForValue()
                .set(codeKey, code, CODE_TTL_MINUTES, TimeUnit.MINUTES);

        // SMS 발송 (실패 시 SmsService에서 예외 발생)
        smsService.sendVerificationCode(phoneNumber, code);
    }

    // 인증번호 검증
    public void verifyCode(String phoneNumber, String inputCode) {

        String codeKey = getCodeKey(phoneNumber);
        String savedCode = redisTemplate.opsForValue().get(codeKey);

        if (savedCode == null) {
            throw new GeneralException(ErrorStatus.SMS_CODE_NOT_FOUND);
        }

        if (!savedCode.equals(inputCode)) {
            throw new GeneralException(ErrorStatus.SMS_CODE_MISMATCH);
        }

        // 인증 성공 → 기존 인증번호 삭제
        redisTemplate.delete(codeKey);

        // 인증 완료 상태 저장 (auth:verified:{phone})
        redisTemplate.opsForValue()
                .set(getVerifiedKey(phoneNumber), "true",
                        VERIFIED_TTL_MINUTES, TimeUnit.MINUTES);
    }

    // 정리
    public void clearVerifiedPhone(String phoneNumber) {
        redisTemplate.delete(getVerifiedKey(phoneNumber));
    }

    // 회원가입 전 인증 완료 여부 검증
    public void validateVerifiedPhone(String phoneNumber) {
        String verified = redisTemplate.opsForValue().get(getVerifiedKey(phoneNumber));

        if (verified == null) {
            throw new GeneralException(ErrorStatus.SMS_NOT_VERIFIED);
        }
    }


    private String generateCode() {
        SecureRandom random = new SecureRandom();
        int number = random.nextInt(900_000) + 100_000;
        return String.valueOf(number);
    }

    private String getCodeKey(String phoneNumber) {
        return "auth:signup:" + phoneNumber;
    }

    private String getVerifiedKey(String phoneNumber) {
        return "auth:verified:" + phoneNumber;
    }
}


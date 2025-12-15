package growthon.withtail_be.domain.user.service;

import growthon.withtail_be.global.code.ErrorStatus;
import growthon.withtail_be.global.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.nurigo.sdk.message.exception.NurigoMessageNotReceivedException;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SmsService {

    private final DefaultMessageService messageService;

    @Value("${coolsms.api.from}")
    private String from;

    // sms 인증번호 발송
    public void sendVerificationCode(String to, String code) {
        Message message = new Message();
        message.setFrom(from);
        message.setTo(to);
        message.setText("[WithTail] 인증번호는 " + code + " 입니다. (3분 이내 입력)");

        try {
            messageService.send(message);
            log.info("SMS 인증번호 발송 성공 - to={}", to);

        } catch (NurigoMessageNotReceivedException e) {
            log.error("SMS 발송 실패 (Nurigo) - to={}, error={}", to, e.getMessage());
            throw new GeneralException(ErrorStatus.SMS_SEND_FAILED);

        } catch (Exception e) {
            log.error("SMS 발송 중 알 수 없는 오류 - to={}", to, e);
            throw new GeneralException(ErrorStatus.SMS_SEND_FAILED);
        }
    }
}

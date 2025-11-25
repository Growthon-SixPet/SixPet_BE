package growthon.withtail_be.global.exception;

import growthon.withtail_be.global.code.BaseCode;
import growthon.withtail_be.global.code.ErrorStatus;
import growthon.withtail_be.global.response.BaseResponse;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice(annotations = {RestController.class})
public class GlobalExceptionAdvice extends ResponseEntityExceptionHandler {
    // 파라미터 타입 잘못 된 경우
    @Override
    protected ResponseEntity<Object> handleTypeMismatch(
            TypeMismatchException e,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {

        return buildErrorResponse(
                ErrorStatus.BAD_REQUEST,
                e.getPropertyName() + ": 올바른 값이 아닙니다."
        );
    }

    // 필수 파라미터 누락
    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(
            MissingServletRequestParameterException e,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {

        return buildErrorResponse(
                ErrorStatus.BAD_REQUEST,
                e.getParameterName() + ": 필수 요청 파라미터입니다."
        );
    }

    // @RequestParam, @PathVariable 등 유효성 검증 실패
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolationException(
            ConstraintViolationException e) {

        String message = e.getConstraintViolations()
                .stream()
                .map(v -> v.getPropertyPath() + ": " + v.getMessage())
                .findFirst()
                .orElse("유효성 검증 오류가 발생했습니다.");

        return buildErrorResponse(
                ErrorStatus.BAD_REQUEST,
                message
        );
    }

    // @Valid 유효성 검사 실패 (DTO 필드 오류 처리)
    @Override
    public ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException e,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {

        Map<String, String> errors = new LinkedHashMap<>();

        e.getBindingResult().getFieldErrors().forEach(fieldError -> {
            String fieldName = fieldError.getField();
            String message = fieldError.getDefaultMessage();

            errors.merge(
                    fieldName,
                    message,
                    (oldMsg, newMsg) -> oldMsg + ", " + newMsg
            );
        });

        return buildErrorResponse(
                ErrorStatus.BAD_REQUEST,
                errors.toString()
        );
    }

    // 커스텀 비즈니스 예외
    @ExceptionHandler(GeneralException.class)
    public ResponseEntity<Object> handleGeneralException(
            GeneralException e,
            HttpServletRequest request) {

        return buildErrorResponse(e.getCode());
    }

    // 정의되지 않은 예외 전체 처리 (catch-all)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleUnhandledException(
            Exception e,
            HttpServletRequest request) {

        log.error("정의되지 않은 예외입니다.", e);
        return buildErrorResponse(ErrorStatus.INTERNAL_SERVER_ERROR);
    }

    // 공통 응답 빌더
    private ResponseEntity<Object> buildErrorResponse(BaseCode code) {
        return ResponseEntity
                .status(code.getReasonHttpStatus().getHttpStatus())
                .body(BaseResponse.onFailure(code, null));
    }

    private ResponseEntity<Object> buildErrorResponse(BaseCode code, String overrideMessage) {
        return ResponseEntity
                .status(code.getReasonHttpStatus().getHttpStatus())
                .body(new BaseResponse<>(
                        false,
                        code.getCode(),
                        overrideMessage,
                        null
                ));
    }
}

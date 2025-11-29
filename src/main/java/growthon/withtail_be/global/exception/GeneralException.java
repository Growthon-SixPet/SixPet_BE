package growthon.withtail_be.global.exception;

import growthon.withtail_be.global.code.BaseCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GeneralException extends RuntimeException {
    private BaseCode code;
    private String message;

    public GeneralException(BaseCode code, Throwable cause) {
        super(code.getReasonHttpStatus().getMessage(), cause);
        this.code = code;
    }
}

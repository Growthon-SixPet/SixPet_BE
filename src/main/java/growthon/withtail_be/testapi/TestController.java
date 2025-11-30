package growthon.withtail_be.testapi;

import growthon.withtail_be.global.code.SuccessStatus;
import growthon.withtail_be.global.response.BaseResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/test/hello")
    public BaseResponse<String> hello() {
        return BaseResponse.onSuccess(
                SuccessStatus.OK,
                "안녕하세요!"
        );
    }
}

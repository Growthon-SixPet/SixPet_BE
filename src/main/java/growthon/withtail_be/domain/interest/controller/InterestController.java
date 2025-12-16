package growthon.withtail_be.domain.interest.controller;

import growthon.withtail_be.domain.interest.dto.InterestReqDto;
import growthon.withtail_be.domain.interest.dto.InterestResDto;
import growthon.withtail_be.domain.interest.service.InterestService;
import growthon.withtail_be.global.code.SuccessStatus;
import growthon.withtail_be.global.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/interests")
public class InterestController {

    private final InterestService interestService;

    // 즐겨찾기 생성
    @PostMapping
    public BaseResponse<InterestResDto> postInterest(
            Authentication authentication,
            @RequestBody InterestReqDto dto
    ) {
        Long userId = Long.parseLong(authentication.getName());
        InterestResDto res = interestService.postInterest(dto, userId);
        return BaseResponse.onSuccess(SuccessStatus.INTEREST_CREATE_SUCCESS, res);
    }

    // 유저별 즐겨찾기 조회
    @GetMapping("/user")
    public BaseResponse<List<InterestResDto>> getMyInterests(Authentication authentication) {
        Long userId = Long.parseLong(authentication.getName());
        List<InterestResDto> res = interestService.findInterestsByUserId(userId);
        return BaseResponse.onSuccess(SuccessStatus.INTEREST_LIST_GET_SUCCESS, res);
    }

    // 즐겨찾기 단건 조회
    @GetMapping("/{interest-id}")
    public BaseResponse<InterestResDto> getInterestById(
            Authentication authentication,
            @PathVariable("interest-id") Long interestId
    ) {
        Long userId = Long.parseLong(authentication.getName());
        InterestResDto res = interestService.findInterestById(interestId, userId);
        return BaseResponse.onSuccess(SuccessStatus.INTEREST_GET_SUCCESS, res);
    }

    // 즐겨찾기 삭제
    @DeleteMapping("/{interest-id}")
    public BaseResponse<Void> deleteInterest(
            Authentication authentication,
            @PathVariable("interest-id") Long interestId
    ) {
        Long userId = Long.parseLong(authentication.getName());
        interestService.deleteInterest(interestId, userId);
        return BaseResponse.onSuccess(SuccessStatus.INTEREST_DELETE_SUCCESS, null);
    }

}

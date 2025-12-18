package growthon.withtail_be.domain.interest.controller;

import growthon.withtail_be.domain.interest.dto.InterestReqDto;
import growthon.withtail_be.domain.interest.dto.InterestResDto;
import growthon.withtail_be.domain.interest.service.InterestService;
import growthon.withtail_be.global.code.SuccessStatus;
import growthon.withtail_be.global.response.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
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
    @Operation(
            summary = "즐겨찾기 신규 생성",
            description = "즐겨찾기를 생성합니다."
    )
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
    @Operation(
            summary = "유저 id를 받아서 유저별 즐겨찾기 목록 조회",
            description = "유저 id를 받아서 유저별로 즐겨찾기 목록을 조회합니다."
    )
    public BaseResponse<List<InterestResDto>> getMyInterests(Authentication authentication) {
        Long userId = Long.parseLong(authentication.getName());
        List<InterestResDto> res = interestService.findInterestsByUserId(userId);
        return BaseResponse.onSuccess(SuccessStatus.INTEREST_LIST_GET_SUCCESS, res);
    }

    // 즐겨찾기 단건 조회
    @GetMapping("/{interest-id}")
    @Operation(
            summary = "즐겨찾기 단건 조회",
            description = "즐겨찾기 id를 받아서 해당 즐겨찾기를 조회합니다."
    )
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
    @Operation(
            summary = "즐겨찾기 삭제",
            description = "즐겨찾기 id를 받아서 해당 즐겨찾기를 삭제합니다."
    )
    public BaseResponse<Void> deleteInterest(
            Authentication authentication,
            @PathVariable("interest-id") Long interestId
    ) {
        Long userId = Long.parseLong(authentication.getName());
        interestService.deleteInterest(interestId, userId);
        return BaseResponse.onSuccess(SuccessStatus.INTEREST_DELETE_SUCCESS, null);
    }

}

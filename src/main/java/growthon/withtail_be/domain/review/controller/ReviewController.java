package growthon.withtail_be.domain.review.controller;

import growthon.withtail_be.domain.review.dto.ReviewCreateReqDto;
import growthon.withtail_be.domain.review.dto.ReviewResDto;
import growthon.withtail_be.domain.review.dto.ReviewUpdateReqDto;
import growthon.withtail_be.domain.review.entity.TargetType;
import growthon.withtail_be.domain.review.service.ReviewService;
import growthon.withtail_be.global.code.SuccessStatus;
import growthon.withtail_be.global.response.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    // 후기 생성 (이미지 1장 optional)
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(
            summary = "후기 생성",
            description = """
                    병원 또는 장례식장에 대한 후기를 작성합니다.
                    이미지 파일은 선택 사항이며, 한 장만 업로드할 수 있습니다.

                    ※ Authorization Header에 Bearer Access Token이 존재해야 합니다.
                    """
    )
    public BaseResponse<ReviewResDto> createReview(
            Authentication authentication,
            @Valid @RequestPart("req") ReviewCreateReqDto req,
            @RequestPart(value = "image", required = false) MultipartFile image
    ) {
        Long userId = Long.parseLong(authentication.getName());
        ReviewResDto res = reviewService.createReview(userId, req, image);
        return BaseResponse.onSuccess(SuccessStatus.REVIEW_CREATE_SUCCESS, res);
    }

    // 병원/장례별 후기 목록 조회
    @GetMapping
    @Operation(
            summary = "병원/장례식장별 후기 목록 조회",
            description = """
                    특정 병원 또는 장례식장에 작성된 후기 목록을 조회합니다.
                    최신 작성 순으로 정렬됩니다.

                    ※ Authorization Header에 Bearer Access Token이 존재해야 합니다.
                    """
    )
    public BaseResponse<List<ReviewResDto>> getReviewsByTarget(
            Authentication authentication,
            @RequestParam TargetType targetType,
            @RequestParam Long targetId
    ) {
        Long userId = Long.parseLong(authentication.getName());
        List<ReviewResDto> res = reviewService.findByTarget(targetType, targetId, userId);
        return BaseResponse.onSuccess(SuccessStatus.REVIEW_LIST_GET_SUCCESS, res);
    }

    // 유저별(내) 후기 목록 조회
    @GetMapping("/user")
    @Operation(
            summary = "내 후기 목록 조회",
            description = """
                    로그인한 사용자가 작성한 후기 목록을 조회합니다.
                    마이페이지에서 사용하는 API입니다.

                    ※ Authorization Header에 Bearer Access Token이 존재해야 합니다.
                    """
    )
    public BaseResponse<List<ReviewResDto>> getMyReviews(Authentication authentication) {
        Long userId = Long.parseLong(authentication.getName());
        List<ReviewResDto> res = reviewService.findMyReviews(userId);
        return BaseResponse.onSuccess(SuccessStatus.REVIEW_MY_LIST_GET_SUCCESS, res);
    }

    // 후기 수정 (이미지 교체 optional)
    @PatchMapping(value = "/{review-id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(
            summary = "후기 수정",
            description = """
                    본인이 작성한 후기를 수정합니다.
                    이미지 파일은 선택 사항이며, 전달 시 기존 이미지가 교체됩니다.

                    ※ Authorization Header에 Bearer Access Token이 존재해야 합니다.
                    """
    )
    public BaseResponse<ReviewResDto> updateReview(
            Authentication authentication,
            @PathVariable("review-id") Long reviewId,
            @Valid @RequestPart("req") ReviewUpdateReqDto req,
            @RequestPart(value = "image", required = false) MultipartFile image
    ) {
        Long userId = Long.parseLong(authentication.getName());
        ReviewResDto res = reviewService.updateReview(reviewId, userId, req, image);
        return BaseResponse.onSuccess(SuccessStatus.REVIEW_UPDATE_SUCCESS, res);
    }

    // 후기 삭제
    @DeleteMapping("/{review-id}")
    @Operation(
            summary = "후기 삭제",
            description = """
                    본인이 작성한 후기를 삭제합니다.
                    후기에 이미지가 존재하는 경우, S3에서도 함께 삭제됩니다.

                    ※ Authorization Header에 Bearer Access Token이 존재해야 합니다.
                    """
    )
    public BaseResponse<Void> deleteReview(
            Authentication authentication,
            @PathVariable("review-id") Long reviewId
    ) {
        Long userId = Long.parseLong(authentication.getName());
        reviewService.deleteReview(reviewId, userId);
        return BaseResponse.onSuccess(SuccessStatus.REVIEW_DELETE_SUCCESS, null);
    }

    // 평균 평점 조회
    @GetMapping("/avg")
    @Operation(
            summary = "병원/장례식장 평균 평점 조회",
            description = """
                    특정 병원 또는 장례식장에 대한 평균 평점을 조회합니다.
                    작성된 후기가 없는 경우 0을 반환합니다.
                    """
    )
    public BaseResponse<Double> getAverageRating(
            @RequestParam TargetType targetType,
            @RequestParam Long targetId
    ) {
        double avg = reviewService.getAverageRating(targetType, targetId);
        return BaseResponse.onSuccess(SuccessStatus.REVIEW_AVG_GET_SUCCESS, avg);
    }
}

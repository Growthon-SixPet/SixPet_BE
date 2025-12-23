package growthon.withtail_be.domain.review.service;

import growthon.withtail_be.domain.interest.repository.FuneralRepository;
import growthon.withtail_be.domain.interest.repository.HospitalRepository;
import growthon.withtail_be.domain.review.dto.ReviewCreateReqDto;
import growthon.withtail_be.domain.review.dto.ReviewResDto;
import growthon.withtail_be.domain.review.dto.ReviewUpdateReqDto;
import growthon.withtail_be.domain.review.entity.Review;
import growthon.withtail_be.domain.review.entity.TargetType;
import growthon.withtail_be.domain.review.repository.ReviewRepository;
import growthon.withtail_be.domain.user.entity.User;
import growthon.withtail_be.domain.user.repository.UserRepository;
import growthon.withtail_be.global.code.ErrorStatus;
import growthon.withtail_be.global.exception.GeneralException;
import growthon.withtail_be.global.service.S3Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final S3Service s3Service;
    private final HospitalRepository hospitalRepository;
    private final FuneralRepository funeralRepository;

    // 후기 생성
    public ReviewResDto createReview(
            Long userId,
            ReviewCreateReqDto req,
            MultipartFile image
    ) {
        User user = findUserOrThrow(userId);

        validateRating(req.getRating());
        validateTarget(req.getTargetType(), req.getTargetId());

        String imageUrl = null;
        if (image != null && !image.isEmpty()) {
            imageUrl = s3Service.upload(image, "reviews");
        }

        Review review = Review.builder()
                .user(user)
                .targetType(req.getTargetType())
                .targetId(req.getTargetId())
                .rating(req.getRating())
                .content(req.getContent())
                .imageUrl(imageUrl)
                .build();

        Review savedReview = reviewRepository.save(review);

        return ReviewResDto.from(savedReview, user.getId());
    }

    // 병원/장례별 후기 목록 조회
    @Transactional(readOnly = true)
    public List<ReviewResDto> findByTarget(
            TargetType targetType,
            Long targetId,
            Long userId
    ) {
        List<Review> reviews =
                reviewRepository.findByTargetTypeAndTargetIdOrderByCreatedAtDesc(
                        targetType, targetId
                );

        return reviews.stream()
                .map(review -> ReviewResDto.from(review, userId))
                .toList();
    }

    // 유저별 후기 목록 조회 (마이페이지용)
    @Transactional(readOnly = true)
    public List<ReviewResDto> findMyReviews(Long userId) {
        findUserOrThrow(userId);

        List<Review> reviews =
                reviewRepository.findByUserIdOrderByCreatedAtDesc(userId);

        return reviews.stream()
                .map(review -> ReviewResDto.from(review, userId))
                .toList();
    }

    // 후기 수정
    public ReviewResDto updateReview(
            Long reviewId,
            Long userId,
            ReviewUpdateReqDto req,
            MultipartFile newImage
    ) {
        Review review = findReviewOrThrow(reviewId);

        validateOwner(review, userId);

        validateRating(req.getRating());

        if (newImage != null && !newImage.isEmpty()) {
            String replacedUrl = s3Service.replace(newImage, "reviews", review.getImageUrl());
            review.updateImageUrl(replacedUrl);
        }

        review.update(req.getRating(), req.getContent());

        return ReviewResDto.from(review, userId);
    }

    // 후기 삭제
    public void deleteReview(Long reviewId, Long userId) {
        Review review = findReviewOrThrow(reviewId);

        validateOwner(review, userId);

        String imageUrl = review.getImageUrl();
        if (imageUrl != null && !imageUrl.isBlank()) {
            try {
                s3Service.deleteByUrl(imageUrl);
            } catch (Exception e) {
                log.warn("S3 이미지 삭제 실패. reviewId={}, url={}", reviewId, imageUrl, e);
            }
        }

        reviewRepository.delete(review);
    }

    // 평균 평점 계산
    @Transactional(readOnly = true)
    public double getAverageRating(TargetType targetType, Long targetId) {
        return reviewRepository.findAverageRating(targetType, targetId);
    }


    // helpers
    private User findUserOrThrow(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));
    }

    private void validateTarget(TargetType targetType, Long targetId) {
        boolean exists = switch (targetType) {
            case HOSPITAL -> hospitalRepository.existsById(targetId);
            case FUNERAL -> funeralRepository.existsById(targetId);
        };

        if (!exists) {
            throw new GeneralException(ErrorStatus.REVIEW_TARGET_NOT_FOUND);
        }
    }

    private void validateRating(Integer rating) {
        if (rating == null || rating < 1 || rating > 5) {
            throw new GeneralException(ErrorStatus.REVIEW_RATING_INVALID);
        }
    }

    private Review findReviewOrThrow(Long reviewId) {
        return reviewRepository.findById(reviewId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.REVIEW_NOT_FOUND));
    }

    private void validateOwner(Review review, Long userId) {
        if (!review.getUser().getId().equals(userId)) {
            throw new GeneralException(ErrorStatus.REVIEW_ACCESS_DENIED);
        }
    }

}

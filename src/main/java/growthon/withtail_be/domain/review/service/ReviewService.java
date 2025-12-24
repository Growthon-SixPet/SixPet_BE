package growthon.withtail_be.domain.review.service;

import growthon.withtail_be.domain.animalfuneral.repository.AnimalFuneralRepository;
import growthon.withtail_be.domain.animalhospital.repository.AnimalHospitalRepository;
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
    private final AnimalHospitalRepository hospitalRepository;
    private final AnimalFuneralRepository funeralRepository;

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

        Review savedReview = reviewRepository.save(
                Review.builder()
                        .user(user)
                        .targetType(req.getTargetType())
                        .targetId(req.getTargetId())
                        .rating(req.getRating())
                        .content(req.getContent())
                        .imageUrl(imageUrl)
                        .build()
        );

        refreshTargetReviewStats(req.getTargetType(), req.getTargetId());

        String targetName = getTargetName(req.getTargetType(), req.getTargetId());
        return ReviewResDto.from(savedReview, userId, targetName);
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

        String targetName = getTargetName(targetType, targetId);

        return reviews.stream()
                .map(r -> ReviewResDto.from(r, userId, targetName))
                .toList();
    }

    // 유저별 후기 목록 조회 (마이페이지용)
    @Transactional(readOnly = true)
    public List<ReviewResDto> findMyReviews(Long userId) {
        findUserOrThrow(userId);

        List<Review> reviews =
                reviewRepository.findByUserIdOrderByCreatedAtDesc(userId);

        return reviews.stream()
                .map(r -> ReviewResDto.from(r, userId, getTargetName(r.getTargetType(), r.getTargetId())))
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

        refreshTargetReviewStats(review.getTargetType(), review.getTargetId());

        String targetName = getTargetName(review.getTargetType(), review.getTargetId());
        return ReviewResDto.from(review, userId, targetName);
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

        TargetType targetType = review.getTargetType();
        Long targetId = review.getTargetId();

        reviewRepository.delete(review);

        refreshTargetReviewStats(targetType, targetId);
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

    private String getTargetName(TargetType targetType, Long targetId) {
        return switch (targetType) {
            case HOSPITAL -> hospitalRepository.findById(targetId)
                    .map(h -> h.getName())
                    .orElseThrow(() -> new GeneralException(ErrorStatus.REVIEW_TARGET_NOT_FOUND));
            case FUNERAL -> funeralRepository.findById(targetId)
                    .map(f -> f.getName())
                    .orElseThrow(() -> new GeneralException(ErrorStatus.REVIEW_TARGET_NOT_FOUND));
        };
    }

    private void refreshTargetReviewStats(TargetType targetType, Long targetId) {
        double avg = reviewRepository.findAverageRating(targetType, targetId); // coalesce로 0 보장
        long cntLong = reviewRepository.countByTarget(targetType, targetId);
        int cnt = Math.toIntExact(cntLong); // int 범위 넘어가면 예외(현실상 거의 없음)

        switch (targetType) {
            case HOSPITAL -> {
                var hospital = hospitalRepository.findById(targetId)
                        .orElseThrow(() -> new GeneralException(ErrorStatus.REVIEW_TARGET_NOT_FOUND));
                hospital.updateReviewStats(avg, cnt);
            }
            case FUNERAL -> {
                var funeral = funeralRepository.findById(targetId)
                        .orElseThrow(() -> new GeneralException(ErrorStatus.REVIEW_TARGET_NOT_FOUND));
                funeral.updateReviewStats(avg, cnt);
            }
        }
    }

}

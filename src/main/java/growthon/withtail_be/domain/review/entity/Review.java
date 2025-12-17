package growthon.withtail_be.domain.review.entity;

import growthon.withtail_be.domain.model.BaseEntity;
import growthon.withtail_be.domain.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

// 병원/장례 공통 자체 후기 엔티티
@Entity
@Getter
@Table(
        name = "review",
        indexes = {
                @Index(name = "idx_review_target", columnList = "targetType, targetId"),
                @Index(name = "idx_review_user", columnList = "user_id")
        }
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Review extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // 후기 대상 타입 (HOSPITAL / FUNERAL)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TargetType targetType;

    // 병원 ID 또는 장례 ID
    @Column(nullable = false)
    private Long targetId;

    // 평점 (1~5)
    @Column(nullable = false)
    private Integer rating;
    
    // 후기 내용
    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(length = 500)
    private String imageUrl;

    @Builder
    private Review(User user, TargetType targetType, Long targetId,
                   Integer rating, String content, String imageUrl) {
        this.user = user;
        this.targetType = targetType;
        this.targetId = targetId;
        this.rating = rating;
        this.content = content;
        this.imageUrl = imageUrl;
    }

    public void update(Integer rating, String content) {
        this.rating = rating;
        this.content = content;
    }

    public void updateImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
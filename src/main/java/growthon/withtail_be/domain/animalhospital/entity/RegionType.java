package growthon.withtail_be.domain.animalhospital.entity;

import lombok.Getter;

@Getter
public enum RegionType {

    SEOUL("서울"),
    INCHEON_GYEONGGI("인천·경기"),
    GANGWON("강원"),
    CHUNGCHEONG("충청"),
    JEOLLA("전라"),
    GYEONGSANG("경상"),
    JEJU("제주");

    private final String displayName;

    RegionType(String displayName) {
        this.displayName = displayName;
    }
}

package growthon.withtail_be.domain.reservation.domain;

public enum ReservationStatus {
    BEFORE_VISIT,   // 방문 전 (기본값)
    COMPLETED,      // 방문 완료
    CANCELED,       // 예약 취소
    NO_SHOW         // 미방문
}

package growthon.withtail_be.domain.reservation.controller;

import growthon.withtail_be.domain.reservation.domain.TargetType;
import growthon.withtail_be.domain.reservation.dto.ReservationReqDto;
import growthon.withtail_be.domain.reservation.dto.ReservationResDto;
import growthon.withtail_be.domain.reservation.service.ReservationService;
import growthon.withtail_be.global.code.SuccessStatus;
import growthon.withtail_be.global.response.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    // 예약 생성
    @PostMapping
    @Operation(
            summary = "신규 예약 생성",
            description = "query param으로 targetType/targetId(병원/장례 대상) + body로 보호자/반려동물/방문 정보를 받아 신규 예약을 생성합니다."
    )
    public BaseResponse<ReservationResDto> postReservation(
            Authentication authentication,
            @RequestParam("targetType") TargetType targetType,
            @RequestParam("targetId") Long targetId,
            @Valid @RequestBody ReservationReqDto dto
    ) {
        Long userId = Long.parseLong(authentication.getName());
        ReservationResDto res = reservationService.postReservation(userId, targetType, targetId, dto);
        return BaseResponse.onSuccess(SuccessStatus.RESERVATION_CREATE_SUCCESS, res);
    }

    // 유저별 예약 조회
    @GetMapping("/user")
    @Operation(
            summary = "유저별 예약 조회",
            description = "유저 id를 받아서 해당 유저가 생성한 예약 목록을 조회합니다."
    )
    public BaseResponse<List<ReservationResDto>> getMyReservations(
            Authentication authentication
    ) {
        Long userId = Long.parseLong(authentication.getName());
        List<ReservationResDto> res = reservationService.findByUserId(userId);
        return BaseResponse.onSuccess(SuccessStatus.RESERVATION_LIST_GET_SUCCESS, res);
    }

    // 예약 단건 조회
    @GetMapping("/{reservation-id}")
    @Operation(
            summary = "예약 단건 조회",
            description = "예약 id를 받아서 해당 예약을 조회합니다."
    )
    public BaseResponse<ReservationResDto> getReservation(
            Authentication authentication,
            @PathVariable("reservation-id") Long reservationId
    ) {
        Long userId = Long.parseLong(authentication.getName());
        ReservationResDto res = reservationService.getReservation(reservationId, userId);
        return BaseResponse.onSuccess(SuccessStatus.RESERVATION_GET_SUCCESS, res);
    }

    // 예약 수정
    @PatchMapping("/{reservation-id}")
    @Operation(
            summary = "예약 수정",
            description = "예약 id과 수정할 정보를 받아서 해당 예약의 정보를 수정합니다."
    )
    public BaseResponse<ReservationResDto> updateReservation(
            Authentication authentication,
            @PathVariable("reservation-id") Long reservationId,
            @Valid @RequestBody ReservationReqDto dto
    ) {
        Long userId = Long.parseLong(authentication.getName());
        ReservationResDto res = reservationService.updateReservation(reservationId, userId, dto);
        return BaseResponse.onSuccess(SuccessStatus.RESERVATION_UPDATE_SUCCESS, res);
    }

    // 예약 취소
    @DeleteMapping("/{reservation-id}")
    @Operation(
            summary = "예약 취소",
            description = "예약 id를 받아서 해당 예약을 삭제합니다."
    )
    public BaseResponse<ReservationResDto> cancelReservation(
            Authentication authentication,
            @PathVariable("reservation-id") Long reservationId
    ) {
        Long userId = Long.parseLong(authentication.getName());
        ReservationResDto res = reservationService.cancelReservation(reservationId, userId);
        return BaseResponse.onSuccess(SuccessStatus.RESERVATION_CANCEL_SUCCESS, res);
    }
}

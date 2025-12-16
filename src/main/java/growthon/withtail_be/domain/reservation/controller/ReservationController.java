package growthon.withtail_be.domain.reservation.controller;

import growthon.withtail_be.domain.reservation.dto.ReservationReqDto;
import growthon.withtail_be.domain.reservation.dto.ReservationResDto;
import growthon.withtail_be.domain.reservation.service.ReservationService;
import growthon.withtail_be.global.code.SuccessStatus;
import growthon.withtail_be.global.response.BaseResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    // 예약 생성
    @PostMapping
    public BaseResponse<ReservationResDto> postReservation(
            Authentication authentication,
            @Valid @RequestBody ReservationReqDto dto
    ) {
        Long userId = Long.parseLong(authentication.getName());
        ReservationResDto res = reservationService.postReservation(userId, dto);
        return BaseResponse.onSuccess(SuccessStatus.RESERVATION_CREATE_SUCCESS, res);
    }

    // 유저별 예약 조회
    @GetMapping("/user")
    public BaseResponse<List<ReservationResDto>> getMyReservations(
            Authentication authentication
    ) {
        Long userId = Long.parseLong(authentication.getName());
        List<ReservationResDto> res = reservationService.findByUserId(userId);
        return BaseResponse.onSuccess(SuccessStatus.RESERVATION_LIST_GET_SUCCESS, res);
    }

    // 예약 단건 조회
    @GetMapping("/{reservation-id}")
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
    public BaseResponse<ReservationResDto> cancelReservation(
            Authentication authentication,
            @PathVariable("reservation-id") Long reservationId
    ) {
        Long userId = Long.parseLong(authentication.getName());
        ReservationResDto res = reservationService.cancelReservation(reservationId, userId);
        return BaseResponse.onSuccess(SuccessStatus.RESERVATION_CANCEL_SUCCESS, res);
    }
}

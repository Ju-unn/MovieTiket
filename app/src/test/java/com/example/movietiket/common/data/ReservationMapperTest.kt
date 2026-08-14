package com.example.movietiket.common.data

import com.example.movietiket.common.model.reservation.HeadCount
import com.example.movietiket.common.model.reservation.Reservation
import com.example.movietiket.common.repository.MovieRepository
import com.example.movietiket.common.repository.TheaterRepository
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatIllegalArgumentException
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class ReservationMapperTest {

    private fun reservation(): Reservation = Reservation.of(
        MovieRepository.findById(0),
        TheaterRepository.findById(2),
    ).increaseHeadCount().selectSeat("B3").selectSeat("A1")

    @Test
    @DisplayName("예매를 테이블 행으로 바꾸면 좌석은 정렬된 문자열이 된다")
    fun toEntity() {
        val entity = reservation().toEntity(reservedAt = 1_700_000_000_000L)

        assertThat(entity.movieId).isEqualTo(0)
        assertThat(entity.theaterId).isEqualTo(2)
        assertThat(entity.headCount).isEqualTo(2)
        assertThat(entity.seats).isEqualTo("A1,B3")
        assertThat(entity.reservedAt).isEqualTo(1_700_000_000_000L)
    }

    @Test
    @DisplayName("테이블 행을 되돌리면 원래 예매 내용이 복원된다")
    fun toReservationHistory() {
        val original = reservation()

        val restored = original.toEntity(reservedAt = 0L).copy(id = 7L).toReservationHistory()

        assertThat(restored.id()).isEqualTo(7L)
        assertThat(restored.displayMovieTitle()).isEqualTo(original.displayMovieTitle())
        assertThat(restored.displayTheaterName()).isEqualTo("너무너무너무긴 극장이름을 가진 코엑스점")
        assertThat(restored.reservation().displaySelectedSeats()).isEqualTo("A1, B3")
        assertThat(restored.reservation().displayHeadCount()).isEqualTo("2")
        assertThat(restored.reservation().displaySelectedDate())
            .isEqualTo(original.displaySelectedDate())
        assertThat(restored.reservation().displaySelectedTime())
            .isEqualTo(original.displaySelectedTime())
        assertThat(restored.reservation().totalAmountWon()).isEqualTo(original.totalAmountWon())
    }

    @Test
    @DisplayName("좌석을 고르지 않은 예매도 저장하고 되돌릴 수 있다")
    fun emptySeats() {
        val entity = Reservation.of(MovieRepository.findById(0), TheaterRepository.findById(0))
            .toEntity(reservedAt = 0L)

        assertThat(entity.seats).isEmpty()
        assertThat(entity.toReservationHistory().reservation().displaySelectedSeats()).isEmpty()
    }

    @Test
    @DisplayName("상영 날짜가 없는 예매는 저장할 수 없다")
    fun cannotSaveWithoutScreeningDate() {
        val reservation = Reservation(
            movie = MovieRepository.findById(0),
            headCount = HeadCount.MINIMUM,
            theater = TheaterRepository.findById(0),
        )

        assertThatIllegalArgumentException().isThrownBy { reservation.toEntity(reservedAt = 0L) }
    }
}

package com.example.movietiket.common.model

import com.example.movietiket.common.fixture.testMovie
import com.example.movietiket.common.fixture.testTheater
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatIllegalArgumentException
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.LocalTime

class ReservationTest {

    @Test
    @DisplayName("예매는 최소 인원 1명, 상영 기간의 첫 날짜와 첫 시간으로 시작한다")
    fun startsWithDefaults() {
        val reservation = Reservation.of(testMovie(), testTheater())

        assertThat(reservation.displayHeadCount()).isEqualTo("1")
        assertThat(reservation.displaySelectedDate()).isEqualTo("2024.3.1")
        assertThat(reservation.displaySelectedTime()).isEqualTo("10:00")
    }

    @Test
    @DisplayName("인원을 늘리면 인원이 증가한 새 예매를 반환한다")
    fun increaseHeadCount() {
        val reservation = Reservation.of(testMovie(), testTheater())

        val increased = reservation.increaseHeadCount()

        assertThat(increased.displayHeadCount()).isEqualTo("2")
    }

    @Test
    @DisplayName("인원 1명일 때 줄여도 1명이 유지된다")
    fun decreaseKeepsMinimum() {
        val reservation = Reservation.of(testMovie(), testTheater())

        val decreased = reservation.decreaseHeadCount()

        assertThat(decreased.displayHeadCount()).isEqualTo("1")
    }

    @Test
    @DisplayName("상영 기간 내의 날짜를 선택할 수 있다")
    fun selectDate() {
        val reservation = Reservation.of(testMovie(), testTheater())

        val selected = reservation.selectDate(LocalDate.of(2024, 3, 2))

        assertThat(selected.displaySelectedDate()).isEqualTo("2024.3.2")
    }

    @Test
    @DisplayName("같은 유형(평일/주말)의 날짜로 바꾸면 기존에 선택한 시간을 유지한다")
    fun selectDateKeepsValidTime() {
        val reservation = Reservation.of(testMovie(), testTheater()).selectTime(LocalTime.of(20, 0)) // 평일 20시

        val selected = reservation.selectDate(LocalDate.of(2024, 3, 5)) // 다른 평일(화요일)

        assertThat(selected.displaySelectedTime()).isEqualTo("20:00")
    }

    @Test
    @DisplayName("평일에서 주말로 날짜를 바꿔 기존 시간이 유효하지 않으면 그 날짜의 첫 시간으로 초기화된다")
    fun selectDateResetsInvalidTime() {
        val reservation = Reservation.of(testMovie(), testTheater()) // 평일 10:00

        val selected = reservation.selectDate(LocalDate.of(2024, 3, 2)) // 주말(토요일), 09:00부터 시작

        assertThat(selected.displaySelectedTime()).isEqualTo("09:00")
    }

    @Test
    @DisplayName("상영 기간 밖의 날짜는 선택할 수 없다")
    fun cannotSelectDateOutsidePeriod() {
        val reservation = Reservation.of(testMovie(), testTheater())

        assertThatIllegalArgumentException()
            .isThrownBy { reservation.selectDate(LocalDate.of(2024, 4, 1)) }
    }

    @Test
    @DisplayName("선택한 날짜에 상영하지 않는 시간은 선택할 수 없다")
    fun cannotSelectUnavailableTime() {
        val reservation = Reservation.of(testMovie(), testTheater()) // 평일은 10시부터 상영

        assertThatIllegalArgumentException()
            .isThrownBy { reservation.selectTime(LocalTime.of(9, 0)) }
    }

    @Test
    @DisplayName("날짜를 선택하지 않은 상태에서 시간을 선택하면 예외가 발생한다")
    fun cannotSelectTimeWithoutDate() {
        val reservation = Reservation(testMovie(), HeadCount.MINIMUM, testTheater())

        assertThatIllegalArgumentException()
            .isThrownBy { reservation.selectTime(LocalTime.of(10, 0)) }
    }

    @Test
    @DisplayName("예매에서 영화 제목을 표시용 값으로 조회할 수 있다")
    fun displayMovieTitle() {
        val reservation = Reservation.of(testMovie(), testTheater())

        assertThat(reservation.displayMovieTitle()).isEqualTo("해리 포터와 마법사의 돌")
    }

    @Test
    @DisplayName("예매에서 영화 소개를 표시용 값으로 조회할 수 있다")
    fun displaySynopsis() {
        val reservation = Reservation.of(testMovie(), testTheater())

        assertThat(reservation.displaySynopsis()).isEqualTo("소개")
    }

    @Test
    @DisplayName("예매에서 상영 기간을 표시용 값으로 조회할 수 있다")
    fun displayScreeningPeriod() {
        val reservation = Reservation.of(testMovie(), testTheater())

        assertThat(reservation.displayScreeningPeriod()).isEqualTo("2024.3.1 ~ 2024.3.28")
    }

    @Test
    @DisplayName("예매에서 러닝타임을 분 단위로 조회할 수 있다")
    fun runningTimeMinutes() {
        val reservation = Reservation.of(testMovie(), testTheater())

        assertThat(reservation.runningTimeMinutes()).isEqualTo(152)
    }

    @Test
    @DisplayName("인원수만큼 좌석을 선택하면 좌석 선택이 완료된다")
    fun selectSeatsUpToHeadCount() {
        val reservation = Reservation.of(testMovie(), testTheater())
            .increaseHeadCount() // 2명
            .selectSeat("A1")
            .selectSeat("A2")

        assertThat(reservation.displaySelectedSeats()).isEqualTo("A1, A2")
        assertThat(reservation.isSeatSelectionComplete()).isTrue()
    }

    @Test
    @DisplayName("인원수보다 많은 좌석은 선택할 수 없다")
    fun cannotSelectMoreSeatsThanHeadCount() {
        val reservation = Reservation.of(testMovie(), testTheater()).selectSeat("A1") // 1명, 1석 선택 완료

        assertThatIllegalArgumentException()
            .isThrownBy { reservation.selectSeat("A2") }
    }

    @Test
    @DisplayName("좌석 선택을 취소할 수 있다")
    fun deselectSeat() {
        val reservation = Reservation.of(testMovie(), testTheater()).selectSeat("A1").deselectSeat("A1")

        assertThat(reservation.displaySelectedSeats()).isEmpty()
        assertThat(reservation.isSeatSelectionComplete()).isFalse()
    }

    @Test
    @DisplayName("좌석 선택 여부를 조회할 수 있다")
    fun isSeatSelected() {
        val reservation = Reservation.of(testMovie(), testTheater()).selectSeat("A1")

        assertThat(reservation.isSeatSelected("A1")).isTrue()
        assertThat(reservation.isSeatSelected("A2")).isFalse()
    }

    @Test
    @DisplayName("예매에서 선택한 극장 이름을 조회할 수 있다")
    fun displayTheaterName() {
        val reservation = Reservation.of(testMovie(), testTheater())

        assertThat(reservation.displayTheaterName()).isEqualTo("강남점")
    }

    @Test
    @DisplayName("인원/날짜/시간/좌석을 바꿔도 선택한 극장은 유지된다")
    fun keepsTheaterAcrossChanges() {
        val reservation = Reservation.of(testMovie(), testTheater())
            .increaseHeadCount()
            .selectDate(LocalDate.of(2024, 3, 2))
            .selectSeat("A1")

        assertThat(reservation.displayTheaterName()).isEqualTo("강남점")
    }

    @Test
    @DisplayName("선택한 좌석의 등급별 가격 합계를 계산한다")
    fun selectedSeatsAmountWon() {
        val reservation = Reservation.of(testMovie(), testTheater())
            .increaseHeadCount()
            .increaseHeadCount() // 3명
            .selectSeat("A1") // B등급 10,000원
            .selectSeat("C1") // S등급 15,000원
            .selectSeat("E1") // A등급 12,000원

        assertThat(reservation.selectedSeatsAmountWon()).isEqualTo(37_000)
        assertThat(reservation.totalAmountWon()).isEqualTo(37_000)
    }
}

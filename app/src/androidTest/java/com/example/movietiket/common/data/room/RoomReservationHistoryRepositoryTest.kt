package com.example.movietiket.common.data.room

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.movietiket.common.fixture.testReservation
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * 실제 SQLite 위에서 Room DAO/Repository가 저장·조회를 제대로 하는지 확인한다
 * (Mapper 로직 자체는 test/의 ReservationMapperTest에서 이미 검증했다)
 */
@RunWith(AndroidJUnit4::class)
class RoomReservationHistoryRepositoryTest {

    private lateinit var database: MovieTicketDatabase
    private lateinit var repository: RoomReservationHistoryRepository

    // 인메모리 Room DB와 리포지토리를 준비한다
    @Before
    fun setUp() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        database = Room.inMemoryDatabaseBuilder(context, MovieTicketDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        repository = RoomReservationHistoryRepository(database.reservationDao())
    }

    // 테스트가 끝나면 DB를 닫는다
    @After
    fun tearDown() {
        database.close()
    }

    // 저장 후 id로 조회하면 동일한 예매 정보를 반환하는지 검증한다
    @Test
    fun saveThenFindByIdReturnsSameReservation() = runBlocking {
        val reservation = testReservation().selectSeat("A1")

        val id = repository.save(reservation)
        val found = repository.findById(id)

        assertEquals(id, found?.id())
        assertEquals(reservation.displayMovieTitle(), found?.displayMovieTitle())
        assertEquals("A1", found?.reservation()?.displaySelectedSeats())
    }

    // 일치하는 데이터가 없으면 null을 반환하는지 검증한다
    @Test
    fun findByIdReturnsNullWhenNoMatch() = runBlocking {
        assertNull(repository.findById(999L))
    }

    // 전체 조회 시 최근 예매 순으로 정렬되는지 검증한다
    @Test
    fun findAllOrdersByMostRecentReservationFirst() = runBlocking {
        var now = 0L
        val orderedRepository = RoomReservationHistoryRepository(database.reservationDao(), now = { now })

        val olderId = orderedRepository.save(testReservation())
        now = 1_000L
        val newerId = orderedRepository.save(testReservation())

        val ids = orderedRepository.findAll().first().map { it.id() }

        assertEquals(listOf(newerId, olderId), ids)
    }
}

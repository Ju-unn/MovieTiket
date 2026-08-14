package com.example.movietiket.common.data

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.movietiket.common.fixture.harryPotterReservation
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

    @Before
    fun setUp() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        database = Room.inMemoryDatabaseBuilder(context, MovieTicketDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        repository = RoomReservationHistoryRepository(database.reservationDao())
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun saveThenFindByIdReturnsSameReservation() = runBlocking {
        val reservation = harryPotterReservation().selectSeat("A1")

        val id = repository.save(reservation)
        val found = repository.findById(id)

        assertEquals(id, found?.id())
        assertEquals(reservation.displayMovieTitle(), found?.displayMovieTitle())
        assertEquals("A1", found?.reservation()?.displaySelectedSeats())
    }

    @Test
    fun findByIdReturnsNullWhenNoMatch() = runBlocking {
        assertNull(repository.findById(999L))
    }

    @Test
    fun findAllOrdersByMostRecentReservationFirst() = runBlocking {
        var now = 0L
        val orderedRepository = RoomReservationHistoryRepository(database.reservationDao(), now = { now })

        val olderId = orderedRepository.save(harryPotterReservation())
        now = 1_000L
        val newerId = orderedRepository.save(harryPotterReservation())

        val ids = orderedRepository.findAll().first().map { it.id() }

        assertEquals(listOf(newerId, olderId), ids)
    }
}

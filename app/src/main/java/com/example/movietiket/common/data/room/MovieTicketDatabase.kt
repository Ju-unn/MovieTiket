package com.example.movietiket.common.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/** 앱의 Room 데이터베이스 — 예매 내역 테이블을 관리한다 */
@Database(entities = [ReservationEntity::class], version = 1, exportSchema = false)
abstract class MovieTicketDatabase : RoomDatabase() {

    abstract fun reservationDao(): ReservationDao

    companion object {
        private const val DATABASE_NAME = "movie_ticket.db"

        @Volatile
        private var instance: MovieTicketDatabase? = null

        // 싱글턴 DB 인스턴스를 반환한다 (없으면 생성)
        fun getInstance(context: Context): MovieTicketDatabase =
            instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    MovieTicketDatabase::class.java,
                    DATABASE_NAME,
                ).build().also { instance = it }
            }
    }
}

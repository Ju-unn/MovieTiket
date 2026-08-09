package com.example.movietiket.common.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [ReservationEntity::class], version = 1, exportSchema = false)
abstract class MovieTicketDatabase : RoomDatabase() {

    abstract fun reservationDao(): ReservationDao

    companion object {
        private const val DATABASE_NAME = "movie_ticket.db"

        @Volatile
        private var instance: MovieTicketDatabase? = null

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

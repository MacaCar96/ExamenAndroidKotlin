package com.macacar96.examenandroidkotlin.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.macacar96.examenandroidkotlin.room.dao.MovieDao
import com.macacar96.examenandroidkotlin.room.entity.MovieEntity
import com.macacar96.examenandroidkotlin.ui.user.UserDao
import com.macacar96.examenandroidkotlin.ui.user.UserEntity

@Database(
    entities = [MovieEntity::class, UserEntity::class],
    version = 1
)
public abstract class TestRoomDatabase: RoomDatabase() {

    abstract fun movieDao(): MovieDao
    abstract fun userDao(): UserDao


    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: TestRoomDatabase? = null

        fun getDatabase(context: Context): TestRoomDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TestRoomDatabase::class.java,
                    "db_test"
                ).build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }

}
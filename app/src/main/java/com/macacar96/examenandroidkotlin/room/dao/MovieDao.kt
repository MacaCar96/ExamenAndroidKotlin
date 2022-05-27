package com.macacar96.examenandroidkotlin.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.macacar96.examenandroidkotlin.room.entity.MovieEntity

@Dao
interface MovieDao {

    @Insert
    fun insert(movieEntity: MovieEntity)

    @Update
    fun update(movieEntity: MovieEntity)

    @Query("DELETE FROM tbl_movies WHERE id = :id")
    fun deleteById(id: Int)

    @Query("SELECT * FROM tbl_movies")
    fun getAll(): List<MovieEntity>
}
package com.ahmed.a.habib.moviecatalogapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.ahmed.a.habib.moviecatalogapp.data.local.entities.CurrentPageEntity

@Dao
interface MoviesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveCurrentPage(currentPageEntity: CurrentPageEntity)

    @Transaction
    @Query("SELECT * FROM pages")
    suspend fun getCurrentPage(): CurrentPageEntity?

    @Query("DELETE FROM pages")
    suspend fun deleteAllMovies()
}
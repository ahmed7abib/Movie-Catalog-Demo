package com.ahmed.a.habib.moviecatalogapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ahmed.a.habib.moviecatalogapp.data.local.entities.PageEntity

@Dao
interface MoviesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveCurrentPage(pageEntity: PageEntity)

    @Query("SELECT * FROM pages WHERE pageNumber = :page")
    suspend fun getPage(page: Int): PageEntity?

    @Query("DELETE FROM pages")
    suspend fun deleteAllPages()
}
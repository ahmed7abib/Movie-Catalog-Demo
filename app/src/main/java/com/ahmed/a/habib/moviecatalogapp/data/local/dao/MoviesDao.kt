package com.ahmed.a.habib.moviecatalogapp.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ahmed.a.habib.moviecatalogapp.data.local.entities.PageEntity

@Dao
interface MoviesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveCurrentPage(pageEntity: PageEntity)

    @Query("SELECT * FROM pages ORDER BY pageNumber ASC")
    fun getPage(): PagingSource<Int, PageEntity>

    @Query("DELETE FROM pages")
    suspend fun deleteAllPages()

    @Query("SELECT COUNT(*) FROM pages")
    suspend fun getPageCount(): Int
}
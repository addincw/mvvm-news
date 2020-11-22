package com.addincendekia.mvvmnews.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.addincendekia.mvvmnews.model.Article

@Dao
interface ArticleDao {
    @Query("select * from articles")
    fun all(): LiveData<List<Article>>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(article: Article)
    @Delete
    suspend fun delete(article: Article)
}
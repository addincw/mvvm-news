package com.addincendekia.mvvmnews.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.addincendekia.mvvmnews.db.dao.ArticleDao
import com.addincendekia.mvvmnews.model.Article

@Database(entities = [Article::class], version = 1)
@TypeConverters(CustomTypeConverters::class)
abstract class NewsDatabase: RoomDatabase() {
    abstract fun getArticleDao(): ArticleDao

    companion object{
        @Volatile
        private var instance: NewsDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK){
            instance ?: createDatabase(context).also { instance = it }
        }

        private fun createDatabase(context: Context): NewsDatabase {
            return Room.databaseBuilder(
                    context,
                    NewsDatabase::class.java,
                    "news_database.db"
                )
                .build()
        }
    }

}
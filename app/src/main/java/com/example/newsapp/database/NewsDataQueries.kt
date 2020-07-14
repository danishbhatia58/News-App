package com.example.newsapp.database


import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query


@Dao
interface NewsDataQueries {

    @Insert
    fun saveNews(news: NewsEntity)

    @Query(value = "Select * from fav_items")
    fun getAllNews() : List<NewsEntity>

    @Delete
    fun deleteNews(news: NewsEntity)

    @Query("DELETE FROM fav_items WHERE title = :title")
    fun deleteByTitle(title: String?)

}
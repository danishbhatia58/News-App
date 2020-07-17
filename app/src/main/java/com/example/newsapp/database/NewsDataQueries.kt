package com.example.newsapp.database


import androidx.room.Dao

import androidx.room.Delete

import androidx.room.Insert

import androidx.room.Query


@Dao
interface NewsDataQueries {

    @Insert
    fun saveNews(news: NewsEntity)


    @Query("UPDATE fav_items SET if_favorite = 1 WHERE title LIKE :title")
    fun updateNews(title: String?)


    @Query(value = "Select * from fav_items")
    fun getAllNews() : List<NewsEntity>


    @Query(value = "Select * from fav_items where if_favorite LIKE 1")
    fun getFavoriteNews() : List<NewsEntity>


    @Query("DELETE FROM fav_items WHERE title = :title")
    fun deleteByTitle(title: String?)


    @Query("DELETE FROM fav_items WHERE if_favorite LIKE 2")
    fun deleteNewsExceptFav()

}
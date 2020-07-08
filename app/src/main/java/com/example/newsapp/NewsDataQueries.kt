package com.example.newsapp


import androidx.room.Dao

import androidx.room.Query

import androidx.room.Delete

import androidx.room.Insert

import androidx.room.Update

import  androidx.room.OnConflictStrategy

import androidx.lifecycle.LiveData


@Dao
interface NewsDataQueries {

    @Insert
    fun saveNews(book: NewsEntity)

    @Query(value = "Select * from fav_items")
    fun getAllNews() : List<NewsEntity>

}
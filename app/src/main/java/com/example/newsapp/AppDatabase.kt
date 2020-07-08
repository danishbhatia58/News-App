package com.example.newsapp


import androidx.room.Database

import androidx.room.RoomDatabase


@Database (entities = [(NewsEntity::class)],version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun newsDataQueries(): NewsDataQueries
}
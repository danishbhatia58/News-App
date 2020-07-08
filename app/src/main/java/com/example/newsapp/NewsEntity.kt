package com.example.newsapp


import androidx.room.Entity

import androidx.room.ColumnInfo

import androidx.room.PrimaryKey


@Entity(tableName = "fav_items")
class NewsEntity {

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

    @ColumnInfo(name = "title")
    var title: String = ""

    @ColumnInfo(name = "description")
    var description: String = ""

}

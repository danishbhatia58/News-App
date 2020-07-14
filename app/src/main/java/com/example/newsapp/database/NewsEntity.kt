package com.example.newsapp.database


import androidx.room.Entity

import androidx.room.ColumnInfo

import androidx.room.PrimaryKey
import com.example.newsapp.StringConstants


@Entity(tableName = StringConstants.tableName)
class NewsEntity {

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

    @ColumnInfo(name = StringConstants.title)
    var title: String = StringConstants.emptyString

    @ColumnInfo(name = StringConstants.description)
    var description: String = StringConstants.emptyString

    @ColumnInfo(name= StringConstants.url)
    var url: String = StringConstants.emptyString

}

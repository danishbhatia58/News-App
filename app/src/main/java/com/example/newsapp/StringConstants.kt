package com.example.newsapp

object StringConstants{

    // Basic
    const val emptyString = ""

    const val error = "Error"


    // API
    const val NewsBaseUrl = "http://newsapi.org/"

    const val apiPath = "v2/everything?q=bitcoin&from=2020-07-17&sortBy=publishedAt&apiKey=8ded7689bd9146398d1f2edda6bd4bf6"

    const val responseParentObject = "articles"


    // DB
    const val dbName = "NewsDB"

    const val tableName = "fav_items"

    const val title = "title"

    const val description = "description"

    const val url = "url"

    const val ifFavorite = "if_favorite"
}
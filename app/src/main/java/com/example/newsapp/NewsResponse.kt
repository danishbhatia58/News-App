package com.example.newsapp

import android.provider.ContactsContract
import  com.google.gson.annotations.SerializedName


class NewsResponse {

    @SerializedName("articles")
    var articles = ArrayList<DataModel>()
}






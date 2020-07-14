package com.example.newsapp.api

import com.example.newsapp.StringConstants

import  com.google.gson.annotations.SerializedName


class NewsResponse {

    @SerializedName(StringConstants.responseParentObject)
    var articles = ArrayList<DataModel>()
}

data class DataModel( var title: String?,  var description: String?, var urlToImage: String?)






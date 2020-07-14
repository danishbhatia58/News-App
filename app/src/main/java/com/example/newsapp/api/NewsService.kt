package com.example.newsapp.api

import com.example.newsapp.R
import com.example.newsapp.StringConstants
import  retrofit2.http.GET

import  retrofit2.Call


interface NewsService {

    @GET(StringConstants.apiPath)
    fun getNewsData(): Call<NewsResponse>

}
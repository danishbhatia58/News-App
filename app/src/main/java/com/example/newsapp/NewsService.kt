package com.example.newsapp

import  retrofit2.http.GET

import retrofit2.http.Query

import  retrofit2.Call

interface NewsService {

    @GET("v2/everything?q=bitcoin&from=2020-07-09&sortBy=publishedAt&apiKey=8ded7689bd9146398d1f2edda6bd4bf6")
    fun getNewsData(): Call<NewsResponse>
}
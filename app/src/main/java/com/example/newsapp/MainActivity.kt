package com.example.newsapp


import android.content.Intent

import android.os.Bundle
import android.util.Log

import androidx.appcompat.app.AppCompatActivity

import androidx.recyclerview.widget.LinearLayoutManager

import androidx.recyclerview.widget.RecyclerView

import androidx.room.Room

import retrofit2.Call

import retrofit2.Callback

import retrofit2.Response

import retrofit2.Retrofit

import retrofit2.converter.gson.GsonConverterFactory


var starClicked: Boolean = false


class MainActivity : AppCompatActivity() {

    var db = Room.databaseBuilder(applicationContext,AppDatabase::class.java,"NewsDB").build()


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)


        // API Tasks

        val retrofit = Retrofit.Builder()
                .baseUrl(BaseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()


        val service = retrofit.create(NewsService::class.java)

        val call = service.getNewsData()

        println("_____________________________________")


            call.enqueue(object : Callback<NewsResponse> {

                override fun onResponse(call: Call<NewsResponse>, response: Response<NewsResponse>) {

                    if (response.code() == 200) {

                            val newsResponse = response.body()!!

                            val newsArray = ArrayList<DataModel>()


                            println(newsResponse.articles + "Articles List")


                            for (article in newsResponse.articles) {

                                newsArray.add(DataModel(article.title, article.description))
                            }

                            val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)

                            recyclerView.layoutManager = LinearLayoutManager (this@MainActivity)

                            recyclerView.setHasFixedSize (true)

                            recyclerView.adapter = CustomAdapter(newsArray)  {newsDetail -> newsItemClicked( newsDetail )}

                    }
                }


                override fun onFailure(call: Call<NewsResponse>, t: Throwable) {

                    println("ERRor" + t.printStackTrace())
                }

            })

    }


    private fun newsItemClicked( newsDetail: DataModel ) {

        if (starClicked == true) {

            val thread = Thread {

                var news = NewsEntity()

                news.title = newsDetail.title.toString()

                news.description = newsDetail.description.toString()

                db.newsDataQueries().saveNews(news)
//

          }
            thread.start()
        }
        else {

            val intent = Intent(this, NewsDetails::class.java)

            intent.putExtra("desc", newsDetail.description)

            startActivity(intent)
        }
    }


    companion object {

        var BaseUrl = "http://newsapi.org/"
    }

}




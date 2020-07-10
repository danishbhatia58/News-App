package com.example.newsapp


import android.content.Intent

import android.os.Bundle

import android.widget.Button

import androidx.appcompat.app.AppCompatActivity

import androidx.recyclerview.widget.LinearLayoutManager

import androidx.recyclerview.widget.RecyclerView

import retrofit2.Call

import retrofit2.Callback

import retrofit2.Response

import retrofit2.Retrofit

import retrofit2.converter.gson.GsonConverterFactory


var pageCheck: Boolean = true


class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        setSupportActionBar(findViewById(R.id.toolbarId))


        pageCheck = true // If Main Activity opens


        var favBtn = (findViewById(R.id.favoriteButton) ) as Button


        favBtn.setOnClickListener {

            val intent = Intent(this, FavoriteActivity::class.java)

            startActivity(intent)
        }


        // API Tasks

        val retrofit = Retrofit.Builder()
                .baseUrl(BaseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        val service = retrofit.create(NewsService::class.java)

        val call = service.getNewsData()

            call.enqueue(object : Callback<NewsResponse> {

                override fun onResponse(call: Call<NewsResponse>, response: Response<NewsResponse>) {

                    if (response.code() == 200) {

                            val newsResponse = response.body()!!


                            val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)

                            recyclerView.layoutManager = LinearLayoutManager (this@MainActivity)

                            recyclerView.setHasFixedSize (true)

                            recyclerView.adapter = CustomAdapter(newsResponse.articles)  {newsDetail -> newsItemClicked( newsDetail )}

                    }
                }


                override fun onFailure(call: Call<NewsResponse>, t: Throwable) {

                    println("ERRor" + t.printStackTrace())
                }

            })

    }


    override fun onRestart() {

        super.onRestart()

        finish()

        startActivity(getIntent())
    }


    private fun newsItemClicked( newsDetail: DataModel ) {

        val intent = Intent(this, NewsDetails::class.java)

        intent.putExtra("desc", newsDetail.description)

        startActivity(intent)
    }


    companion object {

        var BaseUrl = "http://newsapi.org/"
    }

}




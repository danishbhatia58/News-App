package com.example.newsapp.activities

// ---LIBRARIES---

import android.content.Intent

import android.os.Bundle

import android.widget.Button

import androidx.appcompat.app.AppCompatActivity

import com.example.newsapp.*

import com.example.newsapp.api.NewsResponse

import com.example.newsapp.api.NewsService

import com.example.newsapp.adapter.CustomAdapter

import com.example.newsapp.api.DataModel

import retrofit2.Call

import retrofit2.Callback

import retrofit2.Response


var pageCheck: Boolean = true // This will help to determine the current opened Activity b/w Main and Favorite Activities.


class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)


        // ---INITIALIZATIONS---

        setContentView(R.layout.activity_main)

        setSupportActionBar(findViewById(R.id.toolbarId))

        pageCheck = true // If Main Activity opens



        // ---VARIABLES DECLARATION---

        val favoriteIntent = Intent(this, FavoriteActivity::class.java)

        val favBtn = (findViewById(R.id.favoriteButton)) as Button

        val retrofitCall = Singleton.retrofitObject().create(NewsService::class.java).getNewsData()

        var recyclerView = Singleton.recyclerViewContent(findViewById(R.id.recyclerView),this)



        // ---ACTIONS---

        favBtn.setOnClickListener {

            startActivity(favoriteIntent)
        }



        // ---API CALL---

        retrofitCall.enqueue(object : Callback<NewsResponse> {

            override fun onResponse(call: Call<NewsResponse>, response: Response<NewsResponse>) {

                if (response.code() == 200) {

                    recyclerView.adapter =
                        CustomAdapter(response.body()!!.articles) { newsDetail ->
                            newsItemClicked(newsDetail)
                        }

                    Singleton.notifyAdapter(recyclerView)
                }
            }

            override fun onFailure(call: Call<NewsResponse>, t: Throwable) {

                println(StringConstants.error + t.printStackTrace())
            }
        })
    }


    override fun onRestart() {

        super.onRestart()

        finish()

        startActivity(intent)
    }


    private fun newsItemClicked(newsDetail: DataModel) {

        val newsDetailsIntent = Intent(this, NewsDetails::class.java)

        newsDetailsIntent.putExtra(StringConstants.description, newsDetail.description)

        startActivity(newsDetailsIntent)
    }

}




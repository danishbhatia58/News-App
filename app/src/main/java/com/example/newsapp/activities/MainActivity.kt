package com.example.newsapp.activities

// ---LIBRARIES---

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.R
import com.example.newsapp.Singleton
import com.example.newsapp.StringConstants
import com.example.newsapp.adapter.CustomAdapter
import com.example.newsapp.api.DataModel
import com.example.newsapp.api.NewsResponse
import com.example.newsapp.api.NewsService
import com.example.newsapp.database.NewsEntity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.*
import kotlin.collections.ArrayList
import kotlin.concurrent.thread


var pageCheck: Boolean = true // This will help to determine the current opened Activity b/w Main and Favorite Activities.


class MainActivity : AppCompatActivity() {


    private val sharedPrefFile = "kotlinSharedPreference"


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)


        // ---INITIALIZATIONS---

        setContentView(R.layout.activity_main)

        setSupportActionBar(findViewById(R.id.toolbarId))

        pageCheck = true // If Main Activity opens



        // ---VARIABLES DECLARATION---

        val favoriteIntent = Intent(this, FavoriteActivity::class.java)

        val favBtn = (findViewById(R.id.favoriteButton)) as Button

        val progressBar: ProgressBar = (findViewById(R.id.progressBar1))

        val retrofitCall = Singleton.retrofitObject().create(NewsService::class.java).getNewsData()

        var recyclerView = Singleton.recyclerViewContent(findViewById(R.id.recyclerView),this)

        val sharedPreferences: SharedPreferences = this.getSharedPreferences(sharedPrefFile,
            Context.MODE_PRIVATE)


        // ---ACTIONS---

        favBtn.setOnClickListener {

            recyclerView.visibility = View.GONE

            progressBar.visibility = View.VISIBLE

            startActivity(favoriteIntent)
        }



        val storedDateTime = LocalDateTime.parse(retrieveApiHitTime(sharedPreferences))

        if ( storedDateTime.plusHours(3) < Singleton.getCurrentTime() ) {

            println("________API HIT_______")

            deleteNewsData()

            callNewsAPI(retrofitCall, recyclerView, sharedPreferences)
        }
        else{


            var  newsList = ArrayList<DataModel>()

            var thread = Thread {

                for (news in  Singleton.dbObject(this).getAllNews() ){

                    newsList.add(DataModel(news.title,news.description,news.url))
                }
            }
            thread.start()


            Singleton.setTimer() // Waiting for 2 seconds for getting Data from DB


            if (newsList.count() > 0) {

                println("_____DB Hit_____")

                recyclerView.adapter = CustomAdapter(newsList) { newsDetail -> newsItemClicked(newsDetail) }

                Singleton.notifyAdapter(recyclerView)
            }
            else{

                println("________db Null, API Hit_________")

                deleteNewsData()

                callNewsAPI(retrofitCall, recyclerView, sharedPreferences)
            }
        }

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


    private fun saveApiHitTime(sharedPreferences: SharedPreferences){

        val apiHitTime: String = Singleton.getCurrentTime().toString()

        val editor:SharedPreferences.Editor =  sharedPreferences.edit()

        editor.putString("api_hit_timing_key", apiHitTime)

        editor.apply()

        editor.commit()
    }


    private fun retrieveApiHitTime(sharedPreferences: SharedPreferences): String{

        return sharedPreferences.getString("api_hit_timing_key",Singleton.getCurrentTime().toString()).toString()
    }


    private  fun saveAllNewsInDB(allNews: ArrayList<DataModel>) {

        val newsObj = NewsEntity()

        val thread = Thread{

            for (news in allNews) {

                newsObj.title = news.title.toString()

                newsObj.description = news.description.toString()

                newsObj.url = news.urlToImage.toString()

                newsObj.ifFavorite = 2 // false

                Singleton.dbObject(this).saveNews(newsObj)

                println("____" + newsObj.title)
            }
        }
        thread.start()
    }


    fun callNewsAPI(retrofitCall: Call<NewsResponse>, recyclerView : RecyclerView, sharedPreferences: SharedPreferences){

        retrofitCall.enqueue(object : Callback<NewsResponse> {

            override fun onResponse(call: Call<NewsResponse>, response: Response<NewsResponse>) {

                if (response.code() == 200) {

                    recyclerView.adapter =
                        CustomAdapter(response.body()!!.articles) { newsDetail ->
                            newsItemClicked(newsDetail)
                        }

                    Singleton.notifyAdapter(recyclerView)

                    saveApiHitTime(sharedPreferences)

                    saveAllNewsInDB(response.body()!!.articles)
                }
            }

            override fun onFailure(call: Call<NewsResponse>, t: Throwable) {

                println(StringConstants.error + t.printStackTrace())
            }
        })
    }



    fun deleteNewsData() {

        val thread = Thread {

            Singleton.dbObject(this).deleteNewsExceptFav()
        }
        thread.start()
    }

}




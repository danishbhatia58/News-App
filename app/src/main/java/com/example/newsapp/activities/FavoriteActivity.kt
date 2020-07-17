package com.example.newsapp.activities

import android.content.Intent

import android.os.Bundle
import android.provider.ContactsContract
import android.view.View
import android.widget.ProgressBar

import androidx.appcompat.app.AppCompatActivity

import androidx.recyclerview.widget.LinearLayoutManager

import androidx.recyclerview.widget.RecyclerView

import androidx.room.Room

import com.example.newsapp.*

import com.example.newsapp.database.AppDatabase

import com.example.newsapp.adapter.CustomAdapter
import com.example.newsapp.api.DataModel
import com.example.newsapp.database.NewsEntity

import java.util.concurrent.TimeUnit


class FavoriteActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)


        // ---INITIALIZATIONS---

        setContentView(R.layout.activity_favorite)

        setSupportActionBar(findViewById(R.id.toolbarFavId))

        val progressBar: ProgressBar = findViewById(R.id.progressBar2)


        pageCheck = false  // If Fav activity opens


        progressBar.visibility = View.VISIBLE


        // ---VARIABLES DECLARATION---

        var newsList = ArrayList<DataModel>()

        var recyclerView = Singleton.recyclerViewContent(findViewById(R.id.recyclerViewFav), this)



        var thread = Thread {

             for (news in  Singleton.dbObject(this).getFavoriteNews() ){

                 println("______" + news.ifFavorite.toString())

                 newsList.add(DataModel(news.title,news.description,news.url))
             }
        }
        thread.start()


        Singleton.setTimer() // Waiting for 2 seconds for getting Data from DB


        if (newsList.count() > 0) {

            recyclerView.adapter =
                CustomAdapter(newsList) { newsDetail ->
                    newsItemClicked(
                        newsDetail
                    )
                }

            Singleton.notifyAdapter(recyclerView)

            progressBar.visibility = View.GONE
        }
        else{
            println("____________No News")
        }
    }


    override fun onBackPressed() {

        super.onBackPressed()

        val intent = Intent(this, MainActivity::class.java)

        startActivity(intent)

    }


    private fun newsItemClicked( newsDetail: DataModel) {

        val newsDetailIntent = Intent(this, NewsDetails::class.java)

        newsDetailIntent.putExtra(StringConstants.description, newsDetail.description)

        startActivity(newsDetailIntent)
    }

}
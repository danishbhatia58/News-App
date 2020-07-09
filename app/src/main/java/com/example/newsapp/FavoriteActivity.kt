package com.example.newsapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.TimeUnit

class FavoriteActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_favorite)

        pageCheck = false  // If Fav activity opens

        setSupportActionBar(findViewById(R.id.toolbarFavId))




        val newsArray = ArrayList<DataModel>()

        var thread = Thread {

            var db = Room.databaseBuilder(this, AppDatabase::class.java, "NewsDB").build()

            for (news in db.newsDataQueries().getAllNews()) {

                newsArray.add(DataModel(news.title, news.description))
            }

            println(newsArray[0].title + newsArray.count())

            println(newsArray[0].description + "______")



        }
        thread.start()

        TimeUnit.SECONDS.sleep(3L)

        var recyclerView = findViewById<RecyclerView>(R.id.recyclerViewFav)

        recyclerView.layoutManager = LinearLayoutManager (this@FavoriteActivity)

        recyclerView.setHasFixedSize (true)

        recyclerView.adapter = CustomAdapter(newsArray)  {newsDetail -> newsItemClicked( newsDetail )}

        //recyclerView.adapter.notifyDataSetChanged()
    }

    private fun newsItemClicked( newsDetail: DataModel ) {

        val intent = Intent(this, NewsDetails::class.java)

        intent.putExtra("desc", newsDetail.description)

        startActivity(intent)
    }
}
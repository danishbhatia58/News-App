package com.example.newsapp

import android.content.Intent

import android.os.Bundle

import androidx.appcompat.app.AppCompatActivity

import androidx.recyclerview.widget.LinearLayoutManager

import androidx.recyclerview.widget.RecyclerView

import androidx.room.Room

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

            // for loop is using because DB Model is of Collection type and Recycle View needs List type

            // Putting data of collection in list

            for (news in db.newsDataQueries().getAllNews()) {

                newsArray.add(DataModel(news.title, news.description, news.url))
            }
        }
        thread.start()


        TimeUnit.SECONDS.sleep(3L)


        if (newsArray.count() > 0) {

            var recyclerView = findViewById<RecyclerView>(R.id.recyclerViewFav)

            recyclerView.layoutManager = LinearLayoutManager(this@FavoriteActivity)

            recyclerView.setHasFixedSize(true)

            recyclerView.adapter =
            CustomAdapter(newsArray) { newsDetail -> newsItemClicked(newsDetail) }

            //recyclerView.adapter.notifyDataSetChanged()
        }
    }


    private fun newsItemClicked( newsDetail: DataModel ) {

        val intent = Intent(this, NewsDetails::class.java)

        intent.putExtra("desc", newsDetail.description)

        startActivity(intent)
    }

}
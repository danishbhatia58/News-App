package com.example.newsapp

import android.content.Context
import android.os.Build
import android.preference.PreferenceGroup
import android.view.View
import android.widget.ProgressBar
import androidx.annotation.RequiresApi

import androidx.recyclerview.widget.LinearLayoutManager

import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.RoomSQLiteQuery
import com.example.newsapp.database.AppDatabase
import com.example.newsapp.database.NewsDataQueries

import retrofit2.Retrofit

import retrofit2.converter.gson.GsonConverterFactory
import java.time.LocalDateTime
import java.util.*
import java.util.concurrent.TimeUnit


class Singleton {

    companion object{

        fun recyclerViewContent(recyclerView: RecyclerView, context: Context) : RecyclerView{

            recyclerView.layoutManager = LinearLayoutManager(context)

            recyclerView.setHasFixedSize(true)

            return recyclerView
        }


        fun notifyAdapter(recyclerView: RecyclerView){

            recyclerView.adapter!!.notifyDataSetChanged()
        }


        fun retrofitObject(): Retrofit {

            return Retrofit.Builder()
                .baseUrl( StringConstants.NewsBaseUrl )
                .addConverterFactory( GsonConverterFactory.create() )
                .build()
        }


        fun dbObject(context: Context): NewsDataQueries{

            return  Room.databaseBuilder(context, AppDatabase::class.java, StringConstants.dbName )
                .build().newsDataQueries()
        }


        fun setTimer(){

            TimeUnit.SECONDS.sleep(1L)
        }

        fun getCurrentTime(): LocalDateTime {

            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                LocalDateTime.now()
            }
            else {

                TODO("VERSION.SDK_INT < O")
            }
        }


    }
}
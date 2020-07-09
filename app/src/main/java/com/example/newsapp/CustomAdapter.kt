package com.example.newsapp


import android.content.Intent

import android.view.LayoutInflater

import android.view.View

import android.view.ViewGroup
import android.widget.Button

import android.widget.TextView

import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import kotlinx.android.synthetic.main.activity_main.*

//

class CustomAdapter(val userList: ArrayList<DataModel> , val clickListener: (DataModel)-> Unit )  : RecyclerView.Adapter<CustomAdapter.ViewHolder>() {


    //this method is returning the view for each item in the list

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomAdapter.ViewHolder {

        val v = LayoutInflater.from(parent.context).inflate(R.layout.list_layout, parent, false)

        return ViewHolder(v)
    }


    //this method is binding the data on the list

    override fun onBindViewHolder(holder: CustomAdapter.ViewHolder, position: Int) {

        holder.bindItems(userList[position], clickListener)
    }


    //this method is giving the size of the list

    override fun getItemCount(): Int {

        return userList.size

    }


    //the class is hodling the list view

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(article: DataModel,  clickListener: (DataModel) -> Unit) {

            val textViewName = itemView.findViewById(R.id.textViewUsername) as TextView

            val textViewAddress  = itemView.findViewById(R.id.textViewAddress) as TextView

            val buttonStar = itemView.findViewById(R.id.buttonStar) as Button


            textViewName.text = article.title

            textViewAddress.text = article.description


            buttonStar.setOnClickListener {

                val thread = Thread {

                    var news = NewsEntity()

                    news.title = article.title.toString()

                    news.description = article.description.toString()


                    var db =
                        Room.databaseBuilder(itemView.context, AppDatabase::class.java, "NewsDB")
                            .build()

                    println("______" + pageCheck.toString() + "_________________")

                    if (pageCheck) {

                        db.newsDataQueries().saveNews(news)
                    }
                    else {

                        db.newsDataQueries().deleteByTitle(news.title)
                        
                    }

                }
                thread.start()
            }


            itemView.setOnClickListener { clickListener(article)}
        }


    }

}
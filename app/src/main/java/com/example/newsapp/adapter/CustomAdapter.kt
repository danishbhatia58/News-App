package com.example.newsapp.adapter


import android.content.Intent

import android.view.LayoutInflater

import android.view.View

import android.view.ViewGroup

import android.widget.ImageButton

import android.widget.TextView

import androidx.recyclerview.widget.RecyclerView

import com.example.newsapp.R

import com.example.newsapp.Singleton

import com.example.newsapp.StringConstants

import com.example.newsapp.activities.FavoriteActivity

import com.example.newsapp.activities.pageCheck
import com.example.newsapp.api.DataModel

import com.example.newsapp.database.NewsEntity

import com.squareup.picasso.Picasso

import kotlinx.android.synthetic.main.list_layout.view.*


class CustomAdapter(val userList: ArrayList<DataModel>, val clickListener: (DataModel)-> Unit )  : RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

    //this method is returning the view for each item in the list

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val v = LayoutInflater.from(parent.context).inflate(R.layout.list_layout, parent, false)

        return ViewHolder(v)
    }


    //this method is binding the data on the list

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.bindItems(userList[position], clickListener)
    }


    //this method is giving the size of the list

    override fun getItemCount(): Int {

        return userList.size
    }


    //the class is holding the list view

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(article: DataModel, clickListener: (DataModel) -> Unit) {

            val textViewName = itemView.findViewById(R.id.textViewUsername) as TextView

            val textViewAddress  = itemView.findViewById(R.id.textViewAddress) as TextView

            val buttonStar = itemView.findViewById(R.id.buttonStar) as ImageButton


            if (!pageCheck){

                buttonStar.setImageResource(android.R.drawable.ic_delete)
            }


            textViewName.text = article.title

            textViewAddress.text = StringConstants.emptyString // article.description

            Picasso.get().load(article.urlToImage).into(itemView.imageVw)


            buttonStar.setOnClickListener {

                var news = NewsEntity()

                news.title = article.title.toString()

                news.description = article.description.toString()

                news.url = article.urlToImage.toString()


                val thread = Thread {

                    if (pageCheck) {

                        Singleton.dbObject(itemView.context).saveNews(news)

                        itemView.buttonStar.setImageResource(android.R.drawable.star_off)
                    }
                    else {

                        Singleton.dbObject(itemView.context).deleteByTitle(news.title)

                        Singleton.setTimer() // Waiting for 1 second for deleting data

                        reloadFavoriteActivity()
                    }
                }
                thread.start()
            }


            itemView.setOnClickListener { clickListener(article)}
        }


        private fun reloadFavoriteActivity(){

            val i = Intent(itemView.context, FavoriteActivity::class.java)

            i.flags = Intent.FLAG_ACTIVITY_NEW_TASK

            itemView.context.startActivity(i)
        }

    }

}
package com.example.newsapp


import android.content.Intent

import android.view.LayoutInflater

import android.view.View

import android.view.ViewGroup

import android.widget.ImageButton

import android.widget.TextView

import androidx.recyclerview.widget.RecyclerView

import androidx.room.Room

import com.squareup.picasso.Picasso

import kotlinx.android.synthetic.main.list_layout.view.*

import java.util.concurrent.TimeUnit


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

            val buttonStar = itemView.findViewById(R.id.buttonStar) as ImageButton

            textViewName.text = article.title

            if (pageCheck == false){

                buttonStar.setImageResource(android.R.drawable.ic_delete)
            }


            textViewAddress.text = "" // article.description


            Picasso.get().load(article.urlToImage).into(itemView.imageVw)


            buttonStar.setOnClickListener {

                val thread = Thread {

                    var news = NewsEntity()

                    news.title = article.title.toString()

                    news.description = article.description.toString()

                    news.url = article.urlToImage.toString()


                    var db =
                        Room.databaseBuilder(itemView.context, AppDatabase::class.java, "NewsDB")
                            .build()


                    if (pageCheck) {

                        db.newsDataQueries().saveNews(news)

                        itemView.buttonStar.setImageResource(android.R.drawable.star_off)
                    }
                    else {

                        db.newsDataQueries().deleteByTitle(news.title)

                        TimeUnit.SECONDS.sleep(3L)


                        val i = Intent(itemView.context, FavoriteActivity::class.java)

                        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK //add this line

                        itemView.context.startActivity(i)

                    }

                }
                thread.start()
            }


            itemView.setOnClickListener { clickListener(article)}
        }

    }

}
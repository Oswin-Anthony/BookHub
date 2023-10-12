@file:Suppress("DEPRECATION")

package com.example.bookhub.fragment

import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.RelativeLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.bookhub.R
import com.example.bookhub.adapter.FavouritesRecyclerAdapter
import com.example.bookhub.database.BookDatabase
import com.example.bookhub.database.BookEntity
import com.example.bookhub.model.Book

class FavouritesFragment : Fragment() {
    lateinit var recyclerFavourites: RecyclerView
    lateinit var progressLayout: RelativeLayout
    lateinit var progressBar: ProgressBar
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var recyclerAdapter: FavouritesRecyclerAdapter
    var bookList = listOf<BookEntity>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_favourites, container, false)

        recyclerFavourites = view.findViewById(R.id.recyclerFavourites)
        progressLayout = view.findViewById(R.id.progressLayout)
        progressBar = view.findViewById(R.id.progressBar)

        layoutManager = GridLayoutManager(activity as Context, 2)
        bookList = RetrieveFavourites(activity as Context).execute().get()

        if(activity != null) {
            progressLayout.visibility = View.GONE
            recyclerAdapter = FavouritesRecyclerAdapter(activity as Context, bookList)
            recyclerFavourites.adapter = recyclerAdapter
            recyclerFavourites.layoutManager = layoutManager
        }

        return view
    }

    class RetrieveFavourites(val context: Context): AsyncTask<Void, Void, List<BookEntity>>() {
        override fun doInBackground(vararg params: Void?): List<BookEntity> {
            val db = Room.databaseBuilder(context, BookDatabase::class.java, "books-db").build()
            return db.bookDao().getAllBooks()
        }

    }
}
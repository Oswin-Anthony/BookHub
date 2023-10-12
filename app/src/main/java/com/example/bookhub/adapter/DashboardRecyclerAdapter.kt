package com.example.bookhub.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bookhub.R
import com.example.bookhub.activity.DescriptionActivity
import com.example.bookhub.model.Book
import com.squareup.picasso.Picasso

class DashboardRecyclerAdapter(val context: Context, val itemList: ArrayList<Book>): RecyclerView.Adapter<DashboardRecyclerAdapter.DashboardViewHolder>() {

    class DashboardViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val bookName: TextView = view.findViewById(R.id.txtBookName)
        val authorName: TextView = view.findViewById(R.id.txtAuthorName)
        val cost: TextView = view.findViewById(R.id.txtCost)
        val rating: TextView = view.findViewById(R.id.txtRating)
        val bookLogo: ImageView = view.findViewById(R.id.imgBookLogo)
        val rowLayout: RelativeLayout = view.findViewById(R.id.rowRelativeLayout)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DashboardViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_item_dashboard, parent, false)
        return DashboardViewHolder(view)
    }

    override fun onBindViewHolder(holder: DashboardViewHolder, position: Int) {
        val book = itemList[position]
        holder.bookName.text = book.bookName.trim()
        holder.authorName.text = book.authorName.trim()
        holder.cost.text = book.cost.trim()
        holder.rating.text = book.rating.trim()
        Picasso.get().load(book.bookLogo).error(R.drawable.ic_book_app).into(holder.bookLogo)

        holder.rowLayout.setOnClickListener {
            val intent = Intent(context, DescriptionActivity::class.java)
            intent.putExtra("book_id", book.bookId)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }
}
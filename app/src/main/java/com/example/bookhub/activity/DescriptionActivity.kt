@file:Suppress("DEPRECATION")

package com.example.bookhub.activity

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.room.Room
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.bookhub.R
import com.example.bookhub.database.BookDatabase
import com.example.bookhub.database.BookEntity
import com.example.bookhub.util.ConnectionManager
import com.squareup.picasso.Picasso
import org.json.JSONObject

class DescriptionActivity : AppCompatActivity() {
    lateinit var bookName: TextView
    lateinit var authorName: TextView
    lateinit var cost: TextView
    lateinit var rating: TextView
    lateinit var bookLogo: ImageView
    lateinit var btnAddToFavourite: Button
    lateinit var bookDescription: TextView
    lateinit var progressBar: ProgressBar
    lateinit var progressLayout: RelativeLayout
    lateinit var toolBar: androidx.appcompat.widget.Toolbar
    var bookId: String? = "100"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_description)

        bookName = findViewById(R.id.txtBookName)
        authorName = findViewById(R.id.txtAuthorName)
        cost = findViewById(R.id.txtCost)
        rating = findViewById(R.id.txtRating)
        bookLogo = findViewById(R.id.imgBookLogo)
        btnAddToFavourite = findViewById(R.id.btnAddToFavourites)
        bookDescription = findViewById(R.id.txtBookDescription)
        progressBar = findViewById(R.id.progressBar)
        progressLayout = findViewById(R.id.progressLayout)
        toolBar = findViewById(R.id.toolbar)

        progressLayout.visibility = View.VISIBLE
        progressBar.visibility = View.VISIBLE
        setSupportActionBar(toolBar)
        supportActionBar?.title = "Book Details"

        if(intent != null) {
            bookId = intent.getStringExtra("book_id").toString()
        } else {
            finish()
            Toast.makeText(this@DescriptionActivity, "Some unexpected error occurred", Toast.LENGTH_SHORT).show()
        }

        if(bookId == "100") {
            finish()
            Toast.makeText(this@DescriptionActivity, "Some unexpected error occurred", Toast.LENGTH_SHORT).show()
        }

        val queue = Volley.newRequestQueue(this@DescriptionActivity)
        val url = "http://13.235.250.119/v1/book/get_book/"
        val jsonParams = JSONObject()
        jsonParams.put("book_id", bookId)

        if(ConnectionManager().checkConnectivity(this@DescriptionActivity)) {
            val jsonRequest = object: JsonObjectRequest(Method.POST, url, jsonParams,
                Response.Listener {
                    try {
                        val success = it.getBoolean("success")
                        if(success) {
                            val bookJsonObject = it.getJSONObject("book_data")
                            progressLayout.visibility = View.GONE

                            val imgUrl = bookJsonObject.getString("image")
                            Picasso.get().load(bookJsonObject.getString("image")).error(R.drawable.ic_book_app).into(bookLogo)
                            bookName.text = bookJsonObject.getString("name")
                            authorName.text = bookJsonObject.getString("author")
                            cost.text = bookJsonObject.getString("price")
                            rating.text = bookJsonObject.getString("rating")
                            bookDescription.text = bookJsonObject.getString("description")

                            val bookEntity = BookEntity(
                                bookId?.toInt() as Int,
                                bookName.text.toString(),
                                authorName.text.toString(),
                                cost.text.toString(),
                                rating.text.toString(),
                                bookDescription.text.toString(),
                                imgUrl
                            )

                            val checkFav = DBAsyncTask(applicationContext, bookEntity, 1).execute()
                            val isFav = checkFav.get()
                            if(isFav) {
                                btnAddToFavourite.text = "Remove from Favourites"
                                val favColor = ContextCompat.getColor(applicationContext, R.color.fav_color)
                                btnAddToFavourite.setBackgroundColor(favColor)
                            } else {
                                btnAddToFavourite.text = "Add to Favourites"
                                val nofavColor = ContextCompat.getColor(applicationContext, R.color.purple_700)
                                btnAddToFavourite.setBackgroundColor(nofavColor)
                            }

                            btnAddToFavourite.setOnClickListener {
                                if(!DBAsyncTask(applicationContext, bookEntity, 1).execute().get()) {
                                    val async = DBAsyncTask(applicationContext, bookEntity, 2).execute()
                                    val result = async.get()
                                    if(result) {
                                        Toast.makeText(this@DescriptionActivity, "Book added to favourites", Toast.LENGTH_SHORT).show()
                                        btnAddToFavourite.text = "Remove from Favourites"
                                        val favColor = ContextCompat.getColor(applicationContext, R.color.fav_color)
                                        btnAddToFavourite.setBackgroundColor(favColor)
                                    } else {
                                        Toast.makeText(this@DescriptionActivity, "Some error occurred. Please try again", Toast.LENGTH_LONG).show()
                                    }
                                } else {
                                    val async = DBAsyncTask(applicationContext, bookEntity, 3).execute()
                                    val result = async.get()
                                    if(result) {
                                        Toast.makeText(this@DescriptionActivity, "Book removed from favourites", Toast.LENGTH_SHORT).show()
                                        btnAddToFavourite.text = "Add to Favourites"
                                        val favColor = ContextCompat.getColor(applicationContext, R.color.purple_700)
                                        btnAddToFavourite.setBackgroundColor(favColor)
                                    } else {
                                        Toast.makeText(this@DescriptionActivity, "Some error occurred. Please try again", Toast.LENGTH_LONG).show()
                                    }
                                }
                            }
                        } else {
                            Toast.makeText(this@DescriptionActivity, "Some unexpected JSON 1 error occurred.", Toast.LENGTH_LONG).show()
                        }
                    } catch (e: Exception) {
                        Toast.makeText(this@DescriptionActivity, "Some unexpected JSON error occurred.", Toast.LENGTH_LONG).show()
                    }
                }, Response.ErrorListener {
                    Toast.makeText(this@DescriptionActivity, "Volley error occurred.", Toast.LENGTH_LONG).show()
                }) {
                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String, String>()
                    headers["Content-type"] = "application/json"
                    headers["token"] = "8dcdbc2568e5ae"
                    return headers
                }
            }
            queue.add(jsonRequest)
        } else {
            val dialog = AlertDialog.Builder(this@DescriptionActivity)
            dialog.setTitle("Error")
            dialog.setMessage("Internet Connection is not found")
            dialog.setPositiveButton("Open Settings") { _, _ ->
                val settingIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(settingIntent)
                finish()
            }
            dialog.setNegativeButton("Exit") { _, _ ->
                ActivityCompat.finishAffinity(this@DescriptionActivity)
            }
            dialog.create()
            dialog.show()
        }
    }

    class DBAsyncTask(val context: Context, val bookEntity: BookEntity, val mode: Int) : AsyncTask<Void, Void, Boolean>() {
        val db = Room.databaseBuilder(context, BookDatabase::class.java, "books-db").build()

        /*
        Mode 1 = Check if book is favourite or not
        Mode 2 = Add to favourite
        Mode 3 = Remove from favourite
        */

        override fun doInBackground(vararg params: Void?): Boolean {
            when(mode) {
                1 -> {
                    val book: BookEntity? = db.bookDao().getBookById(bookEntity.bookId.toString())
                    db.close()
                    return book != null
                }
                2 -> {
                    db.bookDao().insertBook(bookEntity)
                    db.close()
                    return true
                }
                3 -> {
                    db.bookDao().deleteBook(bookEntity)
                    db.close()
                    return true
                }
            }
            return false
        }
    }
}
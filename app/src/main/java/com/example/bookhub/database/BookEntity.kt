package com.example.bookhub.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "books")
data class BookEntity(
    @PrimaryKey @ColumnInfo(name = "book_id") val bookId: Int,
    @ColumnInfo("book_name")val bookName: String,
    @ColumnInfo("author_name")val authorName: String,
    @ColumnInfo("cost")val cost: String,
    @ColumnInfo("rating")val rating: String,
    @ColumnInfo("des")val description: String,
    @ColumnInfo("img")val bookLogo: String,
)
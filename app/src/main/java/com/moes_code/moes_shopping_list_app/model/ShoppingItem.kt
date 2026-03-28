package com.moes_code.moes_shopping_list_app.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "shopping_items",
    foreignKeys = [
        ForeignKey(
            entity = Category::class,
            parentColumns = ["id"],
            childColumns = ["category_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["category_id"])]
)
data class ShoppingItem(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val quantity: Int = 1,
    @ColumnInfo(name = "category_id")
    val categoryId: Int = 1,
    @ColumnInfo(name = "is_completed", defaultValue = "0")
    val isCompleted: Boolean = false
)

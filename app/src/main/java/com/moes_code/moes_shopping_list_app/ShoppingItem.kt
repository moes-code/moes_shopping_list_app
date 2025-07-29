package com.moes_code.moes_shopping_list_app

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ShoppingItem(
    val item: String,
    val amount: Int,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
)
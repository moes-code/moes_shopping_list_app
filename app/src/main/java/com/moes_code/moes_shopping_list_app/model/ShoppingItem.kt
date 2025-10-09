package com.moes_code.moes_shopping_list_app.model

data class ShoppingItem(
    val id: Int = 0,
    val name: String,
    val quantity: Int = 1,
    val categoryId: Int = 1
)
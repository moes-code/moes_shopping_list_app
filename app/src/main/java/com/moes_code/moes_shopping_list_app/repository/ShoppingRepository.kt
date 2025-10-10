package com.moes_code.moes_shopping_list_app.repository

import android.content.ContentValues
import android.content.Context
import com.moes_code.moes_shopping_list_app.database.DatabaseHelper
import com.moes_code.moes_shopping_list_app.model.Category
import com.moes_code.moes_shopping_list_app.model.ShoppingItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ShoppingRepository(context: Context) {
    private val dbHelper = DatabaseHelper(context)

    // Category operations
    // Retrieves all categories from the database, sorted by name
    suspend fun getAllCategories(): List<Category> = withContext(Dispatchers.IO) {
        val categories = mutableListOf<Category>()
        val db = dbHelper.readableDatabase

        val cursor = db.query(
            DatabaseHelper.TABLE_CATEGORIES,
            null, null, null, null, null,
            DatabaseHelper.CATEGORY_NAME
        )

        cursor.use {
            while (it.moveToNext()) {
                val id = it.getInt(it.getColumnIndexOrThrow(DatabaseHelper.CATEGORY_ID))
                val name = it.getString(it.getColumnIndexOrThrow(DatabaseHelper.CATEGORY_NAME))
                categories.add(Category(id, name))
            }
        }

        categories
    }

    // Inserts a new category into the database
    suspend fun addCategory(category: Category): Long = withContext(Dispatchers.IO) {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(DatabaseHelper.CATEGORY_NAME, category.name)
        }
        db.insert(DatabaseHelper.TABLE_CATEGORIES, null, values)
    }

    // Updates an existing category by ID
    suspend fun updateCategory(category: Category): Int = withContext(Dispatchers.IO) {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(DatabaseHelper.CATEGORY_NAME, category.name)
        }
        db.update(
            DatabaseHelper.TABLE_CATEGORIES,
            values,
            "${DatabaseHelper.CATEGORY_ID} = ?",
            arrayOf(category.id.toString())
        )
    }

    // Deletes a category by ID
    // Cascade delete removes associated shopping items if foreign keys are set accordingly
    suspend fun deleteCategory(id: Int): Int = withContext(Dispatchers.IO) {
        val db = dbHelper.writableDatabase
        db.delete(
            DatabaseHelper.TABLE_CATEGORIES,
            "${DatabaseHelper.CATEGORY_ID} = ?",
            arrayOf(id.toString())
        )
    }

    // Shopping item operations
    // Retrieves all shopping items for a given category ID, sorted by item name
    suspend fun getShoppingItemsByCategory(categoryId: Int): List<ShoppingItem> = withContext(Dispatchers.IO) {
        val items = mutableListOf<ShoppingItem>()
        val db = dbHelper.readableDatabase

        val cursor = db.query(
            DatabaseHelper.TABLE_SHOPPING_ITEMS,
            null,
            "${DatabaseHelper.ITEM_CATEGORY_ID} = ?",
            arrayOf(categoryId.toString()),
            null, null,
            DatabaseHelper.ITEM_NAME
        )

        cursor.use {
            while (it.moveToNext()) {
                val id = it.getInt(it.getColumnIndexOrThrow(DatabaseHelper.ITEM_ID))
                val name = it.getString(it.getColumnIndexOrThrow(DatabaseHelper.ITEM_NAME))
                val quantity = it.getInt(it.getColumnIndexOrThrow(DatabaseHelper.ITEM_QUANTITY))
                items.add(ShoppingItem(id, name, quantity, categoryId))
            }
        }

        items
    }

    // Inserts a new shopping item
    suspend fun addShoppingItem(item: ShoppingItem): Long = withContext(Dispatchers.IO) {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(DatabaseHelper.ITEM_NAME, item.name)
            put(DatabaseHelper.ITEM_QUANTITY, item.quantity)
            put(DatabaseHelper.ITEM_CATEGORY_ID, item.categoryId)
        }
        db.insert(DatabaseHelper.TABLE_SHOPPING_ITEMS, null, values)
    }

    // Updates an existing shopping item by ID
    suspend fun updateShoppingItem(item: ShoppingItem): Int = withContext(Dispatchers.IO) {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(DatabaseHelper.ITEM_NAME, item.name)
            put(DatabaseHelper.ITEM_QUANTITY, item.quantity)
            put(DatabaseHelper.ITEM_CATEGORY_ID, item.categoryId)
        }
        db.update(
            DatabaseHelper.TABLE_SHOPPING_ITEMS,
            values,
            "${DatabaseHelper.ITEM_ID} = ?",
            arrayOf(item.id.toString())
        )
    }

    // Deletes a shopping item by ID
    suspend fun deleteShoppingItem(id: Int): Int = withContext(Dispatchers.IO) {
        val db = dbHelper.writableDatabase
        db.delete(
            DatabaseHelper.TABLE_SHOPPING_ITEMS,
            "${DatabaseHelper.ITEM_ID} = ?",
            arrayOf(id.toString())
        )
    }
}
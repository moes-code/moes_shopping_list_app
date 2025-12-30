package com.moes_code.moes_shopping_list_app.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import androidx.core.database.sqlite.transaction

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_NAME = "shopping_list.db"
        const val DATABASE_VERSION = 1
        const val INDEX_ITEMS_BY_CATEGORY = "index_items_category"

        // Category table
        const val TABLE_CATEGORIES = "categories"
        const val CATEGORY_ID = "id"
        const val CATEGORY_NAME = "name"

        // Shopping items table
        const val TABLE_SHOPPING_ITEMS = "shopping_items"
        const val ITEM_ID = "id"
        const val ITEM_NAME = "name"
        const val ITEM_QUANTITY = "quantity"
        const val ITEM_CATEGORY_ID = "category_id"
    }

    // Enable foreign key constraints for the database connection
    override fun onConfigure(db: SQLiteDatabase) {
        super.onConfigure(db)
        db.setForeignKeyConstraintsEnabled(true)
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.transaction {
            // Create categories table with unique name
            val createCategoriesTable = """
                CREATE TABLE $TABLE_CATEGORIES (
                    $CATEGORY_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                    $CATEGORY_NAME TEXT NOT NULL UNIQUE
                )
            """.trimIndent()

            // Create shopping items table with foreign key referencing categories
            // ON DELETE CASCADE ensures items are removed when their category is deleted
            val createItemsTable = """
                CREATE TABLE $TABLE_SHOPPING_ITEMS (
                    $ITEM_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                    $ITEM_NAME TEXT NOT NULL,
                    $ITEM_QUANTITY INTEGER NOT NULL DEFAULT 1,
                    $ITEM_CATEGORY_ID INTEGER NOT NULL,
                    FOREIGN KEY ($ITEM_CATEGORY_ID) REFERENCES $TABLE_CATEGORIES($CATEGORY_ID) ON DELETE CASCADE
                )
            """.trimIndent()

            execSQL(createCategoriesTable)
            execSQL(createItemsTable)

            // Create index to speed up queries filtering by category
            val createIndex = "CREATE INDEX IF NOT EXISTS $INDEX_ITEMS_BY_CATEGORY ON $TABLE_SHOPPING_ITEMS($ITEM_CATEGORY_ID)"
            execSQL(createIndex)

            // Insert initial category rows if table is empty
            insertDefaultCategories(this)
        }
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.transaction {
            // Placeholder for future migrations
        }
    }

    // Insert default categories only when categories table is empty
    private fun insertDefaultCategories(db: SQLiteDatabase) {
        val defaultCategories = listOf("Beverages", "Groceries", "Personal care")

        val cursor = db.rawQuery("SELECT COUNT(*) FROM $TABLE_CATEGORIES", null)
        var count = 0
        cursor.use {
            if (it.moveToNext()) count = it.getInt(0)
        }

        if (count > 0) return

        defaultCategories.forEach { categoryName ->
            val values = ContentValues().apply {
                put(CATEGORY_NAME, categoryName)
            }
            // Use CONFLICT_IGNORE to avoid errors if a category already exists
            db.insertWithOnConflict(TABLE_CATEGORIES, null, values, SQLiteDatabase.CONFLICT_IGNORE)
        }
    }
}
package com.moes_code.moes_shopping_list_app.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.moes_code.moes_shopping_list_app.database.dao.CategoryDao
import com.moes_code.moes_shopping_list_app.database.dao.ShoppingItemDao
import com.moes_code.moes_shopping_list_app.model.Category
import com.moes_code.moes_shopping_list_app.model.ShoppingItem
import java.util.concurrent.Executors

@Database(
    entities = [Category::class, ShoppingItem::class],
    version = 1,
    exportSchema = false
)
abstract class ShoppingDatabase : RoomDatabase() {
    
    abstract fun categoryDao(): CategoryDao
    abstract fun shoppingItemDao(): ShoppingItemDao
    
    companion object {
        private const val DATABASE_NAME = "shopping_list.db"
        
        @Volatile
        private var INSTANCE: ShoppingDatabase? = null
        
        fun getInstance(context: Context): ShoppingDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }
        }
        
        private fun buildDatabase(context: Context): ShoppingDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                ShoppingDatabase::class.java,
                DATABASE_NAME
            )
                .fallbackToDestructiveMigration(dropAllTables = true)
                .addCallback(SeedDatabaseCallback(context))
                .build()
        }
        
        private class SeedDatabaseCallback(
            private val context: Context
        ) : Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                // Use a single-thread executor to seed default categories
                Executors.newSingleThreadExecutor().execute {
                    getInstance(context).categoryDao().apply {
                        insertSync(Category(name = "Beverages"))
                        insertSync(Category(name = "Groceries"))
                        insertSync(Category(name = "Personal care"))
                    }
                }
            }
        }
    }
}

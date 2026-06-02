package com.moes_code.moes_shopping_list_app.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.moes_code.moes_shopping_list_app.database.dao.CategoryDao
import com.moes_code.moes_shopping_list_app.database.dao.ShoppingItemDao
import com.moes_code.moes_shopping_list_app.model.Category
import com.moes_code.moes_shopping_list_app.model.ShoppingItem

@Database(
    entities = [Category::class, ShoppingItem::class],
    version = 3,
    exportSchema = false
)
abstract class ShoppingDatabase : RoomDatabase() {
    
    abstract fun categoryDao(): CategoryDao
    abstract fun shoppingItemDao(): ShoppingItemDao
    
    companion object {
        private const val DATABASE_NAME = "shopping_list.db"
        
        @Volatile
        private var INSTANCE: ShoppingDatabase? = null

        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    "ALTER TABLE shopping_items ADD COLUMN is_completed INTEGER NOT NULL DEFAULT 0"
                )
            }
        }

        private val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    "ALTER TABLE categories ADD COLUMN isExpanded INTEGER NOT NULL DEFAULT 1"
                )
            }
        }
        
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
                .addMigrations(MIGRATION_1_2, MIGRATION_2_3)
                .addCallback(SeedDatabaseCallback())
                .build()
        }
        
        private class SeedDatabaseCallback : Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                db.execSQL("INSERT INTO categories (name, isExpanded) VALUES ('Beverages', 1)")
                db.execSQL("INSERT INTO categories (name, isExpanded) VALUES ('Groceries', 1)")
                db.execSQL("INSERT INTO categories (name, isExpanded) VALUES ('Personal care', 1)")
            }
        }
    }
}

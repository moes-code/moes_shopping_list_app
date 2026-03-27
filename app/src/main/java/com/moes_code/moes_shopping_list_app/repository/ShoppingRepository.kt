package com.moes_code.moes_shopping_list_app.repository

import android.content.Context
import android.database.sqlite.SQLiteConstraintException
import com.moes_code.moes_shopping_list_app.database.ShoppingDatabase
import com.moes_code.moes_shopping_list_app.database.dao.CategoryDao
import com.moes_code.moes_shopping_list_app.database.dao.ShoppingItemDao
import com.moes_code.moes_shopping_list_app.model.Category
import com.moes_code.moes_shopping_list_app.model.ShoppingItem
import kotlinx.coroutines.flow.Flow

class ShoppingRepository(context: Context) {
    
    private val database = ShoppingDatabase.getInstance(context)
    private val categoryDao: CategoryDao = database.categoryDao()
    private val shoppingItemDao: ShoppingItemDao = database.shoppingItemDao()
    
    // Category operations
    
    fun getAllCategories(): Flow<List<Category>> = categoryDao.getAll()
    
    suspend fun addCategory(category: Category): Long {
        return try {
            categoryDao.insert(category)
        } catch (_: SQLiteConstraintException) {
            -1L // Duplicate name
        }
    }
    
    suspend fun updateCategory(category: Category): Int {
        return try {
            categoryDao.update(category)
        } catch (_: SQLiteConstraintException) {
            -1 // Duplicate name
        }
    }
    
    suspend fun deleteCategory(id: Int): Int = categoryDao.deleteById(id)
    
    // Shopping item operations
    
    fun getShoppingItemsByCategory(categoryId: Int): Flow<List<ShoppingItem>> = 
        shoppingItemDao.getByCategory(categoryId)
    
    suspend fun addShoppingItem(item: ShoppingItem): Long = shoppingItemDao.insert(item)
    
    suspend fun updateShoppingItem(item: ShoppingItem): Int = shoppingItemDao.update(item)
    
    suspend fun deleteShoppingItem(id: Int): Int = shoppingItemDao.deleteById(id)
}

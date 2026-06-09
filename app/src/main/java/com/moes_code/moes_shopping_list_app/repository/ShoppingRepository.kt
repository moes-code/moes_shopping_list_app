package com.moes_code.moes_shopping_list_app.repository

import android.content.Context
import android.database.sqlite.SQLiteConstraintException
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.moes_code.moes_shopping_list_app.database.ShoppingDatabase
import com.moes_code.moes_shopping_list_app.database.dao.CategoryDao
import com.moes_code.moes_shopping_list_app.database.dao.ShoppingItemDao
import com.moes_code.moes_shopping_list_app.model.Category
import com.moes_code.moes_shopping_list_app.model.ShoppingItem
import androidx.room.withTransaction
import kotlinx.coroutines.flow.Flow

data class BackupData(
    @SerializedName("version")
    val version: Int = 1,
    @SerializedName("categories")
    val categories: List<Category>,
    @SerializedName("shoppingItems")
    val shoppingItems: List<ShoppingItem>
)

class ShoppingRepository(context: Context) {

    private val database = ShoppingDatabase.getInstance(context)
    private val categoryDao: CategoryDao = database.categoryDao()
    private val shoppingItemDao: ShoppingItemDao = database.shoppingItemDao()
    private val gson = Gson()

    // Category operations

    fun getAllCategories(): Flow<List<Category>> = categoryDao.getAll()

    fun getAllCategoriesSync(): List<Category> = categoryDao.getAllSync()

    suspend fun addCategory(category: Category): Long {
        return try {
            categoryDao.insert(category)
        } catch (_: SQLiteConstraintException) {
            -1L
        }
    }

    suspend fun updateCategory(category: Category): Int {
        return try {
            categoryDao.update(category)
        } catch (_: SQLiteConstraintException) {
            -1
        }
    }

    suspend fun deleteCategory(id: Int): Int = categoryDao.deleteById(id)

    // Shopping item operations

    fun getShoppingItemsByCategory(categoryId: Int): Flow<List<ShoppingItem>> =
        shoppingItemDao.getByCategory(categoryId)

    fun getAllShoppingItemsSync(): List<ShoppingItem> = shoppingItemDao.getAllSync()

    suspend fun addShoppingItem(item: ShoppingItem): Long = shoppingItemDao.insert(item)

    suspend fun updateShoppingItem(item: ShoppingItem): Int = shoppingItemDao.update(item)

    suspend fun deleteShoppingItem(id: Int): Int = shoppingItemDao.deleteById(id)

    // Backup operations

    fun exportToJson(): String {
        val backup = BackupData(
            categories = getAllCategoriesSync(),
            shoppingItems = getAllShoppingItemsSync()
        )
        return gson.toJson(backup)
    }

    suspend fun importFromJson(jsonString: String): Result<Unit> {
        return try {
            val backup = gson.fromJson(jsonString, BackupData::class.java)
            database.withTransaction {
                shoppingItemDao.deleteAll()
                categoryDao.deleteAll()
                for (category in backup.categories) {
                    categoryDao.insert(category.copy(id = 0))
                }
                val categoryNameToNewId = categoryDao.getAllSync().associate { it.name to it.id }
                for (item in backup.shoppingItems) {
                    val category = backup.categories.find { it.id == item.categoryId }
                    val newCategoryId = categoryNameToNewId[category?.name]
                    if (newCategoryId != null) {
                        shoppingItemDao.insert(
                            item.copy(id = 0, categoryId = newCategoryId)
                        )
                    }
                }
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

package com.moes_code.moes_shopping_list_app.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.moes_code.moes_shopping_list_app.model.ShoppingItem
import kotlinx.coroutines.flow.Flow

@Dao
interface ShoppingItemDao {
    
    @Query("SELECT * FROM shopping_items WHERE category_id = :categoryId ORDER BY name ASC")
    fun getByCategory(categoryId: Int): Flow<List<ShoppingItem>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: ShoppingItem): Long
    
    @Update
    suspend fun update(item: ShoppingItem): Int
    
    @Query("DELETE FROM shopping_items WHERE id = :id")
    suspend fun deleteById(id: Int): Int
}

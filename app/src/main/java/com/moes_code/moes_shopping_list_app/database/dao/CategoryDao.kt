package com.moes_code.moes_shopping_list_app.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.moes_code.moes_shopping_list_app.model.Category
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {
    
    @Query("SELECT * FROM categories ORDER BY name ASC")
    fun getAll(): Flow<List<Category>>
    
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(category: Category): Long
    
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertSync(category: Category): Long
    
    @Update
    suspend fun update(category: Category): Int
    
    @Query("DELETE FROM categories WHERE id = :id")
    suspend fun deleteById(id: Int): Int
}

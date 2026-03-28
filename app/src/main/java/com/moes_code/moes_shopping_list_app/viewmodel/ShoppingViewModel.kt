package com.moes_code.moes_shopping_list_app.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.moes_code.moes_shopping_list_app.model.Category
import com.moes_code.moes_shopping_list_app.model.ShoppingItem
import com.moes_code.moes_shopping_list_app.repository.ShoppingRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class ShoppingViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = ShoppingRepository(application)

    // Loading indicator - true until first data emission
    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // Reactive list of categories - automatically updates when database changes
    val categories: StateFlow<List<Category>> = repository.getAllCategories()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    init {
        // Mark loading as complete after first emission
        viewModelScope.launch {
            categories.first()
            _isLoading.value = false
        }
    }

    // Shopping items grouped by category - automatically updates when database changes
    val shoppingItemsByCategory: StateFlow<Map<Category, List<ShoppingItem>>> = 
        categories.flatMapLatest { categoryList ->
            if (categoryList.isEmpty()) {
                flowOf(emptyMap())
            } else {
                // Combine flows for all categories
                val itemFlows = categoryList.map { category ->
                    repository.getShoppingItemsByCategory(category.id)
                        .combine(flowOf(category)) { items, cat -> cat to items }
                }
                combine(itemFlows) { pairs ->
                    pairs.filter { it.second.isNotEmpty() }
                        .toMap()
                }
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyMap()
        )

    // Optional error message for UI display
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    // Category operations
    fun addCategory(name: String) {
        if (name.isBlank()) {
            _errorMessage.value = "Category name cannot be empty"
            return
        }
        viewModelScope.launch {
            try {
                val category = Category(name = name.trim())
                val result = repository.addCategory(category)
                if (result == -1L) {
                    _errorMessage.value = "Category '${name.trim()}' already exists"
                }
                // No need to reload - Room Flow updates automatically
            } catch (e: Exception) {
                _errorMessage.value = "Error adding category: ${e.message}"
            }
        }
    }

    fun updateCategory(category: Category) {
        if (category.name.isBlank()) {
            _errorMessage.value = "Category name cannot be empty"
            return
        }
        viewModelScope.launch {
            try {
                val result = repository.updateCategory(category)
                if (result == -1) {
                    _errorMessage.value = "Category '${category.name.trim()}' already exists"
                }
                // No need to reload - Room Flow updates automatically
            } catch (e: Exception) {
                _errorMessage.value = "Error updating category: ${e.message}"
            }
        }
    }

    fun deleteCategory(categoryId: Int) {
        viewModelScope.launch {
            try {
                repository.deleteCategory(categoryId)
                // No need to reload - Room Flow updates automatically
            } catch (e: Exception) {
                _errorMessage.value = "Error deleting category: ${e.message}"
            }
        }
    }

    // Shopping item operations
    fun addShoppingItem(name: String, quantity: Int, categoryId: Int) {
        if (name.isBlank()) {
            _errorMessage.value = "Item name cannot be empty"
            return
        }
        if (quantity <= 0) {
            _errorMessage.value = "Quantity must be greater than 0"
            return
        }
        viewModelScope.launch {
            try {
                val item = ShoppingItem(
                    name = name.trim(),
                    quantity = quantity,
                    categoryId = categoryId,
                    isCompleted = false
                )
                repository.addShoppingItem(item)
                // No need to reload - Room Flow updates automatically
            } catch (e: Exception) {
                _errorMessage.value = "Error adding item: ${e.message}"
            }
        }
    }

    fun updateShoppingItem(item: ShoppingItem) {
        if (item.name.isBlank()) {
            _errorMessage.value = "Item name cannot be empty"
            return
        }
        if (item.quantity <= 0) {
            _errorMessage.value = "Quantity must be greater than 0"
            return
        }
        viewModelScope.launch {
            try {
                repository.updateShoppingItem(item)
                // No need to reload - Room Flow updates automatically
            } catch (e: Exception) {
                _errorMessage.value = "Error updating item: ${e.message}"
            }
        }
    }

    fun deleteShoppingItem(itemId: Int) {
        viewModelScope.launch {
            try {
                repository.deleteShoppingItem(itemId)
                // No need to reload - Room Flow updates automatically
            } catch (e: Exception) {
                _errorMessage.value = "Error deleting item: ${e.message}"
            }
        }
    }

    /**
     * Toggle the completed status of a shopping item
     */
    fun toggleItemCompleted(item: ShoppingItem) {
        viewModelScope.launch {
            try {
                val updatedItem = item.copy(isCompleted = !item.isCompleted)
                repository.updateShoppingItem(updatedItem)
                // No need to reload - Room Flow updates automatically
            } catch (e: Exception) {
                _errorMessage.value = "Error updating item: ${e.message}"
            }
        }
    }

    // Helper to clear error state
    fun clearErrorMessage() {
        _errorMessage.value = null
    }
}

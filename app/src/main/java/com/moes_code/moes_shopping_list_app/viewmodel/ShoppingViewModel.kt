package com.moes_code.moes_shopping_list_app.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.moes_code.moes_shopping_list_app.model.Category
import com.moes_code.moes_shopping_list_app.model.ShoppingItem
import com.moes_code.moes_shopping_list_app.repository.ShoppingRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ShoppingViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = ShoppingRepository(application)

    // Reactive list of categories
    private val _categories = MutableStateFlow<List<Category>>(emptyList())
    val categories: StateFlow<List<Category>> = _categories.asStateFlow()

    // Shopping items grouped by category for structured UI
    private val _shoppingItemsByCategory = MutableStateFlow<Map<Category, List<ShoppingItem>>>(emptyMap())
    val shoppingItemsByCategory: StateFlow<Map<Category, List<ShoppingItem>>> = _shoppingItemsByCategory.asStateFlow()

    // Loading indicator
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // Optional error message for UI display
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    init {
        loadData()
    }

    // Load categories and shopping items, set loading state and handle errors
    fun loadData() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                loadCategories()
                loadShoppingItems()
            } catch (e: Exception) {
                _errorMessage.value = "Error loading data: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Fetch all categories from repository
    private suspend fun loadCategories() {
        try {
            _categories.value = repository.getAllCategories()
        } catch (e: Exception) {
            _errorMessage.value = "Error loading categories: ${e.message}"
        }
    }

    // Fetch items for each category and group them
    private suspend fun loadShoppingItems() {
        try {
            val itemsByCategory = mutableMapOf<Category, List<ShoppingItem>>()
            _categories.value.forEach { category ->
                val items = repository.getShoppingItemsByCategory(category.id)
                if (items.isNotEmpty()) {
                    itemsByCategory[category] = items
                }
            }
            _shoppingItemsByCategory.value = itemsByCategory
        } catch (e: Exception) {
            _errorMessage.value = "Error loading shopping items: ${e.message}"
        }
    }

    // Category operations
    fun addCategory(name: String) {
        if (name.isBlank()) {
            _errorMessage.value = "Category name cannot be empty"
            return
        }
        viewModelScope.launch {
            try {
                val category = Category(name = name.trim())
                repository.addCategory(category)
                loadCategories()
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
                repository.updateCategory(category)
                loadCategories()
                loadShoppingItems()
            } catch (e: Exception) {
                _errorMessage.value = "Error updating category: ${e.message}"
            }
        }
    }

    fun deleteCategory(categoryId: Int) {
        viewModelScope.launch {
            try {
                repository.deleteCategory(categoryId)
                loadData()
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
                    categoryId = categoryId
                )
                repository.addShoppingItem(item)
                loadShoppingItems()
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
                loadShoppingItems()
            } catch (e: Exception) {
                _errorMessage.value = "Error updating item: ${e.message}"
            }
        }
    }

    fun deleteShoppingItem(itemId: Int) {
        viewModelScope.launch {
            try {
                repository.deleteShoppingItem(itemId)
                loadShoppingItems()
            } catch (e: Exception) {
                _errorMessage.value = "Error deleting item: ${e.message}"
            }
        }
    }

    // Helper to clear error state
    fun clearErrorMessage() {
        _errorMessage.value = null
    }
}
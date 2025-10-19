package com.moes_code.moes_shopping_list_app.view.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.moes_code.moes_shopping_list_app.model.Category
import com.moes_code.moes_shopping_list_app.model.ShoppingItem
import com.moes_code.moes_shopping_list_app.view.components.CategoryCard
import com.moes_code.moes_shopping_list_app.view.components.dialogs.AddCategoryDialog
import com.moes_code.moes_shopping_list_app.view.components.dialogs.AddItemDialog
import com.moes_code.moes_shopping_list_app.view.components.dialogs.EditItemDialog
import com.moes_code.moes_shopping_list_app.view.theme.Colors
import com.moes_code.moes_shopping_list_app.viewmodel.ShoppingViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingListScreen(
    viewModel: ShoppingViewModel
) {
    val categories by viewModel.categories.collectAsStateWithLifecycle()
    val shoppingItemsByCategory by viewModel.shoppingItemsByCategory.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val errorMessage by viewModel.errorMessage.collectAsStateWithLifecycle()

    var showAddCategoryDialog by remember { mutableStateOf(false) }
    var showAddItemDialog by remember { mutableStateOf(false) }
    var showEditItemDialog by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf<Category?>(null) }
    var selectedItem by remember { mutableStateOf<ShoppingItem?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Shopping List",
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold,
                        color = Colors.secondary,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddCategoryDialog = true },
                containerColor = Colors.third,
                contentColor = Colors.secondary

            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Category")
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                errorMessage != null -> {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = errorMessage ?: "Unknown error",
                            color = MaterialTheme.colorScheme.error
                        )
                        Button(
                            onClick = {
                                viewModel.clearErrorMessage()
                                viewModel.loadData()
                            }
                        ) {
                            Text("Retry")
                        }
                    }
                }

                categories.isEmpty() -> {
                    Text(
                        text = "No categories yet. Add one with the + button!",
                        modifier = Modifier.align(Alignment.Center),
                        fontSize = 18.sp,
                        color = Colors.secondary
                    )
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(categories) { category ->
                            CategoryCard(
                                category = category,
                                items = shoppingItemsByCategory[category] ?: emptyList(),
                                onAddItem = {
                                    selectedCategory = category
                                    showAddItemDialog = true
                                },
                                onEditItem = { item ->
                                    selectedItem = item
                                    showEditItemDialog = true
                                },
                                onDeleteCategory = { viewModel.deleteCategory(category.id) },
                                onDeleteItem = { itemId -> viewModel.deleteShoppingItem(itemId) }
                            )
                        }
                    }
                }
            }
        }
    }

    // Dialogs
    if (showAddCategoryDialog) {
        AddCategoryDialog(
            onDismiss = { showAddCategoryDialog = false },
            onConfirm = { name ->
                viewModel.addCategory(name)
                showAddCategoryDialog = false
            }
        )
    }

    if (showAddItemDialog && selectedCategory != null) {
        AddItemDialog(
            onDismiss = {
                showAddItemDialog = false
                selectedCategory = null
            },
            onConfirm = { name, quantity ->
                viewModel.addShoppingItem(name, quantity, selectedCategory!!.id)
                showAddItemDialog = false
                selectedCategory = null
            }
        )
    }

    if (showEditItemDialog && selectedItem != null) {
        EditItemDialog(
            item = selectedItem!!,
            onDismiss = {
                showEditItemDialog = false
                selectedItem = null
            },
            onConfirm = { name, quantity ->
                val updatedItem = selectedItem!!.copy(
                    name = name,
                    quantity = quantity
                )
                viewModel.updateShoppingItem(updatedItem)
                showEditItemDialog = false
                selectedItem = null
            }
        )
    }
}
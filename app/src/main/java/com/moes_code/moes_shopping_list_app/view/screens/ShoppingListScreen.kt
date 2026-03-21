package com.moes_code.moes_shopping_list_app.view.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.moes_code.moes_shopping_list_app.view.components.dialogs.DeleteConfirmationDialog
import com.moes_code.moes_shopping_list_app.view.components.dialogs.EditCategoryDialog
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

    val snackbarHostState = remember { SnackbarHostState() }

    // Show error messages as Snackbar
    LaunchedEffect(errorMessage) {
        errorMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearErrorMessage()
        }
    }

    var showAddCategoryDialog by remember { mutableStateOf(false) }
    var showAddItemDialog by remember { mutableStateOf(false) }
    var showEditCategoryDialog by remember { mutableStateOf(false) }
    var showEditItemDialog by remember { mutableStateOf(false) }
    var showDeleteCategoryDialog by remember { mutableStateOf(false) }
    var showDeleteItemDialog by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf<Category?>(null) }
    var selectedCategoryToEdit by remember { mutableStateOf<Category?>(null) }
    var selectedCategoryToDelete by remember { mutableStateOf<Category?>(null) }
    var selectedItem by remember { mutableStateOf<ShoppingItem?>(null) }
    var selectedItemToDelete by remember { mutableStateOf<ShoppingItem?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier.padding(bottom = 8.dp),
                title = {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "Shopping List",
                            fontSize = 30.sp,
                            fontWeight = FontWeight.Bold,
                            color = Colors.moe_white,
                            modifier = Modifier
                                .border(
                                    width = 2.dp,
                                    color = Colors.moe_blue,
                                    shape = RoundedCornerShape(16.dp)
                                )
                                .padding(16.dp),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) { data ->
                Snackbar(
                    snackbarData = data,
                    containerColor = Colors.moe_red,
                    contentColor = Colors.moe_white
                )
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddCategoryDialog = true },
                containerColor = Colors.moe_black,
                contentColor = Colors.moe_yellow,
                elevation = FloatingActionButtonDefaults.elevation(
                    defaultElevation = 0.dp,
                    pressedElevation = 0.dp,
                    focusedElevation = 0.dp,
                    hoveredElevation = 0.dp
                ),
                modifier = Modifier
                    .border(
                        width = 2.dp,
                        color = Colors.moe_yellow,
                        shape = RoundedCornerShape(16.dp)
                    )

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

                categories.isEmpty() -> {
                    Text(
                        text = "No categories",
                        modifier = Modifier.align(Alignment.Center),
                        fontSize = 18.sp,
                        color = Colors.moe_white
                    )
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(categories, key = { it.id }) { category ->
                            CategoryCard(
                                category = category,
                                items = shoppingItemsByCategory[category] ?: emptyList(),
                                onAddItem = {
                                    selectedCategory = category
                                    showAddItemDialog = true
                                },
                                onEditCategory = {
                                    selectedCategoryToEdit = category
                                    showEditCategoryDialog = true
                                },
                                onEditItem = { item ->
                                    selectedItem = item
                                    showEditItemDialog = true
                                },
                                onDeleteCategory = {
                                    selectedCategoryToDelete = category
                                    showDeleteCategoryDialog = true
                                },
                                onDeleteItem = { itemId ->
                                    selectedItemToDelete = shoppingItemsByCategory[category]?.find { it.id == itemId }
                                    showDeleteItemDialog = true
                                }
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

    if (showEditCategoryDialog && selectedCategoryToEdit != null) {
        EditCategoryDialog(
            category = selectedCategoryToEdit!!,
            onDismiss = {
                showEditCategoryDialog = false
                selectedCategoryToEdit = null
            },
            onConfirm = { name ->
                val updatedCategory = selectedCategoryToEdit!!.copy(name = name)
                viewModel.updateCategory(updatedCategory)
                showEditCategoryDialog = false
                selectedCategoryToEdit = null
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

    // Delete confirmation dialogs
    if (showDeleteCategoryDialog && selectedCategoryToDelete != null) {
        DeleteConfirmationDialog(
            title = "Delete Category",
            message = "Are you sure you want to delete '${selectedCategoryToDelete!!.name}'? All items in this category will also be deleted.",
            onDismiss = {
                showDeleteCategoryDialog = false
                selectedCategoryToDelete = null
            },
            onConfirm = {
                viewModel.deleteCategory(selectedCategoryToDelete!!.id)
            }
        )
    }

    if (showDeleteItemDialog && selectedItemToDelete != null) {
        DeleteConfirmationDialog(
            title = "Delete Item",
            message = "Are you sure you want to delete '${selectedItemToDelete!!.name}'?",
            onDismiss = {
                showDeleteItemDialog = false
                selectedItemToDelete = null
            },
            onConfirm = {
                viewModel.deleteShoppingItem(selectedItemToDelete!!.id)
            }
        )
    }
}
package com.moes_code.moes_shopping_list_app.view.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.moes_code.moes_shopping_list_app.R
import com.moes_code.moes_shopping_list_app.model.Category
import com.moes_code.moes_shopping_list_app.model.ShoppingItem
import com.moes_code.moes_shopping_list_app.view.components.CategoryCard
import com.moes_code.moes_shopping_list_app.view.components.dialogs.AddCategoryDialog
import com.moes_code.moes_shopping_list_app.view.components.dialogs.AddItemDialog
import com.moes_code.moes_shopping_list_app.view.components.dialogs.DeleteConfirmationDialog
import com.moes_code.moes_shopping_list_app.view.components.dialogs.EditCategoryDialog
import com.moes_code.moes_shopping_list_app.view.components.dialogs.EditItemDialog
import com.moes_code.moes_shopping_list_app.view.theme.Dimensions
import com.moes_code.moes_shopping_list_app.viewmodel.ShoppingViewModel

/**
 * Sealed class representing the different UI states
 */
private sealed class ScreenState {
    data object Loading : ScreenState()
    data object Empty : ScreenState()
    data class Content(val categories: List<Category>) : ScreenState()
}

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
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    
    // Derive screen state
    val screenState by remember {
        derivedStateOf {
            when {
                isLoading -> ScreenState.Loading
                categories.isEmpty() -> ScreenState.Empty
                else -> ScreenState.Content(categories)
            }
        }
    }
    
    // FAB expanded state based on scroll
    val isFabExpanded by remember {
        derivedStateOf {
            scrollBehavior.state.collapsedFraction < 0.5f
        }
    }

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
    
    // Trigger to reset swipe states when dialogs close
    var swipeResetTrigger by remember { mutableIntStateOf(0) }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            ShoppingListTopBar(scrollBehavior = scrollBehavior)
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) { data ->
                Snackbar(
                    snackbarData = data,
                    containerColor = MaterialTheme.colorScheme.errorContainer,
                    contentColor = MaterialTheme.colorScheme.onErrorContainer,
                    shape = MaterialTheme.shapes.medium
                )
            }
        },
        floatingActionButton = {
            AnimatedVisibility(
                visible = screenState !is ScreenState.Loading,
                enter = scaleIn(
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessLow
                    )
                ) + fadeIn(),
                exit = scaleOut() + fadeOut()
            ) {
                ExtendedFloatingActionButton(
                    onClick = { showAddCategoryDialog = true },
                    expanded = isFabExpanded,
                    icon = {
                        Icon(
                            Icons.Default.Add,
                            contentDescription = stringResource(R.string.action_add_category)
                        )
                    },
                    text = { Text(stringResource(R.string.action_add_category)) },
                    containerColor = MaterialTheme.colorScheme.secondary,
                    contentColor = MaterialTheme.colorScheme.onSecondary
                )
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        AnimatedContent(
            targetState = screenState,
            transitionSpec = {
                fadeIn(animationSpec = spring(stiffness = Spring.StiffnessLow)) togetherWith
                    fadeOut(animationSpec = spring(stiffness = Spring.StiffnessLow))
            },
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            label = "ScreenStateAnimation"
        ) { state ->
            when (state) {
                is ScreenState.Loading -> {
                    LoadingContent()
                }
                is ScreenState.Empty -> {
                    EmptyContent()
                }
                is ScreenState.Content -> {
                    CategoryList(
                        categories = state.categories,
                        shoppingItemsByCategory = shoppingItemsByCategory,
                        swipeResetTrigger = swipeResetTrigger,
                        onAddItem = { category ->
                            selectedCategory = category
                            showAddItemDialog = true
                        },
                        onEditCategory = { category ->
                            selectedCategoryToEdit = category
                            showEditCategoryDialog = true
                        },
                        onEditItem = { item ->
                            selectedItem = item
                            showEditItemDialog = true
                        },
                        onDeleteCategory = { category ->
                            selectedCategoryToDelete = category
                            showDeleteCategoryDialog = true
                        },
                        onDeleteItem = { item ->
                            selectedItemToDelete = item
                            showDeleteItemDialog = true
                        },
                        onToggleItemCompleted = { item ->
                            viewModel.toggleItemCompleted(item)
                        }
                    )
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
                swipeResetTrigger++
            },
            onConfirm = { name ->
                val updatedCategory = selectedCategoryToEdit!!.copy(name = name)
                viewModel.updateCategory(updatedCategory)
                showEditCategoryDialog = false
                selectedCategoryToEdit = null
                swipeResetTrigger++
            }
        )
    }

    if (showEditItemDialog && selectedItem != null) {
        EditItemDialog(
            item = selectedItem!!,
            onDismiss = {
                showEditItemDialog = false
                selectedItem = null
                swipeResetTrigger++
            },
            onConfirm = { name, quantity ->
                val updatedItem = selectedItem!!.copy(
                    name = name,
                    quantity = quantity
                )
                viewModel.updateShoppingItem(updatedItem)
                showEditItemDialog = false
                selectedItem = null
                swipeResetTrigger++
            }
        )
    }

    // Delete confirmation dialogs
    if (showDeleteCategoryDialog && selectedCategoryToDelete != null) {
        DeleteConfirmationDialog(
            title = stringResource(R.string.dialog_title_delete_category),
            message = stringResource(R.string.dialog_message_delete_category, selectedCategoryToDelete!!.name),
            onDismiss = {
                showDeleteCategoryDialog = false
                selectedCategoryToDelete = null
                swipeResetTrigger++
            },
            onConfirm = {
                viewModel.deleteCategory(selectedCategoryToDelete!!.id)
                swipeResetTrigger++
            }
        )
    }

    if (showDeleteItemDialog && selectedItemToDelete != null) {
        DeleteConfirmationDialog(
            title = stringResource(R.string.dialog_title_delete_item),
            message = stringResource(R.string.dialog_message_delete_item, selectedItemToDelete!!.name),
            onDismiss = {
                showDeleteItemDialog = false
                selectedItemToDelete = null
                swipeResetTrigger++
            },
            onConfirm = {
                viewModel.deleteShoppingItem(selectedItemToDelete!!.id)
                swipeResetTrigger++
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ShoppingListTopBar(
    scrollBehavior: TopAppBarScrollBehavior
) {
    LargeTopAppBar(
        title = {
            Text(
                text = stringResource(R.string.screen_title_shopping_list),
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background,
            scrolledContainerColor = MaterialTheme.colorScheme.background,
            titleContentColor = MaterialTheme.colorScheme.onBackground
        ),
        scrollBehavior = scrollBehavior
    )
}

@Composable
private fun LoadingContent() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
private fun EmptyContent() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.empty_categories_title),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(Dimensions.emptyContentSpacing))
            Text(
                text = stringResource(R.string.empty_categories_subtitle),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun CategoryList(
    categories: List<Category>,
    shoppingItemsByCategory: Map<Category, List<ShoppingItem>>,
    swipeResetTrigger: Int,
    onAddItem: (Category) -> Unit,
    onEditCategory: (Category) -> Unit,
    onEditItem: (ShoppingItem) -> Unit,
    onDeleteCategory: (Category) -> Unit,
    onDeleteItem: (ShoppingItem) -> Unit,
    onToggleItemCompleted: (ShoppingItem) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            start = Dimensions.listPaddingHorizontal,
            end = Dimensions.listPaddingHorizontal,
            top = Dimensions.listPaddingTop,
            bottom = Dimensions.listPaddingBottom
        ),
        verticalArrangement = Arrangement.spacedBy(Dimensions.listItemSpacing)
    ) {
        items(
            items = categories,
            key = { it.id }
        ) { category ->
            CategoryCard(
                category = category,
                items = shoppingItemsByCategory[category] ?: emptyList(),
                onAddItem = { onAddItem(category) },
                onEditCategory = { onEditCategory(category) },
                onEditItem = onEditItem,
                onDeleteCategory = { onDeleteCategory(category) },
                onDeleteItem = onDeleteItem,
                onToggleItemCompleted = onToggleItemCompleted,
                swipeResetTrigger = swipeResetTrigger,
                modifier = Modifier.animateItem(
                    fadeInSpec = spring(stiffness = Spring.StiffnessLow),
                    fadeOutSpec = spring(stiffness = Spring.StiffnessLow),
                    placementSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessLow
                    )
                )
            )
        }
    }
}

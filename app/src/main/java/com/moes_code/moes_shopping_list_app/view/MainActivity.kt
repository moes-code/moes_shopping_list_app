package com.moes_code.moes_shopping_list_app.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.moes_code.moes_shopping_list_app.view.screens.ShoppingListScreen
import com.moes_code.moes_shopping_list_app.view.theme.ShoppingListAppTheme
import com.moes_code.moes_shopping_list_app.viewmodel.ShoppingViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ShoppingListAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val viewModel: ShoppingViewModel = viewModel()
                    ShoppingListScreen(viewModel = viewModel)
                }
            }
        }
    }
}
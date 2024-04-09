package com.nhom9.message.screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.nhom9.message.MViewModel

@Composable
fun StatusScreen(navController: NavController, viewModel: MViewModel) {
    Text(text = "Status Screen")
    BottomNavigationMenu(
        selectedItem = BottomNavigationItem.STATUSLIST,
        navController = navController
    )
}
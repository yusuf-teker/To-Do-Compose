package com.example.to_do.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.example.to_do.navigation.destinations.listComposable
import com.example.to_do.navigation.destinations.taskComposable
import com.example.to_do.ui.viewmodels.SharedViewModel
import com.example.to_do.util.Constants.LIST_SCREEN

@Composable
fun SetupNavigation(
    navController: NavHostController,
    sharedViewModel: SharedViewModel
) {
    val screen = remember(navController){
        Screeens(navController)
    }
    
    NavHost(navController = navController, startDestination = LIST_SCREEN ){
        listComposable(
            navigateToTaskScreen = screen.task,
            sharedViewModel = sharedViewModel
        )
        taskComposable(
            navigateToListScreen = screen.list
        )
    }
}
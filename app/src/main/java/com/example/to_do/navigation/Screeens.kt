package com.example.to_do.navigation

import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavController
import com.example.to_do.util.Action
import com.example.to_do.util.Constants.LIST_SCREEN
import com.example.to_do.util.Constants.SPLASH_SCREEN

class Screeens(navController: NavController) {

    //navigate to list
    val task: (Action) -> Unit = { action ->
        navController.navigate("list/${action.name}"){
            popUpTo(LIST_SCREEN){ inclusive = true}//inclusive -> list ekranına gelince önceki ekranı siler bu durumda Task Ekranı backstack'ten kaldırılacak
        }
    }

    //Navigate to task detail
    val list: (Int) -> Unit = { taskId ->
        navController.navigate("task/$taskId"){

        }
    }
}
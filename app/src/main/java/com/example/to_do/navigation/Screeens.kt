package com.example.to_do.navigation

import androidx.navigation.NavController
import com.example.to_do.util.Action
import com.example.to_do.util.Constants.LIST_SCREEN

class Screeens(navController: NavController) {

    val list: (Action) -> Unit = { action ->
        navController.navigate("list/${action.name}"){
            popUpTo(LIST_SCREEN){ inclusive = true}//inclusive -> list ekranına gelince önceki ekranı siler bu durumda Task Ekranı backstack'ten kaldırılacak
        }
    }

    val task: (Int) -> Unit = { taskId ->
        navController.navigate("task/$taskId"){

        }
    }
}
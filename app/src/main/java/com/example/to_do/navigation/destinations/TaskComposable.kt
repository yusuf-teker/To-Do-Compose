package com.example.to_do.navigation.destinations

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.to_do.ui.screens.task.TaskScreen
import com.example.to_do.ui.viewmodels.SharedViewModel
import com.example.to_do.util.Action
import com.example.to_do.util.Constants.TASK_ARGUMENT_KEY
import com.example.to_do.util.Constants.TASK_SCREEN

fun NavGraphBuilder.taskComposable(
    navigateToListScreen: (Action) -> Unit,
    sharedViewModel: SharedViewModel
) {
    composable(
        route = TASK_SCREEN,
        arguments = listOf(navArgument(TASK_ARGUMENT_KEY){
            type = NavType.IntType
        }),
        enterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(700)
            )
        },
        exitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(700)
            )
        },
    ){ navBackStackEntry ->
        val taskId: Int = navBackStackEntry.arguments!!.getInt(TASK_ARGUMENT_KEY)
        TaskScreen( navigateToListScreen , taskId, sharedViewModel)
    }
}
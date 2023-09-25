package com.example.to_do.ui.screens.task

import android.annotation.SuppressLint
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.to_do.ui.viewmodels.SharedViewModel
import com.example.to_do.util.Action


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun TaskScreen(
    navigateToListScreen: (Action) -> Unit,
    taskId: Int,
    sharedViewModel: SharedViewModel
) {
    val selectedTask by sharedViewModel.selectedTask.collectAsState()
    LaunchedEffect(key1 = true){
        sharedViewModel.getSelectedTask(taskId)
    }
    Scaffold(
        topBar = {

            TaskAppBar(navigateToListScreen = navigateToListScreen, selectedTask)
        },
        content = {},
    )
}

package com.example.to_do.ui.screens.task

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import com.example.to_do.data.models.Priority
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

    val title: String by sharedViewModel.newTaskTitle
    val description: String by sharedViewModel.newTaskDescription
    val priority: Priority by sharedViewModel.newTaskPriority
    val context = LocalContext.current

    LaunchedEffect(key1 = true){
        sharedViewModel.getSelectedTask(taskId)

    }
    Scaffold(
        topBar = {

            TaskAppBar(
                navigateToListScreen = { action ->
                    sharedViewModel.handleDatabaseAction(action)

                    if (action == Action.NO_ACTION){
                        navigateToListScreen(action)
                    }else{
                        if (sharedViewModel.validateFields()){
                            navigateToListScreen(action)
                        }else{
                            displayInvalidFieldToast(context)
                        }
                    }
                },
                selectedTask
            )
        },
        content = {
            TaskConent(
                title = title,
                onTitleChange = { sharedViewModel.setNewTaskTitle(it) },
                description = description,
                onDescriptionChange = { sharedViewModel.setNewTaskDescription(it) },
                priority = priority,
                onPrioritySelected = { sharedViewModel.setNewTaskPriority(it)}
            )
        },
    )
}

fun displayInvalidFieldToast(context: Context) {
        Toast.makeText(context, "Invalid Fields", Toast.LENGTH_LONG).show()
}

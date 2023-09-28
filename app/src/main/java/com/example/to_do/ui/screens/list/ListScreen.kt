package com.example.to_do.ui.screens.list

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.example.to_do.R
import com.example.to_do.components.DisplayAlertDialog
import com.example.to_do.data.models.ToDoTask
import com.example.to_do.ui.theme.fabBackgroundColor
import com.example.to_do.ui.viewmodels.SharedViewModel
import com.example.to_do.util.Action
import com.example.to_do.util.Constants.CREATE_NEW_TASK_ID
import com.example.to_do.util.RequestState
import com.example.to_do.util.SearchAppBarState
import kotlinx.coroutines.launch

@Composable
fun ListScreen(
    navigateToTaskScreen: (taskId: Int) -> Unit,
    sharedViewModel: SharedViewModel
) {
    LaunchedEffect(key1 = false, block = {sharedViewModel.getAllTask()})
    val allTasks: RequestState<List<ToDoTask>> by sharedViewModel.allTasks.collectAsState()
    val saarchTasks: RequestState<List<ToDoTask>> by sharedViewModel.searchedTasks.collectAsState()
    val searchAppBarState: SearchAppBarState by sharedViewModel.searchAppBarState
    val searchTextState: String by sharedViewModel.searchTextState

    val action: Action by sharedViewModel.action.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    DisplaySnackBack(
        snackbarHostState,
        sharedViewModel.newTaskTitle.value,
        action,
        onUndoClicked = {
            sharedViewModel.undoLastDeletedTask()
        }
    )
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            ListAppBar(
                sharedViewModel,
                searchAppBarState,
                searchTextState
            )
        },
        floatingActionButton = {
            ListFab(onFabClicked ={
                navigateToTaskScreen(it)
                sharedViewModel.clearSearchAppBarState()
                }
            )
        }
    ) {
        it
        ListContent(
            allTasks,
            saarchTasks,
            searchTextState,
            navigateToTaskScreen,
        )
    }
}

@Composable
fun ListFab(onFabClicked: (taskId: Int) -> Unit) {
    FloatingActionButton(
        onClick = {
            onFabClicked(CREATE_NEW_TASK_ID)
        },
        containerColor = MaterialTheme.colorScheme.fabBackgroundColor,
        shape = CircleShape
    ) {
        Icon(
            imageVector = Icons.Filled.Add,
            contentDescription = stringResource(R.string.add_button),
            tint = Color.White,
        )
    }
}


@Composable
fun DisplaySnackBack(
    snackbarHostState: SnackbarHostState,
    taskTitle: String,
    action: Action,
    onUndoClicked: () -> Unit
){
    val scope = rememberCoroutineScope()
    LaunchedEffect(key1 = action){
        scope.launch {
            if (action != Action.NO_ACTION){
                val snackBarResult = snackbarHostState.showSnackbar(
                    message = setSnackbarMessage(action, taskTitle),
                    actionLabel =  setSnackbarLabel(action, taskTitle)
                )

                if (snackBarResult == SnackbarResult.ActionPerformed && action == Action.DELETE) {
                    onUndoClicked()
                }
            }

        }
    }
}
private fun setSnackbarMessage(action: Action, taskTitle: String): String{
    return when(action){
        Action.DELETE_ALL -> "All Tasks Removed."
        else -> "${action.name}: ${taskTitle}"
    }
}
private fun setSnackbarLabel(action: Action, taskTitle: String): String{
    return when(action){
        Action.DELETE -> "UNDO"
        else -> "OK"
    }
}
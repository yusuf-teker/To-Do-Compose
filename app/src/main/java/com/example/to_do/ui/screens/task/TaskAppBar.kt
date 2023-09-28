package com.example.to_do.ui.screens.task

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.example.to_do.components.DisplayAlertDialog
import com.example.to_do.data.models.Priority
import com.example.to_do.data.models.ToDoTask
import com.example.to_do.ui.theme.topAppBarBackgroundColor
import com.example.to_do.ui.theme.topAppBarContentColor
import com.example.to_do.util.Action
import com.example.to_do.util.RequestState


@Composable
fun TaskAppBar(navigateToListScreen: (Action) -> Unit, selectedTask: RequestState<ToDoTask?>) {
    if (selectedTask is RequestState.Idle || selectedTask is RequestState.Loading || (selectedTask is RequestState.Success && selectedTask.data == null)){
        NewTaskAppBar(navigateToListScreen)
    }else if (selectedTask is RequestState.Success && selectedTask.data!=null) {
        ExistingTaskAppBar(selectedTask.data, navigateToListScreen)
    }else{
        NewTaskAppBar(navigateToListScreen)
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewTaskAppBar(
    navigateToListScreen: (Action) -> Unit
) {
    TopAppBar(
        navigationIcon = {
            BackAction(onBackClicked = navigateToListScreen)
        },
        title = {
            Text(
                text = "New Task", color = MaterialTheme.colorScheme.topAppBarContentColor
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.topAppBarBackgroundColor
        ),
        actions = {
            AddAction(navigateToListScreen)
        }
    )
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExistingTaskAppBar(
    selectedTask: ToDoTask,
    navigateToListScreen: (Action) -> Unit
) {
    TopAppBar(
        navigationIcon = {
            CloseAction(onCloseClicked = navigateToListScreen)
        },
        title = {
            Text(
                text = selectedTask.title,
                color = MaterialTheme.colorScheme.topAppBarContentColor,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.topAppBarBackgroundColor
        ),
        actions = {
            ExistingTaskAppBarActions(selectedTask, navigateToListScreen)
        }
    )
}

@Composable
fun ExistingTaskAppBarActions(
    selectedTask: ToDoTask,
    navigateToListScreen: (Action) -> Unit
){
    var openDialog by remember {
        mutableStateOf(false)
    }
    
    DisplayAlertDialog(title = "Remove ${selectedTask.title}?", message = "Are you sure to delete the ${selectedTask.title}?", openDialog = openDialog, onCloseDialog = { openDialog = false }) {
        navigateToListScreen(Action.DELETE)
    }
    DeleteAction( onDeleteClicked = { openDialog = true })
    UpdateAction(navigateToListScreen)
}
@Composable
fun BackAction(
    onBackClicked: (Action) -> Unit
){
    IconButton(onClick = { onBackClicked(Action.NO_ACTION) }) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = "Back To List Button",
            tint = MaterialTheme.colorScheme.topAppBarContentColor
        )
    }
}
@Composable
fun AddAction(
    onAddClicked: (Action) -> Unit
){
    IconButton(onClick = { onAddClicked(Action.ADD) }) {
        Icon(
            imageVector = Icons.Filled.Check,
            contentDescription = "Add To List Button",
            tint = MaterialTheme.colorScheme.topAppBarContentColor
        )
    }
}
@Composable
fun CloseAction(
    onCloseClicked: (Action) -> Unit
){
    IconButton(onClick = { onCloseClicked(Action.NO_ACTION) }) {
        Icon(
            imageVector = Icons.Filled.Close,
            contentDescription = "Close Button",
            tint = MaterialTheme.colorScheme.topAppBarContentColor
        )
    }
}

@Composable
fun DeleteAction(
    onDeleteClicked: () -> Unit
){
    IconButton(onClick = { onDeleteClicked() }) {
        Icon(
            imageVector = Icons.Filled.Delete,
            contentDescription = "Delete Button",
            tint = MaterialTheme.colorScheme.topAppBarContentColor
        )
    }
}

@Composable
fun UpdateAction(
    onUpdateClicked: (Action) -> Unit
){
    IconButton(onClick = { onUpdateClicked(Action.UPDATE) }) {
        Icon(
            imageVector = Icons.Filled.Check,
            contentDescription = "Update Button",
            tint = MaterialTheme.colorScheme.topAppBarContentColor
        )
    }
}
@Composable
@Preview
fun NewTaskAppBarkPreview(){
    NewTaskAppBar {}
}

@Composable
@Preview
fun ExistingTaskAppBarPreview(){
    ExistingTaskAppBar(ToDoTask(0,"Title", "AA",Priority.HIGH)) {}
}
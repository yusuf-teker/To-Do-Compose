package com.example.to_do.ui.screens.list

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.DismissDirection.EndToStart
import androidx.compose.material3.DismissValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.to_do.components.Loading
import com.example.to_do.data.models.Priority
import com.example.to_do.data.models.ToDoTask
import com.example.to_do.ui.theme.HighPriorityColor
import com.example.to_do.ui.theme.LARGE_PADDING
import com.example.to_do.ui.theme.PRIORITY_INDICATOR_SIZE
import com.example.to_do.ui.theme.TASK_ITEM_ELEVATION
import com.example.to_do.ui.theme.TOP_APP_BAR_HEIGHT
import com.example.to_do.ui.theme.taskItemBackgroundColor
import com.example.to_do.ui.theme.taskItemTitleColor
import com.example.to_do.util.Action
import com.example.to_do.util.RequestState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@Composable
fun ListContent(
    allTasks: RequestState<List<ToDoTask>>,
    lowPriorityTasks: List<ToDoTask>,
    highPriorityTasks: List<ToDoTask>,
    sortState: RequestState<Priority>,
    searchTasks: RequestState<List<ToDoTask>>,
    searchText: String,
    navigateToTaskScreen: (taskId: Int) -> Unit,
    onSwipeToDelete: (Action, ToDoTask) -> Unit
) {

    if (searchText.isNotEmpty()) {
        HandleListContent(searchTasks, navigateToTaskScreen,  onSwipeToDelete)
    } else {
        if (sortState is RequestState.Success) {
            when (sortState.data) {
                Priority.LOW -> HandleListContent(
                    RequestState.Success(lowPriorityTasks), navigateToTaskScreen,  onSwipeToDelete
                )

                Priority.HIGH -> HandleListContent(
                    RequestState.Success(highPriorityTasks), navigateToTaskScreen,  onSwipeToDelete
                )

                else -> HandleListContent(allTasks, navigateToTaskScreen,  onSwipeToDelete)
            }
        } else {
            HandleListContent(allTasks, navigateToTaskScreen,  onSwipeToDelete)
        }

    }


}

@Composable
fun HandleListContent(
    tasks: RequestState<List<ToDoTask>>, navigateToTaskScreen: (taskId: Int) -> Unit, onSwipeToDelete: (Action, ToDoTask) -> Unit
) {
    if (tasks is RequestState.Success) {
        if (tasks.data.isEmpty()) {
            EmptyListContent()
        } else {
            DisplayTasks(tasks.data, navigateToTaskScreen, onSwipeToDelete)
        }
    } else if (tasks is RequestState.Loading) {
        Loading()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DisplayTasks(
    taskList: List<ToDoTask>,
    navigateToTaskScreen: (taskId: Int) -> Unit,
    onSwipeToDelete: (Action, ToDoTask) -> Unit

) {

    //  Todo: Geri eklenen obje listeyi düzeltmiyor(aynı id degerine sahip oldugu icin olabilir)
    LazyColumn(modifier = Modifier.padding(top = TOP_APP_BAR_HEIGHT)) {
        items(taskList.size, key = {
            taskList[it].id
        }) { it ->
            val dismissState = rememberDismissState()
            val dismissDirection = dismissState.dismissDirection
            val isDismissed = dismissState.isDismissed(DismissDirection.EndToStart)
            if (isDismissed && dismissDirection == DismissDirection.EndToStart) {
                val scope = rememberCoroutineScope()
                SideEffect {
                    scope.launch {
                        delay(300)
                        onSwipeToDelete(Action.DELETE, taskList[it])
                    }
                }


            }
            val degrees by animateFloatAsState(
                targetValue = if (dismissState.targetValue == DismissValue.Default) 0f else -45f,
                label = ""
            )

            var itemAppeared by remember{ mutableStateOf(false) }//başlangıçta true verilirse unvisible'dan visible'a geçme animasyonasu gözükmez
            LaunchedEffect(key1 = true, block = {itemAppeared = true})
            AnimatedVisibility(
                visible = itemAppeared && !isDismissed,
                enter = expandVertically(
                    animationSpec = tween(
                        durationMillis = 300
                    )
                ),
                exit = shrinkHorizontally(
                    animationSpec = tween(
                        durationMillis = 300
                    )
                )
            ) {
                SwipeToDismiss(
                    state = dismissState,
                    directions = setOf(EndToStart),
                    background = {
                        RedBackground(degrees = degrees)
                    },

                    dismissContent =  {TaskItem(toDoTask = taskList[it], navigateToTaskScreen = navigateToTaskScreen)},

                    )
            }


        }
    }
}


@Composable
fun RedBackground(degrees: Float){


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(HighPriorityColor)
            .padding(24.dp),
        contentAlignment = Alignment.CenterEnd
    ) {
        Icon(
            modifier = Modifier.rotate(degrees),
            imageVector = Icons.Filled.Delete,
            contentDescription = "Delete the task",
            tint = Color.White
        )
    }
}

@Composable
fun TaskItem(
    toDoTask: ToDoTask, navigateToTaskScreen: (taskId: Int) -> Unit
) {

    Surface(modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.taskItemBackgroundColor,
        shape = RectangleShape,
        shadowElevation = TASK_ITEM_ELEVATION,
        onClick = {
            navigateToTaskScreen(toDoTask.id)
        }) {

        Column(
            modifier = Modifier
                .padding(all = LARGE_PADDING)
                .fillMaxWidth()
        ) {
            Row(horizontalArrangement = Arrangement.SpaceBetween) {
                Text(
                    text = toDoTask.title,
                    color = MaterialTheme.colorScheme.taskItemTitleColor,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    modifier = Modifier.weight(1f)
                )
                Box {
                    Canvas(modifier = Modifier.size(PRIORITY_INDICATOR_SIZE), onDraw = {
                        drawCircle(
                            color = toDoTask.priority.color
                        )
                    })
                }
            }
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = toDoTask.description,
                color = MaterialTheme.colorScheme.taskItemTitleColor,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

        }
    }

}

@Composable
@Preview
fun TaskItemPreview() {
    TaskItem(toDoTask = ToDoTask(
        0, "title1", "description sadasda asdas1231 23asdad", Priority.HIGH
    ), navigateToTaskScreen = {})
}
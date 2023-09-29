package com.example.to_do.ui.screens.list

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.example.to_do.components.Loading
import com.example.to_do.data.models.Priority
import com.example.to_do.data.models.ToDoTask
import com.example.to_do.ui.theme.LARGE_PADDING
import com.example.to_do.ui.theme.PRIORITY_INDICATOR_SIZE
import com.example.to_do.ui.theme.TASK_ITEM_ELEVATION
import com.example.to_do.ui.theme.TOP_APP_BAR_HEIGHT
import com.example.to_do.ui.theme.taskItemBackgroundColor
import com.example.to_do.ui.theme.taskItemTitleColor
import com.example.to_do.util.RequestState


@Composable
fun ListContent(
    allTasks: RequestState<List<ToDoTask>>,
    lowPriorityTasks: List<ToDoTask>,
    highPriorityTasks: List<ToDoTask>,
    sortState: RequestState<Priority>,
    searchTasks: RequestState<List<ToDoTask>>,
    searchText: String,
    navigateToTaskScreen: (taskId: Int) -> Unit
) {

    if (searchText.isNotEmpty()) {
        HandleListContent(searchTasks, navigateToTaskScreen)
    } else {
       if (sortState is RequestState.Success){
           when(sortState.data){
               Priority.LOW -> HandleListContent(RequestState.Success(lowPriorityTasks), navigateToTaskScreen)
               Priority.HIGH -> HandleListContent(RequestState.Success(highPriorityTasks), navigateToTaskScreen)
               else ->  HandleListContent(allTasks, navigateToTaskScreen)
           }
       }else{
           HandleListContent(allTasks, navigateToTaskScreen)
       }

    }


}

@Composable
fun HandleListContent(
    tasks: RequestState<List<ToDoTask>>, navigateToTaskScreen: (taskId: Int) -> Unit
) {
    if (tasks is RequestState.Success) {
        if (tasks.data.isEmpty()) {
            EmptyListContent()
        } else {
            DisplayTasks(taskList = tasks.data, navigateToTaskScreen = navigateToTaskScreen)
        }
    } else if (tasks is RequestState.Loading) {
        Loading()
    }
}

@Composable
fun DisplayTasks(
    taskList: List<ToDoTask>, navigateToTaskScreen: (taskId: Int) -> Unit
) {
    LazyColumn(modifier = Modifier.padding(top = TOP_APP_BAR_HEIGHT)) {
        items(taskList.size, key = {
            taskList[it].id
        }

        ) {
            TaskItem(toDoTask = taskList[it], navigateToTaskScreen = navigateToTaskScreen)
        }

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
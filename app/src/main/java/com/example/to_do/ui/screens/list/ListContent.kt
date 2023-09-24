package com.example.to_do.ui.screens.list

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.example.to_do.data.models.Priority
import com.example.to_do.data.models.ToDoTask
import com.example.to_do.ui.theme.LARGE_PADDING
import com.example.to_do.ui.theme.PRIORITY_INDICATOR_SIZE
import com.example.to_do.ui.theme.TASK_ITEM_ELEVATION
import com.example.to_do.ui.theme.taskItemBackgroundColor
import com.example.to_do.ui.theme.taskItemTitleColor


@Composable
fun ListContent()    {

}


@Composable
fun TaskItem(
    toDoTask: ToDoTask,
    navigateToTaskScreen: (taskId: Int) -> Unit
){

    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.taskItemBackgroundColor,
        shape = RectangleShape,
        shadowElevation = TASK_ITEM_ELEVATION,
        onClick = {
            navigateToTaskScreen(toDoTask.id)
        }
    ) {

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
                Box{
                    Canvas(
                        modifier = Modifier.size(PRIORITY_INDICATOR_SIZE),
                        onDraw = {
                            drawCircle(
                                color = toDoTask.priority.color
                            )
                        }
                    )
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
fun TaskItemPreview(){
    TaskItem(toDoTask = ToDoTask(0,"title1","description sadasda asdas1231 23asdad", Priority.HIGH), navigateToTaskScreen = {})
}
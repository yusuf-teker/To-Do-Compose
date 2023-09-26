package com.example.to_do.ui.screens.task

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.to_do.components.PriorityDropDown
import com.example.to_do.data.models.Priority
import com.example.to_do.ui.theme.LARGE_PADDING
import com.example.to_do.ui.theme.SMALL_PADDING
import com.example.to_do.ui.theme.TOP_APP_BAR_HEIGHT

@Composable
fun TaskConent(
    title: String,
    onTitleChange: (String) -> Unit,
    description: String,
    onDescriptionChange: (String) -> Unit,
    priority: Priority,
    onPrioritySelected: (Priority) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(top = TOP_APP_BAR_HEIGHT, start = LARGE_PADDING, end = LARGE_PADDING, bottom = LARGE_PADDING)


    ) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = title,
            onValueChange = {
                onTitleChange(it)
            },
            label = {
              Text(text = "Title")
            },
            textStyle = MaterialTheme.typography.bodyMedium,
            singleLine = true,

        )
        Spacer(modifier = Modifier.padding(top = SMALL_PADDING))
        PriorityDropDown(priority = priority, onPrioritySelected = onPrioritySelected)
        OutlinedTextField(
            modifier = Modifier.fillMaxSize(),
            value = description,
            onValueChange = {
                onDescriptionChange(it)
            },
            label = {
                Text(text = "Description")
            },
            textStyle = MaterialTheme.typography.bodyMedium,
        )
    }
}

@Composable
@Preview
fun TaskContentPreview(){
    TaskConent(
        title = "",
        onTitleChange = {},
        description = "",
        onDescriptionChange = {},
        priority = Priority.HIGH,
        onPrioritySelected = {}
    )
}
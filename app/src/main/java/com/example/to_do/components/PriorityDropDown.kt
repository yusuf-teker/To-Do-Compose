package com.example.to_do.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material3.ContentAlpha
import androidx.wear.compose.material3.Icon
import com.example.to_do.data.models.Priority
import com.example.to_do.ui.theme.PRIORITY_DROP_DOWN_HEIGHT
import com.example.to_do.ui.theme.PRIORITY_INDICATOR_SIZE

@Composable
fun PriorityDropDown(
    priority: Priority, onPrioritySelected: (Priority) -> Unit
) {
    var expanded by remember {
        mutableStateOf(false)
    }
    val angle: Float by animateFloatAsState(
        targetValue = if (expanded) 180f else 0f, label = ""
    )

    var parentSize by remember {
        mutableStateOf(IntSize.Zero)
    }

    Row(modifier = Modifier
        .fillMaxWidth()
        .height(PRIORITY_DROP_DOWN_HEIGHT)
        .onGloballyPositioned {
            parentSize = it.size //size -> Rowun sizeını alır
        }
        .clickable {
            expanded = !expanded
        }
        .background(MaterialTheme.colorScheme.background)
        .border(
            width = 1.dp,
            color = MaterialTheme.colorScheme.onSurface,
            shape = MaterialTheme.shapes.small
        ),

        verticalAlignment = Alignment.CenterVertically) {
        Canvas(
            modifier = Modifier
                .size(PRIORITY_INDICATOR_SIZE)
                .weight(1f)
        ) {
            drawCircle(color = priority.color)
        }
        Text(
            text = priority.name,
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier.weight(8f)
        )
        IconButton(
            onClick = { expanded = true },
            modifier = Modifier
                .alpha(ContentAlpha.medium)
                .rotate(angle)
                .weight(1.5f)
        ) {
            Icon(
                imageVector = Icons.Filled.ArrowDropDown,
                contentDescription = "Drop-Down- Arrow Icon",
                tint = MaterialTheme.colorScheme.onBackground
            )
        }

        DropdownMenu(
            modifier = Modifier.width(with(LocalDensity.current){parentSize.width.toDp()} ),
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {

            Priority.values().slice(0..2).forEach {
                DropdownMenuItem(onClick = {
                    expanded = false
                    onPrioritySelected(it)
                }, text = {
                    PriorityItem(priority = it)
                })
            }

        }

    }
}

@Composable
@Preview
fun PriorityDropDownPreview() {
    PriorityDropDown(priority = Priority.HIGH, onPrioritySelected = {})
}
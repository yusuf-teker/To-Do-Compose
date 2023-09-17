package com.example.to_do.data.models

import androidx.compose.ui.graphics.Color
import com.example.to_do.ui.theme.HighPriorityColor
import com.example.to_do.ui.theme.LowPriorityColor
import com.example.to_do.ui.theme.MediumPriorityColor
import com.example.to_do.ui.theme.NonePriorityColor

enum class Priority(val color: Color) {
    HIGH(color = HighPriorityColor),
    MEDIUM(color = MediumPriorityColor),
    LOW(color = LowPriorityColor),
    NONE(color = NonePriorityColor)
}
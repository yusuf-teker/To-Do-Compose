package com.example.to_do.data.models

import androidx.compose.ui.graphics.Color
import com.example.to_do.ui.theme.HighPriorityColor
import com.example.to_do.ui.theme.LowPriorityColor
import com.example.to_do.ui.theme.MediumGray
import com.example.to_do.ui.theme.MediumPriorityColor

enum class Priority(val color: Color) {
    HIGH(color = HighPriorityColor),
    MEDIUM(color = MediumPriorityColor),
    LOW(color = LowPriorityColor),
    NONE(color = MediumGray)
}
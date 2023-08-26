package com.soethan.todocompose.data.models

import androidx.compose.ui.graphics.Color
import com.soethan.todocompose.ui.theme.HighPriorityColor
import com.soethan.todocompose.ui.theme.LowPriorityColor
import com.soethan.todocompose.ui.theme.MediumPriorityColor
import com.soethan.todocompose.ui.theme.NonePriorityColor

enum class Priority(val color: Color) {
    HIGH(HighPriorityColor),
    MEDIUM(MediumPriorityColor),
    LOW(LowPriorityColor),
    NONE(NonePriorityColor)
}
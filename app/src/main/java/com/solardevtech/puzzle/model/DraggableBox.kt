package com.solardevtech.puzzle.model

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color

data class DraggableBox(
    val id: Int,
    val color: Color,
    val isSnapped: Boolean,
    val snappedPosition: Offset? = null
)

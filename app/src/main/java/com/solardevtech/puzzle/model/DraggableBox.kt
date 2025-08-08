package com.solardevtech.puzzle.model

import androidx.compose.ui.graphics.Color

data class DraggableBox(
    val id: Int,
    val color: Color,
    var isSnapped: Boolean = false,
)
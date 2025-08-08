package com.solardevtech.puzzle.model

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap

data class DraggableBox(
    val id: Int,
    val image: ImageBitmap? = null,
    val isSnapped: Boolean,
    val snappedPosition: Offset? = null
)

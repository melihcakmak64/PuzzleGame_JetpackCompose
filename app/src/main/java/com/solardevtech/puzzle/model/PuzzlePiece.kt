package com.solardevtech.puzzle.model

import androidx.compose.ui.geometry.Offset

data class PuzzlePiece(
    val id: Int,
    val correctPosition: Offset,
    var currentPosition: Offset,
    var isSnapped: Boolean = false,
    val isBeingDragged: Boolean = false
)

package com.solardevtech.puzzle.model

import androidx.compose.ui.geometry.Offset

data class PuzzlePiece(
    val id: Int,
    val correctRow: Int,
    val correctCol: Int,
    val currentRow: Int,
    val currentCol: Int,
    val offsetX: Float = 0f,
    val offsetY: Float = 0f,
    val isDragging: Boolean = false
) {
    val isInCorrectPosition: Boolean
        get() = currentRow == correctRow && currentCol == correctCol
}

data class PuzzleGameState(
    val pieces: List<PuzzlePiece> = emptyList(),
    val isGameWon: Boolean = false,
    val gridSize: Int = 3
)
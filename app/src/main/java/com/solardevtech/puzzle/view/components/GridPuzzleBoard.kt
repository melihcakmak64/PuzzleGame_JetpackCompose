package com.solardevtech.puzzle.view.components

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color

@Composable
fun GridPuzzleBoard(
    puzzleLeftPx: Float,
    puzzleTopPx: Float,
    puzzleSizePx: Float
) {
    Canvas(modifier = Modifier) {
        val puzzleRightPx = puzzleLeftPx + puzzleSizePx
        val puzzleBottomPx = puzzleTopPx + puzzleSizePx
        val strokeWidth = 2f
        val step = puzzleSizePx / 3

        for (i in 1 until 3) {
            val x = puzzleLeftPx + i * step
            drawLine(Color.Black, Offset(x, puzzleTopPx), Offset(x, puzzleBottomPx), strokeWidth)
        }
        for (i in 1 until 3) {
            val y = puzzleTopPx + i * step
            drawLine(Color.Black, Offset(puzzleLeftPx, y), Offset(puzzleRightPx, y), strokeWidth)
        }
        drawRect(
            Color.Black,
            topLeft = Offset(puzzleLeftPx, puzzleTopPx),
            size = androidx.compose.ui.geometry.Size(puzzleSizePx, puzzleSizePx),
            style = androidx.compose.ui.graphics.drawscope.Stroke(strokeWidth)
        )
    }
}

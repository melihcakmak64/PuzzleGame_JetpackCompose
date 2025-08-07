package com.solardevtech.puzzle.view.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import com.solardevtech.puzzle.model.PuzzlePiece

@Composable
fun PuzzlePieceComponent(
    piece: PuzzlePiece,  // data class model nesnesi
    pieceSizeDp: Dp,
) {
    Box(
        modifier = Modifier
            .size(pieceSizeDp)
            .background(Color.LightGray),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = piece.id.toString(),
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
            color = Color.Black
        )
    }

}

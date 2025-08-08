package com.solardevtech.puzzle.util

import androidx.compose.foundation.background
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

fun Modifier.appBackground() = this.background(
    brush = Brush.verticalGradient(
        colors = listOf(Color(0xFFE0F7FA), Color(0xFFFFFFFF))
    )
)

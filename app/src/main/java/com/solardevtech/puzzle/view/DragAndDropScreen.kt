package com.solardevtech.puzzle.view


import PuzzleBoxList
import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.solardevtech.puzzle.viewmodel.DragAndDropViewModel

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun DragAndDropWithGridScreen(
    viewModel: DragAndDropViewModel = viewModel()
) {
    val density = LocalDensity.current

    // Grid ayarları
    val boxSizeDp: Dp = 100.dp
    val gridSizeDp = boxSizeDp * viewModel.cellCountPerRow

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val gridSizePx = with(density) { gridSizeDp.toPx() }
        val cellSizePx = gridSizePx / viewModel.cellCountPerRow

        // Grid'i ekranın ortasına yerleştirmek için başlangıç noktası
        val startX = (constraints.maxWidth - gridSizePx) / 2f
        val startY = (constraints.maxHeight - gridSizePx) / 2f
        val gridStart = Offset(startX, startY)

        // Grid ve yerleşmiş kutular
        PuzzleGrid(
            viewModel = viewModel,
            gridStart = gridStart,
            cellSizePx = cellSizePx,
            boxSizeDp = boxSizeDp
        )

        // Alt tarafta kaydırılabilir sürüklenebilir kutular
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 40.dp)
        ) {
            PuzzleBoxList(
                boxList = viewModel.boxList.filter { !it.isSnapped },
                boxSizeDp = boxSizeDp
            )
        }
    }
}

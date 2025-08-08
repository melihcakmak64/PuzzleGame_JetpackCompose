package com.solardevtech.puzzle.view.screens


import PuzzleBoxList
import android.graphics.BitmapFactory
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.solardevtech.puzzle.R
import com.solardevtech.puzzle.view.components.PuzzleGrid
import com.solardevtech.puzzle.viewmodel.DragAndDropViewModel
import androidx.compose.ui.platform.LocalWindowInfo

@Composable
fun DragAndDropWithGridScreen(
    viewModel: DragAndDropViewModel = viewModel()
) {
    val density = LocalDensity.current
    val context = LocalContext.current
    val windowSizePx = LocalWindowInfo.current.containerSize

    val boxSizeDp = 100.dp
    val gridSizeDp = boxSizeDp * viewModel.cellCountPerRow
    val gridSizePx = with(density) { gridSizeDp.toPx() }
    val cellSizePx = gridSizePx / viewModel.cellCountPerRow

    // Gerçek pencere boyutlarına göre grid'in başlangıç noktası
    val gridStart = Offset(
        x = (windowSizePx.width - gridSizePx) / 2f,
        y = (windowSizePx.height - gridSizePx) / 2f
    )

    LaunchedEffect(Unit) {
        val bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.sample)
        viewModel.generateBoxesFromImage(bitmap)
    }

    Scaffold(modifier = Modifier.fillMaxSize()) {
        innerPadding->
        PuzzleGrid(
            viewModel = viewModel,
            gridStart = gridStart,
            cellSizePx = cellSizePx,
            boxSizeDp = boxSizeDp
        )

        Box(
            modifier = Modifier.fillMaxSize()
                .padding(innerPadding),
                    contentAlignment = Alignment.BottomCenter

        ) {
            PuzzleBoxList(
                boxList = viewModel.boxList.filter { !it.isSnapped },
                boxSizeDp = boxSizeDp
            )
        }
    }
}
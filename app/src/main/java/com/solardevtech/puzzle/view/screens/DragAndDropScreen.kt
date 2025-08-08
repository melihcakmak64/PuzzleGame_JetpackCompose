package com.solardevtech.puzzle.view.screens


import PuzzleBoxList
import android.content.Context
import android.graphics.Bitmap
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
import androidx.core.graphics.drawable.toBitmap
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun DragAndDropWithGridScreen(
    imageUrl: String,
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

    LaunchedEffect(imageUrl) {
        if (imageUrl.isNotBlank()) {
            val bitmap = loadBitmapFromUrl(context, imageUrl)
            if (bitmap != null) {
                viewModel.generateBoxesFromImage(bitmap)
            }
        }
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
suspend fun loadBitmapFromUrl(context: Context, url: String): Bitmap? {
    return withContext(Dispatchers.IO) {
        try {
            val loader = ImageLoader(context)
            val request = ImageRequest.Builder(context)
                .data(url)
                .allowHardware(false) // bitmap olarak işlemek için gerekli
                .build()
            val result = loader.execute(request)
            if (result is SuccessResult) {
                result.drawable.toBitmap()
            } else null
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
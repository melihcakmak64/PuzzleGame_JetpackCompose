package com.solardevtech.puzzle.view.components

import android.content.ClipDescription
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.draganddrop.dragAndDropTarget
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draganddrop.DragAndDropEvent
import androidx.compose.ui.draganddrop.DragAndDropTarget
import androidx.compose.ui.draganddrop.mimeTypes
import androidx.compose.ui.draganddrop.toAndroidDragEvent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import com.solardevtech.puzzle.viewmodel.DragAndDropViewModel
import kotlin.math.roundToInt

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PuzzleGrid(
    viewModel: DragAndDropViewModel,
    gridStart: Offset,
    cellSizePx: Float,
    boxSizeDp: Dp
) {
    val gridSizeDp = boxSizeDp * viewModel.cellCountPerRow

    Box(
        modifier = Modifier
            .size(gridSizeDp)
            .offset {
                IntOffset(gridStart.x.roundToInt(), gridStart.y.roundToInt())
            }
            .background(Color(0xFFE0E0E0))
            .dragAndDropTarget(
                shouldStartDragAndDrop = { event ->
                    event.mimeTypes().contains(ClipDescription.MIMETYPE_TEXT_PLAIN)
                },
                target = remember {
                    object : DragAndDropTarget {
                        override fun onDrop(event: DragAndDropEvent): Boolean {
                            val clipData = event.toAndroidDragEvent().clipData
                            val text = clipData?.getItemAt(0)?.text?.toString()
                            val pos = Offset(event.toAndroidDragEvent().x, event.toAndroidDragEvent().y)
                            if (text != null && text.startsWith("id:")) {
                                val draggedId = text.removePrefix("id:").toIntOrNull() ?: return false
                                return viewModel.onDropReceived(draggedId, pos, gridStart, cellSizePx)
                            }
                            return false
                        }
                    }
                }
            )
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            for (i in 1 until viewModel.cellCountPerRow) {
                val pos = i * cellSizePx
                drawLine(Color.Gray, Offset(pos, 0f), Offset(pos, size.height), 2f)
                drawLine(Color.Gray, Offset(0f, pos), Offset(size.width, pos), 2f)
            }
        }

        viewModel.boxList
            .filter { it.isSnapped }
            .forEach { box ->
                Box(
                    modifier = Modifier
                        .offset {
                            IntOffset(
                                box.snappedPosition!!.x.roundToInt(),
                                box.snappedPosition.y.roundToInt()
                            )
                        }
                        .size(boxSizeDp)
                        .background(box.color)
                )
            }

    }
}

package com.solardevtech.puzzle.viewmodel


import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.lifecycle.ViewModel
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import com.solardevtech.puzzle.model.DraggableBox
import kotlin.random.Random

class DragAndDropViewModel : ViewModel() {

    val cellCountPerRow = 3
    val boxCount = 9

    // Tüm kutular (snapped olsun olmasın)
    var boxList = mutableStateListOf<DraggableBox>()


    init {
        generateBoxes()
    }

    private fun generateBoxes() {
        boxList.clear()
        boxList.addAll(
            List(boxCount) { id ->
                DraggableBox(
                    id = id,
                    color = Color(Random.nextLong()).copy(alpha = 1f),
                    isSnapped = false
                )
            }
        )
    }

    fun onDropReceived(draggedId: Int, dropPosition: Offset, gridStart: Offset, cellSizePx: Float): Boolean {
        val cell = getCellIndexForPosition(dropPosition, gridStart, cellSizePx)
        cell?.let { (row, col) ->
            val expectedId = row * cellCountPerRow + col
            if (draggedId == expectedId) {
                val snappedPos = Offset(col * cellSizePx, row * cellSizePx)
                val index = boxList.indexOfFirst { it.id == draggedId }
                if (index != -1) {
                    boxList[index] = boxList[index].copy(isSnapped = true, snappedPosition = snappedPos)
                }
                return true
            }
        }
        return false
    }

    private fun getCellIndexForPosition(pos: Offset, gridStartPx: Offset, cellSizePx: Float): Pair<Int, Int>? {
        val relativeX = pos.x - gridStartPx.x
        val relativeY = pos.y - gridStartPx.y
        if (relativeX < 0 || relativeY < 0) return null
        if (relativeX > cellSizePx * cellCountPerRow || relativeY > cellSizePx * cellCountPerRow) return null
        val col = (relativeX / cellSizePx).toInt()
        val row = (relativeY / cellSizePx).toInt()
        return row to col
    }

}

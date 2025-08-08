package com.solardevtech.puzzle.viewmodel


import android.content.Context
import android.graphics.Bitmap
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.asImageBitmap
import androidx.lifecycle.viewModelScope
import com.solardevtech.puzzle.data.repository.ImageRepository
import com.solardevtech.puzzle.model.DraggableBox
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class DragAndDropViewModel @Inject constructor( private val repository: ImageRepository
) : ViewModel(){

    val cellCountPerRow = 3

    // Tüm kutular (snapped olsun olmasın)
    var boxList = mutableStateListOf<DraggableBox>()

    fun generateBoxesFromImage(bitmap: Bitmap) {
        boxList.clear()
        val pieceWidth = bitmap.width / cellCountPerRow
        val pieceHeight = bitmap.height / cellCountPerRow

        val tempList = mutableListOf<DraggableBox>()
        for (row in 0 until cellCountPerRow) {
            for (col in 0 until cellCountPerRow) {
                val piece = Bitmap.createBitmap(
                    bitmap,
                    col * pieceWidth,
                    row * pieceHeight,
                    pieceWidth,
                    pieceHeight
                )
                tempList.add(
                    DraggableBox(
                        id = row * cellCountPerRow + col,
                        image = piece.asImageBitmap(),
                        isSnapped = false
                    )
                )
            }
        }

        // Karıştır (isteğe bağlı)
        // tempList.shuffle()
        boxList.addAll(tempList)
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

    fun loadImageAndGenerateBoxes(context: Context,url: String) {
        viewModelScope.launch {

            val bitmap = withContext(Dispatchers.IO) {
                repository.loadBitmapFromUrl(context = context, url =url)
            }
            if (bitmap != null) {
                generateBoxesFromImage(bitmap)
            }}

    }

}

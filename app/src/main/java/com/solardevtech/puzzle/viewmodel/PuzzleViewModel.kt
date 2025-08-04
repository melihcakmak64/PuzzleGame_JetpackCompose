import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.compose.ui.geometry.Offset
import com.solardevtech.puzzle.model.PuzzlePiece
import kotlin.random.Random
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.math.abs

class PuzzleViewModel : ViewModel() {
    private val gridSize = 3
    private val totalPieces = gridSize * gridSize

    private var puzzleLeftPx = 0f
    private var puzzleTopPx = 0f
    private var pieceSizePx = 0f

    private val _pieces = mutableStateListOf<PuzzlePiece>()
    val pieces: List<PuzzlePiece> get() = _pieces

    private val _gameCompleted = mutableStateOf(false)
    val gameCompleted: State<Boolean> get() = _gameCompleted

    fun initPositions(puzzleLeftPx: Float, puzzleTopPx: Float, pieceSizePx: Float) {
        this.puzzleLeftPx = puzzleLeftPx
        this.puzzleTopPx = puzzleTopPx
        this.pieceSizePx = pieceSizePx

        val correctPositions = List(totalPieces) { index ->
            val row = index / gridSize
            val col = index % gridSize
            Offset(puzzleLeftPx + col * pieceSizePx, puzzleTopPx + row * pieceSizePx)
        }

        _pieces.clear()

        val random = Random(System.currentTimeMillis())
        repeat(totalPieces) { i ->
            _pieces.add(
                PuzzlePiece(
                    id = i,
                    correctPosition = correctPositions[i],
                    currentPosition = Offset(
                        x = random.nextFloat() * (puzzleLeftPx * 2 + pieceSizePx),
                        y = random.nextFloat() * (puzzleTopPx * 2 + pieceSizePx)
                    ),
                    isSnapped = false
                )
            )
        }

        _gameCompleted.value = false
    }

    private val snapThreshold by lazy { pieceSizePx / 3f }

    fun onDrag(pieceId: Int, dragAmountX: Float, dragAmountY: Float, screenWidthPx: Float, screenHeightPx: Float) {
        val index = _pieces.indexOfFirst { it.id == pieceId }
        if (index == -1) return
        val piece = _pieces[index]
        if (piece.isSnapped) return

        val newX = (piece.currentPosition.x + dragAmountX).coerceIn(0f, screenWidthPx - pieceSizePx)
        val newY = (piece.currentPosition.y + dragAmountY).coerceIn(0f, screenHeightPx - pieceSizePx)

        _pieces[index] = piece.copy(currentPosition = Offset(newX, newY))
    }

    fun onDragEnd(pieceId: Int) {
        val index = _pieces.indexOfFirst { it.id == pieceId }
        if (index == -1) return
        val piece = _pieces[index]

        val distX = abs(piece.currentPosition.x - piece.correctPosition.x)
        val distY = abs(piece.currentPosition.y - piece.correctPosition.y)

        if (distX < snapThreshold && distY < snapThreshold) {
            _pieces[index] = piece.copy(currentPosition = piece.correctPosition, isSnapped = true)
            checkGameCompleted()
        }
    }

    private fun checkGameCompleted() {
        if (_pieces.all { it.isSnapped }) {
            _gameCompleted.value = true
        }
    }
}

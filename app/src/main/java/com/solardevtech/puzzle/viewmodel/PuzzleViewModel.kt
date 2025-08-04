import androidx.lifecycle.ViewModel
import com.solardevtech.puzzle.model.PuzzleGameState
import com.solardevtech.puzzle.model.PuzzlePiece
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.random.Random

class PuzzleGameViewModel : ViewModel() {
    private val _gameState = MutableStateFlow(PuzzleGameState())
    val gameState: StateFlow<PuzzleGameState> = _gameState.asStateFlow()

    init {
        initializeGame()
    }

    private fun initializeGame() {
        val pieces = mutableListOf<PuzzlePiece>()
        val positions = mutableListOf<Pair<Int, Int>>()

        // 3x3 grid için tüm pozisyonları oluştur
        for (row in 0 until 3) {
            for (col in 0 until 3) {
                positions.add(Pair(row, col))
            }
        }

        // Pozisyonları karıştır
        positions.shuffle(Random.Default)

        // Parçaları oluştur
        for (i in 0 until 9) {
            val correctRow = i / 3
            val correctCol = i % 3
            val currentPosition = positions[i]

            pieces.add(
                PuzzlePiece(
                    id = i,
                    correctRow = correctRow,
                    correctCol = correctCol,
                    currentRow = currentPosition.first,
                    currentCol = currentPosition.second
                )
            )
        }

        _gameState.value = PuzzleGameState(pieces = pieces)
    }

    fun startDragging(pieceId: Int) {
        val currentPieces = _gameState.value.pieces.toMutableList()
        val index = currentPieces.indexOfFirst { it.id == pieceId }

        if (index != -1) {
            currentPieces[index] = currentPieces[index].copy(isDragging = true)
            _gameState.value = _gameState.value.copy(pieces = currentPieces)
        }
    }

    fun updatePieceOffset(pieceId: Int, offsetX: Float, offsetY: Float) {
        val currentPieces = _gameState.value.pieces.toMutableList()
        val index = currentPieces.indexOfFirst { it.id == pieceId }

        if (index != -1) {
            currentPieces[index] = currentPieces[index].copy(
                offsetX = offsetX,
                offsetY = offsetY
            )
            _gameState.value = _gameState.value.copy(pieces = currentPieces)
        }
    }

    fun dropPiece(pieceId: Int, dropX: Float, dropY: Float, cellSize: Float) {
        val currentPieces = _gameState.value.pieces.toMutableList()
        val index = currentPieces.indexOfFirst { it.id == pieceId }

        if (index != -1) {
            val piece = currentPieces[index]

            // Hangi hücreye en yakın olduğunu hesapla
            val targetRow = ((dropY / cellSize).toInt()).coerceIn(0, 2)
            val targetCol = ((dropX / cellSize).toInt()).coerceIn(0, 2)

            // Hedef pozisyonda başka parça var mı kontrol et
            val targetOccupied = currentPieces.any {
                it.id != pieceId && it.currentRow == targetRow && it.currentCol == targetCol
            }

            if (!targetOccupied) {
                // Parçayı yeni pozisyona yerleştir
                currentPieces[index] = piece.copy(
                    currentRow = targetRow,
                    currentCol = targetCol,
                    offsetX = 0f,
                    offsetY = 0f,
                    isDragging = false
                )
            } else {
                // Eski pozisyona geri dön
                currentPieces[index] = piece.copy(
                    offsetX = 0f,
                    offsetY = 0f,
                    isDragging = false
                )
            }

            _gameState.value = _gameState.value.copy(pieces = currentPieces)
            checkWinCondition()
        }
    }

    private fun checkWinCondition() {
        val allCorrect = _gameState.value.pieces.all { it.isInCorrectPosition }
        if (allCorrect && !_gameState.value.isGameWon) {
            _gameState.value = _gameState.value.copy(isGameWon = true)
        }
    }

    fun resetGame() {
        initializeGame()
    }
}

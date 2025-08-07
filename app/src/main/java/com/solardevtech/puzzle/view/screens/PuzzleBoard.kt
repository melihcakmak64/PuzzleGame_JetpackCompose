import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.solardevtech.puzzle.view.components.PuzzlePieceComponent
import kotlin.math.roundToInt

@Composable
fun NumberPuzzleGame(viewModel: PuzzleViewModel = androidx.lifecycle.viewmodel.compose.viewModel()) {
    val configuration = LocalConfiguration.current
    val density = LocalDensity.current

    val screenWidthPx = with(density) { configuration.screenWidthDp.dp.toPx() }
    val screenHeightPx = with(density) { configuration.screenHeightDp.dp.toPx() }

    val puzzleSizeDp = 300.dp
    val pieceSizeDp = puzzleSizeDp / 3
    val puzzleSizePx = with(density) { puzzleSizeDp.toPx() }
    val pieceSizePx = with(density) { pieceSizeDp.toPx() }

    val puzzleLeftPx = (screenWidthPx - puzzleSizePx) / 2f
    val puzzleTopPx = (screenHeightPx - puzzleSizePx) / 2f
    val puzzleRightPx = puzzleLeftPx + puzzleSizePx
    val puzzleBottomPx = puzzleTopPx + puzzleSizePx

    val pieces = viewModel.pieces
    val gameCompleted by viewModel.gameCompleted

    LaunchedEffect(puzzleLeftPx, puzzleTopPx, pieceSizePx) {
        viewModel.initPositions(puzzleLeftPx, puzzleTopPx, pieceSizePx)
    }

    Box(
        modifier = Modifier.fillMaxSize().background(Color.White)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val strokeWidth = 2f
            val step = puzzleSizePx / 3

            for (i in 1 until 3) {
                val x = puzzleLeftPx + i * step
                drawLine(Color.Black, Offset(x, puzzleTopPx), Offset(x, puzzleBottomPx), strokeWidth)
            }
            for (i in 1 until 3) {
                val y = puzzleTopPx + i * step
                drawLine(Color.Black, Offset(puzzleLeftPx, y), Offset(puzzleRightPx, y), strokeWidth)
            }
            drawRect(
                Color.Black,
                topLeft = Offset(puzzleLeftPx, puzzleTopPx),
                size = androidx.compose.ui.geometry.Size(puzzleSizePx, puzzleSizePx),
                style = androidx.compose.ui.graphics.drawscope.Stroke(strokeWidth)
            )
        }

        pieces.forEach { piece ->
            Box(
                modifier = Modifier
                    .offset { IntOffset(piece.currentPosition.x.roundToInt(), piece.currentPosition.y.roundToInt()) }
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

            PuzzlePieceComponent(
                piece = piece,
                pieceSizeDp = pieceSizeDp,
                onDrag = { dx, dy -> viewModel.onDrag(piece.id, dx, dy, screenWidthPx, screenHeightPx) },
                onDragEnd = { viewModel.onDragEnd(piece.id) }
            )
        }

        if (gameCompleted) {
            Box(
                Modifier.fillMaxSize().background(Color(0xAA000000)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "Tebrikler! Puzzle tamamlandı.",
                    color = Color.White,
                    style = MaterialTheme.typography.headlineMedium
                )
            }
        }
    }
}

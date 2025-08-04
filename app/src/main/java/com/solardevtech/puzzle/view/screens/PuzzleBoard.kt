import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlin.math.abs
import kotlin.math.roundToInt
import kotlin.random.Random

@Composable
fun NumberPuzzleGame() {
    val gridSize = 3
    val totalPieces = gridSize * gridSize

    val configuration = LocalConfiguration.current
    val density = LocalDensity.current

    // Ekran boyutları piksel cinsinden
    val screenWidthPx = with(density) { configuration.screenWidthDp.dp.toPx() }
    val screenHeightPx = with(density) { configuration.screenHeightDp.dp.toPx() }

    val puzzleSizeDp = 300.dp
    val pieceSizeDp = puzzleSizeDp / gridSize
    val puzzleSizePx = with(density) { puzzleSizeDp.toPx() }
    val pieceSizePx = with(density) { pieceSizeDp.toPx() }

    // Puzzle grid ortada olacak
    val puzzleLeftPx = (screenWidthPx - puzzleSizePx) / 2f
    val puzzleTopPx = (screenHeightPx - puzzleSizePx) / 2f
    val puzzleRightPx = puzzleLeftPx + puzzleSizePx
    val puzzleBottomPx = puzzleTopPx + puzzleSizePx

    // Doğru pozisyonlar puzzle grid içinde (ekran koordinatları)
    val correctPositions = List(totalPieces) { index ->
        val row = index / gridSize
        val col = index % gridSize
        Offset(puzzleLeftPx + col * pieceSizePx, puzzleTopPx + row * pieceSizePx)
    }

    // Parçaların rastgele pozisyonları (tüm ekran içinde)
    val piecesPositions = remember {
        mutableStateListOf<Offset>().apply {
            for (i in 0 until totalPieces) {
                add(
                    Offset(
                        x = Random.nextFloat() * (screenWidthPx - pieceSizePx),
                        y = Random.nextFloat() * (screenHeightPx - pieceSizePx)
                    )
                )
            }
        }
    }

    val snapped = remember { mutableStateListOf<Boolean>().apply { repeat(totalPieces) { add(false) } } }
    val snapThreshold = pieceSizePx / 3f

    var gameCompleted by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Grid çizgileri ve dış çerçeve (hazır ölçülerle)
        Canvas(modifier = Modifier.fillMaxSize()) {
            val strokeWidth = 2f
            val step = puzzleSizePx / gridSize

            // Dikey çizgiler
            for (i in 1 until gridSize) {
                val x = puzzleLeftPx + i * step
                drawLine(
                    color = Color.Black,
                    start = Offset(x, puzzleTopPx),
                    end = Offset(x, puzzleBottomPx),
                    strokeWidth = strokeWidth
                )
            }
            // Yatay çizgiler
            for (i in 1 until gridSize) {
                val y = puzzleTopPx + i * step
                drawLine(
                    color = Color.Black,
                    start = Offset(puzzleLeftPx, y),
                    end = Offset(puzzleRightPx, y),
                    strokeWidth = strokeWidth
                )
            }
            // Dış çerçeve
            drawRect(
                color = Color.Black,
                topLeft = Offset(puzzleLeftPx, puzzleTopPx),
                size = androidx.compose.ui.geometry.Size(puzzleSizePx, puzzleSizePx),
                style = androidx.compose.ui.graphics.drawscope.Stroke(width = strokeWidth)
            )
        }

        // Parçalar (kutular + sayılar)
        for (i in 0 until totalPieces) {
            val pos = piecesPositions[i]
            val correctPos = correctPositions[i]

            Box(
                modifier = Modifier
                    .offset { IntOffset(pos.x.roundToInt(), pos.y.roundToInt()) }
                    .size(pieceSizeDp)
                    .background(Color.LightGray),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = i.toString(),
                    style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                    color = Color.Black
                )
            }

            Box(
                modifier = Modifier
                    .offset { IntOffset(pos.x.roundToInt(), pos.y.roundToInt()) }
                    .size(pieceSizeDp)
                    .pointerInput(i) {
                        detectDragGestures(
                            onDrag = { change, dragAmount ->
                                change.consume()
                                if (!snapped[i]) {
                                    val newX = (piecesPositions[i].x + dragAmount.x).coerceIn(0f, screenWidthPx - pieceSizePx)
                                    val newY = (piecesPositions[i].y + dragAmount.y).coerceIn(0f, screenHeightPx - pieceSizePx)
                                    piecesPositions[i] = Offset(newX, newY)
                                }
                            },
                            onDragEnd = {
                                val currentPos = piecesPositions[i]
                                val distX = abs(currentPos.x - correctPos.x)
                                val distY = abs(currentPos.y - correctPos.y)
                                if (distX < snapThreshold && distY < snapThreshold) {
                                    piecesPositions[i] = correctPos
                                    snapped[i] = true
                                    if (snapped.all { it }) {
                                        gameCompleted = true
                                    }
                                }
                            }
                        )
                    }
            )
        }

        if (gameCompleted) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xAA000000)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Tebrikler! Puzzle tamamlandı.",
                    color = Color.White,
                    style = MaterialTheme.typography.headlineMedium
                )
            }
        }
    }
}

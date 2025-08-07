import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.solardevtech.puzzle.view.components.GridPuzzleBoard
import com.solardevtech.puzzle.view.components.PuzzlePieceComponent

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

    val pieces = viewModel.pieces
    val gameCompleted by viewModel.gameCompleted

    LaunchedEffect(puzzleLeftPx, puzzleTopPx, pieceSizePx) {
        viewModel.initPositions(puzzleLeftPx, puzzleTopPx, pieceSizePx)
    }

    Scaffold(
        modifier = Modifier.fillMaxSize().background(Color.White)
    ) {
        innerPadding->
        Column (modifier = Modifier.fillMaxSize().padding(innerPadding), verticalArrangement = Arrangement.SpaceBetween){
            GridPuzzleBoard(puzzleSizePx= with(density) { puzzleSizeDp.toPx() }, puzzleLeftPx = puzzleLeftPx, puzzleTopPx =puzzleTopPx)
            LazyRow (modifier = Modifier.wrapContentHeight()){
                items(pieces){
                        piece ->
                    PuzzlePieceComponent(
                        piece = piece,
                        pieceSizeDp = pieceSizeDp,
                        )
                }

            }
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

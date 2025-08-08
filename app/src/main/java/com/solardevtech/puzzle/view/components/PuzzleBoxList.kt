import android.content.ClipData
import androidx.compose.foundation.background
import androidx.compose.foundation.draganddrop.dragAndDropSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draganddrop.DragAndDropTransferData
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.solardevtech.puzzle.model.DraggableBox

@Composable
fun PuzzleBoxList(boxList: List<DraggableBox>, boxSizeDp: Dp) {
    LazyRow(
        modifier = Modifier.wrapContentHeight().padding(horizontal = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        itemsIndexed(boxList) { _, box ->
            Box(
                modifier = Modifier
                    .size(boxSizeDp)
                    .background(Color.LightGray) // opsiyonel
                    .dragAndDropSource(
                        drawDragDecoration = {
                            if (box.image != null) drawImage(box.image)
                        },
                        transferData = {
                            DragAndDropTransferData(
                                clipData = ClipData.newPlainText("text", "id:${box.id}")
                            )
                        }
                    )
            ) {
                if (box.image != null) {
                    androidx.compose.foundation.Image(
                        contentScale = ContentScale.FillBounds,
                        bitmap = box.image,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}

package com.solardevtech.puzzle.view.screens
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.solardevtech.puzzle.model.ImageItem
import com.solardevtech.puzzle.util.appBackground

@Composable
fun ImageListScreen(
    images: List<ImageItem>,
    onImageSelected: (String) -> Unit
) {
    Scaffold(modifier = Modifier.fillMaxSize().appBackground()) { innerPadding ->
        LazyColumn(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            items(images) { image ->
                Text(image.download_url)
                Card(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                        .clickable { onImageSelected(image.download_url) },
                    elevation = CardDefaults.cardElevation()
                ) {
                    AsyncImage(
                        modifier = Modifier.size(height = 300.dp, width = 500.dp),
                        model = image.download_url,
                        contentDescription = "Puzzle image",
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }
    }
}

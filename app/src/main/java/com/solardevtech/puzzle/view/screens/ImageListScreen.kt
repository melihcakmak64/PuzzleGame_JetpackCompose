package com.solardevtech.puzzle.view.screens
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.solardevtech.puzzle.viewmodel.ImageViewModel
@Composable
fun ImageListScreen(
    onImageSelected: (String) -> Unit,
    viewModel: ImageViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val images by viewModel.images.collectAsState()

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(images) { image ->
            Card(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
                    .clickable { onImageSelected(image.download_url) },
                elevation = CardDefaults.cardElevation()
            ) {
                Column(modifier = Modifier.padding(8.dp)) {
                    AsyncImage(
                        model = image.download_url,
                        contentDescription = "puzzle image",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                    )
                }
            }
        }
    }
}

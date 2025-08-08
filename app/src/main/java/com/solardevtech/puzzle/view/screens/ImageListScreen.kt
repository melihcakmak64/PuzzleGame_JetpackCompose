package com.solardevtech.puzzle.view.screens
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.solardevtech.puzzle.util.Resource
import com.solardevtech.puzzle.viewmodel.ImageViewModel
@Composable
fun ImageListScreen(
    onImageSelected: (String) -> Unit,
    viewModel: ImageViewModel = hiltViewModel()
) {
    val state = viewModel.images.value

    when (state) {
        is Resource.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        is Resource.Error -> {
            Box(
                modifier = Modifier.fillMaxSize().padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = state.message ?: "An unknown error occurred.",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.error
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = { viewModel.loadImages() }) {
                        Text("Retry")
                    }
                }
            }
        }

        is Resource.Success -> {
            val images = state.data.orEmpty()

            if (images.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No images found.")
                }
            } else {
               Scaffold (modifier = Modifier.fillMaxSize()){
                   innerPadding->

                   LazyColumn(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
                   items(images) { image ->
                       Card(
                           modifier = Modifier
                               .padding(8.dp)
                               .fillMaxWidth()
                               .clickable { onImageSelected(image.download_url) },
                           elevation = CardDefaults.cardElevation()
                       ) {
                           AsyncImage(
                               contentScale = ContentScale.FillBounds,
                               model = image.download_url,
                               contentDescription = "puzzle image",

                               )

                       }
                   }
               } }
            }
        }
    }
}

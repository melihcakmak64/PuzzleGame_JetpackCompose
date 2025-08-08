package com.solardevtech.puzzle.view.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.solardevtech.puzzle.R
import com.solardevtech.puzzle.model.ImageItem
import com.solardevtech.puzzle.util.Resource
import com.solardevtech.puzzle.viewmodel.ImageViewModel
import com.airbnb.lottie.compose.*
import com.solardevtech.puzzle.util.appBackground

@Composable
fun SplashScreen(
    onSuccess: (List<ImageItem>) -> Unit,
    viewModel: ImageViewModel = hiltViewModel()
) {
    val state by viewModel.images

    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.loading))
    val progress by animateLottieCompositionAsState(composition, iterations = LottieConstants.IterateForever)

    Box(modifier = Modifier
        .fillMaxSize()
        .appBackground(), contentAlignment = Alignment.Center) {
        when (state) {
            is Resource.Loading -> {
                // Her loading durumunda Lottie animasyonu göster
                LottieAnimation(
                    composition,
                    progress,
                    modifier = Modifier.size(150.dp)
                )
            }
            is Resource.Error -> {
                Column(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = state.message ?: "Bir hata oluştu.",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = { viewModel.loadImages() }) {
                        Text("Tekrar Dene")
                    }
                }
            }
            is Resource.Success -> {
                val images = state.data.orEmpty()
                if (images.isNotEmpty()) {
                    LaunchedEffect(Unit) {
                        onSuccess(images)
                    }
                } else {
                    Text(
                        text = "Resim bulunamadı.",
                        modifier = Modifier.align(Alignment.Center),
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
        }
    }
}

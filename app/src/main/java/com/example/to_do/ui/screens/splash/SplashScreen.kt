package com.example.to_do.ui.screens.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.to_do.R
import com.example.to_do.ui.theme.splashScreenBackgroundColor

@Composable
fun SplashScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.splashScreenBackgroundColor),
        contentAlignment = Alignment.Center,
    ) {

        Image(
            painter = painterResource(id = if (isSystemInDarkTheme()) R.drawable.logo_dark_theme else R.drawable.logo),
            contentDescription = "",
            modifier = Modifier.size(400.dp)
        )
    }
}

@Composable
@Preview
fun SplashScreenPreview(){
    SplashScreen()
}

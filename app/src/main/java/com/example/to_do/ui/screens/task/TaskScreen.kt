package com.example.to_do.ui.screens.task

import android.annotation.SuppressLint
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun TaskScreen() {
    Scaffold(
        topBar = { TaskAppBar(navigateToListScreen = {}) },
        content = {},
    )
}
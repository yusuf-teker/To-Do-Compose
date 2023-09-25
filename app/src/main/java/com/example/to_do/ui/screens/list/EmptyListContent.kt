package com.example.to_do.ui.screens.list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.to_do.R
import com.example.to_do.ui.theme.MediumGray


@Composable
fun EmptyListContent() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_sad_face),
            contentDescription = null,
            modifier = Modifier.size(120.dp),
            tint = MediumGray
        )
        Text(
            color = MediumGray,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.headlineSmall,
            text = "No task found.",
        )
    }
}

@Composable
@Preview
fun EmptyListContentPreview() {
    EmptyListContent()
}

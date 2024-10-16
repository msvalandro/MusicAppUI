package com.msvalandro.musicappui.ui.theme

import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import com.msvalandro.musicappui.R

@Composable
fun BrowseScreen() {
    val categories = listOf("Hits", "Happy", "Workout", "Running", "TGIF", "Yoga")
    
    LazyVerticalGrid(GridCells.Fixed(2)) {
        items(categories) { category ->
            BrowserItem(category = category, drawable = R.drawable.ic_browse)
        }
    }
}
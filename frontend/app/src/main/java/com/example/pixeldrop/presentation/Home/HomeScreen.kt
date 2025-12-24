package com.example.pixeldrop.presentation.Home

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.pixeldrop.R
import com.example.pixeldrop.domain.model.Wallpaper
import org.koin.androidx.compose.koinViewModel

import androidx.compose.animation.core.*

import androidx.compose.ui.geometry.Offset

import coil.compose.SubcomposeAsyncImage


// --- 1. The Shimmer Animation Brush ---
@Composable
fun shimmerBrush(showShimmer: Boolean = true, targetValue: Float = 1000f): Brush {
    return if (showShimmer) {
        val shimmerColors = listOf(
            Color.LightGray.copy(alpha = 0.6f),
            Color.LightGray.copy(alpha = 0.2f),
            Color.LightGray.copy(alpha = 0.6f),
        )

        val transition = rememberInfiniteTransition(label = "shimmer")
        val translateAnimation = transition.animateFloat(
            initialValue = 0f,
            targetValue = targetValue,
            animationSpec = infiniteRepeatable(
                animation = tween(800), repeatMode = RepeatMode.Reverse
            ), label = "shimmer"
        )
        Brush.linearGradient(
            colors = shimmerColors,
            start = Offset.Zero,
            end = Offset(x = translateAnimation.value, y = translateAnimation.value)
        )
    } else {
        Brush.linearGradient(
            colors = listOf(Color.Transparent, Color.Transparent),
            start = Offset.Zero,
            end = Offset.Zero
        )
    }
}

// --- 2. The Updated Wallpaper Card ---
@Composable
fun WallpaperCard(
    wallpaper: Wallpaper,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box {
            // CHANGED: Using SubcomposeAsyncImage to handle Loading State
            SubcomposeAsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(wallpaper.thumbnail_url)
                    .crossfade(true) // Smooth fade in
                    .build(),
                contentDescription = wallpaper.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    // Important: We set a fixed random height range for staggered look
                    .heightIn(min = 150.dp, max = 300.dp),

                // THIS IS THE SHIMMER LOGIC
                loading = {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(shimmerBrush()) // Apply Shimmer
                    )
                },

                // Optional: Handle Error State (Gray box with icon)
                error = {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.LightGray),
                        contentAlignment = Alignment.Center
                    ) {
                        // You can add an error icon here if you want
                    }
                }
            )

            // 2. Text Overlay (Gradient at bottom)
            // Kept outside SubcomposeAsyncImage so title is visible even while loading!
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.7f)),
                            startY = 100f
                        )
                    )
            )

            // 3. Title Text
            Text(
                text = wallpaper.title,
                color = Color.White,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(12.dp)
            )
        }
    }
}

// --- 3. Home Screen (Small Tweaks Only) ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = koinViewModel()
) {
    val wallpapers by viewModel.wallpapers.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState() // Global loading (fetching list)
    val selectedCategory by viewModel.selectedCategory.collectAsState()

    val context = LocalContext.current

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Category Filter
        CategorySection(
            selectedCategory = selectedCategory,
            onCategorySelected = { newCategory ->
                viewModel.selectCategory(newCategory)
            }
        )

        Box(modifier = Modifier.weight(1f)) {
            // While fetching the LIST of URLs, we still show the spinner.
            // Once the list arrives, the LazyVerticalStaggeredGrid appears immediately.
            // Then, individual cards will Shimmer -> Load Image.
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = Color(0xFF00ACC1)
                )
            } else {
                LazyVerticalStaggeredGrid(
                    columns = StaggeredGridCells.Fixed(2),
                    contentPadding = PaddingValues(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalItemSpacing = 12.dp,
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(wallpapers) { wallpaper ->
                        WallpaperCard(wallpaper = wallpaper) {
                            Toast.makeText(context, "Clicked: ${wallpaper.title}", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }

            if (!isLoading && wallpapers.isEmpty()) {
                Text(
                    text = "No wallpapers found.",
                    color = Color.Gray,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBar() {
    CenterAlignedTopAppBar( // CenterAligned looks better for Logos
        title = {
            Image(
                painter = painterResource(R.drawable.pixeldrop_logo),
                contentDescription = null,
                modifier = Modifier.width(120.dp)
            )
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = Color.White // Match background
        )
    )
}

@Composable
fun CategorySection(
    selectedCategory: String,
    onCategorySelected: (String) -> Unit
) {
    val categories = listOf("All", "Nature", "Abstract", "Cars", "Minimal", "Technology")

    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth().background(Color.White)
    ) {
        items(categories) { category ->
            val isSelected = category == selectedCategory

            // The "Chip" UI
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(if (isSelected) Color(0xFF00ACC1) else Color.Transparent) // Teal vs Transparent
                    .border(
                        width = if (isSelected) 0.dp else 0.dp,
                        color = if (isSelected) Color.Transparent else Color.White,
                        shape = CircleShape
                    )
                    .clickable { onCategorySelected(category) }
                    .padding(horizontal = 20.dp, vertical = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = category,
                    color = if (isSelected) Color.White else Color.Black,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                    fontSize = 14.sp
                )
            }
        }
    }
}

sealed class BottomNavItem(val icon: ImageVector, val label: String) {
    object Home : BottomNavItem(Icons.Outlined.Home, "Home")
    object Search : BottomNavItem(Icons.Outlined.Search, "Search")
    object Favorites : BottomNavItem(Icons.Outlined.FavoriteBorder, "Favorites")
}

@Composable
fun FloatingBottomNavigation(
    currentScreen: BottomNavItem,
    onScreenSelected: (BottomNavItem) -> Unit
) {
    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Search,
        BottomNavItem.Favorites
    )

    // The Container Box ensures the bar is centered and lifted
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 32.dp), // Lift it up from the bottom edge
        contentAlignment = Alignment.Center
    ) {
        // The Actual Bar (Surface)
        Surface(
            color = Color(0xFFFFFFFF), // Light Gray background (Like the screenshot)
            contentColor = Color.White,
            shape = RoundedCornerShape(12.dp), // Makes it a "Pill" shape
            shadowElevation = 8.dp, // Adds the floating shadow effect
            modifier = Modifier
                .height(60.dp)
                .widthIn(min = 250.dp, max = 300.dp) // Constrain width so it doesn't touch edges
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                items.forEach { item ->
                    IconButton(
                        onClick = { onScreenSelected(item) },
                        modifier = Modifier.size(48.dp)
                    ) {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.label,
                            tint = if (currentScreen == item) Color.Black else Color.Gray, // Highlight selected
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ShimmerWallpaperItem(brush: Brush) {
    Column(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        // The "Image" Placeholder
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp) // Match your real image height
                .clip(RoundedCornerShape(12.dp))
                .background(brush)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // The "Title" Placeholder
        Spacer(
            modifier = Modifier
                .height(20.dp)
                .fillMaxWidth(0.7f) // 70% width
                .clip(RoundedCornerShape(4.dp))
                .background(brush)
        )
    }
}
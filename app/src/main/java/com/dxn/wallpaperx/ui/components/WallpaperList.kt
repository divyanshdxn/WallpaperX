package com.dxn.wallpaperx.ui.components

import android.net.Uri
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import coil.annotation.ExperimentalCoilApi
import com.dxn.wallpaperx.domain.models.Wallpaper
import com.dxn.wallpaperx.ui.screens.Screen
import com.google.gson.Gson

@ExperimentalCoilApi
@ExperimentalFoundationApi
@Composable
fun WallpaperList(
    wallpapers: LazyPagingItems<Wallpaper>,
    favouriteIds: List<Int>,
    addFavourite: (Wallpaper) -> Unit,
    removeFavourite: (Int) -> Unit,
    state: LazyListState = rememberLazyListState(),
    navController: NavHostController
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        LazyVerticalGrid(
            cells = GridCells.Fixed(2),
            modifier = Modifier.padding(bottom = 8.dp),
            state = state
        ) {
            items(wallpapers.itemCount) { index ->
                wallpapers[index]?.let { wallpaper ->
                    val isFavourite = favouriteIds.contains(wallpaper.id)
                    WallpaperCard(
                        modifier = Modifier
                            .padding(top = if (index == 0 || index == 1) 8.dp else 0.dp)
                            .padding(4.dp)
                            .fillMaxWidth()
                            .height(246.dp),
                        wallpaper = wallpaper,
                        onClick = {
                            val data = Uri.encode(Gson().toJson(wallpaper))
                            navController.navigate("${Screen.SetWallpaper.route}/$data" )
                        },
                        isFavourite = isFavourite,
                        onLikedClicked = {
                            if (isFavourite) removeFavourite(wallpaper.id) else addFavourite(
                                wallpaper
                            )
                        }
                    )
                }
            }
            wallpapers.apply {
                when {
                    loadState.append is LoadState.Loading || loadState.refresh is LoadState.Loading -> {
                        item {
                            Box() {
                                CircularProgressIndicator(
                                    modifier = Modifier.padding(16.dp),
                                    color = MaterialTheme.colors.onSurface,
                                    strokeWidth = 2.dp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}


package com.dxn.wallpaperx.app.ui.screen.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.dxn.wallpaperx.data.model.Wallpaper
import com.dxn.wallpaperx.domain.repository.WallpaperRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOf

class HomeScreenVM(
    private val wallpaperRepository: WallpaperRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState = _uiState.asStateFlow()

    var wallpapers = flowOf<PagingData<Wallpaper>>()
        private set

    init {
        loadWallpapers()
        loadCollections()
        loadFavourites()
    }

    private fun loadWallpapers() {
        runCatching {
            wallpapers =
                Pager(
                    config = PagingConfig(pageSize = 20),
                    pagingSourceFactory = {
                        wallpaperRepository.wallpaperSource(
                            "wallpaper",
                            false,
                        )
                    },
                ).flow.cachedIn(viewModelScope)
        }.getOrElse {
            Log.e(TAG, "loadWallpapers: ${it.message}")
        }
    }

    private fun loadCollections() {
    }

    private fun loadFavourites() {
    }

    fun addToFavourites(wallpaper: Wallpaper) {
    }

    companion object {
        const val TAG = "HomeScreenVM"
    }
}

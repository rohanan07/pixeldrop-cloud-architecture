package com.example.pixeldrop.presentation.Home



import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pixeldrop.domain.model.Wallpaper
import com.example.pixeldrop.domain.usecase.GetWallpapersUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val getWallpapersUseCase: GetWallpapersUseCase
) : ViewModel() {

    // State Management
    private val _wallpapers = MutableStateFlow<List<Wallpaper>>(emptyList())
    val wallpapers: StateFlow<List<Wallpaper>> = _wallpapers

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _selectedCategory = MutableStateFlow("All")
    val selectedCategory: StateFlow<String> = _selectedCategory

    init {
        loadWallpapers("All")
    }

    private fun loadWallpapers(category: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                // The Use Case handles the "All" vs "Category" logic now
                val result = getWallpapersUseCase(category)
                _wallpapers.value = result
            } catch (e: Exception) {
                // Handle error
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun selectCategory(category: String) {
        _selectedCategory.value = category
        loadWallpapers(category)
    }
}
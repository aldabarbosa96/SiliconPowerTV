package com.davidbarbosa.siliconpowertv.ui.popular

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.davidbarbosa.siliconpowertv.data.repository.TvRepository
import com.davidbarbosa.siliconpowertv.domain.model.TvShow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class PopularUiState(
    val loading: Boolean = false, val items: List<TvShow> = emptyList(), val error: String? = null
)

class PopularViewModel(
    private val repo: TvRepository = TvRepository()
) : ViewModel() {

    private val _state = MutableStateFlow(PopularUiState(loading = true))
    val state: StateFlow<PopularUiState> = _state

    fun load(language: String = "es-ES") {
        _state.value = PopularUiState(loading = true)

        viewModelScope.launch {
            try {
                val items = repo.getPopular(page = 1, language = language)
                _state.value = PopularUiState(loading = false, items = items)
            } catch (e: Exception) {
                _state.value = PopularUiState(loading = false, error = e.message ?: "Error")
            }
        }
    }
}

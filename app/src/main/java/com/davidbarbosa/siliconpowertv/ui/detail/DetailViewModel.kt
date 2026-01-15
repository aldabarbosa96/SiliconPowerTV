package com.davidbarbosa.siliconpowertv.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.davidbarbosa.siliconpowertv.data.repository.TvRepository
import com.davidbarbosa.siliconpowertv.domain.model.TvShowDetail
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class DetailUiState(
    val loading: Boolean = false, val item: TvShowDetail? = null, val error: String? = null
)

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val repo: TvRepository
) : ViewModel() {

    private val _state = MutableStateFlow(DetailUiState(loading = true))
    val state: StateFlow<DetailUiState> = _state

    fun load(tvId: Long, language: String) {
        _state.value = DetailUiState(loading = true)

        viewModelScope.launch {
            try {
                val item = repo.getDetail(tvId = tvId, language = language)
                _state.value = DetailUiState(loading = false, item = item)
            } catch (e: Exception) {
                _state.value = DetailUiState(loading = false, error = e.message ?: "Error")
            }
        }
    }
}

package com.davidbarbosa.siliconpowertv.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.davidbarbosa.siliconpowertv.data.repository.TvRepository
import com.davidbarbosa.siliconpowertv.domain.model.TvShow
import com.davidbarbosa.siliconpowertv.domain.model.TvShowDetail
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

/**
 * Cargamos el detalle con soporte de caché y, aparte, intentamos obtener recomendaciones.
 * Si las recomendaciones fallan, preferimos que no pete.
 */
data class DetailUiState(
    val loading: Boolean = false,
    val item: TvShowDetail? = null,
    val recommendations: List<TvShow> = emptyList(),
    val error: String? = null
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

                val recs = try {
                    repo.getRecommendations(tvId = tvId, language = language)
                } catch (_: Exception) {
                    emptyList()
                }

                _state.value = DetailUiState(
                    loading = false, item = item, recommendations = recs
                )
            } catch (e: Exception) {
                val code = e.message ?: "Error"
                _state.value = DetailUiState(loading = false, error = code)
            }
        }
    }
}

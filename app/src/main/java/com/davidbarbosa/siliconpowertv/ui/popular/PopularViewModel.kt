package com.davidbarbosa.siliconpowertv.ui.popular

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.davidbarbosa.siliconpowertv.data.repository.TvRepository
import com.davidbarbosa.siliconpowertv.domain.model.TvShow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class PopularCacheState(
    val loading: Boolean = false, val items: List<TvShow> = emptyList(), val error: String? = null
)

@HiltViewModel
class PopularViewModel @Inject constructor(
    private val repo: TvRepository
) : ViewModel() {

    fun popular(language: String): Flow<PagingData<TvShow>> =
        repo.popularPaging(language).cachedIn(viewModelScope)

    private val _cache = MutableStateFlow(PopularCacheState())
    val cache: StateFlow<PopularCacheState> = _cache

    fun loadCache(language: String) {
        if (_cache.value.loading) return

        _cache.value = PopularCacheState(loading = true)

        viewModelScope.launch {
            try {
                val items = repo.getCachedPopular(language)
                _cache.value = PopularCacheState(loading = false, items = items)
            } catch (e: Exception) {
                _cache.value = PopularCacheState(
                    loading = false, items = emptyList(), error = e.message ?: "Error"
                )
            }
        }
    }
}

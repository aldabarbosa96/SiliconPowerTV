package com.davidbarbosa.siliconpowertv.ui.popular

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.davidbarbosa.siliconpowertv.data.repository.TvRepository
import com.davidbarbosa.siliconpowertv.domain.model.TvShow
import kotlinx.coroutines.flow.Flow

class PopularViewModel(
    private val repo: TvRepository = TvRepository()
) : ViewModel() {

    fun popular(language: String): Flow<PagingData<TvShow>> =
        repo.popularPaging(language).cachedIn(viewModelScope)
}

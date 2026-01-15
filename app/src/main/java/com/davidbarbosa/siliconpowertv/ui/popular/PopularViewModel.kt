package com.davidbarbosa.siliconpowertv.ui.popular

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.davidbarbosa.siliconpowertv.data.repository.TvRepository
import com.davidbarbosa.siliconpowertv.domain.model.TvShow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class PopularViewModel @Inject constructor(
    private val repo: TvRepository
) : ViewModel() {

    fun popular(language: String): Flow<PagingData<TvShow>> =
        repo.popularPaging(language).cachedIn(viewModelScope)
}

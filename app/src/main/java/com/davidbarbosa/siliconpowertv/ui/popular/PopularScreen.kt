package com.davidbarbosa.siliconpowertv.ui.popular

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.davidbarbosa.siliconpowertv.R
import com.davidbarbosa.siliconpowertv.data.local.LanguageProvider

@Composable
fun PopularScreen(
    onItemClick: (Long) -> Unit = {}, vm: PopularViewModel = hiltViewModel()
) {
    val language = LanguageProvider.tmdbLanguage()
    val flow = remember(language) { vm.popular(language) }
    val pagingItems = flow.collectAsLazyPagingItems()

    val cacheState by vm.cache.collectAsState()

    val refreshState = pagingItems.loadState.refresh

    // Si falla la carga inicial por red, intentamos tirar del caché
    LaunchedEffect(refreshState) {
        if (refreshState is LoadState.Error) {
            vm.loadCache(language)
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // 1) Si tenemos datos paginados, los mostramos (caso normal online)
        if (pagingItems.itemCount > 0) {
            items(count = pagingItems.itemCount,
                key = { index -> "${pagingItems[index]?.id ?: -1L}-$index" }) { index ->
                val item = pagingItems[index]
                if (item != null) {
                    val rating = String.format("%.1f", item.voteAverage)
                    Text(text = "• ${item.name}  ($rating)",
                        modifier = Modifier
                            .padding(vertical = 8.dp)
                            .clickable { onItemClick(item.id) })
                }
            }
        } else {
            // 2) Si no hay datos paginados (porque la red falló) pero hay caché, mostramos caché
            if (cacheState.items.isNotEmpty()) {
                item { Text(stringResource(R.string.offline_cache_banner)) }

                items(items = cacheState.items, key = { it.id }) { item ->
                    val rating = String.format("%.1f", item.voteAverage)
                    Text(text = "• ${item.name}  ($rating)",
                        modifier = Modifier
                            .padding(vertical = 8.dp)
                            .clickable { onItemClick(item.id) })
                }
            }
        }

        when (refreshState) {
            is LoadState.Loading -> item { Text(stringResource(R.string.loading)) }

            is LoadState.Error -> {
                if (cacheState.items.isEmpty()) {
                    item {
                        val msg =
                            refreshState.error.message ?: stringResource(R.string.error_unknown)
                        Text(stringResource(R.string.error_generic, msg))
                    }
                }
            }

            else -> {}
        }


        when (val state = pagingItems.loadState.append) {
            is LoadState.Loading -> item { Text(stringResource(R.string.loading_more)) }
            is LoadState.Error -> item {
                val msg = state.error.message ?: stringResource(R.string.error_unknown)
                Text(stringResource(R.string.error_paging, msg))
            }

            else -> {}
        }
    }
}

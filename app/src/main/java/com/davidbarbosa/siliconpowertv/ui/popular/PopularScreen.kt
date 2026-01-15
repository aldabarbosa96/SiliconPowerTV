package com.davidbarbosa.siliconpowertv.ui.popular

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.davidbarbosa.siliconpowertv.R
import com.davidbarbosa.siliconpowertv.data.local.LanguageProvider

@Composable
fun PopularScreen(
    onItemClick: (Long) -> Unit = {},
    vm: PopularViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val language = LanguageProvider.tmdbLanguage()
    val flow = remember(language) { vm.popular(language) }
    val pagingItems = flow.collectAsLazyPagingItems()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
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

        when (val state = pagingItems.loadState.refresh) {
            is LoadState.Loading -> item { Text(stringResource(R.string.loading)) }
            is LoadState.Error -> item {
                val msg = state.error.message ?: stringResource(R.string.error_unknown)
                Text(stringResource(R.string.error_generic, msg))
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

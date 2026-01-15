package com.davidbarbosa.siliconpowertv.ui.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import com.davidbarbosa.siliconpowertv.R
import com.davidbarbosa.siliconpowertv.data.local.LanguageProvider

private fun posterUrl(path: String?): String? = path?.let { "https://image.tmdb.org/t/p/w342$it" }

@Composable
private fun RatingBadge(voteAverage: Double) {
    val rating = String.format("%.1f", voteAverage)
    Surface(
        shape = RoundedCornerShape(999.dp), tonalElevation = 2.dp
    ) {
        Text(
            text = stringResource(R.string.rating, rating),
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun OfflineChip(text: String) {
    Surface(
        shape = RoundedCornerShape(999.dp), tonalElevation = 2.dp
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun TvShowRow(
    name: String, posterPath: String?, voteAverage: Double, onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(14.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = posterUrl(posterPath),
                contentDescription = name,
                modifier = Modifier
                    .size(width = 72.dp, height = 108.dp)
                    .clip(RoundedCornerShape(10.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(Modifier.height(8.dp))
                RatingBadge(voteAverage = voteAverage)
            }
        }
    }
}

@Composable
fun PopularScreen(
    onItemClick: (Long) -> Unit = {}, vm: PopularViewModel = hiltViewModel()
) {
    val language = LanguageProvider.tmdbLanguage()
    val flow = remember(language) { vm.popular(language) }
    val pagingItems = flow.collectAsLazyPagingItems()

    val cacheState by vm.cache.collectAsState()
    val refreshState = pagingItems.loadState.refresh

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
        item {
            Text(
                text = stringResource(R.string.app_name),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(Modifier.height(6.dp))

            val offline = cacheState.items.isNotEmpty() && pagingItems.itemCount == 0
            if (offline) {
                OfflineChip(stringResource(R.string.offline_cache_banner))
                Spacer(Modifier.height(10.dp))
            } else {
                Spacer(Modifier.height(4.dp))
            }
        }

        if (pagingItems.itemCount > 0) {
            items(count = pagingItems.itemCount,
                key = { index -> "${pagingItems[index]?.id ?: -1L}-$index" }) { index ->
                val item = pagingItems[index]
                if (item != null) {
                    TvShowRow(name = item.name,
                        posterPath = item.posterPath,
                        voteAverage = item.voteAverage,
                        onClick = { onItemClick(item.id) })
                }
            }
        } else {
            if (cacheState.items.isNotEmpty()) {
                items(items = cacheState.items, key = { it.id }) { item ->
                    TvShowRow(name = item.name,
                        posterPath = item.posterPath,
                        voteAverage = item.voteAverage,
                        onClick = { onItemClick(item.id) })
                }
            }
        }

        when (refreshState) {
            is LoadState.Loading -> item {
                Spacer(Modifier.height(8.dp))
                Text(stringResource(R.string.loading), style = MaterialTheme.typography.bodyMedium)
            }

            is LoadState.Error -> {
                if (cacheState.items.isEmpty()) {
                    item {
                        Spacer(Modifier.height(8.dp))
                        val msg =
                            refreshState.error.message ?: stringResource(R.string.error_unknown)
                        Text(
                            stringResource(R.string.error_generic, msg),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }

            else -> {}
        }

        when (val state = pagingItems.loadState.append) {
            is LoadState.Loading -> item {
                Spacer(Modifier.height(8.dp))
                Text(
                    stringResource(R.string.loading_more),
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            is LoadState.Error -> item {
                Spacer(Modifier.height(8.dp))
                val msg = state.error.message ?: stringResource(R.string.error_unknown)
                Text(
                    stringResource(R.string.error_paging, msg),
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            else -> {}
        }
    }
}

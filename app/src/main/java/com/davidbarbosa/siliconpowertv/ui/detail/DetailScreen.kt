package com.davidbarbosa.siliconpowertv.ui.detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.davidbarbosa.siliconpowertv.R
import com.davidbarbosa.siliconpowertv.data.local.LanguageProvider

private fun tmdbImageUrl(path: String?): String? =
    path?.let { "https://image.tmdb.org/t/p/w780$it" }

private fun tmdbPosterUrl(path: String?): String? =
    path?.let { "https://image.tmdb.org/t/p/w342$it" }

private fun yearFromDate(date: String?): String? = date?.takeIf { it.length >= 4 }?.substring(0, 4)

@Composable
fun DetailScreen(
    tvId: Long, vm: DetailViewModel = hiltViewModel()
) {
    val state by vm.state.collectAsState()
    val language = LanguageProvider.tmdbLanguage()
    val scrollState = rememberScrollState()

    LaunchedEffect(tvId, language) {
        vm.load(tvId = tvId, language = language)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        when {
            state.loading -> Text(stringResource(R.string.loading_detail))

            state.error != null -> {
                val err = state.error

                if (err == "OFFLINE_NO_CACHE") {
                    Text(stringResource(R.string.offline_no_cache_detail))
                } else {
                    val msg = err ?: stringResource(R.string.error_unknown)
                    Text(stringResource(R.string.error_generic, msg))
                }
            }

            state.item != null -> {
                val item = state.item!!
                val imagePath = item.backdropPath ?: item.posterPath
                if (imagePath != null) {
                    AsyncImage(
                        model = tmdbImageUrl(imagePath),
                        contentDescription = item.name,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        contentScale = ContentScale.Crop
                    )

                    Spacer(Modifier.height(12.dp))
                }

                Text(item.name)

                val year = yearFromDate(item.firstAirDate) ?: stringResource(R.string.no_seasons)
                Text(stringResource(R.string.year, year))

                val genres = if (item.genres.isNotEmpty()) item.genres.joinToString(", ")
                else stringResource(R.string.no_seasons)
                Text(stringResource(R.string.genres, genres))

                val rating = String.format("%.1f", item.voteAverage)
                Text(stringResource(R.string.rating, rating))

                val seasons =
                    item.numberOfSeasons?.toString() ?: stringResource(R.string.no_seasons)
                Text(stringResource(R.string.seasons, seasons))

                Spacer(Modifier.height(12.dp))

                Text(item.overview.ifBlank { stringResource(R.string.no_description) })

                val recs = state.recommendations
                if (recs.isNotEmpty()) {
                    Spacer(Modifier.height(16.dp))
                    Text(stringResource(R.string.recommendations_title))
                    Spacer(Modifier.height(8.dp))

                    LazyRow {
                        items(items = recs, key = { it.id }) { r ->
                            Column(
                                modifier = Modifier
                                    .padding(end = 12.dp)
                                    .width(120.dp)
                            ) {
                                AsyncImage(
                                    model = tmdbPosterUrl(r.posterPath),
                                    contentDescription = r.name,
                                    modifier = Modifier
                                        .size(width = 120.dp, height = 180.dp)
                                        .clip(RoundedCornerShape(10.dp)),
                                    contentScale = ContentScale.Crop
                                )
                                Spacer(Modifier.height(6.dp))
                                Text(text = r.name, maxLines = 2)
                            }
                        }
                    }
                }
            }
        }
    }
}

package com.davidbarbosa.siliconpowertv.ui.detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.davidbarbosa.siliconpowertv.R
import com.davidbarbosa.siliconpowertv.data.local.LanguageProvider

@Composable
fun DetailScreen(
    tvId: Long, vm: DetailViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val state by vm.state.collectAsState()
    val language = LanguageProvider.tmdbLanguage()

    LaunchedEffect(tvId, language) {
        vm.load(tvId = tvId, language = language)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        when {
            state.loading -> Text(stringResource(R.string.loading_detail))

            state.error != null -> {
                val msg = state.error ?: stringResource(R.string.error_unknown)
                Text(stringResource(R.string.error_generic, msg))
            }

            state.item != null -> {
                val item = state.item!!
                Text(item.name)

                val rating = String.format("%.1f", item.voteAverage)
                Text(stringResource(R.string.rating, rating))

                val seasons =
                    item.numberOfSeasons?.toString() ?: stringResource(R.string.no_seasons)
                Text(stringResource(R.string.seasons, seasons))

                Text("")

                Text(item.overview.ifBlank { stringResource(R.string.no_description) })
            }
        }
    }
}

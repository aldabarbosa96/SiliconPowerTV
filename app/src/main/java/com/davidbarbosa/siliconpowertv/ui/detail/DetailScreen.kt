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
import androidx.compose.ui.unit.dp

@Composable
fun DetailScreen(
    tvId: Long, vm: DetailViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val state by vm.state.collectAsState()

    LaunchedEffect(tvId) {
        vm.load(tvId = tvId, language = "es-ES")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        when {
            state.loading -> Text("Cargando detalle...")
            state.error != null -> Text("Error: ${state.error}")
            state.item != null -> {
                val item = state.item!!
                Text(item.name)
                Text("Nota: ${String.format("%.1f", item.voteAverage)}")
                Text("Temporadas: ${item.numberOfSeasons ?: "-"}")
                Text("")
                Text(item.overview.ifBlank { "(Sin descripción)" })
            }
        }
    }
}

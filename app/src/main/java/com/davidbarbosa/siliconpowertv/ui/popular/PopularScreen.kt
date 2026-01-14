package com.davidbarbosa.siliconpowertv.ui.popular

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun PopularScreen(
    onItemClick: (Long) -> Unit = {},
    vm: PopularViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val state by vm.state.collectAsState()

    LaunchedEffect(Unit) {
        vm.load(language = "es-ES")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when {
            state.loading -> Text("Cargando series populares...")
            state.error != null -> Text("Error: ${state.error}")
            else -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(items = state.items, key = { show -> show.id }) { item ->
                        Text(text = "• ${item.name}  (${String.format("%.1f", item.voteAverage)})",
                            modifier = Modifier
                                .padding(vertical = 8.dp)
                                .clickable { onItemClick(item.id) })
                    }
                }
            }
        }
    }
}

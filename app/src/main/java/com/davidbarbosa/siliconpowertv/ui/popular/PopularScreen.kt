package com.davidbarbosa.siliconpowertv.ui.popular

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems

@Composable
fun PopularScreen(
    onItemClick: (Long) -> Unit = {},
    vm: PopularViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val pagingItems = vm.popular(language = "es-ES").collectAsLazyPagingItems()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        items(count = pagingItems.itemCount, key = { index ->
            val id = pagingItems[index]?.id ?: -1L
            "$id-$index"
        }) { index ->
            val item = pagingItems[index]
            if (item != null) {
                Text(text = "• ${item.name}  (${String.format("%.1f", item.voteAverage)})",
                    modifier = Modifier
                        .padding(vertical = 8.dp)
                        .clickable { onItemClick(item.id) })
            }
        }

        when (val state = pagingItems.loadState.refresh) {
            is LoadState.Loading -> item { Text("Cargando...") }
            is LoadState.Error -> item { Text("Error: ${state.error.message ?: "desconocido"}") }
            else -> {}
        }

        when (val state = pagingItems.loadState.append) {
            is LoadState.Loading -> item { Text("Cargando más...") }
            is LoadState.Error -> item { Text("Error al paginar: ${state.error.message ?: "desconocido"}") }
            else -> {}
        }
    }
}

package com.lavalama.tabletimegame.presentation.screen.gamesCatalog

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import coil3.compose.AsyncImage
import com.lavalama.tabletimegame.R
import com.lavalama.tabletimegame.domain.entity.TTGame

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GamesCatalogScreen(
    modifier: Modifier = Modifier,
    onSettingsNavigate: () -> Unit,
    onFavoriteNavigate: () -> Unit,
    onGameDetailsNavigate: (Int) -> Unit,
    viewModel: GamesCatalogViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.table_time_title)) },
                actions = {
//                    IconButton(
//                        onClick = onSettingsNavigate
//                    ) {
//                        Icon(
//                            imageVector = Icons.Default.Settings,
//                            contentDescription = "Open Settings screen"
//                        )
//                    }
                    IconButton(
                        onClick = onFavoriteNavigate
                    ) {
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = stringResource(R.string.open_favorites_games_screen),
                            tint = Color.Red
                        )
                    }
                },
            )
        }
    ) { innerPadding ->
        val gamesBySeason = remember(state.filteredGames) {
            state.filteredGames.groupBy { it.season }
        }
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = innerPadding
        ) {

            item {
                SearchPanel(
                    query = state.searchQuery,
                    onInputQuery = {
                        viewModel.processCommand(GamesCatalogCommand.InputGameTitle(it))
                    },
                    onClearQuery = {
                        viewModel.processCommand(GamesCatalogCommand.ClearQuery)
                    }
                )
            }

            item {
                GroupedFilterPanel(
                    filters = state.availableFilters,
                    currentFilters = state.currentFilters,
                    sortedFilterKeys = state.sortedFilterKeys,
                    onClearAllFilters = {
                        viewModel.processCommand(GamesCatalogCommand.ClearAllFilters)
                    },
                    onFiltersChanged = { filterTitle, value ->
                        viewModel.processCommand(
                            GamesCatalogCommand.ChangeFilters(
                                filter = filterTitle,
                                newValues = value
                            )
                        )
                    },
                )
            }

            gamesBySeason.forEach { (seasonNumber, seasonGames) ->
                item(key = "season_$seasonNumber") {
                    SeasonGroup(
                        seasonNumber = seasonNumber,
                        games = seasonGames,
                        onGameClick = onGameDetailsNavigate
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SeasonGroup(
    seasonNumber: Int,
    games: List<TTGame>,
    onGameClick: (Int) -> Unit
) {
    // Локальное состояние UI: развернут ли сезон
    var isExpanded by rememberSaveable { mutableStateOf(true) }

    // Анимация вращения стрелки
    val arrowRotation by animateFloatAsState(
        targetValue = if (isExpanded) 180f else 0f,
        label = "arrowRotation"
    )

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        SeasonHeader(
            seasonNumber = seasonNumber,
            //gamesCount = games.size,
            isExpanded = isExpanded,
            arrowRotation = arrowRotation,
            onToggleExpand = { isExpanded = !isExpanded }
        )

        AnimatedVisibility(
            visible = isExpanded,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            Column(modifier = Modifier.padding(top = 4.dp, bottom = 8.dp)) {
                games.forEach { game ->
                    GameCard(
                        game = game,
                        onClick = onGameClick
                    )
                }
            }
        }
    }
}

@Composable
fun SearchPanel(
    modifier: Modifier = Modifier,
    query: String = "",
    onInputQuery: (String) -> Unit,
    onClearQuery: () -> Unit
) {
    TextField(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        value = query,
        onValueChange = {
            onInputQuery(it)
        },
        readOnly = false,
        label = {
            Text(stringResource(R.string.enter_game_title))
        },
        leadingIcon = {
            if (query.isNotEmpty()){
                IconButton( onClearQuery ) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = stringResource(R.string.clear_query)
                    )
                }
            }
        },
        trailingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = stringResource(R.string.search_games)
            )
        },
        shape = RoundedCornerShape(12.dp),
        singleLine = true,
    )
}

@Composable
fun SeasonHeader(
    seasonNumber: Int,
    isExpanded: Boolean,
    arrowRotation: Float,
    onToggleExpand: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onToggleExpand() },
        color = MaterialTheme.colorScheme.surfaceVariant
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.season_number, seasonNumber),
                modifier = Modifier
                    .weight(1f),
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
            Icon(
                imageVector = Icons.Default.KeyboardArrowDown,
                contentDescription =
                    if (isExpanded) stringResource(R.string.minimize)
                    else stringResource(R.string.open),
                modifier = Modifier.rotate(arrowRotation)
            )
        }
    }
}

@Composable
fun GameCard(
    game: TTGame,
    onClick: (Int) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable { onClick(game.id) },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Постер серии
            AsyncImage(
                model = game.imageUrl,
                contentDescription = game.title,
                modifier = Modifier
                    // Фиксируем высоту, а ширину вычисляем по пропорции
                    .height(64.dp)
                    .aspectRatio(16f / 9f)
                    .clip(RoundedCornerShape(8.dp)), // Скругляем саму картинку
                contentScale = ContentScale.Crop, // Заполняем прямоугольник без полей
                error = painterResource(R.drawable.ic_launcher_background),
                placeholder = painterResource(R.drawable.ic_launcher_background)
            )
            Spacer(modifier = Modifier.width(16.dp))
            // Описание игры
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = game.title,
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = stringResource(R.string.episode_number, game.episodeNumber),
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
            }
            // Индикатор избранного
            if (game.isFavorite) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = null,
                    tint = Color.Red,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupedFilterPanel(
    filters: Map<String, List<String>>,
    currentFilters: Map<String, List<String>>,
    sortedFilterKeys: List<String>,
    onFiltersChanged: (String, List<String>) -> Unit,
    onClearAllFilters: () -> Unit,
    modifier: Modifier = Modifier
) {
    // 1. Создаем и запоминаем состояние скролла для LazyRow
    val listState = rememberLazyListState()
    // 2. Скроллим LazyRow в самое начало (к элементу 0) при изменении отсортированных ключей
    LaunchedEffect(sortedFilterKeys) {
        if (sortedFilterKeys.isNotEmpty()) {
            listState.animateScrollToItem(0)
        }
    }
    var activeBottomSheet by remember { mutableStateOf<String?>(null) }

    // 1. Быстрые горизонтальные кнопки на главном экране
    LazyRow(
        state = listState,
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        if (currentFilters.isNotEmpty()) {
            item(key = "clear_all_filters") {
                IconButton(
                    onClick = onClearAllFilters,
                    modifier = Modifier.animateItem()
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = stringResource(R.string.drop_all_filters)
                    )
                }
            }
        }
        items(
            items = sortedFilterKeys,
            key = { it }
        ) { filterTitle ->
            val selectedValues = currentFilters[filterTitle].orEmpty()
            val isSelected = selectedValues.isNotEmpty()
            FilterChip(
                modifier = Modifier.animateItem(), // Анимация перемещения
                selected = isSelected,
                onClick = { activeBottomSheet = filterTitle },
                label = {
                    Text(if (isSelected) "$filterTitle (${selectedValues.size})" else filterTitle)
                },
                trailingIcon = {
                    Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                }
            )
        }
    }

    // 2. Выплывающая шторка для выбора конкретного параметра
    activeBottomSheet?.let { filter ->
        FilterBottomSheet(
            filterTitle = filter,
            filtersValues = filters[filter].orEmpty(),
            onDismiss = { activeBottomSheet = null },
            onApply = { updatedFilters ->
                onFiltersChanged(filter, updatedFilters)
                activeBottomSheet = null
            },
            selectedValues = currentFilters[filter].orEmpty()
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
private fun FilterBottomSheet(
    filterTitle: String,
    filtersValues: List<String>,
    selectedValues: List<String>,
    onDismiss: () -> Unit,
    onApply: (List<String>) -> Unit
) {
    val sheetState = rememberModalBottomSheetState()
    val tempFilters = remember(selectedValues) {
        mutableStateListOf<String>().apply { addAll(selectedValues) }
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = filterTitle,
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
            )
            Spacer(Modifier.height(16.dp))

            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                filtersValues.forEach { filter ->
                    val isSelected = filter in tempFilters
                    FilterChip(
                        selected = isSelected,
                        onClick = {
                            if (isSelected) tempFilters.remove(filter)
                            else tempFilters.add(filter)
                        },
                        label = { Text(filter) },
                        leadingIcon = if (isSelected) {
                            { Icon(Icons.Default.Done, contentDescription = null) }
                        } else null,
                    )
                }
            }

            Spacer(Modifier.height(24.dp))

            // Кнопки действия
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                OutlinedButton(
                    onClick = {
                        // Сброс текущей категории
                        tempFilters.clear()
                        onApply(emptyList())
                    }
                ) {
                    Text(stringResource(R.string.drop))
                }

                Button(
                    onClick = { onApply(tempFilters.toList()) }
                ) {
                    Text(stringResource(R.string.apply))
                }
            }
        }
    }
}

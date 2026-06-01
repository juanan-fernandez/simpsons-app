package com.example.bigschoolexample.ui.characters

import android.widget.Toast
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.bigschoolexample.domain.model.Character
import com.example.bigschoolexample.ui.components.CitizenProfileCard
import com.example.bigschoolexample.ui.components.CitizenSearchField
import com.example.bigschoolexample.ui.theme.BigSchoolExampleTheme
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlin.random.Random

@Composable
fun CharactersRoute(
    viewModel: CharactersViewModel,
    modifier: Modifier = Modifier,
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    CharactersScreen(
        uiState = uiState,
        onSearchQueryChanged = viewModel::onSearchQueryChanged,
        onLoadMore = viewModel::loadMoreCharacters,
        onRetry = viewModel::retry,
        onClearSearch = { viewModel.onSearchQueryChanged("") },
        onCharacterClick = { character ->
            Toast.makeText(context, character.randomToastPhrase(), Toast.LENGTH_SHORT).show()
        },
        modifier = modifier,
    )
}

@Composable
fun CharactersScreen(
    uiState: CharactersUiState,
    onSearchQueryChanged: (String) -> Unit,
    onLoadMore: () -> Unit,
    onRetry: () -> Unit,
    onClearSearch: () -> Unit,
    onCharacterClick: (Character) -> Unit,
    modifier: Modifier = Modifier,
) {
    val listState = rememberLazyListState()
    val currentUiState by rememberUpdatedState(uiState)
    val currentOnLoadMore by rememberUpdatedState(onLoadMore)

    LaunchedEffect(uiState.searchQuery) {
        if (listState.firstVisibleItemIndex > 0 || listState.firstVisibleItemScrollOffset > 0) {
            listState.animateScrollToItem(index = 0)
        }
    }

    LaunchedEffect(listState) {
        snapshotFlow {
            val layoutInfo = listState.layoutInfo
            val lastVisibleItemIndex = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            lastVisibleItemIndex to layoutInfo.totalItemsCount
        }.distinctUntilChanged().collect { (lastVisibleItemIndex, totalItemsCount) ->
            val shouldLoadMore = totalItemsCount > 0 && lastVisibleItemIndex >= totalItemsCount - 3
            val latestUiState = currentUiState

            if (
                shouldLoadMore &&
                latestUiState.searchQuery.isBlank() &&
                latestUiState.hasMoreCharacters &&
                !latestUiState.isLoading &&
                !latestUiState.isLoadingMore &&
                latestUiState.loadMoreErrorMessage.isNullOrBlank()
            ) {
                currentOnLoadMore()
            }
        }
    }

    LazyColumn(
        state = listState,
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFFFD21F))
            .safeDrawingPadding(),
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        item {
            CitizenSearchField(
                value = uiState.searchQuery,
                onValueChange = onSearchQueryChanged,
                modifier = Modifier.fillMaxWidth(),
            )
        }

        if (uiState.characters.isEmpty()) {
            item {
                AnimatedContent(
                    targetState = bodyStateFor(uiState),
                    transitionSpec = {
                        fadeIn(animationSpec = tween(180)) togetherWith
                            fadeOut(animationSpec = tween(180))
                    },
                    label = "characters_body_state",
                ) { bodyState ->
                    when (bodyState) {
                        CharactersBodyState.Loading -> LoadingContent()
                        CharactersBodyState.Offline -> OfflineContent(onRetry = onRetry)
                        CharactersBodyState.Error -> ErrorContent(
                            message = uiState.errorMessage ?: uiState.loadMoreErrorMessage.orEmpty(),
                            onRetry = onRetry,
                        )
                        CharactersBodyState.Empty -> EmptyContent(
                            searchQuery = uiState.searchQuery,
                            onClearSearch = onClearSearch,
                        )
                    }
                }
            }
        } else {
            items(
                items = uiState.characters,
                key = { character -> character.id },
            ) { character ->
                CitizenProfileCard(
                    imageUrl = character.portraitUrl,
                    name = character.name,
                    role = character.occupation,
                    ageText = character.ageLabel(),
                    quote = character.primaryPhrase(),
                    statusText = character.statusLabel(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .animateContentSize(animationSpec = tween(220))
                        .clickable { onCharacterClick(character) },
                )
            }

            if (uiState.isLoadingMore) {
                item {
                    LoadingMoreContent(modifier = Modifier.animateContentSize(animationSpec = tween(220)))
                }
            }

            if (!uiState.loadMoreErrorMessage.isNullOrBlank()) {
                item {
                    LoadMoreErrorContent(
                        message = uiState.loadMoreErrorMessage.orEmpty(),
                        onRetry = onRetry,
                        modifier = Modifier.animateContentSize(animationSpec = tween(220)),
                    )
                }
            }
        }
    }
}

private fun bodyStateFor(uiState: CharactersUiState): CharactersBodyState {
    return when {
        uiState.isLoading || (uiState.searchQuery.isNotBlank() && uiState.isLoadingMore) -> CharactersBodyState.Loading
        uiState.isOfflineError -> CharactersBodyState.Offline
        !uiState.errorMessage.isNullOrBlank() ||
            (uiState.searchQuery.isNotBlank() && !uiState.loadMoreErrorMessage.isNullOrBlank()) -> CharactersBodyState.Error
        else -> CharactersBodyState.Empty
    }
}

private enum class CharactersBodyState {
    Loading,
    Offline,
    Error,
    Empty,
}

@Composable
private fun LoadingContent(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 48.dp),
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator(color = Color.Black)
    }
}

@Composable
private fun ErrorContent(
    message: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 24.dp),
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(
                text = message,
                color = Color.Black,
                textAlign = TextAlign.Center,
            )
            Button(onClick = onRetry) {
                Text(text = "Retry")
            }
        }
    }
}

@Composable
private fun OfflineContent(
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.96f)
                .aspectRatio(1.12f),
        ) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .shadow(10.dp, RoundedCornerShape(18.dp), clip = false)
                    .clip(RoundedCornerShape(18.dp))
                    .background(Color(0xFF161616))
                    .border(width = 5.dp, color = Color.Black, shape = RoundedCornerShape(18.dp))
                    .padding(12.dp),
            ) {
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .background(Color(0xFF3A3A3A)),
                )

                Row(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(top = 10.dp),
                    horizontalArrangement = Arrangement.spacedBy(42.dp),
                ) {
                    repeat(3) {
                        Box(
                            modifier = Modifier
                                .size(14.dp)
                                .background(Color(0xFF808080), RoundedCornerShape(99.dp)),
                        )
                    }
                }

                Row(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .fillMaxWidth(0.8f)
                        .fillMaxHeight(0.68f),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    repeat(3) {
                        Box(
                            modifier = Modifier
                                .width(76.dp)
                                .fillMaxHeight()
                                .background(Color.White.copy(alpha = 0.15f))
                        )
                    }
                }

                Row(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .fillMaxWidth(0.72f),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    repeat(4) {
                        Box(
                            modifier = Modifier
                                .size(width = 58.dp, height = 36.dp)
                                .background(Color(0xFFD9D9D9))
                                .border(width = 2.dp, color = Color(0xFF4A4A4A)),
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth(0.92f)
                        .height(94.dp)
                        .background(Color(0xFF2B2B2B))
                        .border(width = 3.dp, color = Color(0xFF111111)),
                )

                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .offset(y = (-34).dp)
                        .size(width = 74.dp, height = 124.dp),
                ) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .size(width = 42.dp, height = 78.dp)
                            .background(Color(0xFF5B5B5B), RoundedCornerShape(20.dp)),
                    )
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .size(42.dp)
                            .background(Color(0xFF6A6A6A), RoundedCornerShape(99.dp)),
                    )
                }
            }

            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .offset(x = 10.dp, y = (-18).dp)
                    .background(Color(0xFFC81D18))
                    .border(width = 4.dp, color = Color.Black)
                    .padding(horizontal = 18.dp, vertical = 12.dp),
            ) {
                Text(
                    text = "!ALERTA\nNUCLEAR!",
                    style = OfflineStateDefaults.alertStyle,
                    textAlign = TextAlign.Center,
                )
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth(0.94f)
                .shadow(8.dp, RoundedCornerShape(2.dp), clip = false)
                .background(Color(0xFFF5F0DF))
                .border(width = 4.dp, color = Color.Black)
                .padding(horizontal = 24.dp, vertical = 30.dp),
        ) {
            Text(
                text = "ERROR DE CONEXION EN EL SECTOR\n7-G.\n\nLAS CAMARAS NO ESTAN\nTRANSMITIENDO.",
                style = OfflineStateDefaults.messageStyle,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .shadow(8.dp, RoundedCornerShape(24.dp), clip = false)
                .background(Color(0xFF46A9E8), RoundedCornerShape(24.dp))
                .border(width = 4.dp, color = Color.Black, shape = RoundedCornerShape(24.dp))
                .clickable(onClick = onRetry)
                .padding(vertical = 26.dp, horizontal = 18.dp),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = "REINTENTAR ACCESO",
                style = OfflineStateDefaults.buttonStyle,
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Composable
private fun LoadingMoreContent(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator(color = Color.Black)
    }
}

@Composable
private fun LoadMoreErrorContent(
    message: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(
                text = message,
                color = Color.Black,
                textAlign = TextAlign.Center,
            )
            Button(onClick = onRetry) {
                Text(text = "Retry")
            }
        }
    }
}

@Composable
private fun EmptyContent(
    searchQuery: String,
    onClearSearch: () -> Unit,
    modifier: Modifier = Modifier,
) {
    if (searchQuery.isBlank()) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .padding(vertical = 24.dp),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = "No characters found.",
                color = Color.Black,
                textAlign = TextAlign.Center,
            )
        }
        return
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.78f)
                .shadow(elevation = 10.dp, shape = RoundedCornerShape(4.dp), clip = false)
                .background(Color(0xFF3E6848))
                .border(width = 5.dp, color = Color.Black)
                .padding(6.dp),
        ) {
            AsyncImage(
                model = "https://cdn.thesimpsonsapi.com/500/character/3.webp",
                contentDescription = "Bart Simpson",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .background(Color(0xFFEDE6CF)),
            )
        }

        Text(
            text = "!AY CARAMBA!",
            style = SearchEmptyStateDefaults.titleStyle,
            textAlign = TextAlign.Center,
        )

        Text(
            text = "NO ENCONTRAMOS A NADIE CON ESE NOMBRE.",
            style = SearchEmptyStateDefaults.messageStyle,
            textAlign = TextAlign.Center,
        )

        Spacer(modifier = Modifier.height(8.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth(0.62f)
                .background(Color(0xFFFFD21F))
                .border(width = 5.dp, color = Color.Black)
                .clickable(onClick = onClearSearch)
                .padding(vertical = 24.dp, horizontal = 16.dp),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = "LIMPIAR BUSQUEDA",
                style = SearchEmptyStateDefaults.buttonStyle,
                textAlign = TextAlign.Center,
            )
        }
    }
}

private object SearchEmptyStateDefaults {
    val titleStyle = TextStyle(
        color = Color.Black,
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Black,
        fontSize = 34.sp,
        lineHeight = 34.sp,
    )

    val messageStyle = TextStyle(
        color = Color.Black,
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Black,
        fontStyle = FontStyle.Italic,
        fontSize = 18.sp,
        lineHeight = 22.sp,
    )

    val buttonStyle = TextStyle(
        color = Color.Black,
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Black,
        fontStyle = FontStyle.Italic,
        fontSize = 18.sp,
    )
}

private object OfflineStateDefaults {
    val alertStyle = TextStyle(
        color = Color.White,
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Black,
        fontStyle = FontStyle.Italic,
        fontSize = 18.sp,
        lineHeight = 20.sp,
    )

    val messageStyle = TextStyle(
        color = Color.Black,
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Black,
        fontStyle = FontStyle.Italic,
        fontSize = 20.sp,
        lineHeight = 26.sp,
    )

    val buttonStyle = TextStyle(
        color = Color.Black,
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Black,
        fontSize = 22.sp,
    )
}

private fun Character.primaryPhrase(): String {
    val availablePhrases = phrases.filter { it.isNotBlank() }

    if (availablePhrases.isEmpty()) {
        return "..."
    }

    return availablePhrases[Random(id).nextInt(availablePhrases.size)]
}

private fun Character.ageLabel(): String {
    return age?.let { "AGE $it" } ?: "AGE ?"
}

private fun Character.statusLabel(): String {
    return status.uppercase()
}

private fun Character.randomToastPhrase(): String {
    val availablePhrases = phrases.filter { it.isNotBlank() }

    if (availablePhrases.isEmpty()) {
        return "..."
    }

    return availablePhrases.random()
}

@Preview(showBackground = true, backgroundColor = 0xFFFFD21F)
@Composable
private fun CharactersScreenPreview() {
    BigSchoolExampleTheme {
        CharactersScreen(
            uiState = CharactersUiState(
                characters = listOf(
                    Character(
                        id = 1,
                        name = "Homer Simpson",
                        age = 39,
                        occupation = "Nuclear Safety Inspector",
                        portraitUrl = "https://images.unsplash.com/photo-1516321318423-f06f85e504b3?auto=format&fit=crop&w=900&q=80",
                        phrases = listOf("D'oh!"),
                        status = "Alive",
                    ),
                ),
            ),
            onSearchQueryChanged = {},
            onLoadMore = {},
            onRetry = {},
            onClearSearch = {},
            onCharacterClick = {},
        )
    }
}

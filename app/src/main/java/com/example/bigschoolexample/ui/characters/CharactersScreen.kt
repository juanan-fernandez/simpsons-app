package com.example.bigschoolexample.ui.characters

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.bigschoolexample.domain.model.Character
import com.example.bigschoolexample.ui.components.CitizenProfileCard
import com.example.bigschoolexample.ui.components.CitizenSearchField
import com.example.bigschoolexample.ui.theme.BigSchoolExampleTheme

@Composable
fun CharactersRoute(
    viewModel: CharactersViewModel,
    modifier: Modifier = Modifier,
) {
    val uiState by viewModel.uiState.collectAsState()

    CharactersScreen(
        uiState = uiState,
        onSearchQueryChanged = viewModel::onSearchQueryChanged,
        onRetry = viewModel::retry,
        modifier = modifier,
    )
}

@Composable
fun CharactersScreen(
    uiState: CharactersUiState,
    onSearchQueryChanged: (String) -> Unit,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
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

        when {
            uiState.isLoading && uiState.characters.isEmpty() -> {
                item {
                    LoadingContent()
                }
            }

            !uiState.errorMessage.isNullOrBlank() && uiState.characters.isEmpty() -> {
                item {
                    ErrorContent(
                        message = uiState.errorMessage,
                        onRetry = onRetry,
                    )
                }
            }

            uiState.characters.isEmpty() -> {
                item {
                    EmptyContent()
                }
            }

            else -> {
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
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            }
        }
    }
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
        androidx.compose.foundation.layout.Column(
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
private fun EmptyContent(modifier: Modifier = Modifier) {
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
}

private fun Character.primaryPhrase(): String {
    return phrases.firstOrNull()?.takeIf { it.isNotBlank() } ?: "..."
}

private fun Character.ageLabel(): String {
    return age?.let { "AGE $it" } ?: "AGE ?"
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
                    ),
                ),
            ),
            onSearchQueryChanged = {},
            onRetry = {},
        )
    }
}

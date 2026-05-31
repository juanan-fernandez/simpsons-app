package com.example.bigschoolexample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.example.bigschoolexample.ui.characters.CharactersRoute
import com.example.bigschoolexample.ui.characters.CharactersViewModel
import com.example.bigschoolexample.ui.characters.CharactersViewModelFactory
import com.example.bigschoolexample.ui.theme.BigSchoolExampleTheme

class MainActivity : ComponentActivity() {

    private val viewModel: CharactersViewModel by viewModels {
        CharactersViewModelFactory(
            (application as SimpsonsApplication).appContainer.getCharactersUseCase,
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BigSchoolExampleTheme {
                CharactersRoute(viewModel = viewModel)
            }
        }
    }
}

package com.opentable.openfoods

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.opentable.openfoods.feature.foodlist.ui.FoodListScreen
import com.opentable.openfoods.feature.foodlist.ui.FoodListScreenVM
import com.opentable.openfoods.ui.theme.OpenFoodsTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Currently not supporting dark mode.
        //Always falling to light mode wth dark icons in status and nav bar.
        enableEdgeToEdge(statusBarStyle = SystemBarStyle.auto(
            Color.Transparent.toArgb(), // Translucent status bar background
            Color.Transparent.toArgb()
        ) { false }, // isLight: true for dark icons/text
            navigationBarStyle = SystemBarStyle.auto(
                Color.Transparent.toArgb(), // Translucent nav bar background
                Color.Transparent.toArgb()
            ) { false })
        setContent {
            val snackbarHostState = remember { SnackbarHostState() }
            val scope = rememberCoroutineScope()

            OpenFoodsTheme {
                Scaffold(modifier = Modifier.fillMaxSize().windowInsetsPadding(
                    WindowInsets.statusBars
                ).windowInsetsPadding(WindowInsets.navigationBars),
                    snackbarHost = { SnackbarHost(snackbarHostState) },
                    topBar = {
                        TopAppBar(
                            title = { Text("OpenFood", fontWeight = FontWeight.Bold, fontSize = 24.sp) },
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = Color.White
                            ),
                            scrollBehavior = null,
                            modifier = Modifier.shadow(4.dp) // Add shadow
                        )
                    }
                ) { innerPadding ->
                    FoodListScreenVM(modifier = Modifier.background(Color.White).fillMaxSize().padding(innerPadding)) {
                        scope.launch {
                            snackbarHostState.showSnackbar(it)
                        }
                    }
                    //Greeting("Nikki")
                }
            }
        }
    }
}

@Composable
fun Greeting(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text,
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    OpenFoodsTheme {
        Greeting("Good luck with your tech test!")
    }
}
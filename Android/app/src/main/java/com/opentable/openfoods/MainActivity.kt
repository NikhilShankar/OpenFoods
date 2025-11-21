package com.opentable.openfoods

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.opentable.openfoods.feature.foodlist.ui.FoodListScreen
import com.opentable.openfoods.feature.foodlist.ui.FoodListScreenVM
import com.opentable.openfoods.ui.theme.OpenFoodsTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            OpenFoodsTheme {
                Scaffold(modifier = Modifier.fillMaxSize().windowInsetsPadding(
                    WindowInsets.statusBars
                ).windowInsetsPadding(WindowInsets.navigationBars)) { innerPadding ->
                    FoodListScreenVM(modifier = Modifier.padding(innerPadding))
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
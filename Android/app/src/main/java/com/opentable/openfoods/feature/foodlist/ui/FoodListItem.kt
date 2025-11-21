package com.opentable.openfoods.feature.foodlist.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.opentable.openfoods.feature.foodlist.models.FoodItem
import com.opentable.openfoods.ui.theme.OpenFoodsTheme

@Composable
fun FoodItemCard(item: FoodItem, onFavClick: (FoodItem) -> Unit = {}) {
    val fillProgress = animateFloatAsState(
        targetValue = if (item.isLiked) 1f else 0f,
        animationSpec = tween(durationMillis = 2000),
        label = "fillAnimation",

        )

    val startColor = Color.LightGray
    val endColor = Color(0xFFFF6B6B)

    val gradientBrush = Brush.linearGradient(
        0f to if (fillProgress.value > 0f) endColor else startColor,
        fillProgress.value.coerceAtLeast(0.01f) to endColor,
        fillProgress.value to startColor,
        1f to startColor,
        start = androidx.compose.ui.geometry.Offset(0f, 0f),
        end = androidx.compose.ui.geometry.Offset.Infinite
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(horizontal = 8.dp, vertical = 6.dp)
            .background(gradientBrush, shape = RoundedCornerShape(12.dp)),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors().copy(containerColor = Color.Transparent)

    ) {
        Card(
            modifier = Modifier.fillMaxWidth().wrapContentHeight()
                .padding(horizontal = 1.5.dp, vertical = 1.dp),
            shape = RoundedCornerShape(12.dp),
            border = BorderStroke(width = 0.1.dp, color = Color.Black),
            colors = CardDefaults.cardColors().copy(containerColor = Color.LightGray)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth().wrapContentHeight().padding(horizontal = 8.dp, vertical = 4.dp), horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Top
            ) {
                Text(item.name, fontSize = 24.sp)
                Text(item.description ?: "", fontSize = 16.sp)
                Text(item.countryOfOrigin ?: "", fontSize = 16.sp)

                Row {
                    Spacer(modifier = Modifier.weight(1f))
                    Icon(modifier = Modifier.clickable {
                        onFavClick.invoke(item)
                    },
                        imageVector = if(item.isLiked) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                        contentDescription = "Rating"
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ListItemPreview() {
    OpenFoodsTheme {
        val item = remember {
            mutableStateOf(FoodItem(
                id = 1,
                name = "Pizza",
                isLiked = false,
                photoUrl = null,
                description = null,
                countryOfOrigin = null,
                lastUpdatedDate = null
            ))
        }
        FoodItemCard(item.value) {
            item.value = item.value.copy(isLiked = !item.value.isLiked)
        }
    }
}
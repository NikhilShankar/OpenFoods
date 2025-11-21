package com.opentable.openfoods.feature.foodlist.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.opentable.openfoods.feature.foodlist.models.FoodItem
import com.opentable.openfoods.ui.theme.LikePink
import com.opentable.openfoods.ui.theme.OpenFoodsTheme

@Composable
fun FoodItemCard(item: FoodItem, onFavClick: (FoodItem) -> Unit = {}) {

    val outerCardColor = animateColorAsState(targetValue = if(item.isLiked) LikePink else LikePink.copy(alpha = 0.0f),
        animationSpec = tween(durationMillis = 500),
        label = "cardColorAnimation")
    val favIconColor = animateColorAsState(targetValue = if(item.isLiked) LikePink else Color.Black.copy(alpha = 0.33f),
        animationSpec = tween(durationMillis = 1000),
        label = "cardColorAnimation")
    val clicked = remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(horizontal = 8.dp, vertical = 6.dp),
        colors = CardDefaults.cardColors().copy(containerColor = outerCardColor.value),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(12.dp)

        ) {
        Card(
            modifier = Modifier.fillMaxWidth().wrapContentHeight()
                .padding(horizontal = 2.dp, vertical = 2.dp).clickable {
                    clicked.value = !clicked.value
                },
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors().copy(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth().wrapContentHeight().padding(horizontal = 12.dp, vertical = 12.dp), horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Top
            ) {
                Text(item.name, fontSize = 24.sp, fontWeight = FontWeight.Bold)

                Text(item.description ?: "",
                    fontSize = 16.sp,
                    maxLines = if(clicked.value) Int.MAX_VALUE else 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.animateContentSize( // Add this for smooth height animation
                        animationSpec = tween(
                            300, easing = EaseInOut
                        )
                    ),
                )
                Text(item.countryOfOrigin ?: "", fontSize = 16.sp)

                Row {
                    Spacer(modifier = Modifier.weight(1f))
                    Icon(modifier = Modifier.size(28.dp).clickable {
                        onFavClick.invoke(item)
                    },
                        imageVector = if(item.isLiked) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                        contentDescription = "Rating",
                        tint = favIconColor.value)
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
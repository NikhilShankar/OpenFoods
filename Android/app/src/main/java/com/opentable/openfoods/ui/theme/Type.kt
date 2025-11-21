package com.opentable.openfoods.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.opentable.openfoods.R


// Define your custom font family
val CustomFontFamily = FontFamily(
    Font(R.font.mirage_regular, FontWeight.Normal),
    Font(R.font.mirage_medium, FontWeight.Medium),
    Font(R.font.mirage_bold, FontWeight.Bold)
)

// Extension function to apply font family to all styles
fun Typography.withFontFamily(fontFamily: FontFamily): Typography {
    return this.copy(
        displayLarge = this.displayLarge.copy(fontFamily = fontFamily, color = Color.Black),
        displayMedium = this.displayMedium.copy(fontFamily = fontFamily, color = Color.Black),
        displaySmall = this.displaySmall.copy(fontFamily = fontFamily, color = Color.Black),
        headlineLarge = this.headlineLarge.copy(fontFamily = fontFamily, color = Color.Black),
        headlineMedium = this.headlineMedium.copy(fontFamily = fontFamily, color = Color.Black),
        headlineSmall = this.headlineSmall.copy(fontFamily = fontFamily, color = Color.Black),
        titleLarge = this.titleLarge.copy(fontFamily = fontFamily, color = Color.Black),
        titleMedium = this.titleMedium.copy(fontFamily = fontFamily, color = Color.Black),
        titleSmall = this.titleSmall.copy(fontFamily = fontFamily, color = Color.Black),
        bodyLarge = this.bodyLarge.copy(fontFamily = fontFamily, color = Color.Black),
        bodyMedium = this.bodyMedium.copy(fontFamily = fontFamily, color = Color.Black),
        bodySmall = this.bodySmall.copy(fontFamily = fontFamily, color = Color.Black),
        labelLarge = this.labelLarge.copy(fontFamily = fontFamily, color = Color.Black),
        labelMedium = this.labelMedium.copy(fontFamily = fontFamily, color = Color.Black),
        labelSmall = this.labelSmall.copy(fontFamily = fontFamily, color = Color.Black),
    )
}

// Set of Material typography styles to start with
val Typography = Typography(

    bodyLarge = TextStyle(
        fontFamily = CustomFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )
    /* Other default text styles to override
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
    */
).withFontFamily(CustomFontFamily)
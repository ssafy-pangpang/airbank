package com.example.myapplication.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.example.myapplication.R

object PretendardFont {
    val Pretendard = FontFamily(
        Font(R.font.pretendardregular, FontWeight.Normal),
        Font(R.font.pretendardblack, FontWeight.Black),
        Font(R.font.pretendardbold, FontWeight.Bold),
        Font(R.font.pretendardextrabold, FontWeight.ExtraBold),
        Font(R.font.pretendardextralight, FontWeight.ExtraLight),
        Font(R.font.pretendardlight, FontWeight.Light),
        Font(R.font.pretendardmedium, FontWeight.Medium),
        Font(R.font.pretendardregular, FontWeight.Normal),
        Font(R.font.pretendardsemibold, FontWeight.SemiBold),
        Font(R.font.pretendardthin, FontWeight.Thin)
    )
}
private val defaultTypography = Typography()

// Set of Material typography styles to start with
val Typography = Typography(
//    bodyLarge = TextStyle(
//        fontFamily = FontFamily.Default,
//        fontWeight = FontWeight.Normal,
//        fontSize = 16.sp,
//        lineHeight = 24.sp,
//        letterSpacing = 0.5.sp
//    )
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


    displayLarge = defaultTypography.displayLarge.copy(fontFamily = PretendardFont.Pretendard),
    displayMedium = defaultTypography.displayMedium.copy(fontFamily = PretendardFont.Pretendard),
    displaySmall = defaultTypography.displaySmall.copy(fontFamily = PretendardFont.Pretendard),

    headlineLarge = defaultTypography.headlineLarge.copy(fontFamily = PretendardFont.Pretendard),
    headlineMedium = defaultTypography.headlineMedium.copy(fontFamily = PretendardFont.Pretendard),
    headlineSmall = defaultTypography.headlineSmall.copy(fontFamily = PretendardFont.Pretendard),

    titleLarge = defaultTypography.titleLarge.copy(fontFamily = PretendardFont.Pretendard),
    titleMedium = defaultTypography.titleMedium.copy(fontFamily = PretendardFont.Pretendard),
    titleSmall = defaultTypography.titleSmall.copy(fontFamily = PretendardFont.Pretendard),

    bodyLarge = defaultTypography.bodyLarge.copy(fontFamily = PretendardFont.Pretendard),
    bodyMedium = defaultTypography.bodyMedium.copy(fontFamily = PretendardFont.Pretendard),
    bodySmall = defaultTypography.bodySmall.copy(fontFamily = PretendardFont.Pretendard),

    labelLarge = defaultTypography.labelLarge.copy(fontFamily = PretendardFont.Pretendard),
    labelMedium = defaultTypography.labelMedium.copy(fontFamily = PretendardFont.Pretendard),
    labelSmall = defaultTypography.labelSmall.copy(fontFamily = PretendardFont.Pretendard)
)
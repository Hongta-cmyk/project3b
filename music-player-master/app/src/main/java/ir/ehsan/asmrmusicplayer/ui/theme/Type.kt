package ir.ehsan.asmrmusicplayer.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import ir.ehsan.asmrmusicplayer.R


val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily(listOf(
            Font(R.font.pusia)
        )),
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )

)
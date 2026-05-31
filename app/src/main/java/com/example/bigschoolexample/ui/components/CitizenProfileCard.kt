package com.example.bigschoolexample.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.bigschoolexample.ui.theme.BigSchoolExampleTheme

@Composable
fun CitizenProfileCard(
    imageUrl: String,
    name: String,
    role: String,
    ageText: String,
    quote: String,
    modifier: Modifier = Modifier,
    actionText: String? = null,
    onActionClick: (() -> Unit)? = null,
    imageContentDescription: String? = name,
    statusText: String? = null,
    contentPadding: PaddingValues = PaddingValues(24.dp),
    containerColor: Color = Color(0xFFFFFEF7),
    borderColor: Color = Color.Black,
    buttonColor: Color = Color(0xFF42B8FF),
    statusColor: Color = Color(0xFF15E66D),
    statusTextColor: Color = Color.White,
    nameStyle: TextStyle = CitizenProfileCardDefaults.nameStyle,
    roleStyle: TextStyle = CitizenProfileCardDefaults.roleStyle,
    ageStyle: TextStyle = CitizenProfileCardDefaults.ageStyle,
    quoteStyle: TextStyle = CitizenProfileCardDefaults.quoteStyle,
    actionStyle: TextStyle = CitizenProfileCardDefaults.actionStyle,
    borderWidth: Dp = 5.dp,
    imageBorderWidth: Dp = 4.dp,
    actionBorderWidth: Dp = 5.dp,
) {
    Box(
        modifier = modifier
            .border(width = borderWidth, color = borderColor)
            .background(containerColor)
            .padding(contentPadding)
    ) {
        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(modifier = Modifier.fillMaxWidth()) {
                AsyncImage(
                    model = imageUrl,
                    contentDescription = imageContentDescription,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(0.9f)
                        .border(width = imageBorderWidth, color = borderColor)
                        .background(Color(0xFFE2D6C2))
                )

                if (statusText != null) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .offset(x = (-16).dp, y = 18.dp)
                            .background(statusColor)
                            .border(width = 3.dp, color = borderColor)
                            .padding(horizontal = 14.dp, vertical = 10.dp)
                    ) {
                        Text(
                            text = statusText,
                            style = CitizenProfileCardDefaults.statusStyle.copy(color = statusTextColor)
                        )
                    }
                }
            }

            Text(
                text = name,
                style = nameStyle,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                TextBadge(
                    text = role,
                    textStyle = roleStyle,
                    containerColor = Color(0xFFF5F5F5),
                    borderColor = Color(0xFFD4D4D4)
                )

                TextBadge(
                    text = ageText,
                    textStyle = ageStyle,
                    containerColor = Color(0xFFFFE028),
                    borderColor = borderColor
                )
            }

            QuoteBubble(
                text = quote,
                textStyle = quoteStyle,
                borderColor = borderColor
            )

            if (!actionText.isNullOrBlank() && onActionClick != null) {
                Spacer(modifier = Modifier.height(8.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.92f)
                        .background(buttonColor)
                        .border(width = actionBorderWidth, color = borderColor)
                        .clickable(onClick = onActionClick)
                        .padding(vertical = 18.dp, horizontal = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = actionText,
                        style = actionStyle,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Composable
private fun TextBadge(
    text: String,
    textStyle: TextStyle,
    containerColor: Color,
    borderColor: Color,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(horizontal = 14.dp, vertical = 10.dp),
) {
    Box(
        modifier = modifier
            .background(containerColor)
            .border(width = 2.dp, color = borderColor)
            .padding(contentPadding)
    ) {
        Text(text = text, style = textStyle)
    }
}

@Composable
private fun QuoteBubble(
    text: String,
    textStyle: TextStyle,
    borderColor: Color,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .padding(top = 12.dp)
            .fillMaxWidth(0.95f)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(28.dp))
                .background(Color.White)
                .border(width = 4.dp, color = borderColor, shape = RoundedCornerShape(28.dp))
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            Text(
                text = text,
                style = textStyle,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
            )
        }

        TextBadge(
            text = "FRASE",
            textStyle = CitizenProfileCardDefaults.roleStyle.copy(color = Color.Black),
            containerColor = Color(0xFFFFE028),
            borderColor = borderColor,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .offset(x = (-18).dp, y = (-16).dp),
            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
        )
    }
}

object CitizenProfileCardDefaults {
    val nameStyle = TextStyle(
        color = Color.Black,
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Black,
        fontStyle = FontStyle.Italic,
        fontSize = 30.sp,
        lineHeight = 30.sp
    )

    val roleStyle = TextStyle(
        color = Color(0xFF1197E8),
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Black,
        fontStyle = FontStyle.Italic,
        fontSize = 16.sp
    )

    val ageStyle = TextStyle(
        color = Color.Black,
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Black,
        fontStyle = FontStyle.Italic,
        fontSize = 18.sp
    )

    val quoteStyle = TextStyle(
        color = Color.Black,
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Black,
        fontStyle = FontStyle.Italic,
        fontSize = 18.sp
    )

    val actionStyle = TextStyle(
        color = Color.Black,
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Black,
        fontStyle = FontStyle.Italic,
        fontSize = 22.sp
    )

    val statusStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Black,
        fontStyle = FontStyle.Italic,
        fontSize = 18.sp
    )
}

@Preview(showBackground = true, backgroundColor = 0xFFF6F0DF)
@Composable
private fun CitizenProfileCardPreview() {
    BigSchoolExampleTheme {
        CitizenProfileCard(
            imageUrl = "https://images.unsplash.com/photo-1516321318423-f06f85e504b3?auto=format&fit=crop&w=900&q=80",
            name = "HOMER SIMP...",
            role = "SAFETY INSPECTOR",
            ageText = "AGE 39",
            quote = "\"DOH!\"",
            actionText = "WHO'S THIS?",
            statusText = "ALIVE",
            onActionClick = {},
            modifier = Modifier.padding(16.dp)
        )
    }
}

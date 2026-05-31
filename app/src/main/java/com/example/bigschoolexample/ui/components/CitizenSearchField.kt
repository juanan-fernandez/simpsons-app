package com.example.bigschoolexample.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bigschoolexample.ui.theme.BigSchoolExampleTheme

@Composable
fun CitizenSearchField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "FIND A CITIZEN...",
    enabled: Boolean = true,
    readOnly: Boolean = false,
    singleLine: Boolean = true,
    minHeight: Dp = 72.dp,
    onSearch: (() -> Unit)? = null,
    textStyle: TextStyle = CitizenSearchFieldDefaults.textStyle,
    placeholderStyle: TextStyle = CitizenSearchFieldDefaults.placeholderStyle,
    containerColor: Color = Color.White,
    borderColor: Color = Color.Black,
    borderWidth: Dp = 4.dp,
    contentPadding: PaddingValues = PaddingValues(horizontal = 16.dp, vertical = 14.dp),
    leadingIcon: @Composable (() -> Unit) = {
        Icon(
            imageVector = Icons.Filled.Search,
            contentDescription = null,
            tint = Color.Black,
            modifier = Modifier.size(24.dp)
        )
    },
    keyboardOptions: KeyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
    keyboardActions: KeyboardActions = KeyboardActions(onSearch = { onSearch?.invoke() }),
) {
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        enabled = enabled,
        readOnly = readOnly,
        singleLine = singleLine,
        textStyle = textStyle,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        cursorBrush = SolidColor(borderColor),
        decorationBox = { innerTextField ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .defaultMinSize(minHeight = minHeight)
                    .background(containerColor)
                    .border(width = borderWidth, color = borderColor)
                    .padding(contentPadding),
                verticalAlignment = Alignment.CenterVertically
            ) {
                leadingIcon()

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 16.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    if (value.isEmpty()) {
                        Text(
                            text = placeholder,
                            style = placeholderStyle,
                            modifier = Modifier.alpha(if (enabled) 1f else 0.6f)
                        )
                    }

                    innerTextField()
                }
            }
        }
    )
}

object CitizenSearchFieldDefaults {
    val textStyle = TextStyle(
        color = Color(0xFF111111),
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Bold,
        fontStyle = FontStyle.Italic,
        fontSize = 24.sp,
        letterSpacing = 0.5.sp
    )

    val placeholderStyle = textStyle.copy(
        color = Color(0xFF6E7484),
        fontWeight = FontWeight.Black
    )
}

@Preview(showBackground = true, backgroundColor = 0xFFFFD21F)
@Composable
private fun CitizenSearchFieldPreview() {
    BigSchoolExampleTheme {
        var value by remember { mutableStateOf("") }

        CitizenSearchField(
            value = value,
            onValueChange = { value = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )
    }
}

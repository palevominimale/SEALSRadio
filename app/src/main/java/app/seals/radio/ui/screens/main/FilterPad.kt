package app.seals.radio.ui.screens.main

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import app.seals.radio.R
import app.seals.radio.ui.theme.Typography

@Composable
@Preview
fun FilterPad() {
    Surface(
        color = Color.White,
        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 5.dp,
                shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
            )
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .padding(top = 8.dp)
                    .fillMaxWidth()
            ) {
                Row() {
                    Icon(
                        imageVector = Icons.Outlined.Close,
                        contentDescription = null,
                        tint = Color.LightGray,
                        modifier = Modifier
                            .size(30.dp)
                            .clip(CircleShape)
                            .clickable {

                            }
                    )
                    Icon(
                        imageVector = Icons.Outlined.Delete,
                        contentDescription = null,
                        tint = Color.LightGray,
                        modifier = Modifier
                            .size(30.dp)
                            .clip(CircleShape)
                            .clickable {

                            }
                    )
                }
                Text(
                    text = stringResource(R.string.filter_title),
                    textAlign = TextAlign.Center,
                    style = Typography.labelLarge,
                    modifier = Modifier
                        .padding(8.dp)
                )

                Icon(
                    imageVector = Icons.Outlined.Done,
                    contentDescription = null,
                    tint = Color.LightGray,
                    modifier = Modifier
                        .padding(start = 30.dp)
                        .size(30.dp)
                        .clip(CircleShape)
                        .clickable {

                        }
                )
            }

            Surface(
                color = Color.LightGray,
                shape = RoundedCornerShape(2.dp),
                content = {},
                modifier = Modifier
                    .height(1.dp)
                    .fillMaxWidth()
            )
            Text(
                text = stringResource(R.string.filter_countries),
                textAlign = TextAlign.Center,
                style = Typography.labelMedium,
                modifier = Modifier
                    .padding(vertical = 8.dp,)
            )
            DropdownList(
                items = listOf(
                    "France",
                    "Germany",
                    "Austria",
                    "Spain",
                    "Italy",
                    "Netherlands",
                    "Poland",
                    "Ukraine",
                )
            )
            Text(
                text = stringResource(R.string.filter_languages),
                textAlign = TextAlign.Center,
                style = Typography.labelMedium,
                modifier = Modifier
                    .padding(vertical = 8.dp,)
            )
            DropdownList(
                items = listOf(
                    "French",
                    "German",
                    "English",
                    "Russian",
                )
            )
            Text(
                text = stringResource(R.string.filter_tags),
                textAlign = TextAlign.Center,
                style = Typography.labelMedium,
                modifier = Modifier
                    .padding(vertical = 8.dp,)
            )
            SearchTextField(
                modifier = Modifier
                    .padding(bottom = 16.dp)
            )
        }
    }
}

@Composable
private fun DropdownList(
    modifier: Modifier = Modifier,
    style: TextStyle = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Light),
    items: List<String>
) {

    var expanded by remember { mutableStateOf(false) }
    var selectedText by remember { mutableStateOf("") }
    var textFieldSize by remember { mutableStateOf(Size.Zero) }

    val icon =
        if (expanded) Icons.Filled.KeyboardArrowUp
        else Icons.Filled.KeyboardArrowDown

    Surface(
        shape = RoundedCornerShape(10.dp),
        color = Color.White,
        border = BorderStroke(1.dp, Color.LightGray),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(4.dp)
        ) {
            BasicTextField(modifier = Modifier
                .onGloballyPositioned {
                    textFieldSize = it.size.toSize()
                },
                readOnly = true,
                value = selectedText,
                onValueChange = { selectedText = it },
                singleLine = true,
                cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                textStyle = style,
                decorationBox = { innerTextField ->
                    Row(
                        modifier = Modifier.padding(start = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(Modifier.weight(1f)) {
                            if (selectedText.isEmpty()) {
                                Text(
                                    text = items[0],
                                    style = style.copy(Color.LightGray)
                                )
                            }
                            innerTextField()
                        }
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            modifier = Modifier
                                .clickable { expanded = !expanded }
                        )
                    }
                }
            )
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier
                    .width(with(LocalDensity.current) { textFieldSize.width.toDp() })
                    .background(color = Color.Unspecified, shape = RoundedCornerShape(20.dp))
            ) {
                items.forEach { label ->
                    DropdownMenuItem(text = { Text(text = label) },
                        onClick = {
                            selectedText = label
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun SearchTextField(
    modifier: Modifier = Modifier,
    style: TextStyle = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Light),
) {
    var value by remember { mutableStateOf("") }

    Surface(
        shape = RoundedCornerShape(10.dp),
        color = Color.White,
        border = BorderStroke(1.dp, Color.LightGray),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(4.dp)
        ) {
            BasicTextField(
                modifier = Modifier.padding(vertical = 4.dp),
                value = value,
                onValueChange = { value = it },
                singleLine = true,
                cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                textStyle = style,
                decorationBox = { innerTextField ->
                    Row(
                        modifier = Modifier.padding(start = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(Modifier.weight(1f)) {
                            if (value.isEmpty()) {
                                Text(
                                    text = stringResource(R.string.filter_tags_placeholder),
                                    style = style.copy(Color.LightGray)
                                )
                            }
                            innerTextField()
                        }
                    }
                }
            )
        }
    }
}
package app.seals.radio.ui.bars

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.seals.radio.R
import app.seals.radio.ui.theme.Typography

@Composable
@Preview
fun SearchBar(
    switchFilter: () -> Unit = {},
    searchUpdate: (String) -> Unit = {},
    filterVisible: Boolean = false,
    modifier: Modifier = Modifier
) {

    val focusManager = LocalFocusManager.current

    Surface(
        color = Color.White,
        shadowElevation = 5.dp,
        modifier = modifier
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            CustomSearchField(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f)
                    .shadow(
                        elevation = 5.dp,
                        shape = RoundedCornerShape(20.dp)
                    )
                    .background(
                        color = Color.White,
                        shape = RoundedCornerShape(20.dp)
                    ),
                leadingIcon = {
                    Icon(
                        modifier = Modifier.padding(end = 8.dp),
                        painter = rememberVectorPainter(Icons.Outlined.Search),
                        contentDescription = stringResource(id = R.string.search_text),
                        tint = Color.LightGray,
                    )
                },
                style = Typography.labelMedium,
                textUpdate = {
                    searchUpdate(it)
                    focusManager.clearFocus(true)
                }
            )
            Icon(
                imageVector = Icons.Default.Menu,
                contentDescription = null,
                tint = Color.LightGray,
                modifier = Modifier
                    .padding(start = 16.dp)
                    .clip(CircleShape)
                    .clickable { switchFilter() }
            )
        }
    }
}

@Composable
private fun CustomSearchField(
    modifier: Modifier = Modifier,
    leadingIcon: (@Composable () -> Unit)? = null,
    trailingIcon: (@Composable () -> Unit)? = null,
    placeholderText: String = stringResource(id = R.string.search_text),
    style: TextStyle = MaterialTheme.typography.labelMedium,
    textUpdate: (text: String) -> Unit = {}
) {

    var text by remember { mutableStateOf("") }

    BasicTextField(
        modifier = modifier,
        value = text,
        onValueChange = {
            text = it
            },
        singleLine = true,
        cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
        textStyle = style,
        decorationBox = { innerTextField ->
            Row(
                modifier.padding(start = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (leadingIcon != null) leadingIcon()
                Box(Modifier.weight(1f)) {
                    if (text.isEmpty()) Text(
                        placeholderText,
                        style = style
                    )
                    innerTextField()
                }
                if (trailingIcon != null) trailingIcon()
            }
        },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Go),
        keyboardActions = KeyboardActions(
            onGo = {
                textUpdate(text)
            }
        )
    )
}

@Composable
private fun ClearFocus() {
    LocalFocusManager.current.clearFocus(force = true)
}

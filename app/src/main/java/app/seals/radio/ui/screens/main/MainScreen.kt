package app.seals.radio.ui.screens.main

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.seals.radio.ui.theme.Typography
import app.seals.redio.entities.StationModel
import coil.compose.AsyncImage
import com.google.accompanist.placeholder.PlaceholderDefaults
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.placeholder
import com.google.accompanist.placeholder.shimmer

@Composable
@Preview
fun MainScreen(
    list: List<StationModel?> =
        mutableListOf<StationModel?>().apply { repeat(10) {this.add(null)} },
    placeholders: Boolean = true,
    modifier: Modifier = Modifier
) {

    LazyColumn(
        modifier = modifier.fillMaxSize()
    ) {
        list.forEachIndexed { _, model ->
            item {
                StationItem(model)
            }
        }
    }
}

@Composable
private fun StationItem(model : StationModel? = null, modifier: Modifier = Modifier) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = Color.White,
        shadowElevation = 5.dp,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .placeholder(
                visible = model == null,
                color = Color.LightGray,
                shape = RoundedCornerShape(20.dp),
                highlight = PlaceholderHighlight.shimmer(
                    highlightColor = Color.White,
                    animationSpec = PlaceholderDefaults.shimmerAnimationSpec
                )
            )
    ) {
        if (model != null) {
            Row(
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    var placeholderState by mutableStateOf(true)
                    AsyncImage(
                        model = model.favicon,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        alignment = Alignment.Center,
                        modifier = Modifier
                            .padding(8.dp)
                            .size(80.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .placeholder(
                                visible = placeholderState,
                                color = Color.LightGray,
                                shape = RoundedCornerShape(20.dp),
                                highlight = PlaceholderHighlight.shimmer(
                                    highlightColor = Color.White,
                                    animationSpec = PlaceholderDefaults.shimmerAnimationSpec
                                )
                            ),
                        onState = { placeholderState = false}
                    )
                }
                Column(
                    verticalArrangement = Arrangement.SpaceAround,
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = model.name.toString(),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        style = Typography.labelMedium
                    )
                    Text(
                        text = model.country.toString(),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = Typography.labelSmall
                    )
                    Text(
                        text = model.codec.toString(),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = Typography.labelSmall
                    )
                }
            }
        }
    }
}
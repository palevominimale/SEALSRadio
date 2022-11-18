package app.seals.radio.ui.screens.main

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.seals.radio.ui.theme.Typography
import app.seals.radio.entities.responses.StationModel
import coil.compose.AsyncImage
import com.google.accompanist.placeholder.PlaceholderDefaults
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.placeholder
import com.google.accompanist.placeholder.shimmer
import app.seals.radio.R
import app.seals.radio.main.MainActivityViewModel
import coil.compose.AsyncImagePainter
import coil.request.ImageRequest

@Composable
@Preview
fun MainScreen(
    list: List<StationModel?> =
        mutableListOf<StationModel?>().apply { repeat(10) { this.add(null) } },
    placeholders: Boolean = true,
    vm: MainActivityViewModel? = null,
    modifier: Modifier = Modifier
) {

    LazyColumn(
        modifier = modifier.fillMaxSize()
    ) {
        list.forEachIndexed { _, model ->
            item {
                StationItem(model, onClick = { vm!!.selectStation(it) })
            }
        }
    }
}

@Composable
private fun StationItem(
    model : StationModel? = null,
    onClick: (item: StationModel) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = Color.White,
        shadowElevation = 5.dp,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable(
                indication = null,
                interactionSource = MutableInteractionSource()
            ) {
                onClick(model ?: StationModel())
            }
    ) {
        val highlight = PlaceholderHighlight.shimmer(
            highlightColor = Color.White,
            animationSpec = PlaceholderDefaults.shimmerAnimationSpec
        )
        Row(
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column {
                var placeholderState by remember { mutableStateOf(true) }

                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(model?.favicon)
                        .error(R.drawable.ic_radio)
                        .crossfade(true)
                        .build(),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    alignment = Alignment.Center,
                    modifier = Modifier
                        .padding(8.dp)
                        .size(80.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .placeholder(
                            visible = placeholderState,
                            color = Color.LightGray,
                            shape = RoundedCornerShape(16.dp),
                            highlight = highlight
                        ),
                    onState = {
                        if(it is AsyncImagePainter.State.Success) placeholderState = false
                        if(it is AsyncImagePainter.State.Error) {
                            placeholderState = false
                        }
                    }
                )
            }
            Column(
                verticalArrangement = Arrangement.SpaceAround,
                horizontalAlignment = Alignment.Start
            ) {
                val placeholderState = model == null
                Text(
                    text = model?.name ?: stringResource(id = R.string.stations_name),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    style = Typography.labelMedium,
                    modifier = Modifier
                        .placeholder(
                            visible = placeholderState,
                            color = Color.LightGray,
                            shape = RoundedCornerShape(20.dp),
                            highlight = highlight
                        )
                        .padding(end = 8.dp)
                )
                Text(
                    text = model?.country?: stringResource(id = R.string.stations_country),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = Typography.labelSmall,
                    modifier = Modifier
                        .placeholder(
                            visible = placeholderState,
                            color = Color.LightGray,
                            shape = RoundedCornerShape(20.dp),
                            highlight = highlight
                        )
                        .padding(end = 8.dp)
                )
                Text(
                    text = model?.codec?: stringResource(id = R.string.stations_codec),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = Typography.labelSmall,
                    modifier = Modifier
                        .placeholder(
                            visible = placeholderState,
                            color = Color.LightGray,
                            shape = RoundedCornerShape(20.dp),
                            highlight = highlight
                        )
                        .padding(end = 8.dp)
                )
            }
        }

    }
}
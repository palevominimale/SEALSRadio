package app.seals.radio.ui.bars

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.seals.radio.R
import app.seals.radio.entities.responses.StationModel
import app.seals.radio.ui.theme.Typography
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import coil.request.ImageRequest
import com.google.accompanist.placeholder.PlaceholderDefaults
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.placeholder
import com.google.accompanist.placeholder.shimmer

@Composable
@Preview
fun PlayerBar(station: StationModel = StationModel(), modifier: Modifier = Modifier) {

    val highlight = PlaceholderHighlight.shimmer(
        highlightColor = Color.White,
        animationSpec = PlaceholderDefaults.shimmerAnimationSpec
    )

    Surface(
        color = Color.White,
        shadowElevation = 5.dp,
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .fillMaxWidth()
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.Center
                ) {
                    var placeholderState by remember { mutableStateOf(true) }

                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(station.favicon)
                            .error(R.drawable.ic_radio)
                            .crossfade(true)
                            .build(),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        alignment = Alignment.Center,
                        modifier = Modifier
                            .padding(8.dp)
                            .size(60.dp)
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
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .height(IntrinsicSize.Min)
                ) {
                    Text(
                        text = station.name.toString(),
                        style = Typography.labelMedium,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 2
                    )
                    Text(
                        text = station.country.toString(),
                        style = Typography.labelSmall,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1
                    )
                    Text(
                        text = station.tags.toString(),
                        style = Typography.labelSmall,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 2
                    )
                }
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.padding(end = 8.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_skip_previous),
                    contentDescription = null,
                    modifier = Modifier
                        .size(24.dp)
                )
                Icon(
                    painter = painterResource(R.drawable.ic_skip_next),
                    contentDescription = null,
                    modifier = Modifier
                        .size(24.dp)
                )
                Icon(
                    painter = painterResource(R.drawable.ic_play),
                    contentDescription = null,
                    modifier = Modifier
                        .size(40.dp)
                )
            }
        }
    }
}
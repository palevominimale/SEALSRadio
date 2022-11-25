package app.seals.radio.ui.screens.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.TabRow
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
import app.seals.radio.domain.models.FilterOptions
import app.seals.radio.main.MainActivityViewModel
import coil.compose.AsyncImagePainter
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager

@OptIn(ExperimentalPagerApi::class, ExperimentalFoundationApi::class)
@Composable
@Preview
fun MainScreen(
    list: List<StationModel?> =
        mutableListOf<StationModel?>().apply { repeat(10) { this.add(null) } },
    placeholders: Boolean = true,
    vm: MainActivityViewModel? = null,
    modifier: Modifier = Modifier
) {
    val favoriteList : List<String> = vm?.getFavoritesUuids() ?: emptyList()
    val filterIsShown = vm?.filterState?.collectAsState()

    HorizontalPager(count = 2) { page ->
        when(page) {
            0 -> {
                LazyColumn(
                    modifier = modifier
                        .fillMaxSize()
                ) {
                    list.forEachIndexed { _, model ->
                        item {
                            StationItem(
                                model = model,
                                onClick = { vm!!.selectStation(it) },
                                isFavorite = favoriteList.contains(model?.stationuuid ?: false),
                                addFavorite = { vm?.addFavorite(it) },
                                delFavorite = { vm?.delFavorite(it) }
                            )
                        }
                    }
                }
            }
            1 -> {
                LazyColumn(
                    modifier = modifier
                        .fillMaxSize()
                ) {
                    vm!!.getFavorites().forEachIndexed { _, model ->
                        item {
                            StationItem(
                                model = model,
                                onClick = { vm.selectStation(it) },
                                isFavorite = favoriteList.contains(model.stationuuid ?: false),
                                addFavorite = { vm.addFavorite(it) },
                                delFavorite = { vm.delFavorite(it) }
                            )
                        }
                    }
                }
            }
        }
    }

    Box(
        contentAlignment = Alignment.BottomCenter,
        modifier = modifier
            .fillMaxSize()
    ) {
        AnimatedVisibility(
            visible = filterIsShown?.value ?: false,
            enter = slideInVertically(initialOffsetY = {2*it}),
            exit = slideOutVertically(targetOffsetY = {it}),
        ) {
            FilterPad(
                hideFilter = { vm?.hideFilter() },
                setFilter = { vm?.setFilter(it) },
                filterOptions = vm?.getFilter() ?: FilterOptions()
            )
        }
    }
}

@Composable
fun StationItem(
    model : StationModel? = null,
    onClick: (item: StationModel) -> Unit,
    isFavorite: Boolean = false,
    addFavorite: (StationModel) -> Unit = {},
    delFavorite: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {

    val placeholderState = model == null
    val fav = mutableStateOf(isFavorite)

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
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column {
                    var placeholderState by remember { mutableStateOf(true) }
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .memoryCachePolicy(CachePolicy.ENABLED)
                            .diskCachePolicy(CachePolicy.ENABLED)
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
                    horizontalAlignment = Alignment.Start,
                    modifier = Modifier
                        .widthIn(min = 100.dp, max = 220.dp)
                ) {
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
            Column(
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(8.dp)
            ) {
                Icon(
                    imageVector = if(fav.value) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = null,
                    tint = if(fav.value) Color.Red else Color.LightGray,
                    modifier = Modifier
                        .padding(bottom = 8.dp)
                        .size(24.dp)
                        .clip(CircleShape)
                        .clickable {
                            if (isFavorite && model?.stationuuid != null) delFavorite(model.stationuuid!!)
                            else if (model?.stationuuid != null) addFavorite(model)
                            fav.value = !fav.value
                        }
                        .placeholder(
                            visible = placeholderState,
                            color = Color.LightGray,
                            shape = RoundedCornerShape(20.dp),
                            highlight = highlight
                        )
                )
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = null,
                    tint = Color.LightGray,
                    modifier = Modifier
                        .size(30.dp)
                        .clip(CircleShape)
                        .placeholder(
                            visible = placeholderState,
                            color = Color.LightGray,
                            shape = RoundedCornerShape(20.dp),
                            highlight = highlight
                        )
                )
            }

        }
    }
}
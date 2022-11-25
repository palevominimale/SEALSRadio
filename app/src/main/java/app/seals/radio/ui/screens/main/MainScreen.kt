package app.seals.radio.ui.screens.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.Interaction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
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
import app.seals.radio.intents.MainIntent
import app.seals.radio.states.UiState
import coil.compose.AsyncImagePainter
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class)
@Composable
@Preview
fun MainScreen(
    state: UiState.Ready = UiState.Ready.Empty,
    modifier: Modifier = Modifier,
    intent: (MainIntent) -> Unit = {}
) {

    val pagerState = rememberPagerState(0)
    val tabs = listOf(
        "Search" to Icons.Default.Search,
        "Favorite" to Icons.Default.FavoriteBorder
    )
    val scope = rememberCoroutineScope()
    val list = mutableListOf<StationModel>()
    val favs = mutableListOf<StationModel>()
    var filterIsShown = false

    when(state) {
        is UiState.Ready.Empty -> list.addAll(emptyList())
        is UiState.Ready.Main -> {
            list.addAll(state.list)
            favs.addAll(state.favs)
            filterIsShown = state.filterIsShown
        }
    }
    Column(
    modifier = modifier,
    ) {
        TabRow(
            selectedTabIndex = pagerState.currentPage,
            contentColor = Color.LightGray,
            containerColor = Color.White,
            indicator = { tabPositions ->
                Box(
                    modifier = Modifier
                        .tabIndicatorOffset(tabPositions[pagerState.currentPage])
                        .height(4.dp)
                        .padding(horizontal = 16.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(color = Color.LightGray)
                )
            }
        ) {
            tabs.forEachIndexed { index, item ->
                Tab(
                    selected = pagerState.currentPage == index ,
                    onClick = { scope.launch { pagerState.animateScrollToPage(index) }},
                    modifier = Modifier.height(40.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = item.second,
                            contentDescription = null,
                            modifier = Modifier.padding(horizontal = 8.dp)
                        )
                        Text(text = item.first)
                    }
                }
            }
        }

        HorizontalPager(
            count = 2,
            userScrollEnabled = true,
            state = pagerState
        ) { page ->
            when(page) {
                0 -> {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        list.forEachIndexed { _, model ->
                            item {
                                StationItem(
                                    model = model,
                                    onClick = { intent(MainIntent.Select(it)) },
                                    isFavorite = favs.contains(model),
                                    addFavorite = { intent(MainIntent.AddFavorite(it)) },
                                    delFavorite = { intent(MainIntent.DelFavorite(it)) }
                                )
                            }
                        }
                    }
                }
                1 -> {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        favs.forEachIndexed { _, model ->
                            item {
                                StationItem(
                                    model = model,
                                    onClick = { intent(MainIntent.Select(it)) },
                                    isFavorite = true,
                                    addFavorite = { intent(MainIntent.AddFavorite(it)) },
                                    delFavorite = { intent(MainIntent.DelFavorite(it)) }
                                )
                            }
                        }
                    }
                }
            }
        }

    }
    if(state is UiState.Ready.Main) {
        Box(
            contentAlignment = Alignment.BottomCenter,
            modifier = modifier
                .fillMaxSize()
        ) {
            AnimatedVisibility(
                visible = filterIsShown,
                enter = slideInVertically(initialOffsetY = {2*it}),
                exit = slideOutVertically(targetOffsetY = {it}),
            ) {
                FilterPad(
                    hideFilter = { intent(MainIntent.HideFilter) },
                    setFilter = { intent(MainIntent.SetFilter(it)) },
                    filterOptions = state.filterOptions ?: FilterOptions()
                )
            }
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
                    var phState by remember { mutableStateOf(true) }
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
                                visible = phState,
                                color = Color.LightGray,
                                shape = RoundedCornerShape(16.dp),
                                highlight = highlight
                            ),
                        onState = {
                            if(it is AsyncImagePainter.State.Success) phState = false
                            if(it is AsyncImagePainter.State.Error) {
                                phState = false
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
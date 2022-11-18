package app.seals.radio.ui.screens.main

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.seals.redio.entities.StationModel
import com.google.accompanist.placeholder.PlaceholderDefaults
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.placeholder
import com.google.accompanist.placeholder.shimmer

@Composable
@Preview
fun MainScreen(
    list: List<StationModel?> = mutableListOf<StationModel?>().apply { repeat(10) {this.add(null)} },
    placeholders: Boolean = true,
    modifier: Modifier = Modifier
) {

    LazyColumn(
        modifier = modifier.fillMaxSize()
    ) {
        list.forEachIndexed { index, model ->
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
            .height(120.dp)
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

    }
}
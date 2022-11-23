package app.seals.radio.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.seals.radio.R
import app.seals.radio.ui.theme.Typography

@Composable
@Preview
fun ExceptionScreen(
    t: Throwable? = null,
    code: Int? = 0,
    message: String? = "empty message"
) {
    Surface(
    color = Color.White,
    modifier = Modifier
        .fillMaxSize()
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Surface(
                shape = CircleShape,
                shadowElevation = 5.dp,
                color = Color.White,
                modifier = Modifier
                    .padding(bottom = 32.dp)
                    .size(100.dp)
            ) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_radio),
                        contentDescription = null,
                        tint = Color.LightGray,
                        modifier = Modifier
                            .size(70.dp)
                    )
                }
            }
            Surface(
                shape = RoundedCornerShape(40.dp),
                shadowElevation = 15.dp,
                color = Color.White,
            ) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.Start,
                    modifier = Modifier.padding(24.dp)
                ) {
                    Text(
                        text = stringResource(R.string.exception_title),
                        style = Typography.bodyLarge,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    if (t != null) {
                        Text(
                            text = t.message ?: "",
                            style = Typography.labelMedium
                        )
                    } else {
                        Text(
                            text = "Error code: ${code.toString()}",
                            style = Typography.labelMedium
                        )

                        Text(
                            text = message ?: "",
                            style = Typography.labelMedium
                        )
                    }
                }
            }
        }
    }
}
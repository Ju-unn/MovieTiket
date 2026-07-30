package com.example.movietiket.view.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.movietiket.R
import com.example.movietiket.ui.theme.MovieTiketTheme

private val COUNTER_BUTTON_WIDTH = 65.dp
private val COUNTER_BUTTON_HEIGHT = 45.dp
private val COUNTER_BUTTON_CORNER = 6.dp

/**
 * 예매 인원 선택 컴포넌트 (- / 인원 수 / +)
 */
@Composable
fun HeadCountSelector(
    headCount: String,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        CounterButton(
            text = stringResource(R.string.head_count_decrease),
            onClick = onDecrease,
        )
        Text(
            text = headCount,
            fontSize = 20.sp,
            fontWeight = FontWeight.Medium,
        )
        CounterButton(
            text = stringResource(R.string.head_count_increase),
            onClick = onIncrease,
        )
    }
}

@Composable
private fun CounterButton(text: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .width(COUNTER_BUTTON_WIDTH)
            .height(COUNTER_BUTTON_HEIGHT),
        shape = RoundedCornerShape(COUNTER_BUTTON_CORNER),
        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
        contentPadding = PaddingValues(0.dp),
    ) {
        Text(text = text, fontSize = 25.sp, fontWeight = FontWeight.Bold)
    }
}

@Preview(showBackground = true)
@Composable
private fun HeadCountSelectorPreview() {
    MovieTiketTheme {
        HeadCountSelector(
            headCount = "1",
            onIncrease = {},
            onDecrease = {},
        )
    }
}

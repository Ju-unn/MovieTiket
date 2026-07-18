package com.example.movietiket.view.reservation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.movietiket.R
import com.example.movietiket.ui.theme.MovieTiketTheme

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
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        CounterButton(
            text = stringResource(R.string.head_count_decrease),
            onClick = onDecrease,
        )
        Text(
            text = headCount,
            fontSize = 22.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.width(40.dp),
        )
        CounterButton(
            text = stringResource(R.string.head_count_increase),
            onClick = onIncrease,
        )
    }
}

@Composable
private fun CounterButton(text: String, onClick: () -> Unit) {
    OutlinedButton(
        onClick = onClick,
        modifier = Modifier.size(48.dp),
        contentPadding = PaddingValues(0.dp),
    ) {
        Text(text = text, fontSize = 20.sp)
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

package com.example.movietiket.history.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.movietiket.common.model.reservation.Reservation
import com.example.movietiket.common.model.reservation.ReservationHistory
import com.example.movietiket.common.repository.MovieRepository
import com.example.movietiket.common.repository.TheaterRepository
import com.example.movietiket.ui.theme.MovieTiketTheme
import com.example.movietiket.ui.theme.NavigationDivider
import com.example.movietiket.ui.theme.PressedBackground
import com.example.movietiket.ui.theme.ReservationMetaSeparator
import com.example.movietiket.ui.theme.ReservationMetaText

// 디자인 "Booking Row"(padding 12/10, gap 5, meta gap 3) 기준 수치
private val ITEM_VERTICAL_PADDING = 12.dp
private val ITEM_HORIZONTAL_PADDING = 10.dp
private val META_TO_TITLE_GAP = 5.dp
private val META_GAP = 3.dp
private val DIVIDER_THICKNESS = 1.dp

private const val META_SEPARATOR = "|"

/**
 * 예매 내역 목록의 한 줄 (날짜 | 시간 | 극장 / 영화 제목)
 */
@Composable
fun ReservationHistoryItem(
    history: ReservationHistory,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()

    Column(modifier = modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(if (pressed) PressedBackground else Color.White)
                .clickable(interactionSource = interactionSource, indication = null, onClick = onClick)
                .padding(
                    horizontal = ITEM_HORIZONTAL_PADDING,
                    vertical = ITEM_VERTICAL_PADDING,
                ),
            verticalArrangement = Arrangement.spacedBy(META_TO_TITLE_GAP),
        ) {
            ReservationMeta(history = history)
            Text(
                text = history.displayMovieTitle(),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
            )
        }
        HorizontalDivider(thickness = DIVIDER_THICKNESS, color = NavigationDivider)
    }
}

@Composable
private fun ReservationMeta(history: ReservationHistory) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(META_GAP),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        MetaValue(history.displayScreeningDate())
        MetaSeparator()
        MetaValue(history.displayScreeningTime())
        MetaSeparator()
        MetaValue(history.displayTheaterName())
    }
}

@Composable
private fun MetaValue(text: String) {
    Text(
        text = text,
        color = ReservationMetaText,
        fontSize = 11.sp,
        fontWeight = FontWeight.Medium,
    )
}

@Composable
private fun MetaSeparator() {
    Text(
        text = META_SEPARATOR,
        color = ReservationMetaSeparator,
        fontSize = 11.sp,
    )
}

@Preview(showBackground = true)
@Composable
private fun ReservationHistoryItemPreview() {
    val reservation = Reservation.of(
        MovieRepository.findAll().toList().first(),
        TheaterRepository.findAll().toList().first(),
    )
    MovieTiketTheme {
        ReservationHistoryItem(
            history = ReservationHistory(id = 1L, reservation = reservation),
            onClick = {},
        )
    }
}

package com.example.movietiket.history.view

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.movietiket.common.model.reservation.ReservationHistory

/**
 * 예매 내역 탭 (내가 예매한 내역을 최근 순으로 보여준다)
 */
@Composable
fun ReservationHistoryScreen(
    histories: List<ReservationHistory>,
    onHistoryClick: (ReservationHistory) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(modifier = modifier.fillMaxSize()) {
        items(histories, key = { it.id() }) { history ->
            ReservationHistoryItem(
                history = history,
                onClick = { onHistoryClick(history) },
            )
        }
    }
}

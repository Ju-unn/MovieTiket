package com.example.movietiket.seat.view

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.movietiket.R
import com.example.movietiket.common.repository.MovieRepository
import com.example.movietiket.common.repository.TheaterRepository
import com.example.movietiket.common.model.reservation.Reservation
import com.example.movietiket.ui.theme.MovieTiketTheme
import com.example.movietiket.ui.theme.SeatDisabledButton
import com.example.movietiket.ui.theme.SeatGradeFirst
import com.example.movietiket.ui.theme.SeatGradeSecond
import com.example.movietiket.ui.theme.SeatGradeThird
import com.example.movietiket.ui.theme.SeatMapBackground
import com.example.movietiket.ui.theme.SeatPriceText
import com.example.movietiket.ui.theme.SeatScreenBanner
import com.example.movietiket.ui.theme.SeatSelected
import com.example.movietiket.common.view.BackNavigationTopBar

private val SCREEN_BANNER_WIDTH = 292.dp
private val SCREEN_BANNER_HEIGHT = 34.dp
private val SEAT_GRID_TOP_SPACING = 56.dp
private val SEAT_CELL_WIDTH = 73.dp
private val SEAT_CELL_HEIGHT = 80.dp
private val BOTTOM_BAR_HEIGHT = 78.dp
private val CONFIRM_BUTTON_WIDTH = 88.dp
private val CONTENT_HORIZONTAL_PADDING = 20.dp

private val DIALOG_WIDTH = 311.dp
private val DIALOG_CORNER = 2.dp
private val DIALOG_PADDING = 24.dp

private val SEAT_ROWS = listOf('A', 'B', 'C', 'D', 'E')
private const val SEATS_PER_ROW = 4

private fun seatGradeColor(row: Char): Color = when (row) {
    'A', 'B' -> SeatGradeFirst
    'C', 'D' -> SeatGradeSecond
    else -> SeatGradeThird
}

/**
 * 좌석 선택 화면 (인원수만큼 선택해야 확인 버튼이 활성화된다)
 */
@Composable
fun SeatSelectionScreen(
    reservation: Reservation,
    seatLimitExceededEvent: Int,
    onSelectSeat: (String) -> Unit,
    onDeselectSeat: (String) -> Unit,
    onConfirmClick: () -> Unit,
    onBackClick: () -> Unit,
) {
    var showConfirmDialog by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    val seatLimitExceededMessage = stringResource(R.string.seat_limit_exceeded_message)

    LaunchedEffect(seatLimitExceededEvent) {
        if (seatLimitExceededEvent > 0) {
            snackbarHostState.showSnackbar(seatLimitExceededMessage)
        }
    }

    if (showConfirmDialog) {
        ReservationConfirmDialog(
            onCancel = { showConfirmDialog = false },
            onConfirm = {
                showConfirmDialog = false
                onConfirmClick()
            },
        )
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { BackNavigationTopBar(onBackClick = onBackClick) },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        bottomBar = {
            SeatSelectionBottomBar(
                movieTitle = reservation.displayMovieTitle(),
                totalPrice = reservation.selectedSeatsAmountWon(),
                enabled = reservation.isSeatSelectionComplete(),
                onConfirmClick = { showConfirmDialog = true },
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(SeatMapBackground)
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            ScreenBanner()
            Spacer(modifier = Modifier.height(SEAT_GRID_TOP_SPACING))
            SeatGrid(
                isSeatSelected = reservation::isSeatSelected,
                onSeatClick = { seat ->
                    if (reservation.isSeatSelected(seat)) onDeselectSeat(seat) else onSelectSeat(seat)
                },
            )
        }
    }
}

@Composable
private fun ScreenBanner() {
    Box(
        modifier = Modifier
            .width(SCREEN_BANNER_WIDTH)
            .height(SCREEN_BANNER_HEIGHT)
            .background(SeatScreenBanner),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = stringResource(R.string.screen_banner),
            color = Color.White,
            fontSize = 22.sp,
            fontWeight = FontWeight.Medium,
        )
    }
}

@Composable
private fun SeatGrid(isSeatSelected: (String) -> Boolean, onSeatClick: (String) -> Unit) {
    Column {
        SEAT_ROWS.forEach { row ->
            Row {
                for (column in 1..SEATS_PER_ROW) {
                    val seat = "$row$column"
                    SeatCell(
                        seat = seat,
                        gradeColor = seatGradeColor(row),
                        selected = isSeatSelected(seat),
                        onClick = { onSeatClick(seat) },
                    )
                }
            }
        }
    }
}

@Composable
private fun SeatCell(seat: String, gradeColor: Color, selected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .width(SEAT_CELL_WIDTH)
            .height(SEAT_CELL_HEIGHT)
            .border(width = 1.dp, color = SeatMapBackground)
            .background(if (selected) SeatSelected else Color.White)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Text(text = seat, color = gradeColor, fontSize = 22.sp, fontWeight = FontWeight.Medium)
    }
}

@Composable
private fun SeatSelectionBottomBar(
    movieTitle: String,
    totalPrice: Int,
    enabled: Boolean,
    onConfirmClick: () -> Unit,
) {
    Row(modifier = Modifier.fillMaxWidth().height(BOTTOM_BAR_HEIGHT)) {
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .background(Color.White)
                .padding(horizontal = CONTENT_HORIZONTAL_PADDING),
            verticalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterVertically),
        ) {
            Text(text = movieTitle, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Text(
                text = stringResource(R.string.seat_price_format, totalPrice),
                fontSize = 18.sp,
                color = SeatPriceText,
            )
        }
        Box(
            modifier = Modifier
                .width(CONFIRM_BUTTON_WIDTH)
                .fillMaxHeight()
                .background(if (enabled) MaterialTheme.colorScheme.primary else SeatDisabledButton)
                .clickable(enabled = enabled, onClick = onConfirmClick),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = stringResource(R.string.confirm),
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}

@Composable
private fun ReservationConfirmDialog(onCancel: () -> Unit, onConfirm: () -> Unit) {
    // 배경 터치로는 다이얼로그가 닫히지 않아야 하므로 dismissOnClickOutside를 끈다
    Dialog(
        onDismissRequest = onCancel,
        properties = DialogProperties(dismissOnClickOutside = false),
    ) {
        Surface(
            modifier = Modifier.width(DIALOG_WIDTH),
            shape = RoundedCornerShape(DIALOG_CORNER),
            color = Color.White,
        ) {
            Column(modifier = Modifier.padding(DIALOG_PADDING)) {
                Text(
                    text = stringResource(R.string.reservation_confirm_dialog_title),
                    fontSize = 18.sp,
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = stringResource(R.string.reservation_confirm_dialog_message),
                    fontSize = 14.sp,
                )
                Spacer(modifier = Modifier.height(20.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(25.dp, Alignment.End),
                ) {
                    Text(
                        text = stringResource(R.string.cancel),
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 14.sp,
                        modifier = Modifier.clickable(onClick = onCancel),
                    )
                    Text(
                        text = stringResource(R.string.reservation_confirm),
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 14.sp,
                        modifier = Modifier.clickable(onClick = onConfirm),
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, name = "예매 확정 안내")
@Composable
private fun ReservationConfirmDialogPreview() {
    MovieTiketTheme {
        ReservationConfirmDialog(onCancel = {}, onConfirm = {})
    }
}

@Preview(showBackground = true, name = "비활성화 (좌석 미선택)")
@Composable
private fun SeatSelectionScreenInactivePreview() {
    val reservation = Reservation.of(MovieRepository.findAll().toList().first(), TheaterRepository.findAll().toList().first())
    MovieTiketTheme {
        SeatSelectionScreen(
            reservation = reservation,
            seatLimitExceededEvent = 0,
            onSelectSeat = {},
            onDeselectSeat = {},
            onConfirmClick = {},
            onBackClick = {},
        )
    }
}

@Preview(showBackground = true, name = "활성화 (좌석 선택 완료)")
@Composable
private fun SeatSelectionScreenActivePreview() {
    val reservation = Reservation.of(MovieRepository.findAll().toList().first(), TheaterRepository.findAll().toList().first())
        .increaseHeadCount()
        .selectSeat("A1")
        .selectSeat("A2")
    MovieTiketTheme {
        SeatSelectionScreen(
            reservation = reservation,
            seatLimitExceededEvent = 0,
            onSelectSeat = {},
            onDeselectSeat = {},
            onConfirmClick = {},
            onBackClick = {},
        )
    }
}

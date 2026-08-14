package com.example.movietiket.movielist.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.movietiket.common.model.theater.Theater
import com.example.movietiket.ui.theme.TheaterItemBackground

// Figma "극장 선택 다이얼로그"(360x207) 기준 수치
private val SHEET_HORIZONTAL_PADDING = 8.dp
private val SHEET_VERTICAL_PADDING = 16.dp
private val THEATER_ITEM_HEIGHT = 53.dp
private val THEATER_ITEM_SPACING = 8.dp
private val THEATER_ITEM_CORNER = 6.dp
private val THEATER_ITEM_TEXT_PADDING = 16.dp

/**
 * "지금 예매"를 눌렀을 때 극장을 고르는 바텀시트
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TheaterSelectionBottomSheet(
    theaters: List<Theater>,
    onTheaterClick: (Theater) -> Unit,
    onDismissRequest: () -> Unit,
) {
    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = rememberModalBottomSheetState(),
        containerColor = Color.White,
        dragHandle = null,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = SHEET_HORIZONTAL_PADDING,
                    vertical = SHEET_VERTICAL_PADDING,
                ),
            verticalArrangement = Arrangement.spacedBy(THEATER_ITEM_SPACING),
        ) {
            theaters.forEach { theater ->
                TheaterItem(theater = theater, onClick = { onTheaterClick(theater) })
            }
        }
    }
}

@Composable
private fun TheaterItem(theater: Theater, onClick: () -> Unit) {
    Surface(
        shape = RoundedCornerShape(THEATER_ITEM_CORNER),
        color = TheaterItemBackground,
        modifier = Modifier
            .fillMaxWidth()
            .height(THEATER_ITEM_HEIGHT)
            .clickable(onClick = onClick),
    ) {
        Box(
            contentAlignment = Alignment.CenterStart,
            modifier = Modifier.padding(horizontal = THEATER_ITEM_TEXT_PADDING),
        ) {
            Text(
                text = theater.displayName(),
                fontSize = 16.sp,
                // 화면 폭을 넘기는 긴 극장 이름은 뒤를 "..."으로 줄인다
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

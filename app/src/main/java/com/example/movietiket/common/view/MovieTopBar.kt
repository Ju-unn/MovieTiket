package com.example.movietiket.common.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.movietiket.R
import com.example.movietiket.ui.theme.MoviePrimary

// 디자인 "App Bar"(360x54, padding 0/24) 기준 수치
private val TOP_BAR_HEIGHT = 54.dp
private val TOP_BAR_HORIZONTAL_PADDING = 24.dp

/**
 * 탭 화면 공통 상단바 (타이틀은 화면과 무관하게 "Movie" 고정)
 */
@Composable
fun MovieTopBar(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(TOP_BAR_HEIGHT)
            .background(MoviePrimary)
            .padding(horizontal = TOP_BAR_HORIZONTAL_PADDING),
        contentAlignment = Alignment.CenterStart,
    ) {
        Text(
            text = stringResource(R.string.app_top_bar_title),
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
        )
    }
}

package com.example.movietiket.movielist.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.movietiket.R

// 디자인 "Ad Banner"(전체 106, 이미지 98) 기준 수치
private val BANNER_HEIGHT = 106.dp
private val BANNER_IMAGE_HEIGHT = 98.dp

/**
 * 영화 목록 사이에 끼어드는 광고 배너
 */
@Composable
fun AdBanner(modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxWidth().height(BANNER_HEIGHT)) {
        Image(
            painter = painterResource(R.drawable.ad_banner),
            contentDescription = stringResource(R.string.ad_banner_description),
            modifier = Modifier.fillMaxWidth().height(BANNER_IMAGE_HEIGHT),
            contentScale = ContentScale.Crop,
        )
    }
}

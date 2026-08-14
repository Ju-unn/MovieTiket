package com.example.movietiket.common.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.List
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.movietiket.R
import com.example.movietiket.navigation.Screen
import com.example.movietiket.ui.theme.MoviePrimary
import com.example.movietiket.ui.theme.NavigationDivider
import com.example.movietiket.ui.theme.NavigationInactive

// 디자인 "Bottom Nav"(360x59, padding 8/0/9/0, 아이콘 24, 라벨 11sp, gap 2) 기준 수치
private val NAVIGATION_HEIGHT = 59.dp
private val NAVIGATION_TOP_PADDING = 8.dp
private val NAVIGATION_BOTTOM_PADDING = 9.dp
private val ICON_SIZE = 24.dp
private val ICON_TO_LABEL_GAP = 2.dp
private val DIVIDER_THICKNESS = 1.dp

/**
 * 예매 내역 / 홈 / 설정 하단 네비게이션
 */
@Composable
fun MovieBottomNavigation(
    currentTab: Screen.Tab,
    onTabClick: (Screen.Tab) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth().background(Color.White)) {
        HorizontalDivider(thickness = DIVIDER_THICKNESS, color = NavigationDivider)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(NAVIGATION_HEIGHT - DIVIDER_THICKNESS)
                .padding(top = NAVIGATION_TOP_PADDING, bottom = NAVIGATION_BOTTOM_PADDING),
        ) {
            NavigationTabs.forEach { tab ->
                NavigationTab(
                    tab = tab,
                    selected = tab.screen == currentTab,
                    onClick = { onTabClick(tab.screen) },
                    modifier = Modifier.weight(1f),
                )
            }
        }
    }
}

// 개별 하단 네비게이션 탭 한 칸을 그린다
@Composable
private fun NavigationTab(
    tab: NavigationTabItem,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val contentColor = if (selected) MoviePrimary else NavigationInactive
    // 탭 전체가 눌리도록 하되 디자인에 리플이 없어 표시 효과는 끈다
    val interactionSource = remember { MutableInteractionSource() }

    Column(
        modifier = modifier.clickable(
            interactionSource = interactionSource,
            indication = null,
            onClick = onClick,
        ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(ICON_TO_LABEL_GAP, Alignment.CenterVertically),
    ) {
        Icon(
            imageVector = tab.icon,
            contentDescription = null,
            tint = contentColor,
            modifier = Modifier.size(ICON_SIZE),
        )
        Text(
            text = stringResource(tab.labelResId),
            color = contentColor,
            fontSize = 11.sp,
        )
    }
}

/**
 * 하단 네비게이션 탭 하나(화면, 아이콘, 라벨)를 담는 값 객체
 */
private class NavigationTabItem(
    val screen: Screen.Tab,
    val icon: ImageVector,
    val labelResId: Int,
)

private val NavigationTabs = listOf(
    NavigationTabItem(Screen.Tab.History, Icons.AutoMirrored.Outlined.List, R.string.tab_history),
    NavigationTabItem(Screen.Tab.Home, Icons.Outlined.Home, R.string.tab_home),
    NavigationTabItem(Screen.Tab.Settings, Icons.Outlined.Settings, R.string.tab_settings),
)

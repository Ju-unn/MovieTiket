package com.example.movietiket.settings.view

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.movietiket.R
import com.example.movietiket.ui.theme.MoviePrimary
import com.example.movietiket.ui.theme.MovieTiketTheme
import com.example.movietiket.ui.theme.NavigationDivider
import com.example.movietiket.ui.theme.SettingDescription

// 디자인 "Push Setting Row"(padding 12/15/12/14, gap 12, 라벨 gap 4) 기준 수치
private val ROW_PADDING_TOP = 12.dp
private val ROW_PADDING_END = 15.dp
private val ROW_PADDING_BOTTOM = 12.dp
private val ROW_PADDING_START = 14.dp
private val LABEL_TO_SWITCH_GAP = 12.dp
private val LABEL_GAP = 4.dp
private val SWITCH_TOP_PADDING = 3.dp

// 디자인 "Switch"(36x20, 노브 16, 좌우 여백 2)
private val SWITCH_WIDTH = 36.dp
private val SWITCH_HEIGHT = 20.dp
private val SWITCH_KNOB_SIZE = 16.dp
private val SWITCH_PADDING = 2.dp

/**
 * 설정 탭 (푸시 알림 수신 토글)
 */
@Composable
fun SettingsScreen(
    pushNotificationEnabled: Boolean,
    onPushNotificationToggle: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxSize()) {
        PushSettingRow(
            enabled = pushNotificationEnabled,
            onToggle = onPushNotificationToggle,
        )
    }
}

@Composable
private fun PushSettingRow(enabled: Boolean, onToggle: (Boolean) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                role = Role.Switch,
                onClick = { onToggle(!enabled) },
            )
            .padding(
                start = ROW_PADDING_START,
                top = ROW_PADDING_TOP,
                end = ROW_PADDING_END,
                bottom = ROW_PADDING_BOTTOM,
            ),
        horizontalArrangement = Arrangement.spacedBy(LABEL_TO_SWITCH_GAP),
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(LABEL_GAP),
        ) {
            Text(
                text = stringResource(R.string.setting_push_notification_title),
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = stringResource(R.string.setting_push_notification_description),
                color = SettingDescription,
                fontSize = 11.sp,
            )
        }
        PushSwitch(
            enabled = enabled,
            modifier = Modifier.padding(top = SWITCH_TOP_PADDING),
        )
    }
}

/**
 * Material3 기본 Switch는 52x32로 디자인(36x20)보다 커서 직접 그린다
 * 행 전체가 클릭을 받으므로 스위치 자체는 상태만 표시한다
 */
@Composable
private fun PushSwitch(enabled: Boolean, modifier: Modifier = Modifier) {
    val knobOffset by animateDpAsState(
        targetValue = if (enabled) SWITCH_WIDTH - SWITCH_KNOB_SIZE - SWITCH_PADDING * 2 else 0.dp,
        label = "knobOffset",
    )

    Box(
        modifier = modifier
            .size(width = SWITCH_WIDTH, height = SWITCH_HEIGHT)
            .background(
                color = if (enabled) MoviePrimary else NavigationDivider,
                shape = RoundedCornerShape(percent = 50),
            )
            .padding(horizontal = SWITCH_PADDING),
        contentAlignment = Alignment.CenterStart,
    ) {
        Box(
            modifier = Modifier
                .offset(x = knobOffset)
                .size(SWITCH_KNOB_SIZE)
                .background(color = Color.White, shape = CircleShape),
        )
    }
}

@Preview(showBackground = true, widthDp = 360)
@Composable
private fun SettingsScreenPreview() {
    MovieTiketTheme {
        SettingsScreen(pushNotificationEnabled = true, onPushNotificationToggle = {})
    }
}

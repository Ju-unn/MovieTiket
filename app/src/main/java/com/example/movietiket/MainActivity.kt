package com.example.movietiket

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.movietiket.ui.theme.MovieTiketTheme
import com.example.movietiket.MovieTicketApp

/**
 * 앱 진입점 — 루트 Composable(MovieTicketApp)만 띄운다
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MovieTiketTheme {
                MovieTicketApp()
            }
        }
    }
}
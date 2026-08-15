package com.example.movietiket.movielist.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.movietiket.common.model.theater.Theater
import com.example.movietiket.movielist.model.MovieListRow
import com.example.movietiket.movielist.presenter.MovieListContract
import com.example.movietiket.movielist.presenter.MovieListPresenter
import com.example.movietiket.navigation.MovieNavigationController

/** 영화 목록 화면의 상태를 보관하는 View 구현체 */
private class MovieListViewState : MovieListContract.View {
    var rows by mutableStateOf(emptyList<MovieListRow>())
        private set

    // null이면 극장 선택 바텀시트를 띄우지 않는다
    var theaterSelection by mutableStateOf<List<Theater>?>(null)
        private set

    override fun showMovies(rows: List<MovieListRow>) {
        this.rows = rows
    }

    override fun showTheaterSelection(theaters: List<Theater>) {
        theaterSelection = theaters
    }

    override fun hideTheaterSelection() {
        theaterSelection = null
    }
}

// 영화 목록 화면을 보여준다
@Composable
internal fun MovieListRoute(
    navigationController: MovieNavigationController,
    modifier: Modifier = Modifier,
) {
    val view = remember { MovieListViewState() }
    val presenter = remember {
        MovieListPresenter(
            view = view,
            onMovieReserveRequested = navigationController::moveToReservation,
        )
    }

    MovieListScreen(
        modifier = modifier,
        rows = view.rows,
        theaterSelection = view.theaterSelection,
        onReserveClick = presenter::onReserveClick,
        onTheaterClick = presenter::onTheaterSelected,
        onTheaterSelectionDismiss = presenter::onTheaterSelectionDismissed,
    )
}

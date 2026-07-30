package com.example.movietiket.controller

import com.example.movietiket.model.Movie
import com.example.movietiket.repository.MovieRepository
import com.example.movietiket.model.Movies

/**
 * 영화 목록 화면의 흐름을 제어하는 컨트롤러
 */
class MovieListController(
    private val movies: Movies = MovieRepository.findAll(),
) {
    fun movies(): List<Movie> = movies.toList()
}

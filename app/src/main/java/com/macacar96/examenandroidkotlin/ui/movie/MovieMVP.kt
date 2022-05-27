package com.macacar96.examenandroidkotlin.ui.movie

import android.content.Context
import com.macacar96.examenandroidkotlin.retrofit.response.MovieResponse
import com.macacar96.examenandroidkotlin.room.entity.MovieEntity

interface MovieMVP {

    interface View {
        fun showResultView(result: List<MovieEntity?>?, size: Int)
        fun showErrorView(result: String?)
    }

    interface Presenter {
        fun showResultPresenter(result: List<MovieEntity?>?, size: Int)
        fun showErrorPresenter(result: String?)
        fun getDataPeliculasPresenter(context: Context?)
    }

    interface Interactor {
        fun getDataPeliculasInteractor(context: Context?)
    }

}
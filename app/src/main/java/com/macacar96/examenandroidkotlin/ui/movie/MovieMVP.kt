package com.macacar96.examenandroidkotlin.ui.movie

import android.content.Context
import com.macacar96.examenandroidkotlin.retrofit.response.MovieResponse

interface MovieMVP {

    interface View {
        fun showResultView(result: List<MovieResponse?>?, size: Int)
        fun showErrorView(result: String?)
    }

    interface Presenter {
        fun showResultPresenter(result: List<MovieResponse?>?, size: Int)
        fun showErrorPresenter(result: String?)
        fun getDataPeliculasPresenter(context: Context?)
        fun getInitPresenter(context: Context?)
    }

    interface Interactor {
        fun getDataPeliculasInteractor(context: Context?)
        fun getInitInteractor(context: Context?)
    }

}
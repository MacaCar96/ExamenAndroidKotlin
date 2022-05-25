package com.macacar96.examenandroidkotlin.ui.movie

import android.content.Context
import com.macacar96.examenandroidkotlin.retrofit.response.MovieResponse

class MoviePresenter(view: MovieMVP.View): MovieMVP.Presenter {

    private var mView: MovieMVP.View = view
    private var mInteractor: MovieMVP.Interactor = MovieInteractor(this)

    override fun showResultPresenter(result: List<MovieResponse?>?, size: Int) {
        if (mView != null) {
            mView.showResultView(result, size)
        }
    }

    override fun showErrorPresenter(result: String?) {
        if (mView != null) {
            mView.showErrorView(result)
        }
    }

    override fun getDataPeliculasPresenter(context: Context?) {
        if (mView != null) {
            mInteractor.getDataPeliculasInteractor(context)
        }
    }

    override fun getInitPresenter(context: Context?) {
        TODO("Not yet implemented")
    }
}
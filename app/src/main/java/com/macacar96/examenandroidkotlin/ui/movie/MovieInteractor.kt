package com.macacar96.examenandroidkotlin.ui.movie

import android.content.Context
import android.widget.Toast
import com.macacar96.examenandroidkotlin.common.Constans
import com.macacar96.examenandroidkotlin.retrofit.APIService
import com.macacar96.examenandroidkotlin.retrofit.APIServiceAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MovieInteractor(presenter: MovieMVP.Presenter): MovieMVP.Interactor {

    private var mPresenter: MovieMVP.Presenter = presenter
    private lateinit var apiService: APIService

    override fun getDataPeliculasInteractor(context: Context?) {
        CoroutineScope(Dispatchers.IO).launch {
            apiService = APIServiceAdapter.getService().create(APIService::class.java)

            val call = apiService.getPeliculasPopulares("${Constans.API_KEY}", "${Constans.LANGUAGE}", "${Constans.PAGE}")

            val mMovies = call.body()

            if(call.isSuccessful){
                //show Recyclerview
                mMovies?.results?.let { mPresenter.showResultPresenter(mMovies?.results, it.size) }
            }else{
                //show error
                mPresenter.showErrorPresenter("Error al consultar los datos")
            }
        }
    }

    override fun getInitInteractor(context: Context?) {
        TODO("Not yet implemented")
    }
}
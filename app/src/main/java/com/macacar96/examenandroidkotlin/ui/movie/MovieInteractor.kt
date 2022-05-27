package com.macacar96.examenandroidkotlin.ui.movie

import android.content.Context
import com.macacar96.examenandroidkotlin.common.Constans
import com.macacar96.examenandroidkotlin.retrofit.APIService
import com.macacar96.examenandroidkotlin.retrofit.APIServiceAdapter
import com.macacar96.examenandroidkotlin.retrofit.response.MovieResponse
import com.macacar96.examenandroidkotlin.room.TestRoomDatabase
import com.macacar96.examenandroidkotlin.room.dao.MovieDao
import com.macacar96.examenandroidkotlin.room.entity.MovieEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MovieInteractor(presenter: MovieMVP.Presenter): MovieMVP.Interactor {

    private var mPresenter: MovieMVP.Presenter = presenter
    private lateinit var apiService: APIService
    private lateinit var movieDao: MovieDao

    override fun getDataPeliculasInteractor(context: Context?) {
        val db: TestRoomDatabase = TestRoomDatabase.getDatabase(context!!)
        movieDao = db.movieDao()

        CoroutineScope(Dispatchers.IO).launch {

            try {
                apiService = APIServiceAdapter.getService().create(APIService::class.java)

                val call = apiService.getPeliculasPopulares("${Constans.API_KEY}", "${Constans.LANGUAGE}", "${Constans.PAGE}")

                val mMovies = call.body()

                if(call.isSuccessful){

                    // Obtenemos todos las películas de room
                    var movieAll: List<MovieEntity> = movieDao.getAll()

                    // Insertamos los datos en Room
                    val movieList: List<MovieResponse> = mMovies!!.results

                    if (movieAll.isNotEmpty()) {
                        // Actualizamos las películas
                        var count: Int = 0
                        for (i in movieList) {
                            val movie: MovieEntity = MovieEntity(0, i.id, i.title, i.overview, i.releaseDate, i.backdropPath)
                            var statusEliminarPelicula = false

                            for (a in movieAll) {
                                if (movie.idServer != a.idServer) {
                                    statusEliminarPelicula = true
                                }
                            }

                            if (statusEliminarPelicula) {
                                // Eliminamos
                                movieDao.deleteById(movieAll[count].id)
                            }

                            //movieDao.insert(movie)
                            count++
                        }


                        // Actializamos
                        movieAll = movieDao.getAll()
                        count = 0
                        for (i in movieList) {
                            val movie: MovieEntity = MovieEntity(0, i.id, i.title, i.overview, i.releaseDate, i.backdropPath)
                            var statusActualizarPelicula = false

                            for (a in movieAll) {

                                if (movie.idServer == a.idServer) {
                                    statusActualizarPelicula = true
                                }

                            }

                            if (statusActualizarPelicula) {
                                movieDao.update(movie)
                            } else {
                                movieDao.insert(movie)
                            }

                            count++
                        }

                    } else {
                        // Registramos las peliculas por primera vez
                        for (i in movieList) {
                            val movie: MovieEntity = MovieEntity(0, i.id, i.title, i.overview, i.releaseDate, i.backdropPath)
                            movieDao.insert(movie)
                        }
                    }

                    movieAll = movieDao.getAll()
                    mPresenter.showResultPresenter(movieAll, movieAll.size)


                    //mMovies?.results?.let { mPresenter.showResultPresenter(mMovies?.results, it.size) }
                }else{
                    //show error
                    mPresenter.showErrorPresenter("Error al actualizar las nuevas películas del servidor.")
                    // Obtenemos todos las películas de room
                    val movieAll: List<MovieEntity> = movieDao.getAll()
                    mPresenter.showResultPresenter(movieAll, movieAll.size)
                }
            } catch (e: Exception) {
                //show error
                mPresenter.showErrorPresenter("Sin conexión a internet.")
                // Obtenemos todos las películas de room
                val movieAll: List<MovieEntity> = movieDao.getAll()
                mPresenter.showResultPresenter(movieAll, movieAll.size)
            }

        }
    }

}
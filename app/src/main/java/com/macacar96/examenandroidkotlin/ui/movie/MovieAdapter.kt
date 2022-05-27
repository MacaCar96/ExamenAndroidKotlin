package com.macacar96.examenandroidkotlin.ui.movie

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.macacar96.examenandroidkotlin.R
import com.macacar96.examenandroidkotlin.databinding.ItemMovieBinding
import com.macacar96.examenandroidkotlin.retrofit.response.MovieResponse
import com.macacar96.examenandroidkotlin.room.entity.MovieEntity
import com.squareup.picasso.Picasso

class MovieAdapter(movieList: List<MovieEntity>, context: Context): RecyclerView.Adapter<MovieAdapter.ViewHolder>() {

    private var mContext: Context = context
    private var mMovieList: List<MovieEntity> = movieList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =LayoutInflater.from(parent.context).inflate(R.layout.item_movie, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // Pasamos el objeto de MovieResponse
        holder.movie = mMovieList.get(position)

        // Iniciamos la función data
        holder.initData(holder.movie)
    }

    override fun getItemCount(): Int {
        return mMovieList.size
    }


    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        private val vBinding = ItemMovieBinding.bind(itemView)
        lateinit var movie: MovieEntity

        fun initData(mMovie: MovieEntity) {
            vBinding.textViewTitulo.text = mMovie.title.toString()
            vBinding.textViewDescripcion.text = mMovie.overview.toString()
            vBinding.textViewFecha.text = mMovie.releaseDate.toString()

            Picasso.get()
                .load("https://image.tmdb.org/t/p/w500" + mMovie.backdropPath)
                .into(vBinding.imageViewPortada);

            ;

        }

    }

}
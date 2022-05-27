package com.macacar96.examenandroidkotlin.ui.movie

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.macacar96.examenandroidkotlin.databinding.FragmentMovieBinding
import com.macacar96.examenandroidkotlin.retrofit.response.MovieResponse
import com.macacar96.examenandroidkotlin.room.entity.MovieEntity

class MovieFragment : Fragment(), MovieMVP.View {

    private lateinit var _binding: FragmentMovieBinding
    private lateinit var presenter: MovieMVP.Presenter
    private lateinit var adapter: MovieAdapter

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentMovieBinding.inflate(inflater, container, false)
        val root: View = binding.root

        presenter = MoviePresenter(this) // Creamos la instancia del presenter
        presenter.getDataPeliculasPresenter(root.context) // Inicializamos el metodo que nos traira la Data de la API Movie



        return root
    }

    // Método que trae el resultado de la respuesta
    override fun showResultView(result: List<MovieEntity?>?, size: Int) {
        activity?.runOnUiThread(Runnable {
            //Toast.makeText(activity, "Datos cargado con éxito: $size", Toast.LENGTH_LONG).show()

            // Ocultamos el progress y mostramos el RV
            _binding.pbLoading.visibility = View.GONE
            _binding.rvMovies.visibility = View.VISIBLE

            adapter = MovieAdapter(result as List<MovieEntity>, _binding!!.root.context)
            _binding.rvMovies.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            _binding.rvMovies.adapter = adapter
        })
    }

    // Método que trae el error de la respuesta
    override fun showErrorView(result: String?) {
        activity?.runOnUiThread(Runnable {
            Toast.makeText(activity, result.toString(), Toast.LENGTH_LONG).show()
        })
    }


}
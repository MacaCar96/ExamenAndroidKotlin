package com.macacar96.examenandroidkotlin.ui.movie

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.macacar96.examenandroidkotlin.databinding.FragmentMovieBinding
import com.macacar96.examenandroidkotlin.retrofit.response.MovieResponse

class MovieFragment : Fragment(), MovieMVP.View {

    private var _binding: FragmentMovieBinding? = null

    private lateinit var presenter: MovieMVP.Presenter

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
        val textView: TextView = binding.textHome

        presenter = MoviePresenter(this) // Creamos la instancia del presenter
        presenter.getDataPeliculasPresenter(root.context) // Inicializamos el metodo que nos traira la Data de la API Movie

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // Método que trae el resultado de la respuesta
    override fun showResultView(result: List<MovieResponse?>?, size: Int) {
        activity?.runOnUiThread(Runnable {
            Toast.makeText(activity, "Datos cargado con éxito: $size", Toast.LENGTH_LONG).show()
        })
    }

    // Método que trae el error de la respuesta
    override fun showErrorView(result: String?) {
        activity?.runOnUiThread(Runnable {
            Toast.makeText(activity, result.toString(), Toast.LENGTH_LONG).show()
        })
    }


}
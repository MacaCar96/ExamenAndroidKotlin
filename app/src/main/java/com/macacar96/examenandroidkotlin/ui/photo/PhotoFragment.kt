package com.macacar96.examenandroidkotlin.ui.photo

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.macacar96.examenandroidkotlin.utils.LoadingScreen
import com.macacar96.examenandroidkotlin.utils.NotificationDialog
import com.macacar96.examenandroidkotlin.databinding.FragmentPhotoBinding
import java.util.HashMap

class PhotoFragment : Fragment() {

    private var _binding: FragmentPhotoBinding? = null

    private val fileResult = 1

    private val database = Firebase.database
    private val reference = database.getReference("images")

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentPhotoBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Acción click para abrir galería y posteriormente subir imagen
        binding.uploadImageView.setOnClickListener {
            fileManager()
        }

        return root
    }

    private fun fileManager() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        intent.type = "*/*"

        resultLauncher.launch(intent)

    }

    private var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                // There are no request codes
                val data: Intent? = result.data

                val clipData = data?.clipData

                if (clipData != null) {
                    // Show loading - upload files
                    LoadingScreen.displayLoading(activity,false)
                    // Bucle que manda a subir las imagenes selecionadas
                    for (i in 0 until clipData.itemCount) {
                        val uri = clipData.getItemAt(i).uri
                        uri?.let { fileUpload(it) }
                    }
                    // Esconder loading
                    LoadingScreen.hideLoading()

                    // Notificar al usuario - success
                    NotificationDialog(
                        "¡Subidos!",
                        "Los archivos se han subido con éxito"
                    ).show(childFragmentManager, NotificationDialog.TAG)
                } else {
                    val uri = data?.data
                    uri?.let { fileUpload(it) }
                }

            }
        }

    private fun fileUpload(mUri: Uri) {

        val folder: StorageReference = FirebaseStorage.getInstance().reference.child("Images")
        val path = mUri.lastPathSegment.toString()
        val fileName: StorageReference = folder.child(path.substring(path.lastIndexOf('/') + 1))

        fileName.putFile(mUri).addOnSuccessListener {
            fileName.downloadUrl.addOnSuccessListener { uri ->
                val hashMap = HashMap<String, String>()
                hashMap["link"] = java.lang.String.valueOf(uri)

                reference.child(reference.push().key.toString()).setValue(hashMap)

                Log.i("message", "File upload successfully")
            }

        }.addOnFailureListener {
            Log.i("message", "File upload error")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
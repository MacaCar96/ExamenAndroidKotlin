package com.macacar96.examenandroidkotlin.ui.user

import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.macacar96.examenandroidkotlin.databinding.FragmentUserBinding
import com.macacar96.examenandroidkotlin.utils.NotificationDialog

class UserFragment : Fragment() {

    private var _binding: FragmentUserBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentUserBinding.inflate(inflater, container, false)
        val root: View = binding.root

        fullNameFocusListener()
        phoneFocusListener()
        emailFocusListener()
        addressFocusListener()

        binding.submitButton.setOnClickListener { submitForm() }

        return root
    }

    private fun submitForm()
    {
        binding.fullNameContainer.helperText = validFullName()
        binding.phoneContainer.helperText = validPhone()
        binding.emailContainer.helperText = validEmail()
        binding.addressContainer.helperText = validAddress()

        val validFullName = binding.fullNameContainer.helperText == null
        val validPhone = binding.phoneContainer.helperText == null
        val validEmail = binding.emailContainer.helperText == null
        val validAddress = binding.addressContainer.helperText == null

        if (validFullName && validPhone && validEmail && validAddress)
            saveData()
        else
            invalidForm()
    }

    private fun saveData()
    {
        var message = "Email: " + binding.emailEditText.text
        message += "\nPhone: " + binding.phoneEditText.text
        message += "\nFullName: " + binding.fullNameEditText.text
        message += "\nAddress: " + binding.addressEditText.text

        Toast.makeText(activity, "SUBMMIT: $message", Toast.LENGTH_LONG).show()
    }

    private fun invalidForm()
    {
        var message = ""
        if(binding.fullNameContainer.helperText != null)
            message += "\n\nNombre: " + binding.fullNameContainer.helperText
        if(binding.phoneContainer.helperText != null)
            message += "\n\nTeléfono: " + binding.phoneContainer.helperText
        if(binding.emailContainer.helperText != null)
            message += "\n\nCorreo: " + binding.emailContainer.helperText
        if(binding.addressContainer.helperText != null)
            message += "\n\nDirección: " + binding.addressContainer.helperText

        // Notificar al usuario
        NotificationDialog(
            "Datos inválidos",
            message
        ).show(childFragmentManager, NotificationDialog.TAG)
    }

    private fun fullNameFocusListener()
    {
        binding.fullNameEditText.setOnFocusChangeListener { _, focused ->
            if(!focused)
            {
                binding.fullNameContainer.helperText = validFullName()
            }
        }
    }

    private fun validFullName(): String?
    {
        val fullNameText = binding.fullNameEditText.text.toString()
        if(fullNameText.isEmpty())
        {
            return "El nombre completo es requerido"
        }

        return null
    }

    private fun phoneFocusListener()
    {
        binding.phoneEditText.setOnFocusChangeListener { _, focused ->
            if(!focused)
            {
                binding.phoneContainer.helperText = validPhone()
            }
        }
    }

    private fun validPhone(): String?
    {
        val phoneText = binding.phoneEditText.text.toString()
        if(!phoneText.matches(".*[0-9].*".toRegex()))
        {
            return "Deben de ser solo números"
        }
        if(phoneText.length != 10)
        {
            return "Sólo deben ser 10 dígitos"
        }
        return null
    }

    private fun emailFocusListener()
    {
        binding.emailEditText.setOnFocusChangeListener { _, focused ->
            if(!focused)
            {
                binding.emailContainer.helperText = validEmail()
            }
        }
    }

    private fun validEmail(): String?
    {
        val emailText = binding.emailEditText.text.toString()
        if(!Patterns.EMAIL_ADDRESS.matcher(emailText).matches())
        {
            return "El correo tiene un formato inválido"
        }
        return null
    }

    private fun addressFocusListener()
    {
        binding.addressEditText.setOnFocusChangeListener { _, focused ->
            if(!focused)
            {
                binding.addressContainer.helperText = validAddress()
            }
        }
    }

    private fun validAddress(): String?
    {
        val addressText = binding.addressEditText.text.toString()
        if(addressText.isEmpty())
        {
            return "La dirección es requerida"
        }

        return null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
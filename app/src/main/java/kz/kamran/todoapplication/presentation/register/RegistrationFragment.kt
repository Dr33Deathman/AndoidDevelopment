package kz.kamran.todoapplication.presentation.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kz.kamran.todoapplication.databinding.RegistrationFragmentBinding
import kz.kamran.todoapplication.presentation.register.state.RegistrationState

@AndroidEntryPoint
class RegistrationFragment : Fragment() {

    private var _binding: RegistrationFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: RegistrationViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = RegistrationFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        stopLoading()
        _binding = null
    }

    override fun onDestroy() {
        viewModel.cancel()
        super.onDestroy()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListeners()
        setObservers()
    }

    private fun setObservers() {
        viewModel.state.observe(viewLifecycleOwner) {
            when (it) {
                is RegistrationState.Loading -> startLoading()
                is RegistrationState.Error -> {
                    stopLoading()
                    showMessage(it.message)
                }
                is RegistrationState.Success -> {
                    stopLoading()
                    showMessage("SUCCESS")
                    findNavController().navigateUp()
                }
                else -> stopLoading()
            }
        }
    }

    private fun setListeners() {
        with(binding) {
            registrationButton.setOnClickListener {
                register()
            }
        }
    }

    private fun register() {
        with(binding) {
            val login = loginEditText.text.toString()
            val password = passwordEditText.text.toString()
            val retryPassword = retryPasswordEditText.text.toString()
            if (password != retryPassword) {
                showMessage("Passwords doesn't match")
                return
            }

            viewModel.register(login, password)
        }
    }

    private fun showMessage(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }

    private fun stopLoading() {
        binding.progressBar.visibility = View.INVISIBLE
    }

    private fun startLoading() {
        binding.progressBar.visibility = View.VISIBLE
    }


}
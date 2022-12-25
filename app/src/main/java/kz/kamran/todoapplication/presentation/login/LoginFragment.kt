package kz.kamran.todoapplication.presentation.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kz.kamran.todoapplication.databinding.LoginFragmentBinding
import kz.kamran.todoapplication.presentation.login.state.LoginState

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding: LoginFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = LoginFragmentBinding.inflate(inflater, container, false)
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

    private fun stopLoading() {
        binding.progressBar.visibility = View.INVISIBLE
    }

    private fun startLoading() {
        binding.progressBar.visibility = View.VISIBLE
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListeners()
        setObservers()
    }

    private fun setObservers() {
        viewModel.loginState.observe(viewLifecycleOwner) {
            when (it) {
                is LoginState.Loading -> {
                    startLoading()
                    binding.loginInputLayout.isHelperTextEnabled = false
                    binding.passwordInputLayout.isHelperTextEnabled = false
                }
                is LoginState.Error -> {
                    stopLoading()
                    binding.loginInputLayout.helperText = it.loginMessage
                    binding.passwordInputLayout.helperText = it.passwordMessage
                    if (!it.message.isNullOrEmpty()) {
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                    }
                }
                is LoginState.Success -> {
                    stopLoading()
                    findNavController().navigate(
                        LoginFragmentDirections.actionLoginFragmentToRemoteFragment()
                    )
                }
                else -> stopLoading()
            }
        }
    }

    private fun setListeners() = binding.run {
        loginButton.setOnClickListener {
            val login = loginEditText.text.toString()
            val password = passwordEditText.text.toString()
            viewModel.login(login, password)
        }
        registrationTextView.setOnClickListener {
            findNavController().navigate(
                LoginFragmentDirections.actionLoginFragmentToRegistrationFragment()
            )
        }

    }


}
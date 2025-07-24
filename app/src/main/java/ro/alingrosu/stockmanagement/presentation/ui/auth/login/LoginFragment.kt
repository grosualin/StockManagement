package ro.alingrosu.stockmanagement.presentation.ui.auth.login

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import ro.alingrosu.stockmanagement.R
import ro.alingrosu.stockmanagement.databinding.FragmentLoginBinding
import ro.alingrosu.stockmanagement.presentation.state.UiState
import ro.alingrosu.stockmanagement.presentation.ui.base.BaseFragment
import ro.alingrosu.stockmanagement.presentation.ui.main.MainActivity
import ro.alingrosu.stockmanagement.presentation.util.Factory
import ro.alingrosu.stockmanagement.presentation.util.getAppComponent

class LoginFragment : BaseFragment(R.layout.fragment_login) {

    private lateinit var binding: FragmentLoginBinding

    private val viewModel: LoginViewModel by viewModels {
        Factory {
            getAppComponent().loginViewModel
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        FragmentLoginBinding.inflate(inflater, container, false).also { inflated ->
            binding = inflated
            return binding.root
        }
    }

    override fun initView() {
        binding.buttonLogin.setOnClickListener {
            val (user, pass) = binding.emailPhoneInput.text.toString() to binding.passInput.text.toString()
            viewModel.login(user, pass)
        }
    }

    override fun listenFoUiState() {
        viewModel.uiState.observe(viewLifecycleOwner) { uiState ->
            when (uiState) {
                is UiState.Loading -> {
                    binding.loading.isVisible = true
                    binding.buttonLogin.isEnabled = false
                }

                is UiState.Success -> {
                    binding.loading.isVisible = false
                    binding.buttonLogin.isEnabled = true
                    Toast.makeText(context, "Login success: ${uiState.data}", Toast.LENGTH_LONG).show()

                    if (uiState.data == true) {
                        val mainActivity = Intent(requireContext(), MainActivity::class.java)
                        startActivity(mainActivity)
                        requireActivity().finish()
                    }
                }

                is UiState.Error -> {
                    Toast.makeText(context, uiState.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}
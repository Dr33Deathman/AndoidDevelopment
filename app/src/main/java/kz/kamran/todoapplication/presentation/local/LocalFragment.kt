package kz.kamran.todoapplication.presentation.local

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kz.kamran.todoapplication.databinding.LocalFragmentBinding
import kz.kamran.todoapplication.presentation.local.state.LocalTodoListState
import kz.kamran.todoapplication.presentation.remote.todo_list_adapter.TodoListAdapter

@AndroidEntryPoint
class LocalFragment : Fragment() {

    private var _binding: LocalFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: LocalViewModel by viewModels()

    private val adapter: TodoListAdapter by lazy { TodoListAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = LocalFragmentBinding.inflate(inflater, container, false)
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
        setupRV()
        setupListeners()
        setupObservers()
        loadData()
    }

    private fun setupListeners() = binding.run {
        addButton.setOnClickListener {
            findNavController().navigate(
                LocalFragmentDirections.actionLocalFragmentToTodoFragment(
                    isLocal = true
                )
            )
        }
    }

    private fun loadData() = viewModel.getLocalTodoList()

    private fun setupObservers() {
        viewModel.todoListState.observe(viewLifecycleOwner) {
            when (it) {
                is LocalTodoListState.Loading -> startLoading()
                is LocalTodoListState.Error -> {
                    stopLoading()
                    binding.errorImageView.visibility = View.VISIBLE
                    showMessage(it.message)
                }
                is LocalTodoListState.Success -> {
                    stopLoading()
                    binding.errorImageView.visibility = View.GONE
                    adapter.todoList = it.todoList.toMutableList()
                }
                else -> Unit
            }
        }
    }

    private fun showMessage(message: String) =
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()

    private fun setupRV() {
        binding.todoListRecyclerView.adapter = adapter
        adapter.onItemClick = {
            findNavController().navigate(
                LocalFragmentDirections.actionLocalFragmentToTodoFragment(
                    isLocal = true,
                    todo = it
                )
            )
        }
        adapter.onCompleteClick = {
            viewModel.changeState(it)
        }
    }

    private fun startLoading() {
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun stopLoading() {
        binding.progressBar.visibility = View.GONE
    }
}
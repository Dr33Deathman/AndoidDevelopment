package kz.kamran.todoapplication.presentation.remote

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kz.kamran.todoapplication.databinding.RemoteFragmentBinding
import kz.kamran.todoapplication.presentation.remote.state.RemoteListState
import kz.kamran.todoapplication.presentation.remote.todo_list_adapter.TodoListAdapter

@AndroidEntryPoint
class RemoteFragment : Fragment() {

    private var _binding: RemoteFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: RemoteViewModel by viewModels()

    private val adapter: TodoListAdapter by lazy {
        TodoListAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = RemoteFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDestroy() {
        viewModel.cancel()
        super.onDestroy()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListeners()
        setupRV()
        setObservers()
        loadData()
    }

    private fun loadData() = viewModel.getTodoList()

    private fun setObservers() {
        viewModel.state.observe(viewLifecycleOwner) {
            when (it) {
                is RemoteListState.Loading -> {
                    startLoading()
                }
                is RemoteListState.Unauthorized -> {
                    stopLoading()
                    showError("Please, login")
                }
                is RemoteListState.Error -> {
                    stopLoading()
                    showError(it.message)
                }
                is RemoteListState.Success -> {
                    stopLoading()
                    binding.errorImageView.visibility = View.GONE
                    adapter.todoList = it.list
                }
                else -> Unit
            }
        }
    }

    private fun showError(message: String) {
        binding.errorImageView.visibility = View.VISIBLE
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }

    private fun setupRV() {
        with(binding) {
            todoListRecyclerView.adapter = adapter
            adapter.onCompleteClick = {
                viewModel.changeState(it)
            }
            adapter.onItemClick = {
                findNavController().navigate(
                    RemoteFragmentDirections.actionRemoteFragmentToTodoFragment2(
                        todo = it
                    )
                )
            }
        }
    }

    private fun setListeners() {
        with(binding) {
            addButton.setOnClickListener {
                findNavController().navigate(
                    RemoteFragmentDirections.actionRemoteFragmentToTodoFragment2()
                )
            }

            logoutButton.setOnClickListener{
                viewModel.logout()
                findNavController().navigateUp()
            }

            refreshLayout.setOnRefreshListener {
                viewModel.getTodoList()
                refreshLayout.isRefreshing = false
            }
        }
    }

    private fun startLoading() {
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun stopLoading() {
        binding.progressBar.visibility = View.GONE
    }

}
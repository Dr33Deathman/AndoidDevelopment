package kz.kamran.todoapplication.presentation.add_edit

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import kz.kamran.todoapplication.data.model.Todo
import kz.kamran.todoapplication.databinding.TodoFragmentBinding
import kz.kamran.todoapplication.presentation.add_edit.state.TodoState
import java.util.*

@AndroidEntryPoint
class TodoFragment : Fragment() {

    private var _binding: TodoFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: TodoViewModel by viewModels()

    private val args: TodoFragmentArgs by navArgs()
    private val todo: Todo? by lazy { args.todo }
    private val isLocal: Boolean by lazy { args.isLocal }

    private val calendar = Calendar.getInstance()
    private var mYear: Int = calendar.get(Calendar.YEAR)
    private var mMonth: Int = calendar.get(Calendar.MONTH) + 1 // january = 0
    private var mDay: Int = calendar.get(Calendar.DAY_OF_MONTH)
    private var mHour: Int = calendar.get(Calendar.HOUR_OF_DAY) + 1
    private var mMinute: Int = calendar.get(Calendar.MINUTE)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = TodoFragmentBinding.inflate(inflater, container, false)
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
        setData()
        setListeners()
        setObservers()
    }

    private fun setObservers() {
        viewModel.state.observe(viewLifecycleOwner) {
            when (it) {
                is TodoState.Loading -> {
                    startLoading()
                }
                is TodoState.Error -> {
                    stopLoading()
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                }
                is TodoState.Success -> {
                    stopLoading()
                    findNavController().navigateUp()
                }
                else -> stopLoading()
            }
        }
    }

    private fun stopLoading() {
        with(binding) {
            progressBar.visibility = View.INVISIBLE
            timePickButton.isClickable = true
            datePickButton.isClickable = true
            saveButton.isClickable = true
            titleEditText.isClickable = true
            descriptionEditText.isClickable = true
            titleEditText.isFocusable = true
            descriptionEditText.isFocusable = true
        }
    }

    private fun startLoading() {
        with(binding) {
            progressBar.visibility = View.VISIBLE
            timePickButton.isClickable = false
            datePickButton.isClickable = false
            saveButton.isClickable = false
            titleEditText.isClickable = false
            descriptionEditText.isClickable = false
            titleEditText.isFocusable = false
            descriptionEditText.isFocusable = false
        }
    }

    private fun setData() {
        if (todo != null) {
            with(binding) {
                titleEditText.setText(todo!!.title)
                descriptionEditText.setText(todo!!.description)
                categoryEditText.setText(todo!!.category.title)

                val calendar = Calendar.getInstance()
                calendar.time = todo!!.deadline
                mYear = calendar.get(Calendar.YEAR)
                mMonth = calendar.get(Calendar.MONTH) + 1
                mDay = calendar.get(Calendar.DAY_OF_MONTH)
                mHour = calendar.get(Calendar.HOUR_OF_DAY)
                mMinute = calendar.get(Calendar.MINUTE)
            }
        }

        setTime()
        setDate()

    }

    private fun setListeners() {
        with(binding) {
            backImageView.setOnClickListener {
                findNavController().navigateUp()
            }

            saveButton.setOnClickListener {
                val title = titleEditText.text.toString()
                val description = descriptionEditText.text.toString()
                val category = categoryEditText.text.toString()

                val calendar = Calendar.getInstance()
                calendar.set(Calendar.YEAR, mYear)
                calendar.set(Calendar.MONTH, mMonth)
                calendar.set(Calendar.DAY_OF_MONTH, mDay)
                calendar.set(Calendar.HOUR_OF_DAY, mHour)
                calendar.set(Calendar.MINUTE, mMinute)

                viewModel.saveTodo(
                    title = title,
                    description = description,
                    categoryTitle = category,
                    calendar = calendar,
                    isLocal = isLocal,
                    id = todo?.id ?: 0
                )
            }

            setupDatePicker()
            setupTimePicker()
        }
    }

    private fun setupTimePicker() {
        binding.timePickButton.setOnClickListener {
            val timePickerDialog = TimePickerDialog(
                requireContext(), { _, hourOfDay, minute ->
                    mHour = hourOfDay
                    mMinute = minute
                    setTime()
                }, mHour, mMinute, true
            )
            timePickerDialog.show()
        }
    }

    private fun setupDatePicker() {
        binding.datePickButton.setOnClickListener {
            val datePickerDialog = DatePickerDialog(
                requireContext(), { _, year, monthOfYear, dayOfMonth ->
                    mYear = year
                    mMonth = monthOfYear + 1
                    mDay = dayOfMonth
                    setDate()
                }, mYear, mMonth, mDay
            )
            datePickerDialog.show()
        }
    }

    private fun setTime() {
        val hourString = if (mHour < 10) {
            "0$mHour"
        } else {
            "$mHour"
        }

        val minuteString = if (mMinute < 10) {
            "0$mMinute"
        } else {
            "$mMinute"
        }
        val time = "$hourString:$minuteString"
        binding.timeTextView.text = time
    }

    private fun setDate() {
        val date = "$mDay - $mMonth - $mYear"
        binding.dateTextView.text = date
    }
}
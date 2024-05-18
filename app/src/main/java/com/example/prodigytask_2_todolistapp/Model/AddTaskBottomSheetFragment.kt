package com.example.prodigytask_2_todolistapp.Model

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.prodigytask_2_todolistapp.R
import com.example.prodigytask_2_todolistapp.SQLiteHelper
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.addnewtask.*


class AddTaskBottomSheetFragment : BottomSheetDialogFragment() {

    private lateinit var dbHelper: SQLiteHelper
    private var listener: OnTaskAddedListener? = null
    private var task: Task? = null

    interface OnTaskAddedListener {
        fun onTaskAdded()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnTaskAddedListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement OnTaskAddedListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        task = arguments?.getParcelable(ARG_TASK)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.addnewtask, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dbHelper = SQLiteHelper(requireContext())

        task?.let {
            edittext.setText(it.task)
            btnsave.text = "Update"
        }

        btnsave.setOnClickListener {
            val taskText = edittext.text.toString().trim()
            if (taskText.isNotEmpty()) {
                if (task == null) {
                    val newTask = Task(task = taskText)
                    dbHelper.addTask(newTask)
                } else {
                    task?.task = taskText
                    dbHelper.updateTask(task!!)
                }
                listener?.onTaskAdded()
                dismiss()
            }
        }
    }

    companion object {
        const val TAG = "AddTaskBottomSheetFragment"
        private const val ARG_TASK = "task"

        fun newInstance(task: Task): AddTaskBottomSheetFragment {
            val fragment = AddTaskBottomSheetFragment()
            val args = Bundle()
            args.putParcelable(ARG_TASK, task)
            fragment.arguments = args
            return fragment
        }
    }
}
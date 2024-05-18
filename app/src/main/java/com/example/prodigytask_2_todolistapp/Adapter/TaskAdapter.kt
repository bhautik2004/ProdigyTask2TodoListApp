package com.example.prodigytask_2_todolistapp.Adapter

import android.content.Context
import android.view.*
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.example.prodigytask_2_todolistapp.MainActivity
import com.example.prodigytask_2_todolistapp.Model.Task
import com.example.prodigytask_2_todolistapp.R
import com.example.prodigytask_2_todolistapp.SQLiteHelper
import com.google.android.material.checkbox.MaterialCheckBox

class TaskAdapter(
    private val context: Context,
    private val taskList: ArrayList<Task>,
    private val dbHelper: SQLiteHelper
) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.task_layout, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = taskList[position]
        holder.checkBox.text = task.task
        holder.checkBox.isChecked = task.isCompleted

        holder.checkBox.setOnCheckedChangeListener { _, isChecked ->
            task.isCompleted = isChecked
            dbHelper.updateTask(task)
        }

        holder.itemView.setOnLongClickListener {
            showPopup(holder.itemView, task)
            true
        }
    }

    override fun getItemCount(): Int {
        return taskList.size
    }

    private fun showPopup(view: View, task: Task) {
        val popup = PopupMenu(context, view)
        val inflater: MenuInflater = popup.menuInflater
        inflater.inflate(R.menu.task_options_menu, popup.menu)
        popup.setOnMenuItemClickListener { item -> handleMenuItemClick(item, task) }
        popup.show()
    }

    private fun handleMenuItemClick(item: MenuItem, task: Task): Boolean {
        return when (item.itemId) {
            R.id.edit_task -> {
                if (context is MainActivity) {
                    context.editTask(task)
                }
                true
            }
            R.id.delete_task -> {
                dbHelper.deleteTask(task)
                taskList.remove(task)
                notifyDataSetChanged()
                true
            }
            else -> false
        }
    }

    inner class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val checkBox: MaterialCheckBox = itemView.findViewById(R.id.mcheckbox)
    }
}
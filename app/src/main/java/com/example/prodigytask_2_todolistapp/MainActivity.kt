package com.example.prodigytask_2_todolistapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.prodigytask_2_todolistapp.Adapter.TaskAdapter
import com.example.prodigytask_2_todolistapp.Model.AddTaskBottomSheetFragment
import com.example.prodigytask_2_todolistapp.Model.Task
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), AddTaskBottomSheetFragment.OnTaskAddedListener {

    private lateinit var dbHelper: SQLiteHelper
    private lateinit var taskList: ArrayList<Task>
    private lateinit var taskAdapter: TaskAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dbHelper = SQLiteHelper(this)
        taskList = dbHelper.getAllTasks()

        taskAdapter = TaskAdapter(this, taskList, dbHelper)
        recyclerview.layoutManager = LinearLayoutManager(this)
        recyclerview.adapter = taskAdapter

        fab.setOnClickListener {
            val addTaskBottomSheet = AddTaskBottomSheetFragment()
            addTaskBottomSheet.show(supportFragmentManager, AddTaskBottomSheetFragment.TAG)
        }
    }

    override fun onTaskAdded() {
        taskList.clear()
        taskList.addAll(dbHelper.getAllTasks())
        taskAdapter.notifyDataSetChanged()
    }

    fun editTask(task: Task) {
        val editTaskBottomSheet = AddTaskBottomSheetFragment.newInstance(task)
        editTaskBottomSheet.show(supportFragmentManager, AddTaskBottomSheetFragment.TAG)
    }
}
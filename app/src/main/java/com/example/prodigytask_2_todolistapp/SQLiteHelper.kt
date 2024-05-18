package com.example.prodigytask_2_todolistapp

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.prodigytask_2_todolistapp.Model.Task

class SQLiteHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "TaskDatabase"
        private const val TABLE_TASKS = "Tasks"
        private const val KEY_ID = "id"
        private const val KEY_TASK = "task"
        private const val KEY_COMPLETED = "completed"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = ("CREATE TABLE $TABLE_TASKS("
                + "$KEY_ID INTEGER PRIMARY KEY,"
                + "$KEY_TASK TEXT,"
                + "$KEY_COMPLETED INTEGER)")
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_TASKS")
        onCreate(db)
    }

    fun addTask(task: Task): Long {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_TASK, task.task)
        contentValues.put(KEY_COMPLETED, if (task.isCompleted) 1 else 0)

        val success = db.insert(TABLE_TASKS, null, contentValues)
        db.close()
        return success
    }

    @SuppressLint("Range")
    fun getAllTasks(): ArrayList<Task> {
        val taskList = ArrayList<Task>()
        val selectQuery = "SELECT * FROM $TABLE_TASKS"
        val db = this.readableDatabase
        val cursor = db.rawQuery(selectQuery, null)

        if (cursor.moveToFirst()) {
            do {
                val task = Task(
                    id = cursor.getInt(cursor.getColumnIndex(KEY_ID)),
                    task = cursor.getString(cursor.getColumnIndex(KEY_TASK)),
                    isCompleted = cursor.getInt(cursor.getColumnIndex(KEY_COMPLETED)) == 1
                )
                taskList.add(task)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return taskList
    }

    fun updateTask(task: Task): Int {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_TASK, task.task)
        contentValues.put(KEY_COMPLETED, if (task.isCompleted) 1 else 0)

        return db.update(TABLE_TASKS, contentValues, "$KEY_ID=?", arrayOf(task.id.toString()))
    }

    fun deleteTask(task: Task): Int {
        val db = this.writableDatabase
        return db.delete(TABLE_TASKS, "$KEY_ID=?", arrayOf(task.id.toString()))
    }
}
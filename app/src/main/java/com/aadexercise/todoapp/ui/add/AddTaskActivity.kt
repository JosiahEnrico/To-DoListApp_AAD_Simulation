package com.aadexercise.todoapp.ui.add

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.aadexercise.todoapp.R
import com.aadexercise.todoapp.data.Task
import com.aadexercise.todoapp.databinding.ActivityAddTaskBinding
import com.aadexercise.todoapp.ui.ViewModelFactory
import com.aadexercise.todoapp.utils.DatePickerFragment
import com.google.android.material.textfield.TextInputEditText
import java.text.SimpleDateFormat
import java.util.*

class AddTaskActivity : AppCompatActivity(), DatePickerFragment.DialogDateListener {
    private var dueDateMillis: Long = System.currentTimeMillis()
    private lateinit var binding: ActivityAddTaskBinding
    private lateinit var viewModel: AddTaskViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_task)
        binding = ActivityAddTaskBinding.inflate(layoutInflater)

        supportActionBar?.title = getString(R.string.add_task)
        val viewFactory = ViewModelFactory.getInstance(this)
        viewModel = ViewModelProvider(this, viewFactory)[AddTaskViewModel::class.java]
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_add, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_save -> {
                //TODO 12 : Create AddTaskViewModel and insert new task to database
                val editTitle = findViewById<TextInputEditText>(R.id.add_ed_title )
                val editDesc = findViewById<TextInputEditText>(R.id.add_ed_description)
                val title = editTitle.text.toString()
                val description = editDesc.text.toString()
                val newTask = Task(0, title, description, dueDateMillis, false)

                viewModel.addTask(newTask)
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun showDatePicker(view: View) {
        val dialogFragment = DatePickerFragment()
        dialogFragment.show(supportFragmentManager, "datePicker")
    }

    override fun onDialogDateSet(tag: String?, year: Int, month: Int, dayOfMonth: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, dayOfMonth)
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        findViewById<TextView>(R.id.add_tv_due_date).text = dateFormat.format(calendar.time)

        dueDateMillis = calendar.timeInMillis
    }
}
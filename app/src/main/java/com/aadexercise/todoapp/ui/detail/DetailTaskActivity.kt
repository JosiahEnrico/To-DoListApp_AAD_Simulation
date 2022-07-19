package com.aadexercise.todoapp.ui.detail

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.aadexercise.todoapp.R
import com.aadexercise.todoapp.databinding.ActivityTaskDetailBinding
import com.aadexercise.todoapp.ui.ViewModelFactory
import com.aadexercise.todoapp.utils.DateConverter
import com.aadexercise.todoapp.utils.TASK_ID
import com.google.android.material.textfield.TextInputEditText

class DetailTaskActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTaskDetailBinding
    private lateinit var viewModel: DetailTaskViewModel
    private lateinit var buttonDelete : Button
    private lateinit var detailTitle: TextInputEditText
    private lateinit var detailDesc: TextInputEditText
    private lateinit var detailDueDate : TextInputEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_detail)
        binding = ActivityTaskDetailBinding.inflate(layoutInflater)

        //TODO 11 : Show detail task and implement delete action
        detailTitle = findViewById(R.id.detail_ed_title)
        detailDesc = findViewById(R.id.detail_ed_description)
        detailDueDate = findViewById(R.id.detail_ed_due_date)
        buttonDelete = findViewById(R.id.btn_delete_task)

        val viewFactory = ViewModelFactory.getInstance(this)
        viewModel = ViewModelProvider(this, viewFactory)[DetailTaskViewModel::class.java]
        viewModel.setTaskId(intent.getIntExtra(TASK_ID,0))
        viewModel.task.observe(this) { task ->
            if (task != null) {
                detailTitle.setText(task.title)
                detailDesc.setText(task.description)
                detailDueDate.setText(DateConverter.convertMillisToString(task.dueDateMillis))
            }
        }

        buttonDelete.setOnClickListener{
            viewModel.deleteTask()
            onBackPressed()
        }
    }
}
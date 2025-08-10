package com.example.todolist;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.app.AlertDialog;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    EditText itemInput;
    Button addButton;
    ListView listView;

    ArrayList<Task> taskList = new ArrayList<>();
    TaskAdapter taskAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        itemInput = findViewById(R.id.editText);
        addButton = findViewById(R.id.button);
        listView = findViewById(R.id.List);

        taskList = FileHelper.readData(this);  // Load from file
        taskAdapter = new TaskAdapter(this, taskList);
        listView.setAdapter(taskAdapter);

        // ➕ Add Task without date
        addButton.setOnClickListener(v -> {
            String taskTitle = itemInput.getText().toString().trim();
            if (taskTitle.isEmpty()) return;

            Task task = new Task(taskTitle, ""); // Empty date initially
            taskList.add(task);
            FileHelper.writedata(taskList, getApplicationContext());
            taskAdapter.notifyDataSetChanged();
            itemInput.setText("");
        });

    }
}

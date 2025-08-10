package com.example.todolist;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class TaskAdapter extends ArrayAdapter<Task> {

    public TaskAdapter(Context context, ArrayList<Task> tasks) {
        super(context, 0, tasks);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Task task = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_task, parent, false);
        }

        final View rowView = convertView;

        TextView title = convertView.findViewById(R.id.taskTitle);
        TextView dueDate = convertView.findViewById(R.id.dueDateText);
        TextView timeText = convertView.findViewById(R.id.timeText);
        ImageView calendarIcon = convertView.findViewById(R.id.calendarIcon);
        ImageView clockIcon = convertView.findViewById(R.id.clockIcon);
        Button toggleButton = convertView.findViewById(R.id.toggleButton);
        Button removeButton = convertView.findViewById(R.id.removeButton);

        // Set task title
        title.setText(task.getTitle());

        // Due date visibility
        if (task.getDueDate() == null || task.getDueDate().trim().isEmpty()) {
            dueDate.setVisibility(View.GONE);
        } else {
            dueDate.setText("Due: " + task.getDueDate());
            dueDate.setVisibility(View.VISIBLE);
        }

        // Time visibility
        if (task.getTime() == null || task.getTime().trim().isEmpty()) {
            timeText.setVisibility(View.GONE);
        } else {
            timeText.setText("Time: " + task.getTime());
            timeText.setVisibility(View.VISIBLE);
        }

        // Calendar icon click - only if no date yet
        calendarIcon.setOnClickListener(v -> {
            if (task.getDueDate() == null || task.getDueDate().trim().isEmpty()) {
                openDatePicker(task);
            }
        });

        // Due date click - open picker
        dueDate.setOnClickListener(v -> openDatePicker(task));

        // Clock icon click - only if no time yet
        clockIcon.setOnClickListener(v -> {
            if (task.getTime() == null || task.getTime().trim().isEmpty()) {
                openTimePicker(task);
            }
        });

        // Time click - edit anytime
        timeText.setOnClickListener(v -> openTimePicker(task));

        // Completed styling
        if (task.isCompleted()) {
            title.setPaintFlags(title.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            toggleButton.setText("Undone");
            toggleButton.setBackgroundTintList(android.content.res.ColorStateList.valueOf(0xFFA1887F));
            rowView.setAlpha(0.4f);
        } else {
            title.setPaintFlags(title.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            toggleButton.setText("Done");
            toggleButton.setBackgroundTintList(android.content.res.ColorStateList.valueOf(0xFFFDD835));
            rowView.setAlpha(1f);
        }

        // Toggle complete
        toggleButton.setOnClickListener(v -> {
            task.setCompleted(!task.isCompleted());
            FileHelper.writedata(getAllTasks(), getContext());
            rowView.animate().alpha(task.isCompleted() ? 0.4f : 1f).setDuration(300).start();
            notifyDataSetChanged();
        });

        // Remove
        removeButton.setOnClickListener(v -> {
            remove(task);
            FileHelper.writedata(getAllTasks(), getContext());
            notifyDataSetChanged();
        });

        return convertView;
    }

    private void openDatePicker(Task task) {
        final Calendar calendar = Calendar.getInstance();
        DatePickerDialog picker = new DatePickerDialog(getContext(),
                (view, year, month, dayOfMonth) -> {
                    String newDate = dayOfMonth + "/" + (month + 1) + "/" + year;
                    task.setDueDate(newDate);
                    FileHelper.writedata(getAllTasks(), getContext());
                    notifyDataSetChanged();
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        picker.show();
    }

    private void openTimePicker(Task task) {
        final Calendar calendar = Calendar.getInstance();
        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
                (view, hourOfDay, minute) -> {
                    // Format time as hh:mm a
                    calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    calendar.set(Calendar.MINUTE, minute);
                    String formattedTime = new SimpleDateFormat("hh:mm a", Locale.getDefault()).format(calendar.getTime());
                    task.setTime(formattedTime);
                    FileHelper.writedata(getAllTasks(), getContext());
                    notifyDataSetChanged();
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                false
        );
        timePickerDialog.show();
    }

    private ArrayList<Task> getAllTasks() {
        ArrayList<Task> all = new ArrayList<>();
        for (int i = 0; i < getCount(); i++) {
            all.add(getItem(i));
        }
        return all;
    }
}

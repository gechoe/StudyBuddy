package com.example.studybuddy;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class StudyHomePage extends AppCompatActivity {
    int MENU_ID = 10, SETTINGS_ID = 11;
    private Button startStopButton;
    private Button resetButton;
    private TextView timerTextView;
    private CountDownTimer countDownTimer;
    private boolean timerRunning;
    private long timeLeftInMillis = 1800000; // 30 minutes
    private List<Task> taskList;
    private EditText editTextTask;
    private DisappearingRingView disappearingRingView;
    private Handler handler = new Handler();
    private int incrPoint = 1;
    private User currUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.study);

        // Setting up user accounts
        CurrentUser current = new CurrentUser();
        currUser = current.getCurrentUser();

        disappearingRingView = findViewById(R.id.disappearingRingView);

        startStopButton = findViewById(R.id.startStopButton);
        resetButton = findViewById(R.id.resetButton);
        timerTextView = findViewById(R.id.timerTextView);

        // Initialize the user's taskList if it's null
        if (taskList == null) {
            taskList = new ArrayList<>();
        }

        // Load tasks from db
        taskList = currUser.getTasks();

        // Checks if user pressed start or stop for the study timer on the home page
        startStopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (timerRunning) {
                    stopTimer();
                } else {
                    startTimer();
                }
            }
        });

        // Check if user pressed reset timer for hte study timer on the home page
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetTimer();
            }
        });

        // Updates the timer with the correct time/tracking
        updateTimer();

        editTextTask = findViewById(R.id.editTextTask);
        Button buttonAdd = findViewById(R.id.buttonAdd);

        // Keeps track of the logged in user's tasks list
        // Checks if a task is created, saved, or marked as done
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String taskName = editTextTask.getText().toString().trim();

                if (!taskName.isEmpty()) {
                    addTask(taskName);
                    editTextTask.setText("");
                    currUser.setTasks(taskList);
                    current.saveUserData(currUser);
                }
            }
        });

        disappearingRingView.startAnimation(timeLeftInMillis);

        // Display tasks in the taskListContainer
        displayTasks();
    }

    // On click, leads to Menu page
    public void onMenuButtonClick(View v) {
        Intent i = new Intent(this, Menu.class);

        // Pass intent to activity, using specified request code
        startActivityForResult(i, MENU_ID);
        setResult(RESULT_OK);
        finish();
    }

    // On click, leads to Settings page
    public void onSettingButtonClick(View v) {
        Intent i = new Intent(this, Settings.class);

        // Pass intent to activity, using specified request code
        startActivityForResult(i, SETTINGS_ID);
        setResult(RESULT_OK);
        finish();
    }

    // Starts the timer
    private void startTimer() {
        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateTimer();
                disappearingRingView.startAnimation(timeLeftInMillis); // Update the circle bar
            }

            @Override
            public void onFinish() {
                timerRunning = false;
                startStopButton.setText("Start");
                disappearingRingView.stopAnimation();
            }
        }.start();

        timerRunning = true;
        // Renames the button to say stop, to let the user stop the timer
        startStopButton.setText("Stop");
    }

    // Stops the timer
    private void stopTimer() {
        countDownTimer.cancel();
        timerRunning = false;
        startStopButton.setText("Start");
        disappearingRingView.stopAnimation();
    }

    // Resets the timer back to 30 minutes
    private void resetTimer() {
        if (timerRunning) {
            stopTimer();
        }

        timeLeftInMillis = 1800000; // Reset time to 30 minutes
        updateTimer();

        disappearingRingView.startAnimation(timeLeftInMillis);
    }

    // Updates the timer, based on countdown time passed
    private void updateTimer() {
        int minutes = (int) (timeLeftInMillis / 1000) / 60;
        int seconds = (int) (timeLeftInMillis / 1000) % 60;

        String timeLeftFormatted = String.format("%02d:%02d", minutes, seconds);
        timerTextView.setText(timeLeftFormatted);

        if (timeLeftFormatted == "00:00") {
            disappearingRingView.makeRingDisappear();
        }
    }

    // Adds a task to the logged in user's tasks list
    private void addTask(String taskName) {
        // Create a new Task object with the provided name
        Task newTask = new Task(taskName);

        // Add the new task to the task list
        taskList.add(newTask);

        CurrentUser current = new CurrentUser();
        currUser.setTasks(taskList);  // Save task list to db
        current.saveUserData(currUser);  // Save the updated points to SharedPreferences

        // Update the UI to display the new task
        displayTasks();
    }

    // Method to remove the task from the tasks list
    private void removeTask(Task task) {
        taskList.remove(task);

        CurrentUser current = new CurrentUser();

        // Increment user's points when a task is removed
        if (currUser != null) {
            currUser.incrementPoints(incrPoint); // Adjust the points as needed
            currUser.setTasks(taskList);
            current.saveUserData(currUser); // Save the updated points and tasks to db
        }

        displayTasks();
    }

    // Method to display the user's tasks list
    // So that when they logg in again the created tasks are still there.
    private void displayTasks() {
        LinearLayout taskListContainer = findViewById(R.id.taskListContainer);
        taskListContainer.removeAllViews(); // Clear previous views

        for (Task task : taskList) {
            // Inflate the task item layout
            View taskItemView = getLayoutInflater().inflate(R.layout.task_item, null);

            // Find views within the inflated layout
            TextView textViewTask = taskItemView.findViewById(R.id.textViewTask);

            // Set task name to the TextView
            textViewTask.setText(task.getName());

            // Add the inflated view to the taskListContainer
            taskListContainer.addView(taskItemView);

            CheckBox checkBoxTask = taskItemView.findViewById(R.id.checkBoxTask);

            // Set click listener for the checkbox
            checkBoxTask.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        // If checkbox is checked, post a delayed action to remove the task
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                removeTask(task); // Call method to remove the task
                            }
                        }, 2000); // 3 seconds delay
                    }
                }
            });
        }
    }
}
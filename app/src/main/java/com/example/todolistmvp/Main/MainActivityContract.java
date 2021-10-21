package com.example.todolistmvp.Main;

import android.content.Intent;

import androidx.activity.result.ActivityResultLauncher;

import com.example.todolistmvp.Model.Task;

import java.util.List;

public interface MainActivityContract {
    interface View{
        void showEmptyList(boolean doShow);
        void showTasks(List<Task> task);
        void deleteTask(Task task);
        void deleteAllTasks();
        void updateTask(Task task);
    }
    interface Presentation{
        void onAttach(View view);
        void onDestroy();
        void onDeleteAllTasksClick();
        void searchTask(String query);
        void onDeleteTask(Task task);
        void onTaskClick(Task task);
        void onAddTaskClick(ActivityResultLauncher<Intent> activityResultLauncher);
    }
}

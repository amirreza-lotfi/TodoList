package com.example.todolistmvp.Main;

import android.content.Intent;

import androidx.activity.result.ActivityResultLauncher;

import com.example.todolistmvp.Detail.TaskDetailActivity;
import com.example.todolistmvp.Model.Task;
import com.example.todolistmvp.Model.TaskDao;

import java.util.List;

public class MainActivityPresentation implements MainActivityContract.Presentation {
    private TaskDao taskDao;
    private MainActivityContract.View view;

    MainActivityPresentation(TaskDao taskDao){
        this.taskDao = taskDao;
    }

    @Override
    public void onAttach(MainActivityContract.View view) {
        this.view = view;
        List<Task> allTaskAvailable = taskDao.getAll();
        view.showEmptyList(allTaskAvailable.size() == 0);
        view.showTasks(allTaskAvailable);

    }


    @Override
    public void onDeleteAllTasksClick() {
        this.taskDao.deleteAll();
        view.deleteAllTasks();
        view.showEmptyList(true);
    }

    @Override
    public void searchTask(String query) {
        List<Task> tasks;
        if(!query.isEmpty()) {
            tasks = taskDao.search(query);
        }else{
            tasks = taskDao.getAll();
        }

        view.showTasks(tasks);
        view.showEmptyList(tasks.size() == 0);
    }

    @Override
    public void onDeleteTask(Task task) {
        taskDao.delete(task);
        List<Task> tasks = taskDao.getAll();
        view.showEmptyList(tasks.size() == 0);
        view.deleteTask(task);
    }

    @Override
    public void onTaskClick(Task task) {
        task.setCompleted(!task.isCompleted());
        int result = taskDao.update(task);
        if (result > 0)
            view.updateTask(task);
    }

    @Override
    public void onAddTaskClick(ActivityResultLauncher<Intent> activityResultLauncher) {
        Intent intent = new Intent((MainActivity)view, TaskDetailActivity.class);
        activityResultLauncher.launch(intent);
    }

    @Override
    public void onDestroy() {

    }

}

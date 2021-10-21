package com.example.todolistmvp.Detail;

import android.view.View;

import com.example.todolistmvp.Model.Task;
import com.example.todolistmvp.Model.TaskDao;

public class TaskDetailPresenter implements TaskDetailActivityContracts.Presentation{

    private TaskDetailActivityContracts.View view;
    private TaskDao taskDao;
    /**
     *   if controlTask is null, its mean we are in add new task state,else we are in edit task mode
     */
    private Task controlTask;

    public TaskDetailPresenter(TaskDao taskDao, Task task){
        this.taskDao = taskDao;
        controlTask = task;

    }

    @Override
    public void onAttach(TaskDetailActivityContracts.View view) {
        this.view = view;
        if(controlTask!=null && controlTask.getTitle().length()!=0){
            view.showTasks(controlTask);
            view.setVisibilityOfDeleteIcon(View.VISIBLE);
            view.setActivityToolbarText("Edit Task");
        }
        else{
            view.setVisibilityOfDeleteIcon(View.GONE);
            view.setActivityToolbarText("Add Task");
        }
    }

    @Override
    public void saveChange(int importance, String title) {
        if(title.length()==0) {
            view.showError();
            return;
        }
        Task task = new Task();
        task.setTitle(title);
        task.setImportance(importance);
        task.setId(taskDao.add(task));

        view.addTaskResult(task);
    }

    @Override
    public void onControlInsert(int importance, String title) {
        if(controlTask==null){
            saveChange(importance,title);
        }
        else{
            Task updatedTask = new Task();
            updatedTask.setTitle(title);
            updatedTask.setImportance(importance);
            updatedTask.setId(controlTask.getId());
            taskDao.update(updatedTask);
            view.updateTask(updatedTask);
        }
    }

    @Override
    public void onDeleteIconClick(Task task) {
        view.deleteTaskResult(task);
    }


    @Override
    public void onDestroy() {

    }
}

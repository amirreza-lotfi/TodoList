package com.example.todolistmvp.Detail;

import com.example.todolistmvp.Main.MainActivityContract;
import com.example.todolistmvp.Model.Task;

public interface TaskDetailActivityContracts {
    interface View{
        void setVisibilityOfDeleteIcon(int visibility);
        void showTasks(Task task);
        void addTaskResult(Task task);
        void deleteTaskResult(Task task);
        void updateTask(Task task);
        void showError();
        void setActivityToolbarText(String title);
    }

    interface Presentation{
        void saveChange(int importance, String title);
        void onControlInsert(int importance, String title);
        void onDeleteIconClick(Task task);
        void onAttach(View view);
        void onDestroy();
    }
}

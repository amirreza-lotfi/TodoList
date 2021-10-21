package com.example.todolistmvp.Main;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.todolistmvp.Detail.TaskDetailActivity;
import com.example.todolistmvp.Model.AppDatabase;
import com.example.todolistmvp.Model.Task;
import com.example.todolistmvp.R;
import com.google.android.material.button.MaterialButton;

import java.util.List;

public class MainActivity extends AppCompatActivity implements MainActivityContract.View,TaskAdapter.TaskItemEventListener {
    public static final int DELETE = -1;
    public static final int UPDATE = 1;
    public static final int ADD = 1;

    private ActivityResultLauncher<Intent> intentToTaskDetailActivity_toSave;
    private ActivityResultLauncher<Intent> intentToTaskDetailActivity_toEdit;
    private MainActivityPresentation mainActivityPresentation;
    private TaskAdapter taskAdapter;
    private RecyclerView recyclerView;

    private View addTaskButton;
    private MaterialButton deleteAll;
    private EditText search;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setRecyclerView();

        setPresentLayout();

        intentToTaskDetailActivity_toEdit_Controller();
        intentToTaskDetailActivity_toSave_Controller();

        addTaskButtonClick();

        deleteAllButtonClick();

        searchBoxListener();
    }

    private void setRecyclerView(){
        recyclerView = findViewById(R.id.taskListRv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        taskAdapter = new TaskAdapter(this, this);
        recyclerView.setAdapter(taskAdapter);
    }

    private void setPresentLayout(){
        mainActivityPresentation = new MainActivityPresentation(AppDatabase.getAppDatabase(this).getTaskDao());
        mainActivityPresentation.onAttach(this);
    }

    private void intentToTaskDetailActivity_toSave_Controller(){
        intentToTaskDetailActivity_toSave = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if(result.getResultCode()==ADD && result.getData()!=null) {
                        Task task = result.getData().getParcelableExtra("addTask");
                        showEmptyList(false);
                        taskAdapter.addItem(task);
                    }
                });
    }

    private void intentToTaskDetailActivity_toEdit_Controller(){
        intentToTaskDetailActivity_toEdit = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if(result.getResultCode()==DELETE) {
                        Task resultTask = result.getData().getParcelableExtra("deleteTask");
                        mainActivityPresentation.onDeleteTask(resultTask);
                        taskAdapter.deleteItem(resultTask);
                        showEmptyList(taskAdapter.getItemCount()==0);
                    }
                    if(result.getResultCode()==UPDATE){
                        Task resultTask = result.getData().getParcelableExtra("updateTask");
                        taskAdapter.updateItem(resultTask);
                    }
                }
        );
    }

    @Override
    public void onClick(Task task) {
        mainActivityPresentation.onTaskClick(task);
    }

    @Override
    public void onLongClick(Task task) {
        Intent intent = new Intent(MainActivity.this, TaskDetailActivity.class);
        intent.putExtra("editTask", task);
        intentToTaskDetailActivity_toEdit.launch(intent);
    }

    private void addTaskButtonClick(){
        addTaskButton = findViewById(R.id.addNewTaskBtn);
        addTaskButton.setOnClickListener(view -> mainActivityPresentation.onAddTaskClick(intentToTaskDetailActivity_toSave));
    }

    private void deleteAllButtonClick(){
        deleteAll = findViewById(R.id.deleteAllBtn);
        deleteAll.setOnClickListener(view -> mainActivityPresentation.onDeleteAllTasksClick());
    }

    private void searchBoxListener(){
        search = findViewById(R.id.searchEt);
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mainActivityPresentation.searchTask(charSequence.toString());

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    public void showEmptyList(boolean doShow) {
        LinearLayout emptyState = findViewById(R.id.emptyState);
        if(doShow)
            emptyState.setVisibility(View.VISIBLE);
        else
            emptyState.setVisibility(View.GONE);
    }

    @Override
    public void showTasks(List<Task> task) {
        taskAdapter.setTasks(task);
    }

    @Override
    public void deleteTask(Task task) {
        taskAdapter.deleteItem(task);
    }

    @Override
    public void deleteAllTasks() {
        taskAdapter.clearItems();
    }

    @Override
    public void updateTask(Task task) {
        taskAdapter.updateItem(task);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mainActivityPresentation.onDestroy();
    }


}
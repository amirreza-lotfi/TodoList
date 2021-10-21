package com.example.todolistmvp.Detail;

import androidx.activity.result.ActivityResult;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.todolistmvp.Main.MainActivity;
import com.example.todolistmvp.Model.AppDatabase;
import com.example.todolistmvp.Model.Task;
import com.example.todolistmvp.R;


public class TaskDetailActivity extends AppCompatActivity implements TaskDetailActivityContracts.View{
    private int selectedImportance = Task.IMPORTANCE_NORMAL;

    private ImageView lastSelectedImportanceIv;
    private TaskDetailPresenter taskDetailPresenter;
    private EditText titleEditText;
    private ImageView deleteButton;
    private Button saveChanges;
    private View backBtn;

    private View normalImportanceBtn;
    private View highImportanceBtn;
    private View lowImportanceBtn;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_edit);
        titleEditText = findViewById(R.id.taskEt);

        setPresenterLayout();

        deleteButtonClick();
        backButtonClick();
        saveButtonClick();

        normalImportanceButton();
        highImportanceButton();
        lowImportanceButton();
    }

    private void setPresenterLayout(){
        taskDetailPresenter = new TaskDetailPresenter(AppDatabase.getAppDatabase(this).getTaskDao(),getIntent().getParcelableExtra("editTask"));
        taskDetailPresenter.onAttach(this);
    }

    private void normalImportanceButton(){
        normalImportanceBtn = findViewById(R.id.normalImportanceBtn);
        normalImportanceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedImportance != Task.IMPORTANCE_NORMAL) {
                    lastSelectedImportanceIv.setImageResource(0);
                    ImageView imageView = v.findViewById(R.id.normalImportanceCheckIv);
                    imageView.setImageResource(R.drawable.ic_check_white_24dp);
                    selectedImportance = Task.IMPORTANCE_NORMAL;

                    lastSelectedImportanceIv = imageView;
                }
            }
        });
        lastSelectedImportanceIv = normalImportanceBtn.findViewById(R.id.normalImportanceCheckIv);
    }
    private void highImportanceButton(){
        highImportanceBtn = findViewById(R.id.highImportanceBtn);
        highImportanceBtn.setOnClickListener(v -> {
            if (selectedImportance != Task.IMPORTANCE_HIGH) {
                lastSelectedImportanceIv.setImageResource(0);
                ImageView imageView = v.findViewById(R.id.highImportanceCheckIv);
                imageView.setImageResource(R.drawable.ic_check_white_24dp);
                selectedImportance = Task.IMPORTANCE_HIGH;

                lastSelectedImportanceIv = imageView;
            }
        });
    }
    private void lowImportanceButton(){
        lowImportanceBtn = findViewById(R.id.lowImportanceBtn);
        lowImportanceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedImportance != Task.IMPORTANCE_LOW) {
                    lastSelectedImportanceIv.setImageResource(0);
                    ImageView imageView = v.findViewById(R.id.lowImportanceCheckIv);
                    imageView.setImageResource(R.drawable.ic_check_white_24dp);
                    selectedImportance = Task.IMPORTANCE_LOW;

                    lastSelectedImportanceIv = imageView;
                }
            }
        });
    }

    private void deleteButtonClick(){
        deleteButton = findViewById(R.id.deleteTaskBtn);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = getIntent();
                Task task1 = intent1.getParcelableExtra("editTask");
                taskDetailPresenter.onDeleteIconClick(task1);
            }
        });

    }
    private void backButtonClick(){
        backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    private void saveButtonClick(){
        saveChanges = findViewById(R.id.saveChangesBtn);
        saveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                taskDetailPresenter.onControlInsert(selectedImportance,titleEditText.getText().toString());
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void setVisibilityOfDeleteIcon(int visibility) {
        ImageView deleteBtn = findViewById(R.id.deleteTaskBtn);
        deleteBtn.setVisibility(visibility);
    }

    @Override
    public void showTasks(Task task) {
        titleEditText.setText(task.getTitle());
        selectedImportance = task.getImportance();
    }

    @Override
    public void addTaskResult(Task task) {
        Intent intent = new Intent();
        intent.putExtra("addTask",task);
        setResult(MainActivity.ADD,intent);
        finish();
    }

    @Override
    public void deleteTaskResult(Task task) {
        Intent intent = new Intent();
        intent.putExtra("deleteTask",task);
        setResult(MainActivity.DELETE,intent);
        finish();
    }

    @Override
    public void updateTask(Task task) {
        Intent intent = new Intent();
        intent.putExtra("updateTask",task);
        setResult(MainActivity.UPDATE,intent);
        finish();
    }

    @Override
    public void showError() {
        Toast.makeText(this,"please insert a text",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setActivityToolbarText(String title) {
        TextView toolbarTitleTv = findViewById(R.id.toolbarTitleTv);
        toolbarTitleTv.setText(title);
    }

}

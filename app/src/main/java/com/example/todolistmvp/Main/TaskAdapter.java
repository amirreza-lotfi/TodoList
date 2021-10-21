package com.example.todolistmvp.Main;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;


import com.example.todolistmvp.Model.Task;
import com.example.todolistmvp.R;

import java.util.ArrayList;
import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {
    private List<Task> tasks = new ArrayList<>();
    private TaskItemEventListener eventListener;
    private Drawable highImportantDrawable;
    private Drawable normalImportantDrawable;
    private Drawable lowImportantDrawable;

    public TaskAdapter(Context context, TaskItemEventListener eventListener) {

        this.eventListener = eventListener;
        highImportantDrawable = ResourcesCompat.getDrawable(context.getResources(), R.drawable.shape_importance_high_rect, null);
        normalImportantDrawable = ResourcesCompat.getDrawable(context.getResources(), R.drawable.shape_importance_normal_rect, null);
        lowImportantDrawable = ResourcesCompat.getDrawable(context.getResources(), R.drawable.shape_importance_low_rect, null);
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TaskViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_task, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        holder.bindTask(tasks.get(position));
    }

    public void addItem(Task task) {
        tasks.add(0, task);
        notifyItemInserted(0);
    }

    public void updateItem(Task task) {
        for (int i = 0; i < tasks.size(); i++) {
            if (task.getId() == tasks.get(i).getId()) {
                tasks.set(i, task);
                notifyItemChanged(i);
            }
        }
    }

    public void addItems(List<Task> tasks) {
        this.tasks.addAll(tasks);
        notifyDataSetChanged();
    }

    public void deleteItem(Task task) {
        for (int i = 0; i < tasks.size(); i++) {
            if (tasks.get(i).getId() == task.getId()) {
                tasks.remove(i);
                notifyItemRemoved(i);
                break;
            }
        }
    }

    public void clearItems() {
        this.tasks.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return this.tasks.size();
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
        notifyDataSetChanged();
    }

    public class TaskViewHolder extends RecyclerView.ViewHolder {
        private TextView titleTv;
        private ImageView checkBoxIv;
        private View importanceView;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTv = itemView.findViewById(R.id.taskCheckBox);
            checkBoxIv = itemView.findViewById(R.id.checkBoxIv);
            importanceView = itemView.findViewById(R.id.importanceView);
        }

        public void bindTask(final Task task) {
            titleTv.setText(task.getTitle());
            if (task.isCompleted()) {
                checkBoxIv.setBackgroundResource(R.drawable.shape_checkbox_checked);
                checkBoxIv.setImageResource(R.drawable.ic_check_white_24dp);
                titleTv.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);

            } else {
                checkBoxIv.setImageResource(0);
                checkBoxIv.setBackgroundResource(R.drawable.shape_checkbox_default);
                titleTv.setPaintFlags(0);

            }

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    eventListener.onLongClick(task);
                    return false;
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    eventListener.onClick(task);
                }
            });


            switch (task.getImportance()) {
                case Task.IMPORTANCE_HIGH:
                    importanceView.setBackground(highImportantDrawable);
                    break;
                case Task.IMPORTANCE_LOW:
                    importanceView.setBackground(lowImportantDrawable);
                    break;
                case Task.IMPORTANCE_NORMAL:
                    importanceView.setBackground(normalImportantDrawable);
                    break;
            }


        }
    }


    public interface TaskItemEventListener {
        void onClick(Task task);

        void onLongClick(Task task);
    }


}

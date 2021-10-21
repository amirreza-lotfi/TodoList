package com.example.todolistmvp.Model;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TaskDao {

    @Insert
    long add(Task task);

    @Delete
    int delete(Task task);

    @Update
    int update(Task task);

    @Query("SELECT * FROM tbl_tasks ORDER BY id DESC")
    List<Task> getAll();

    @Query("SELECT * FROM tbl_tasks WHERE title LIKE '%' || :query || '%' ORDER BY id DESC")
    List<Task> search(String query);

    @Query("DELETE FROM tbl_tasks")
    void deleteAll();


}

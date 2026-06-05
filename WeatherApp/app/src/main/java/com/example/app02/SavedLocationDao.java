package com.example.app02;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;

@Dao
public interface SavedLocationDao {
    @Query("SELECT * FROM saved_locations")
    List<SavedLocation> getAll();

    @Insert
    void insert(SavedLocation location);

    @Delete
    void delete(SavedLocation location);
}
package com.example.alihandemir.androidmidterm;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Query;
import java.util.List;

@Dao
public interface ItemDao {

    @Query("Select * From items")
    List<ItemDetail> getAllUsers();

    @Delete
    void delete(List items);

}

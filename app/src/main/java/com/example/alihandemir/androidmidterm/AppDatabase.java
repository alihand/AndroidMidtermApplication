package com.example.alihandemir.androidmidterm;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.annotation.NonNull;
import android.os.AsyncTask;


@Database(entities = {ItemDetail.class} , version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

   private static AppDatabase instance;

    public abstract ItemDao itemDao();

    public static synchronized AppDatabase getInstance(Context context){
        if(instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(),AppDatabase.class,"my_db")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask(instance).execute();
        }
    };


    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {
        private ItemDao itemDao;

        private PopulateDbAsyncTask(AppDatabase db) {
            itemDao = db.itemDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            return null;
        }
    }
}

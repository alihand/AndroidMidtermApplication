package com.example.alihandemir.androidmidterm;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import static com.example.alihandemir.androidmidterm.R.id;
import static com.example.alihandemir.androidmidterm.R.layout;

public class MainActivity extends AppCompatActivity implements ItemAdapter.OnItemClickListener{

    public static final String EXTRA_URL = "imageUrl";
    public static final String EXTRA_CREATOR = "creatorName";
    public static final String EXTRA_LIKES = "likeCount";

    private RecyclerView mRecyclerView;
    private ItemAdapter mItemAdapter;
    private List<ItemDetail> mItemList;
    private RequestQueue mRequestQueue;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.show_deleted,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.show_deleted) {
            Intent intent = new Intent(this,MainActivity.class);
            this.startActivity(intent);
            return true;
        }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_main);

        mRecyclerView = findViewById(id.recycler_view);

        AppDatabase db = Room.databaseBuilder(getApplicationContext(),AppDatabase.class,"my_db")
                .allowMainThreadQueries()
                .build();

         db.itemDao().getAllUsers();

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mItemList = new List<ItemDetail>();

        mRequestQueue = Volley.newRequestQueue(this);


        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT ) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                int position = viewHolder.getAdapterPosition();
                mItemAdapter.removeItem(position);

                Toast.makeText(MainActivity.this,
                        "Item Deleted", Toast.LENGTH_LONG)
                        .show();

            }
        }).attachToRecyclerView(mRecyclerView);

        parseJSON();
    }

    private void parseJSON() {
        String url = "https://pixabay.com/api/?key=9745639-19ef2fadb8edd9c8f561f20c7&q=familys&image_type=photo&pretty=true";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("hits");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject hit = jsonArray.getJSONObject(i);
                                String creatorName = hit.getString("user");
                                String imageUrl = hit.getString("webformatURL");
                                int likeCount = hit.getInt("likes");
                                mItemList.add(new ItemDetail(imageUrl, creatorName, likeCount));
                            }

                            mItemAdapter = new ItemAdapter(MainActivity.this, mItemList);
                            mRecyclerView.setAdapter(mItemAdapter);
                            mItemAdapter.setOnItemClickListener(MainActivity.this);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        mRequestQueue.add(request);
    }

    @Override
    public void onItemClick(int position) {
        Intent detailIntent = new Intent(this,DetailActivity.class);
        ItemDetail clickedItem = mItemList.get(position);

        detailIntent.putExtra(EXTRA_URL, clickedItem.getImageUrl());
        detailIntent.putExtra(EXTRA_CREATOR, clickedItem.getCreator());
        detailIntent.putExtra(EXTRA_LIKES, clickedItem.getLikes());

        startActivity(detailIntent);
    }
}

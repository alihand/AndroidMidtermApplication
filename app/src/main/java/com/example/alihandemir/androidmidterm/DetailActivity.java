package com.example.alihandemir.androidmidterm;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import android.os.AsyncTask;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.os.SystemClock;

import static com.example.alihandemir.androidmidterm.MainActivity.EXTRA_CREATOR;
import static com.example.alihandemir.androidmidterm.MainActivity.EXTRA_LIKES;
import static com.example.alihandemir.androidmidterm.MainActivity.EXTRA_URL;

public class DetailActivity extends AppCompatActivity {

    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        progressBar = (ProgressBar) findViewById(R.id.progress);
        Intent intent = getIntent();
        String imageUrl = intent.getStringExtra(EXTRA_URL);
        String creatorName = intent.getStringExtra(EXTRA_CREATOR);
        int likeCount = intent.getIntExtra(EXTRA_LIKES, 0);

        ImageView imageView = findViewById(R.id.image_view_detail);
        TextView textViewCreator = findViewById(R.id.text_view_creator_detail);
        TextView textViewLikes = findViewById(R.id.text_view_like_detail);

        Picasso.get()
                .load(imageUrl)
                .fit()
                .centerInside()
                .into(imageView);

        textViewCreator.setText(creatorName);
        textViewLikes.setText("Likes: " + likeCount);

        new ProgressTask().execute();
    }

    public class ProgressTask extends AsyncTask<Void, Integer, Void> {

        int myProgressCount;

        @Override
        protected Void doInBackground(Void... voids) {
            while (myProgressCount < 100) {
                myProgressCount++;
                publishProgress(myProgressCount);
                SystemClock.sleep(10);
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            Toast.makeText(DetailActivity.this,
                    "onPreExecute Start Progress Bar", Toast.LENGTH_LONG)
                    .show();
            progressBar.setProgress(0);
            myProgressCount = 0;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            progressBar.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Toast.makeText(DetailActivity.this, "onPostExecute End Progress Bar",
                    Toast.LENGTH_LONG).show();
        }
    }
}

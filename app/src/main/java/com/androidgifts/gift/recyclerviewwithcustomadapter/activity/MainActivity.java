package com.androidgifts.gift.recyclerviewwithcustomadapter.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.androidgifts.gift.recyclerviewwithcustomadapter.R;
import com.androidgifts.gift.recyclerviewwithcustomadapter.adapter.MovieRecyclerviewAdapter;
import com.androidgifts.gift.recyclerviewwithcustomadapter.data.Movie;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MovieRecyclerviewAdapter movieRecyclerviewAdapter;

    private List<Movie> movies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        movies = new ArrayList<>();

        recyclerView = (RecyclerView) findViewById(R.id.movies_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        movies = getData();

        movieRecyclerviewAdapter = new MovieRecyclerviewAdapter(this, movies);
        recyclerView.setAdapter(movieRecyclerviewAdapter);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this, recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Movie thisMovie = movies.get(position);

                Toast.makeText(getApplicationContext(), thisMovie.getName() + " is clicked !", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClick(View view, int position) {

                Movie thisMovie = movies.get(position);

                Toast.makeText(getApplicationContext(), thisMovie.getName() + " is Long Clicked !", Toast.LENGTH_SHORT).show();

            }
        }));
    }

    private List<Movie> getData() {
        List<Movie> movies = new ArrayList<>();

        String names[] = {"The Look Of Silence", "Ant Man", "Minions", "Trainwreck"};
        String ratings[] = {"10.15.2015", "10.15.2015", "10.15.2015", "10.15.2015"};
        String logos[] = {"look_of_silence", "ant_man", "minions", "trainwreck"};

        for (int i = 0; i < names.length; i++) {
            Movie newMovie = new Movie(names[i], logos[i], ratings[i]);

            movies.add(newMovie);
        }

        return movies;
    }

    class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ClickListener clickListener) {

            this.clickListener = clickListener;

            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener(){
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null){
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)){
                clickListener.onClick(child, rv.getChildPosition(child));
            }

            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {

        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }

    public interface ClickListener {
        public void onClick(View view, int position);

        public void onLongClick(View view, int position);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //https://www.youtube.com/watch?v=yEWHBDpVPUc
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }
}

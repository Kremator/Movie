package com.androidgifts.gift.recyclerviewwithcustomadapter.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import com.androidgifts.gift.recyclerviewwithcustomadapter.Constant;
import com.androidgifts.gift.recyclerviewwithcustomadapter.R;
import com.androidgifts.gift.recyclerviewwithcustomadapter.adapter.MovieRecyclerviewAdapter;
import com.androidgifts.gift.recyclerviewwithcustomadapter.data.Movie;
import com.squareup.picasso.Picasso;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MovieRecyclerviewAdapter movieRecyclerviewAdapter;
    ArrayList<Movie> mPopularList;
    ArrayList<Movie> mTopVotedList;
    final static String TAG = "Movie Results";
    final static String POP_LIST = "popList";
    final static String TOP_VOTE_LIST = "topVoteList";

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        outState.putSerializable(POP_LIST, mPopularList);
        outState.putSerializable(TOP_VOTE_LIST, mTopVotedList);
        super.onSaveInstanceState(outState);

    }


    // Loading SavedInstance
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);


        // Loading Saved data
        mPopularList = (ArrayList<Movie>) savedInstanceState.getSerializable(POP_LIST);
        mTopVotedList = (ArrayList<Movie>) savedInstanceState.getSerializable(TOP_VOTE_LIST);
        loadPreferenceList();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mPopularList == null || mTopVotedList == null){ // Checking to see if data is present before loading
            if (NetworkConnections.networkcheck(MainActivity.this)) {
                new MainSync().execute();
            } else {
                AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                dialog.setTitle(getString(R.string.network_alert_title));
                dialog.setMessage(getString(R.string.network_alert_message));
                dialog.setCancelable(false);
                dialog.show();
            }
        } else {
            loadPreferenceList();
        }

       // recyclerView.setOnItemClickListener(MainActivity.this);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        recyclerView = (RecyclerView) findViewById(R.id.movies_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mPopularList = getData();

        movieRecyclerviewAdapter = new MovieRecyclerviewAdapter(this, mPopularList);
        recyclerView.setAdapter(movieRecyclerviewAdapter);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this, recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Movie thisMovie = mPopularList.get(position);

                Toast.makeText(getApplicationContext(), thisMovie.getName() + " is clicked !", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClick(View view, int position) {

                Movie thisMovie = mPopularList.get(position);

                Toast.makeText(getApplicationContext(), thisMovie.getName() + " is Long Clicked !", Toast.LENGTH_SHORT).show();

            }
        }));
    }

    private ArrayList<Movie> getData() {
        ArrayList<Movie> movies = new ArrayList<>();

        String names[] = {"The Look Of Silence", "Ant Man", "Minions", "Trainwreck"};
        String ratings[] = {"10.15.2015", "10.15.2015", "10.15.2015", "10.15.2015"};
        String logos[] = {"look_of_silence", "ant_man", "minions", "trainwreck"};

        for (int i = 0; i < names.length; i++) {
            Movie newMovie = new Movie();

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
    public class MainSync extends AsyncTask<Void, Void, Void> {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(MainActivity.this);
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

            dialog.setTitle("Loading...");
            dialog.show();

        }

        @Override
        protected Void doInBackground(Void... params) {



            String WebAddress;
            String WebAddressVote;


            WebAddress = "http://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&api_key="
                    + Constant.API_KEY;

            WebAddressVote = "http://api.themoviedb.org/3/discover/movie?sort_by=vote_average.desc&api_key="
                    + Constant.API_KEY;

            mPopularList = new ArrayList<>();
            mTopVotedList = new ArrayList<>();

            URLResult(WebAddress, mPopularList);
            URLResult(WebAddressVote, mTopVotedList);

            mPopularList.toString();


            return null;
        }


        @Override
        protected void onPostExecute(Void  s) {
            super.onPostExecute(s);
            loadPreferenceList();





            dialog.cancel();

        }
    }

    private void loadPreferenceList() {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        if (sharedPreferences.getString("ORG_PREF_LIST", "popular").equals("popular")) {
            loadMovieAdapter(mPopularList);
        } else {
            loadMovieAdapter(mTopVotedList);

        }
    }

    private void loadMovieAdapter(ArrayList<Movie> _list) {
        MovieRecyclerviewAdapter adapter = new MovieRecyclerviewAdapter(MainActivity.this,
                _list);
        recyclerView.setAdapter(adapter);
    }

    private void URLResult(String webAddress, ArrayList<Movie> _List) {
        try {

            URL url = new URL(webAddress);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();

            InputStream inputStream = connection.getInputStream();
            String results = IOUtils.toString(inputStream);
            jsonParser(results, _List);
            inputStream.close();

        } catch (IOException e) {
            e.printStackTrace();


        }
    }

    private void jsonParser(String s, ArrayList<Movie> movies) {
        try {
            JSONObject mainObject = new JSONObject(s);

            JSONArray resultsArray = mainObject.getJSONArray("results");
            for (int i = 0; i < resultsArray.length(); i++) {
                JSONObject indexObject = resultsArray.getJSONObject(i);
                Movie indexMovie = new Movie();
                indexMovie.setBackdropPath(indexObject.getString("backdrop_path"));
                indexMovie.setId(indexObject.getInt("id"));
                indexMovie.setOriginalTitel(indexObject.getString("original_title"));
                indexMovie.setOverview(indexObject.getString("overview"));
                indexMovie.setRelaseDate(indexObject.getString("release_date"));
                indexMovie.setPosterPath(indexObject.getString("poster_path"));
                indexMovie.setPopularity(indexObject.getDouble("popularity"));
                indexMovie.setTitle(indexObject.getString("title"));
                indexMovie.setAverage(indexObject.getInt("vote_average"));
                indexMovie.setCount(indexObject.getInt("vote_count"));

                movies.add(indexMovie);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(TAG, "JSON Error", e);
        }
    }


}

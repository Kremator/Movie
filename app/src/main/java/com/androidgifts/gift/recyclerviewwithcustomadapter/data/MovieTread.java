package com.androidgifts.gift.recyclerviewwithcustomadapter.data;

import android.os.AsyncTask;

import com.androidgifts.gift.recyclerviewwithcustomadapter.Constant;
import com.androidgifts.gift.recyclerviewwithcustomadapter.activity.MainActivity;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Stas on 29.06.2016.
 */
public class MovieTread extends AsyncTask<Void,Void,Void> {


        ArrayList<Movie> mPopularList;
        ArrayList<Movie> mTopVotedList;
        @Override
        protected Void doInBackground(Void... params) {
            String WebAddress;
            String WebAddressVote;


            WebAddress = "http://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&api_key="+ Constant.API_KEY;
            WebAddressVote = "http://api.themoviedb.org/3/discover/movie?sort_by=vote_average.desc&api_key="+Constant.API_KEY;
            mPopularList = new ArrayList<>();
            mTopVotedList = new ArrayList<>();

           URLResult(WebAddress, mPopularList);
            URLResult(WebAddressVote, mTopVotedList);

            mPopularList.toString();
            return null;

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            MainActivity a=new MainActivity();
//            a.loadPreferenceList();
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
//                   Movie indexMovie = new Movie();
//                    indexMovie.setBackdropPath(indexObject.getString("backdrop_path"));
//                   indexMovie.setId(indexObject.getInt("id"));
//                   indexMovie.setOriginalTitel(indexObject.getString("original_title"));
//                   indexMovie.setOverview(indexObject.getString("overview"));
//                    indexMovie.setRelaseDate(indexObject.getString("release_date"));
//                    indexMovie.setPosterPath(indexObject.getString("poster_path"));
//                    indexMovie.setPopularity(indexObject.getDouble("popularity"));
//                    indexMovie.setTitle(indexObject.getString("title"));
//                    indexMovie.setAverage(indexObject.getInt("vote_average"));
//                    indexMovie.setCount(indexObject.getInt("vote_count"));
//
//                   movies.add(indexMovie);
                }
           } catch (JSONException e) {
                e.printStackTrace();

            }
        }


    }


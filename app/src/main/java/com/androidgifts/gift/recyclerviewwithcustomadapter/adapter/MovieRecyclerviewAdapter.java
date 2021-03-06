package com.androidgifts.gift.recyclerviewwithcustomadapter.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidgifts.gift.recyclerviewwithcustomadapter.Constant;
import com.androidgifts.gift.recyclerviewwithcustomadapter.R;
import com.androidgifts.gift.recyclerviewwithcustomadapter.data.Movie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MovieRecyclerviewAdapter extends RecyclerView.Adapter<MovieRecyclerviewAdapter.MovieViewHolder> {


    ArrayList<Movie> movies ;
    LayoutInflater layoutInflater;
    Context context;
        Uri uri;
    public MovieRecyclerviewAdapter(Context context, ArrayList<Movie> movies){
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        this.movies = movies;
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View row = layoutInflater.inflate(R.layout.single_movie_row, parent, false);
        MovieViewHolder holder = new MovieViewHolder(row);

        return holder;
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        Movie thisMovie = movies.get(position);

        holder.movieName.setText(thisMovie.getOriginalTitel());
        holder.movieRating.setText("Release date: " + thisMovie.getRelaseDate());
      uri= Uri.parse("http://image.tmdb.org/t/p/w342/"+thisMovie.getBackdropPath());
        Context context=holder.movieLogo.getContext();
       Picasso.with(context).load(uri)
               .into(holder.movieLogo);

    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder {

        private TextView movieName;
        private TextView movieRating;
        private ImageView movieLogo;

        public MovieViewHolder(View itemView) {
            super(itemView);

            movieName = (TextView) itemView.findViewById(R.id.movie_name);
            movieRating = (TextView) itemView.findViewById(R.id.release_date);
            movieLogo = (ImageView) itemView.findViewById(R.id.movie_logo);
        }
    }
}

package com.movie.heaven.details;

import android.view.LayoutInflater;
import android.view.View;
import com.movie.heaven.R;
import com.movie.heaven.data.models.MovieDetails;

import android.view.ViewGroup;

import androidx.leanback.widget.Presenter;

public class MovieDetailsDescriptionPresenter extends Presenter {

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.vh_movie_details, parent, false);
        return new MovieDetailsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Object item) {
        MovieDetails movie = (MovieDetails) item;
        MovieDetailsViewHolder holder = (MovieDetailsViewHolder) viewHolder;
        holder.bind(movie);
    }

    @Override
    public void onUnbindViewHolder(ViewHolder viewHolder) {

    }
}
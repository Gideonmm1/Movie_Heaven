package com.movie.heaven;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import butterknife.BindView;
import butterknife.ButterKnife;

import com.movie.heaven.data.models.Movie;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.movie.heaven.dagger.modules.HttpClientModule;
import com.movie.heaven.BindableCardView;
import java.util.Locale;

import static com.movie.heaven.R.*;

public class MovieCardView extends BindableCardView<Movie> {

    @BindView(R.id.poster_iv) ImageView mPosterIV;

    @BindView(R.id.vote_average_tv) TextView mVoteAverageTV;

    public MovieCardView(Context context) {
        super(context);
        ButterKnife.bind(this);
    }

    @Override
    protected void bind(Movie data) {
        Glide.with(getContext())
                .load(HttpClientModule.POSTER_URL + data.getPosterPath())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(mPosterIV);


        mVoteAverageTV.setText(String.format(Locale.getDefault(), "%.2f", data.getVoteAverage()));
    }

    public ImageView getPosterIV() {
        return mPosterIV;
    }

    @Override
    protected int getLayoutResource() {
        return layout.moviecardview;
    }

}
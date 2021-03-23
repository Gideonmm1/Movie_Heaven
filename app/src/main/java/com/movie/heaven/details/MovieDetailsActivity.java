package com.movie.heaven.details;

import android.os.Bundle;

import androidx.core.content.ContextCompat;

import com.movie.heaven.BaseTvActivity;
import com.movie.heaven.R;
import com.movie.heaven.GlideBackgroundManager;
import com.movie.heaven.dagger.modules.HttpClientModule;
import com.movie.heaven.data.models.Movie;

public class MovieDetailsActivity extends BaseTvActivity {

    GlideBackgroundManager mBackgroundManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Retrieve the movie through the intent
        Movie movie = getIntent().getExtras().getParcelable(Movie.class.getSimpleName());
        MovieDetailsFragment detailsFragment = MovieDetailsFragment.newInstance(movie);
        addFragment(detailsFragment); // Method from BaseTvActivity

        // Sets the background of the activity to the backdrop of the movie
        mBackgroundManager = new GlideBackgroundManager(this);
        if (movie != null && movie.getBackdropPath() != null) {
            mBackgroundManager.loadImage(HttpClientModule.BACKDROP_URL + movie.getBackdropPath());
        } else {
            mBackgroundManager.setBackground(ContextCompat.getDrawable(this, R.drawable.material_bg));
        }
    }
}
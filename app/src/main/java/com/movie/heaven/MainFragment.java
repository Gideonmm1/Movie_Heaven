package com.movie.heaven;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;

import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.ContextCompat;
import androidx.leanback.app.BrowseFragment;
import androidx.leanback.widget.ArrayObjectAdapter;
import androidx.leanback.widget.HeaderItem;
import androidx.leanback.widget.ListRow;
import androidx.leanback.widget.ListRowPresenter;
import androidx.leanback.widget.OnItemViewClickedListener;
import androidx.leanback.widget.OnItemViewSelectedListener;
import androidx.leanback.widget.Presenter;
import androidx.leanback.widget.Row;
import androidx.leanback.widget.RowPresenter;

import dagger.android.AndroidInjection;
import dagger.android.support.AndroidSupportInjection;
import io.reactivex.android.schedulers.AndroidSchedulers;
import com.movie.heaven.dagger.modules.HttpClientModule;
import com.movie.heaven.data.Api.TheMovieDbAPI;
import com.movie.heaven.data.models.Movie;
import com.movie.heaven.data.models.MovieResponse;
import com.movie.heaven.details.MovieDetailsActivity;
import com.movie.heaven.details.MovieDetailsFragment;

import javax.inject.Inject;

import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;


public class MainFragment extends BrowseFragment implements OnItemViewSelectedListener, OnItemViewClickedListener {
        @Inject
        TheMovieDbAPI mDbAPI;

        private GlideBackgroundManager mBackgroundManager;

        private static final int NOW_PLAYING = 0;
        private static final int TOP_RATED = 1;
        private static final int POPULAR = 2;
        private static final int UPCOMING = 3;

        SparseArray<MovieRow> mRows;

        public static MainFragment newInstance() {
            Bundle args = new Bundle();
            MainFragment fragment = new MainFragment();
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            //AndroidInjection.inject(this);
            App.instance().appComponent().inject(this);
            Timber.i(String.valueOf(App.instance()));
            mBackgroundManager = new GlideBackgroundManager(getActivity());

            // Add code here for initialization of HeadersFragment and badge title
            setBrandColor(ContextCompat.getColor(getActivity(), R.color.primary_transparent));
            setHeadersState(HEADERS_ENABLED);
            setHeadersTransitionOnBackEnabled(true);

            // The TMDB logo on the right corner. It is necessary to show based on their API usage policy
            setBadgeDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.powered_by));

            createDataRows();
            createRows();
            prepareEntranceTransition();
            fetchNowPlayingMovies();
            fetchTopRatedMovies();
            fetchPopularMovies();
            fetchUpcomingMovies();
        }

        /**
         * Creates the data rows objects
         */
        private void createDataRows() {
            mRows = new SparseArray<>();
            MoviePresenter moviePresenter = new MoviePresenter();
            mRows.put(NOW_PLAYING, new MovieRow()
                    .setId(NOW_PLAYING)
                    .setAdapter(new ArrayObjectAdapter(moviePresenter))
                    .setTitle("Now Playing")
                    .setPage(1)
            );
            mRows.put(TOP_RATED, new MovieRow()
                    .setId(TOP_RATED)
                    .setAdapter(new ArrayObjectAdapter(moviePresenter))
                    .setTitle("Top Rated")
                    .setPage(1)
            );
            mRows.put(POPULAR, new MovieRow()
                    .setId(POPULAR)
                    .setAdapter(new ArrayObjectAdapter(moviePresenter))
                    .setTitle("Popular")
                    .setPage(1)
            );
            mRows.put(UPCOMING, new MovieRow()
                    .setId(UPCOMING)
                    .setAdapter(new ArrayObjectAdapter(moviePresenter))
                    .setTitle("Upcoming")
                    .setPage(1)
            );
        }

        /**
         * Creates the rows and sets up the adapter of the fragment
         */
        private void createRows() {
            ArrayObjectAdapter rowsAdapter = new ArrayObjectAdapter(new ListRowPresenter());
            for (int i = 0; i < mRows.size(); i++) {
                MovieRow row = mRows.get(i);
                // Adds a new ListRow to the adapter. Each row will contain a collection of Movies
                // That will be rendered using the MoviePresenter
                HeaderItem headerItem = new HeaderItem(row.getId(), row.getTitle());
                ListRow listRow = new ListRow(headerItem, row.getAdapter());
                rowsAdapter.add(listRow);
            }
            // Sets this fragments Adapter.
            // The setAdapter method is defined in the BrowseFragment of the Leanback Library
            setAdapter(rowsAdapter);
            setOnItemViewSelectedListener(this);
            setOnItemViewClickedListener((OnItemViewClickedListener) this);
        }

    // Implement the onItemClicked required by the OnItemViewClickedListener interface
    @Override
    public void onItemClicked(Presenter.ViewHolder itemViewHolder, Object item, RowPresenter.ViewHolder rowViewHolder, Row row) {
        if (item instanceof Movie) {
            Movie movie = (Movie) item;
            Intent i = new Intent(getActivity(), MovieDetailsActivity.class);
            // Pass the movie to the activity
            i.putExtra(Movie.class.getSimpleName(), movie);

            if (itemViewHolder.view instanceof MovieCardView) {
                // Pass the ImageView to allow a nice transition
                Bundle bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        getActivity(),
                        ((MovieCardView) itemViewHolder.view).getPosterIV(),
                        MovieDetailsFragment.TRANSITION_NAME).toBundle();
                getActivity().startActivity(i, bundle);
            } else {
                startActivity(i);
            }
        }
    }

        /**
         * Fetches now playing movies from TMDB
         */
        private void fetchNowPlayingMovies() {
            mDbAPI.getNowPlayingMovies(config.API_KEY_URL, mRows.get(NOW_PLAYING).getPage())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<MovieResponse>() {
                                   @Override
                                   public void accept(MovieResponse response) throws Exception {
                                       MainFragment.this.bindMovieResponse(response, NOW_PLAYING);
                                       MainFragment.this.startEntranceTransition();
                                   }
                               },
                            new Consumer<Throwable>() {
                                @Override
                                public void accept(Throwable e) throws Exception {
                                    Timber.e(e, "Error fetching now playing movies: %s", e.getMessage());
                                }
                            });
        }

        /**
         * Fetches the popular movies from TMDB
         */
        private void fetchPopularMovies() {
            mDbAPI.getPopularMovies(config.API_KEY_URL, mRows.get(POPULAR).getPage())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<MovieResponse>() {
                        @Override
                        public void accept(MovieResponse response) throws Exception {
                            MainFragment.this.bindMovieResponse(response, POPULAR);
                            MainFragment.this.startEntranceTransition();
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable e) throws Exception {
                            Timber.e(e, "Error fetching popular movies: %s", e.getMessage());
                        }
                    });
        }

        /**
         * Fetches the upcoming movies from TMDB
         */
        private void fetchUpcomingMovies() {
            mDbAPI.getUpcomingMovies(config.API_KEY_URL, mRows.get(UPCOMING).getPage())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<MovieResponse>() {
                        @Override
                        public void accept(MovieResponse response) throws Exception {
                            MainFragment.this.bindMovieResponse(response, UPCOMING);
                            MainFragment.this.startEntranceTransition();
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable e) throws Exception {
                            Timber.e(e, "Error fetching upcoming movies: %s", e.getMessage());
                        }
                    });
        }

        /**
         * Fetches the top rated movies from TMDB
         */
        private void fetchTopRatedMovies() {
            mDbAPI.getTopRatedMovies(config.API_KEY_URL, mRows.get(TOP_RATED).getPage())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<MovieResponse>() {
                        @Override
                        public void accept(MovieResponse response) throws Exception {
                            MainFragment.this.bindMovieResponse(response, TOP_RATED);
                            MainFragment.this.startEntranceTransition();
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable e) throws Exception {
                            Timber.e(e, "Error fetching top rated movies: %s", e.getMessage());
                        }
                    });
        }

        /**
         * Binds a movie response to it's adapter
         * @param response
         *      The response from TMDB API
         * @param id
         *      The ID / position of the row
         */
        private void bindMovieResponse(MovieResponse response, int id) {
            MovieRow row = mRows.get(id);
            row.setPage(row.getPage() + 1);
            for(Movie m : response.getResults()) {
                if (m.getPosterPath() != null) { // Avoid showing movie without posters
                    row.getAdapter().add(m);
                }
            }
        }

    @Override
    public void onItemSelected(Presenter.ViewHolder itemViewHolder, Object item, RowPresenter.ViewHolder rowViewHolder, Row row) {
        // Check if the item is a movie
        if (item instanceof Movie) {
            Movie movie = (Movie) item;
            // Check if the movie has a backdrop
            if(movie.getBackdropPath() != null) {
                mBackgroundManager.loadImage(HttpClientModule.BACKDROP_URL + movie.getBackdropPath());
            } else {
                // If there is no backdrop for the movie we just use a default one
                mBackgroundManager.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.material_bg));
            }
        }
    }
}
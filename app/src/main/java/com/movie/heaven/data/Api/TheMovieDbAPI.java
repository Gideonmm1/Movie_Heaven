package com.movie.heaven.data.Api;


import com.movie.heaven.dagger.modules.HttpClientModule;
import com.movie.heaven.data.models.CreditsResponse;
import com.movie.heaven.data.models.MovieDetails;
import com.movie.heaven.data.models.MovieResponse;
import com.movie.heaven.data.models.Person;
import com.movie.heaven.data.models.VideoResponse;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface TheMovieDbAPI {

    @GET(HttpClientModule.NOW_PLAYING)
    Observable<MovieResponse> getNowPlayingMovies(
            @Query("api_key") String apiKey,
            @Query("page") int page
    );

    @GET(HttpClientModule.TOP_RATED)
    Observable<MovieResponse> getTopRatedMovies(
            @Query("api_key") String apiKey,
            @Query("page") int page
    );

    @GET(HttpClientModule.UPCOMING)
    Observable<MovieResponse> getUpcomingMovies(
            @Query("api_key") String apiKey,
            @Query("page") int page
    );

    @GET(HttpClientModule.POPULAR)
    Observable<MovieResponse> getPopularMovies(
            @Query("api_key") String apiKey,
            @Query("page") int page
    );

    @GET(HttpClientModule.MOVIE + "{id}/similar")
    Observable<MovieResponse> getSimilarMovies(
            @Path("id") String movieId,
            @Query("api_key") String apiKey
    );

    @GET(HttpClientModule.MOVIE + "{id}/recommendations")
    Observable<MovieResponse> getRecommendations(
            @Path("id") String movieId,
            @Query("api_key") String apiKey
    );

    @GET(HttpClientModule.MOVIE + "{id}/credits")
    Observable<CreditsResponse> getCredits(
            @Path("id") String movieId,
            @Query("api_key") String apiKey
    );

    @GET(HttpClientModule.MOVIE + "{id}")
    Observable<MovieDetails> getMovieDetails(
            @Path("id") String movieId,
            @Query("api_key") String apiKey
    );

    @GET(HttpClientModule.MOVIE + "{id}/videos")
    Observable<VideoResponse> getMovieVideos(
            @Path("id") String movieId,
            @Query("api_key") String apiKey
    );

    @GET(HttpClientModule.PERSON + "{id}")
    Observable<Person> getPerson(
            @Path("id") int personId,
            @Query("api_key") String apiKey
    );

    @GET(HttpClientModule.DISCOVER)
    Observable<MovieResponse> getMoviesForCastID(
            @Query("with_cast") int castId,
            @Query("api_key") String apiKey
    );

    @GET(HttpClientModule.SEARCH_MOVIE)
    Observable<MovieResponse> searchMovies(
            @Query("query") String query,
            @Query("api_key") String apiKey
    );

}

package com.movie.heaven.dagger.components;

import com.movie.heaven.MainFragment;
import com.movie.heaven.dagger.AppScope;
import com.movie.heaven.dagger.modules.HttpClientModule;
import com.movie.heaven.App;
import javax.inject.Singleton;

import dagger.Component;

import com.movie.heaven.dagger.modules.ApplicationModule;
import com.movie.heaven.details.MovieDetailsFragment;

@AppScope
@Singleton
@Component(modules = {
        ApplicationModule.class,
        HttpClientModule.class,
})
public interface ApplicationComponent {

    void inject(App app);
    void inject(MainFragment mainFragment);
    void inject(MovieDetailsFragment movieDetailsFragment);
}
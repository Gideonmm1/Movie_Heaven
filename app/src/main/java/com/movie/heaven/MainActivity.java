package com.movie.heaven;

import android.app.Activity;
import android.os.Bundle;

/*
 * Main Activity class that loads {@link MainFragment}.
 */
public class MainActivity extends BaseTvActivity  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addFragment(MainFragment.newInstance());
    }
}

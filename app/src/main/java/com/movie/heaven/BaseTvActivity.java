package com.movie.heaven;


import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;

import com.movie.heaven.R;


public class BaseTvActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
    }

    public void addFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.tv_frame_content, fragment);
        fragmentTransaction.commit();
    }

}

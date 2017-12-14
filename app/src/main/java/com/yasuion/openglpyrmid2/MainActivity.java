package com.yasuion.openglpyrmid2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.yasuion.openglpyrmid2.view.FGLView;

public class MainActivity extends AppCompatActivity {


    private FGLView fglView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fglView = (FGLView) findViewById(R.id.fglview);
    }

    @Override
    protected void onPause() {
        super.onPause();
        fglView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        fglView.onResume();
    }
}

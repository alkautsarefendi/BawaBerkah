package org.bawaberkah.app.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import org.bawaberkah.app.R;

public class KebijakanPrivacyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kebijakan_privacy);
    }

    public void goBack(View view) {
        onBackPressed();
    }
}

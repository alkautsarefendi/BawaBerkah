package org.bawaberkah.app.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import org.bawaberkah.app.R;

public class LayananPemulazaranJenazahActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layanan_pemulazaran_jenazah);
    }

    public void goBack(View view) {
        onBackPressed();
    }
}

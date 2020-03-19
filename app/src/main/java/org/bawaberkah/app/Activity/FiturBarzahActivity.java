package org.bawaberkah.app.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.bawaberkah.app.R;


public class FiturBarzahActivity extends AppCompatActivity {

    TextView linkLayananMobilJenazah, linkPemulazaranJenazah, linkPemulazaranJenazahLengkap, linkDonasiPerawatanMobilJenazah;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fitur_barzah);

        linkLayananMobilJenazah = findViewById(R.id.linkLayananMobilJenazah);
        linkPemulazaranJenazah = findViewById(R.id.linkPemulazaranJenazah);
        linkPemulazaranJenazahLengkap = findViewById(R.id.linkPemulazaranJenazahLengkap);
        linkDonasiPerawatanMobilJenazah = findViewById(R.id.linkDonasiPerawatanMobilJenazah);

        linkLayananMobilJenazah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FiturBarzahActivity.this, LayananMobilJenazahActivity.class));
            }
        });

        linkPemulazaranJenazah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FiturBarzahActivity.this, LayananPemulazaranJenazahActivity.class));
            }
        });

        linkPemulazaranJenazahLengkap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FiturBarzahActivity.this, LayananPemulazaranJenazahLengkapActivity.class));
            }
        });

        linkDonasiPerawatanMobilJenazah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FiturBarzahActivity.this, DonasiMobilJenazahActivity.class));
            }
        });

    }

    public void goBack(View view) {
        onBackPressed();
    }

}

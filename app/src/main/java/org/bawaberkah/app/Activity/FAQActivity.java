package org.bawaberkah.app.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;


import org.bawaberkah.app.R;
import org.bawaberkah.app.Util.ExpandableTextView;

public class FAQActivity extends AppCompatActivity {

    String faq1, faq2, faq3, faq4, faq5, faq6, faq7, faq8, faq9, faq10, faq11, faq12,
            faq13, faq14, faq15, faq16, faq17, faq18, faq19, faq20, faq21, faq22, faq23;
    ExpandableTextView judul1, judul2, judul3, judul4, judul5, judul6, judul7, judul8,
            judul9, judul10, judul11, judul12, judul13, judul14, judul15, judul16, judul17,
            judul18, judul19, judul20, judul21, judul22, judul23;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq);

        faq1 = getString(R.string.faq1);
        faq2 = getString(R.string.faq2);
        faq3 = getString(R.string.faq3);
        faq4 = getString(R.string.faq4);
        faq5 = getString(R.string.faq5);
        faq6 = getString(R.string.faq6);
        faq7 = getString(R.string.faq7);
        faq8 = getString(R.string.faq8);
        faq9 = getString(R.string.faq9);
        faq10 = getString(R.string.faq10);
        faq11 = getString(R.string.faq11);
        faq12 = getString(R.string.faq12);
        faq13 = getString(R.string.faq13);
        faq14 = getString(R.string.faq14);
        faq15 = getString(R.string.faq15);
        faq16 = getString(R.string.faq16);
        faq17 = getString(R.string.faq17);
        faq18 = getString(R.string.faq18);
        faq19 = getString(R.string.faq19);
        faq20 = getString(R.string.faq20);
        faq21 = getString(R.string.faq21);
        faq22 = getString(R.string.faq22);
        faq23 = getString(R.string.faq23);

        judul1 = findViewById(R.id.faq1);
        judul2 = findViewById(R.id.faq2);
        judul3 = findViewById(R.id.faq3);
        judul4 = findViewById(R.id.faq4);
        judul5 = findViewById(R.id.faq5);
        judul6 = findViewById(R.id.faq6);
        judul7 = findViewById(R.id.faq7);
        judul8 = findViewById(R.id.faq8);
        judul9 = findViewById(R.id.faq9);
        judul10 = findViewById(R.id.faq10);
        judul11 = findViewById(R.id.faq11);
        judul12 = findViewById(R.id.faq12);
        judul13 = findViewById(R.id.faq13);
        judul14 = findViewById(R.id.faq14);
        judul15 = findViewById(R.id.faq15);
        judul16 = findViewById(R.id.faq16);
        judul17 = findViewById(R.id.faq17);
        judul18 = findViewById(R.id.faq18);
        judul19 = findViewById(R.id.faq19);
        judul20 = findViewById(R.id.faq20);
        judul21 = findViewById(R.id.faq21);
        judul22 = findViewById(R.id.faq22);
        judul23 = findViewById(R.id.faq23);


        judul1.setText(faq1);
        judul2.setText(faq2);
        judul3.setText(faq3);
        judul4.setText(faq4);
        judul5.setText(faq5);
        judul6.setText(faq6);
        judul7.setText(faq7);
        judul8.setText(faq8);
        judul9.setText(faq9);
        judul10.setText(faq10);
        judul11.setText(faq11);
        judul12.setText(faq12);
        judul13.setText(faq13);
        judul14.setText(faq14);
        judul15.setText(faq15);
        judul16.setText(faq16);
        judul17.setText(faq17);
        judul18.setText(faq18);
        judul19.setText(faq19);
        judul20.setText(faq20);
        judul21.setText(faq21);
        judul22.setText(faq22);
        judul23.setText(faq23);
    }

    public void goBack(View view){ onBackPressed(); }

}

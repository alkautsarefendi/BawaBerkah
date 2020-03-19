package org.bawaberkah.app.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.github.aakira.expandablelayout.ExpandableRelativeLayout;

import org.bawaberkah.app.Fragment.TransaksiFragment;
import org.bawaberkah.app.R;

public class DonasiMobilJenazahActivity extends AppCompatActivity {

    Button btnExpandSinopsisDonasiBarzah, btnExpandUpdateDonasiBarzah, btnExpandDonaturDonasiBarzah, btnDonasiBarzah;
    ExpandableRelativeLayout expandSinopsisDonasiBarzah, expandUpdateDonasiBarzah, expandDonaturDonasiBarzah;
    TextView lblSinopsisDonasiBarzah, lblUpdateDonasiBarzah;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donasi_mobil_jenazah);

        lblSinopsisDonasiBarzah = findViewById(R.id.lblSinopsisDonasiBarzah);
        lblUpdateDonasiBarzah = findViewById(R.id.lblUpdateDonasiBarzah);
        expandSinopsisDonasiBarzah = findViewById(R.id.expandSinopsisDonasiBarzah);
        expandUpdateDonasiBarzah = findViewById(R.id.expandUpdateDonasiBarzah);
        expandDonaturDonasiBarzah = findViewById(R.id.expandDonaturDonasiBarzah);
        btnExpandSinopsisDonasiBarzah = findViewById(R.id.btnExpandSinopsisDonasiBarzah);
        btnExpandUpdateDonasiBarzah = findViewById(R.id.btnExpandUpdateDonasiBarzah);
        btnExpandDonaturDonasiBarzah = findViewById(R.id.btnExpandDonaturDonasiBarzah);
        btnDonasiBarzah = findViewById(R.id.btnDonasiBarzah);
        recyclerView = findViewById(R.id.lvlDonaturDonasiBarzah);

        lblSinopsisDonasiBarzah.setMovementMethod(new ScrollingMovementMethod());
        lblUpdateDonasiBarzah.setMovementMethod(new ScrollingMovementMethod());

        btnExpandSinopsisDonasiBarzah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expandSinopsisDonasiBarzah.toggle();
            }
        });

        btnExpandUpdateDonasiBarzah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expandUpdateDonasiBarzah.toggle();
            }
        });

        btnExpandDonaturDonasiBarzah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expandDonaturDonasiBarzah.toggle();
            }
        });

        btnDonasiBarzah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TransaksiFragment bottomSheetFragment = new TransaksiFragment();
                Bundle data = new Bundle();
                /*data.putString("idCampaign",id);*/
                data.putString("namaCampaign", "Donasi Perawatan Mobil Jenazah");
                data.putString("kdTransaksi", "barzah");
                bottomSheetFragment.setArguments(data);
                bottomSheetFragment.show(getSupportFragmentManager(), bottomSheetFragment.getTag());
            }
        });

    }

    public void goBack(View view) {
        onBackPressed();
    }
}

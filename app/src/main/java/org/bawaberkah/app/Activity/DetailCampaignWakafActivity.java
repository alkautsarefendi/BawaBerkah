package org.bawaberkah.app.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.github.aakira.expandablelayout.ExpandableRelativeLayout;

import org.bawaberkah.app.Adapter.DonaturAdapter;
import org.bawaberkah.app.Fragment.TransaksiFragment;
import org.bawaberkah.app.Model.DonaturModel;
import org.bawaberkah.app.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.ArrayList;

public class DetailCampaignWakafActivity extends AppCompatActivity {

    public static final String EXTRA_ID = "id";
    public static final String EXTRA_IMAGE_CAMPAIGN = "path_campaign";
    public static final String EXTRA_IMAGE_CAMPAIGNER = "path_campaigner";
    public static final String EXTRA_JUDUL = "judul";
    public static final String EXTRA_TERKUMPUL = "terkumpul";
    public static final String EXTRA_LOKASI = "lokasi";
    public static final String EXTRA_CAMPAIGNER = "campaigner";
    public static final String EXTRA_NOM_DONATUR = "jumlah_donatur";
    public static final String EXTRA_START = "start";
    public static final String EXTRA_END = "end";

    ImageView imageCampaignWakaf, imgYayasanWakaf;
    TextView txtJudulCampaignWakaf, txtNamaYayasanWakaf, txtLokasiYayasanWakaf, txtDanaTerkumpulWakaf,
            txtNomDonaturWakaf, lblSinopsisWakaf, lblUpdateWakaf ;
    ProgressBar progressDanaCampaignWakaf;
    ExpandableRelativeLayout expandSinopsisWakaf, expandDonaturWakaf, expandUpdateWakaf;
    Button btnExpandSinopsisWakaf, btnExpandUpdateWakaf, btnExpandDonaturWakaf, btnPayWakaf;
    private int percentage;
    private String judul, terkumpul, path, pathCampaign, start, end, id, campaigner, lokasi, jumDonatur, sinopsis;
    private static final String USER = "bawaberkahudin";
    private static final String PASS = "17aug1945";

    private ArrayList<DonaturModel> itemArrayList;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_campaign_wakaf);

        imageCampaignWakaf = findViewById(R.id.imageCampaignWakaf);
        txtJudulCampaignWakaf = findViewById(R.id.txtJudulCampaignWakaf);
        imgYayasanWakaf = findViewById(R.id.imgYayasanWakaf);
        txtNamaYayasanWakaf = findViewById(R.id.txtNamaYayasanWakaf);
        txtLokasiYayasanWakaf = findViewById(R.id.txtLokasiYayasanWakaf);
        txtDanaTerkumpulWakaf = findViewById(R.id.txtDanaTerkumpulWakaf);
        progressDanaCampaignWakaf = findViewById(R.id.progressDanaCampaignWakaf);
        txtNomDonaturWakaf = findViewById(R.id.txtNomDonaturWakaf);
        expandSinopsisWakaf = findViewById(R.id.expandSinopsisWakaf);
        expandDonaturWakaf = findViewById(R.id.expandDonaturWakaf);
        expandUpdateWakaf = findViewById(R.id.expandUpdateWakaf);
        btnExpandSinopsisWakaf = findViewById(R.id.btnExpandSinopsisWakaf);
        btnExpandUpdateWakaf = findViewById(R.id.btnExpandUpdateWakaf);
        btnExpandDonaturWakaf = findViewById(R.id.btnExpandDonaturWakaf);
        btnPayWakaf = findViewById(R.id.btnPayWakaf);
        lblSinopsisWakaf = findViewById(R.id.lblSinopsisWakaf);
        lblUpdateWakaf = findViewById(R.id.lblUpdateWakaf);
        recyclerView = findViewById(R.id.lvlDonaturWakaf);

        lblSinopsisWakaf.setMovementMethod(new ScrollingMovementMethod());
        lblUpdateWakaf.setMovementMethod(new ScrollingMovementMethod());

        itemArrayList = new ArrayList();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        btnExpandSinopsisWakaf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expandSinopsisWakaf.toggle();
            }
        });
        btnExpandUpdateWakaf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expandUpdateWakaf.toggle();
            }
        });
        btnExpandDonaturWakaf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expandDonaturWakaf.toggle();
            }
        });

        Intent intent = getIntent();
        id = intent.getStringExtra(EXTRA_ID);
        judul = intent.getStringExtra(EXTRA_JUDUL);
        terkumpul = intent.getStringExtra(EXTRA_TERKUMPUL);
        path = intent.getStringExtra(EXTRA_IMAGE_CAMPAIGN);
        pathCampaign = intent.getStringExtra(EXTRA_IMAGE_CAMPAIGNER);
        start = intent.getStringExtra(EXTRA_START);
        end = intent.getStringExtra(EXTRA_END);
        campaigner = intent.getStringExtra(EXTRA_CAMPAIGNER);
        lokasi = intent.getStringExtra(EXTRA_LOKASI);
        jumDonatur = intent.getStringExtra(EXTRA_NOM_DONATUR);

        System.out.println("idURL = "+ id);
        System.out.println("judul = "+ judul);
        System.out.println("terkumpul = "+ terkumpul);
        System.out.println("path = "+ path);
        System.out.println("pathCampaign = "+ pathCampaign);
        System.out.println("start = "+ start);
        System.out.println("end = "+ end);
        System.out.println("campaigner = "+ campaigner);
        System.out.println("lokasi = "+ lokasi);
        System.out.println("jumDonatur = "+ jumDonatur);

        //Set Currency Format String
        NumberFormat nf = NumberFormat.getCurrencyInstance();
        DecimalFormatSymbols decimalFormatSymbols = ((DecimalFormat) nf).getDecimalFormatSymbols();
        decimalFormatSymbols.setCurrencySymbol("Rp ");
        ((DecimalFormat) nf).setDecimalFormatSymbols(decimalFormatSymbols);
        /*Double danaTerkumpul = Double.parseDouble(terkumpul);
        String jumTerkumpul = nf.format(danaTerkumpul).trim();*/

        Glide.with(DetailCampaignWakafActivity.this).load(path).into(imageCampaignWakaf);
        Glide.with(DetailCampaignWakafActivity.this).load(pathCampaign).into(imgYayasanWakaf);
        txtJudulCampaignWakaf.setText(judul);
        txtNamaYayasanWakaf.setText(campaigner);
        txtLokasiYayasanWakaf.setText(lokasi);
        txtDanaTerkumpulWakaf.setText("Rp. "+terkumpul);
        txtNomDonaturWakaf.setText(jumDonatur);

        btnPayWakaf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TransaksiFragment bottomSheetFragment = new TransaksiFragment();
                Bundle data = new Bundle();
                data.putString("idCampaign",id);
                data.putString("namaCampaign", judul);
                data.putString("kdTransaksi", "wakaf");
                bottomSheetFragment.setArguments(data);
                bottomSheetFragment.show(getSupportFragmentManager(), bottomSheetFragment.getTag());

            }
        });

        getSinopsis();
        getUpdate();
        getDonatur();

    }

    private void getDonatur() {

        String URL_TOTAL_DONASI = getString(R.string.url_header)+"/v1/apps/campaigndonates/"+id+"?username="+USER+"&password="+PASS;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_TOTAL_DONASI,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.d("UPDATE", ">>" + response);
                            //converting the string to json array object
                            JSONObject jObj = new JSONObject(response);
                            String arrDonasi = jObj.getString("donates");
                            JSONArray array = new JSONArray(arrDonasi);
                            for (int i = 0; i < array.length(); i++){
                                JSONObject objDonasi = array.getJSONObject(i);
                                String catatan = objDonasi.getString("catatan");
                                String id_campaign = objDonasi.getString("id_campaign");
                                String id_donatur = objDonasi.getString("id_donatur");
                                String nama_donatur = objDonasi.getString("nama_donatur");
                                String nominal = objDonasi.getString("nominal");

                                if (nominal.contains(".")){
                                    nominal = nominal.replace(".","");
                                } else if (nominal.contains(",")){
                                    nominal = nominal.replace(",","");
                                }

                                Double a = Double.parseDouble(nominal);
                                //Set Currency Format String
                                NumberFormat nf = NumberFormat.getCurrencyInstance();
                                DecimalFormatSymbols decimalFormatSymbols = ((DecimalFormat) nf).getDecimalFormatSymbols();
                                decimalFormatSymbols.setCurrencySymbol("Rp ");
                                ((DecimalFormat) nf).setDecimalFormatSymbols(decimalFormatSymbols);

                                String nominalDonasi = nf.format(a).trim();

                                if (nominalDonasi.contains(".00")){
                                    nominalDonasi = nominalDonasi.substring(0, nominalDonasi.length() - 3);
                                    System.out.println("UPDATE = "+nominalDonasi);
                                }

                                DonaturModel donaturM = new DonaturModel(catatan,id_campaign,id_donatur,nama_donatur,nominal);
                                itemArrayList.add(donaturM);
                            }

                            DonaturAdapter adapter = new DonaturAdapter(DetailCampaignWakafActivity.this, itemArrayList);
                            adapter.notifyDataSetChanged();
                            recyclerView.setAdapter(adapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        //adding our stringrequest to queue
        Volley.newRequestQueue(DetailCampaignWakafActivity.this).add(stringRequest);

    }

    private void getUpdate() {

        String URL_TOTAL_DONASI = getString(R.string.url_header)+"/v1/apps/campaignupdate/"+id+"?username="+USER+"&password="+PASS;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_TOTAL_DONASI,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.d("UPDATE", ">>" + response);
                            //converting the string to json array object
                            JSONObject jObj = new JSONObject(response);
                            String update = jObj.getString("campaign");

                            System.out.println("UPDATE = "+update);
                            lblUpdateWakaf.setText(update);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        //adding our stringrequest to queue
        Volley.newRequestQueue(DetailCampaignWakafActivity.this).add(stringRequest);

    }

    private void getSinopsis() {

        String URL_TOTAL_DONASI = getString(R.string.url_header)+"/v1/apps/campaign/"+id+"?username="+USER+"&password="+PASS;
        try {
            StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_TOTAL_DONASI,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                Log.d("DETAIL", ">>" + response);
                                //converting the string to json array object
                                JSONObject jObj = new JSONObject(response);
                                String campaign = jObj.getString("campaign");
                                JSONObject objCampaign = new JSONObject(campaign);
                                sinopsis = objCampaign.getString("sinopsis");

                                System.out.println("sinopsis = "+ sinopsis);
                                lblSinopsisWakaf.setText(sinopsis);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    });

            //adding our stringrequest to queue
            Volley.newRequestQueue(DetailCampaignWakafActivity.this).add(stringRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void goBack(View view) {
        onBackPressed();
    }
}

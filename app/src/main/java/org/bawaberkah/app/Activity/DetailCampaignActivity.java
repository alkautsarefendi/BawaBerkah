package org.bawaberkah.app.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
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
import com.google.android.gms.common.SignInButton;

import org.bawaberkah.app.Adapter.DonaturAdapter;
import org.bawaberkah.app.Fragment.TransaksiFragment;
import org.bawaberkah.app.Model.DonaturModel;
import org.bawaberkah.app.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static org.bawaberkah.app.Activity.SearchActivity.EXTRA_URL;

public class DetailCampaignActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    public static final String EXTRA_ID = "id";
    private static final String TAG = "AndroidClarified";
    private SignInButton googleSignInButton;
    Button btnExpandSinopsis, btnExpandUpdate, btnExpandDonatur;
    ExpandableRelativeLayout expandSinopsis, expandDonatur, expandUpdate;
    TextView lblSinopsis, lblUpdate, judulCampaign, txtTerkumpul, txtTarget, txtpercentase, txtLama, txtNamaYayasan, txtId, txtLokasiYayasan;
    String id, gambar, sinopsis, sisaHari, path, id_campaign, judul_campaign;
    ImageView imageCampaign;
    ProgressBar progressDanaCampaign;
    private ArrayList<DonaturModel> itemArrayList;
    private RecyclerView recyclerView;

    //Declare Params for MySQL Connection
    private static final String USER = "bawaberkahudin";
    private static final String PASS = "17aug1945";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_campaign);

        //Declare Items
        imageCampaign = (ImageView) findViewById(R.id.imageCampaign);
        judulCampaign = (TextView) findViewById(R.id.txtJudulCampaign);
        txtTerkumpul = (TextView) findViewById(R.id.txtDanaTerkumpul);
        txtTarget = (TextView) findViewById(R.id.txtDanaTarget);
        txtpercentase = (TextView) findViewById(R.id.txtJumlahProgress);
        txtLama = (TextView) findViewById(R.id.txtSisaCampaign);
        txtNamaYayasan = findViewById(R.id.txtNamaYayasan);
        txtLokasiYayasan = findViewById(R.id.txtLokasiYayasan);
        recyclerView = findViewById(R.id.lvlDonatur);
        Button btnDonasi = findViewById(R.id.btnOk);
        btnExpandSinopsis = findViewById(R.id.btnExpandSinopsis);
        btnExpandDonatur = findViewById(R.id.btnExpandDonatur);
        btnExpandUpdate = findViewById(R.id.btnExpandUpdate);
        expandSinopsis = findViewById(R.id.expandSinopsis);
        expandUpdate = findViewById(R.id.expandUpdate);
        expandDonatur = findViewById(R.id.expandDonatur);
        lblSinopsis = findViewById(R.id.lblSinopsis);
        lblUpdate = findViewById(R.id.lblUpdate);
        progressDanaCampaign = findViewById(R.id.progressDanaCampaign);

        getDetail();
        getUpdate();
        getDonatur();

        itemArrayList = new ArrayList();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        lblSinopsis.setMovementMethod(new ScrollingMovementMethod());
        lblUpdate.setMovementMethod(new ScrollingMovementMethod());

        progressDanaCampaign.getProgressDrawable().setColorFilter(Color.MAGENTA, PorterDuff.Mode.SRC_IN);

        btnExpandSinopsis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expandSinopsis.toggle();
            }
        });
        btnExpandUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expandUpdate.toggle();
            }
        });
        btnExpandDonatur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expandDonatur.toggle();
            }
        });

        btnDonasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TransaksiFragment bottomSheetFragment = new TransaksiFragment();
                Bundle data = new Bundle();
                data.putString("idCampaign",id_campaign);
                data.putString("namaCampaign", judul_campaign);
                data.putString("kdTransaksi", "donasi");
                bottomSheetFragment.setArguments(data);
                bottomSheetFragment.show(getSupportFragmentManager(), bottomSheetFragment.getTag());

            }
        });

    }

    private void getDonatur() {

        Intent intent = getIntent();
        id = intent.getStringExtra(EXTRA_ID);
        path = intent.getStringExtra(EXTRA_URL);
        System.out.println("idURL = "+ id);

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
                                    System.out.println("Donatur = "+nominalDonasi);
                                }

                                DonaturModel donaturM = new DonaturModel(catatan,id_campaign,id_donatur,nama_donatur,nominal);
                                itemArrayList.add(donaturM);
                            }

                            DonaturAdapter adapter = new DonaturAdapter(DetailCampaignActivity.this, itemArrayList);
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
        Volley.newRequestQueue(DetailCampaignActivity.this).add(stringRequest);

    }

    private void getUpdate() {

        Intent intent = getIntent();
        id = intent.getStringExtra(EXTRA_ID);
        path = intent.getStringExtra(EXTRA_URL);
        System.out.println("idURL = "+ id);

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
                            lblUpdate.setText(update);

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
        Volley.newRequestQueue(DetailCampaignActivity.this).add(stringRequest);
    }

    private void getDetail() {
        Intent intent = getIntent();
        id = intent.getStringExtra(EXTRA_ID);
        path = intent.getStringExtra(EXTRA_URL);
        System.out.println("idURL = "+ id);

        String URL_TOTAL_DONASI = getString(R.string.url_header)+"/v1/apps/campaign/"+id+"?username=" + USER + "&password=" + PASS;
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
                                String campaigner = objCampaign.getString("campaigner");
                                String dana_target = objCampaign.getString("dana_target");
                                String dana_terkumpul = objCampaign.getString("dana_terkumpul");
                                id_campaign = objCampaign.getString("id_campaign");
                                judul_campaign = objCampaign.getString("judul_campaign");
                                String lokasi_campaigner = objCampaign.getString("lokasi_campaigner");
                                sinopsis = objCampaign.getString("sinopsis");
                                String tanggal_mulai = objCampaign.getString("tanggal_mulai");
                                String tanggal_selesai = objCampaign.getString("tanggal_selesai");
                                String arrImage = objCampaign.getString("gambar_campaign");
                                JSONArray gambarCampaign = new JSONArray(arrImage);
                                for (int i = 0; i < gambarCampaign.length(); i++) {
                                    JSONObject patchImage = gambarCampaign.getJSONObject(i);
                                    gambar = patchImage.getString("pathImage");
                                }

                                System.out.println("campaigner = "+ campaigner);
                                System.out.println("dana_target = "+ dana_target);
                                System.out.println("dana_terkumpul = "+ dana_terkumpul);
                                System.out.println("id_campaign = "+ id_campaign);
                                System.out.println("judul_campaign = "+ judul_campaign);
                                System.out.println("lokasi_campaigner = "+ lokasi_campaigner);
                                System.out.println("sinopsis = "+ sinopsis);
                                System.out.println("tanggal_mulai = "+ tanggal_mulai);
                                System.out.println("tanggal_selesai = "+ tanggal_selesai);
                                System.out.println("gambar = "+ gambar);

                                if (gambar == null){
                                    Glide.with(DetailCampaignActivity.this).load(path).into(imageCampaign);
                                    judulCampaign.setText(judul_campaign);
                                } else {
                                    Glide.with(DetailCampaignActivity.this).load(gambar).into(imageCampaign);
                                    judulCampaign.setText(judul_campaign);
                                }

                                if (dana_terkumpul.contains(".")){
                                    dana_terkumpul = dana_terkumpul.replace(".","");
                                } else if (dana_terkumpul.contains(",")){
                                    dana_terkumpul = dana_terkumpul.replace(",","");
                                }

                                Double terkumpul = Double.parseDouble(dana_terkumpul);
                                Double danaTarget = Double.parseDouble(dana_target);
                                double percentage = terkumpul / danaTarget *100.0D ;
                                int y = (int)percentage;
                                String perc = String.valueOf(y) + " %";

                                //Set Currency Format String
                                NumberFormat nf = NumberFormat.getCurrencyInstance();
                                DecimalFormatSymbols decimalFormatSymbols = ((DecimalFormat) nf).getDecimalFormatSymbols();
                                decimalFormatSymbols.setCurrencySymbol("Rp ");
                                ((DecimalFormat) nf).setDecimalFormatSymbols(decimalFormatSymbols);

                                String hasil = nf.format(terkumpul).trim();
                                String dana = nf.format(danaTarget).trim();

                                if (hasil.contains(".00")){
                                    hasil = hasil.substring(0, hasil.length() - 3);
                                }

                                //Current Date
                                Date current = new Date();
                                SimpleDateFormat dateAkhir = new SimpleDateFormat("yyyy-MM-dd");
                                String curDate = dateAkhir.format(current);
                                String tgl = tanggal_selesai;

                                //Campaign End Date
                                DateFormat dateAwal = new SimpleDateFormat("yyyy-MM-dd");
                                try {
                                    Date tglAwal = dateAwal.parse(curDate);
                                    Date tglAkhir = dateAkhir.parse(tgl);
                                    Date TGLAwal = tglAwal;
                                    Date TGLAkhir = tglAkhir;
                                    Calendar cal1 = Calendar.getInstance();
                                    cal1.setTime(TGLAwal);
                                    Calendar cal2 = Calendar.getInstance();
                                    cal2.setTime(TGLAkhir);

                                    sisaHari = String.valueOf(daysBetween(cal1, cal2));
                                } catch (ParseException e) {

                                }

                                txtTerkumpul.setText(hasil);
                                txtTarget.setText(dana);
                                txtpercentase.setText(perc);
                                txtLama.setText(sisaHari+" Hari");
                                progressDanaCampaign.setProgress(y);
                                txtNamaYayasan.setText(campaigner);
                                txtLokasiYayasan.setText(lokasi_campaigner);
                                lblSinopsis.setText(sinopsis);

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
            Volley.newRequestQueue(DetailCampaignActivity.this).add(stringRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static long daysBetween(Calendar tanggalAwal, Calendar tanggalAkhir) {
        long lama = 0;
        Calendar tanggal = (Calendar) tanggalAwal.clone();
        while (tanggal.before(tanggalAkhir)) {
            tanggal.add(Calendar.DAY_OF_MONTH, 1);
            lama++;
        }
        return lama;
    }


    public void goBack(View view) {
        onBackPressed();
    }
}

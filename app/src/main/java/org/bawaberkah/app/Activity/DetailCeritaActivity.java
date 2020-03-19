package org.bawaberkah.app.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;

import org.bawaberkah.app.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class DetailCeritaActivity extends AppCompatActivity {

    public static final String EXTRA_ID = "id";
    public static final String EXTRA_IMAGE_CAMPAIGN = "path_campaign";
    public static final String EXTRA_JUDUL = "judul";
    public static final String EXTRA_LOKASI = "lokasi";
    public static final String EXTRA_TGL = "tanggal";
    public static final String EXTRA_WAKTU = "waktu";

    private static final String USER = "bawaberkahudin";
    private static final String PASS = "17aug1945";

    HashMap<String, String> Hash_file_maps;

    TextView judulCerita, tanggal, waktu, lokasi, isiCerita, txtJudulCerita, judulCampaign;

    LinearLayout sliderDotspanel;
    private int dotscount;
    private ImageView[] dots;

    RequestQueue rq;
    private SliderLayout sliderLayout;
    private LinearLayout mLinearLayout;

    String gambarCerita, judul, tgl, jam, wil, idCerita;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_cerita);

        sliderLayout = findViewById(R.id.sliderCerita);
        mLinearLayout = findViewById(R.id.pagesContainer);
        judulCerita = findViewById(R.id.txtJudulCerita);
        tanggal = findViewById(R.id.txtTglCerita);
        waktu = findViewById(R.id.txtWaktuCerita);
        lokasi = findViewById(R.id.txtLokasiCerta);
        isiCerita = findViewById(R.id.txtCerita);
        judulCampaign = findViewById(R.id.lblJudulCampaign);

        txtJudulCerita = findViewById(R.id.lblJudulCerita);

        Intent intent = getIntent();
        idCerita = intent.getStringExtra(EXTRA_ID);
        judul = intent.getStringExtra(EXTRA_JUDUL);
        tgl = intent.getStringExtra(EXTRA_LOKASI);
        gambarCerita = intent.getStringExtra(EXTRA_IMAGE_CAMPAIGN);
        jam = intent.getStringExtra(EXTRA_TGL);
        wil = intent.getStringExtra(EXTRA_WAKTU);

        System.out.println("ID CERITA = "+idCerita);

        loadDetailCerita();
    }

    private void loadDetailCerita() {

        Hash_file_maps = new HashMap<String, String>();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, getString(R.string.url_header)+"/v1/apps/cerita_kebaikan/"+idCerita+"?username="+USER+"&password="+PASS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.d("DETAI CERITA", ">>" + response);
                            //converting the string to json array object
                            JSONObject jObj = new JSONObject(response);
                            String judul = jObj.getString("judul");
                            String sinopsis = jObj.getString("sinopsis");
                            String tglCerita = jObj.getString("tanggal_cerita");
                            String user = jObj.getString("user");
                            String waktu_cerita = jObj.getString("waktu_cerita");
                            String wilayah = jObj.getString("wilayah");
                            String jArray = jObj.getString("link");
                            JSONArray array = new JSONArray(jArray);
                            //traversing through all the object
                            for (int i = 0; i < 1; i++) {

                                //getting product object from json array
                                JSONObject product = array.getJSONObject(i);
                                String path = product.getString("pathImage");

                                System.out.println("GAMBAR = "+path);
                                System.out.println("JUDUL = "+judul);
                                Hash_file_maps.put(judul, path);

                            }

                            judulCerita.setText(judul);
                            isiCerita.setText(sinopsis);
                            tanggal.setText(tglCerita);
                            lokasi.setText(wilayah);
                            waktu.setText(waktu_cerita);

                            for (String name : Hash_file_maps.keySet()) {

                                TextSliderView textSliderView = new TextSliderView(DetailCeritaActivity.this);
                                textSliderView
                                        .description(name)
                                        .image(Hash_file_maps.get(name))
                                        .setScaleType(BaseSliderView.ScaleType.Fit)
                                        .setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                                            @Override
                                            public void onSliderClick(BaseSliderView slider) {
                                                judulCampaign.setText(slider.getBundle().get("extra").toString());
                                                System.out.println("JUDL CAMPAIGN = " + judulCampaign.getText().toString().replace(" ", "_"));

                                            }
                                        });
                                textSliderView.bundle(new Bundle());
                                textSliderView.getBundle()
                                        .putString("extra", name);
                                sliderLayout.addSlider(textSliderView);
                            }
                            sliderLayout.setPresetTransformer(SliderLayout.Transformer.Accordion);
                            sliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
                            sliderLayout.setCustomAnimation(new DescriptionAnimation());
                            sliderLayout.setDuration(9000000);
                            sliderLayout.addOnPageChangeListener(new ViewPagerEx.OnPageChangeListener() {
                                @Override
                                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                                }

                                @Override
                                public void onPageSelected(int position) {
                                    Log.d("Slider Demo", "Page Changed: " + position);
                                }

                                @Override
                                public void onPageScrollStateChanged(int state) {

                                }
                            });

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
        Volley.newRequestQueue(DetailCeritaActivity.this).add(stringRequest);

    }

    public void goBack(View view) {
        onBackPressed();
    }
}

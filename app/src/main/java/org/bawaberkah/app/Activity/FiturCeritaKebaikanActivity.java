package org.bawaberkah.app.Activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.bawaberkah.app.Adapter.ProductCeritaAdapter;
import org.bawaberkah.app.Model.ProductCerita;
import org.bawaberkah.app.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FiturCeritaKebaikanActivity extends AppCompatActivity {

    private static final String USER = "bawaberkahudin";
    private static final String PASS = "17aug1945";
    ProgressBar progressBar;
    private RecyclerView recyclerView;
    private List<ProductCerita> productCerita;

    public static final String EXTRA_ID = "id";
    public static final String EXTRA_IMAGE_CAMPAIGN = "path_campaign";
    public static final String EXTRA_JUDUL = "judul";
    public static final String EXTRA_LOKASI = "lokasi";
    public static final String EXTRA_TGL = "tanggal";
    public static final String EXTRA_WAKTU = "waktu";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fitur_cerita_kebaikan);

        productCerita = new ArrayList<>();

        progressBar = findViewById(R.id.progressbarCerita);
        recyclerView = findViewById(R.id.listCerita);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager managerCampain = new GridLayoutManager(this,2);
        recyclerView.setLayoutManager(managerCampain);

        loadCerita();

    }

    private void loadCerita() {

        progressBar.setVisibility(View.VISIBLE);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, getString(R.string.url_header)+"/v1/apps/cerita_kebaikan?username="+USER+"&password="+PASS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        productCerita.clear();
                        try {
                            //converting the string to json array object

                            Log.d("strrrrr", ">>" + response);

                            JSONObject jObj = new JSONObject(response);
                            String jArray = jObj.getString("cerita");
                            JSONArray array = new JSONArray(jArray);
                            //traversing through all the object
                            for (int i = 0; i < array.length(); i++) {

                                //getting product object from json array
                                JSONObject product = array.getJSONObject(i);
                                String gambar_cerita = product.getString("gambar_cerita");
                                String id_cerita = product.getString("id_cerita");
                                String judul = product.getString("judul");
                                String tanggal_cerita = product.getString("tanggal_cerita");
                                String wilayah = product.getString("wilayah");
                                String waktu_cerita = product.getString("waktu_cerita");

                                System.out.println("gambar_cerita = " + gambar_cerita);
                                System.out.println("id_cerita = " + id_cerita);
                                System.out.println("judul = " + judul);
                                System.out.println("tanggal_cerita = " + tanggal_cerita);
                                System.out.println("wilayah = " + wilayah);

                                ProductCerita pc = new ProductCerita();
                                pc.setGambar(gambar_cerita);
                                pc.setId_cerita(id_cerita);
                                pc.setJudul(judul);
                                pc.setTanggal(tanggal_cerita);
                                pc.setWilayah(wilayah);
                                pc.setWaktu(waktu_cerita);
                                productCerita.add(pc);

                            }

                            ProductCeritaAdapter adapter = new ProductCeritaAdapter(FiturCeritaKebaikanActivity.this, productCerita);
                            adapter.notifyDataSetChanged();
                            recyclerView.setAdapter(adapter);
                            adapter.setOnItemClickListener(new ProductCeritaAdapter.OnItemClickListener() {
                                @Override
                                public void onDonasiClick(int position) {

                                    Intent detailIntent = new Intent(FiturCeritaKebaikanActivity.this, DetailCeritaActivity.class);
                                    ProductCerita clickedItem = productCerita.get(position);

                                    detailIntent.putExtra(EXTRA_ID,clickedItem.getId_cerita());
                                    System.out.println("ID = " + clickedItem.getId_cerita());
                                    detailIntent.putExtra(EXTRA_JUDUL, clickedItem.getJudul());
                                    detailIntent.putExtra(EXTRA_IMAGE_CAMPAIGN, clickedItem.getGambar());
                                    detailIntent.putExtra(EXTRA_LOKASI, clickedItem.getWilayah());
                                    detailIntent.putExtra(EXTRA_TGL, clickedItem.getTanggal());
                                    detailIntent.putExtra(EXTRA_WAKTU, clickedItem.getWaktu());
                                    startActivity(detailIntent);

                                }

                                @Override
                                public void onDetailClick(int position) {
                                    Intent detailIntent = new Intent(FiturCeritaKebaikanActivity.this, DetailCeritaActivity.class);
                                    ProductCerita clickedItem = productCerita.get(position);

                                    detailIntent.putExtra(EXTRA_ID,clickedItem.getId_cerita());
                                    System.out.println("ID = " + clickedItem.getId_cerita());
                                    detailIntent.putExtra(EXTRA_JUDUL, clickedItem.getJudul());
                                    detailIntent.putExtra(EXTRA_IMAGE_CAMPAIGN, clickedItem.getGambar());
                                    detailIntent.putExtra(EXTRA_LOKASI, clickedItem.getWilayah());
                                    detailIntent.putExtra(EXTRA_TGL, clickedItem.getTanggal());
                                    detailIntent.putExtra(EXTRA_WAKTU, clickedItem.getWaktu());
                                    startActivity(detailIntent);
                                }

                                @Override
                                public void onImageClick(int position) {
                                    Intent detailIntent = new Intent(FiturCeritaKebaikanActivity.this, DetailCeritaActivity.class);
                                    ProductCerita clickedItem = productCerita.get(position);

                                    detailIntent.putExtra(EXTRA_ID,clickedItem.getId_cerita());
                                    System.out.println("ID = " + clickedItem.getId_cerita());
                                    detailIntent.putExtra(EXTRA_JUDUL, clickedItem.getJudul());
                                    detailIntent.putExtra(EXTRA_IMAGE_CAMPAIGN, clickedItem.getGambar());
                                    detailIntent.putExtra(EXTRA_LOKASI, clickedItem.getWilayah());
                                    detailIntent.putExtra(EXTRA_TGL, clickedItem.getTanggal());
                                    detailIntent.putExtra(EXTRA_WAKTU, clickedItem.getWaktu());
                                    startActivity(detailIntent);
                                }

                                @Override
                                public void onJudulClick(int position) {
                                    Intent detailIntent = new Intent(FiturCeritaKebaikanActivity.this, DetailCeritaActivity.class);
                                    ProductCerita clickedItem = productCerita.get(position);

                                    detailIntent.putExtra(EXTRA_ID,clickedItem.getId_cerita());
                                    System.out.println("ID = " + clickedItem.getId_cerita());
                                    detailIntent.putExtra(EXTRA_JUDUL, clickedItem.getJudul());
                                    detailIntent.putExtra(EXTRA_IMAGE_CAMPAIGN, clickedItem.getGambar());
                                    detailIntent.putExtra(EXTRA_LOKASI, clickedItem.getWilayah());
                                    detailIntent.putExtra(EXTRA_TGL, clickedItem.getTanggal());
                                    detailIntent.putExtra(EXTRA_WAKTU, clickedItem.getWaktu());
                                    startActivity(detailIntent);
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
        //adding our stringrequest to queue
        Volley.newRequestQueue(this).add(stringRequest);new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.GONE);
            }
        },3000);
    }

    public void goBack(View view){
        onBackPressed();
    }
}

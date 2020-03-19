package org.bawaberkah.app.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;

import org.bawaberkah.app.Adapter.MenuZakatAdapter;
import org.bawaberkah.app.Adapter.ProductsAdapter;
import org.bawaberkah.app.Adapter.ProductsWakafAdapter;
import org.bawaberkah.app.Adapter.ProductsZakatAdapter;
import org.bawaberkah.app.Model.MenuZakat;
import org.bawaberkah.app.Model.ProductWakaf;
import org.bawaberkah.app.R;
import org.bawaberkah.app.Util.RecyclerItemClickListener;
import org.bawaberkah.app.Util.Util;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FiturZakatActivity extends AppCompatActivity {

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

    private RecyclerView recyclerView;

    private final int recyclerViewImages[] = {
            R.drawable.logo_mpz_dd,
            R.drawable.logo_mpz_yth,
            R.drawable.logo_mpz_ymai,
            R.drawable.logo_mpz_pitakuning,
            R.drawable.logo_mpz_assyakirin,
            R.drawable.logo_mpz_almuhajirin,
            R.drawable.logo_mpz_dd,
            R.drawable.logo_mpz_yth,
            R.drawable.logo_mpz_ymai,
            R.drawable.logo_mpz_pitakuning,
            R.drawable.logo_mpz_assyakirin,
            R.drawable.logo_mpz_almuhajirin,
            R.drawable.logo_mpz_dd,
            R.drawable.logo_mpz_yth,
            R.drawable.logo_mpz_ymai
    };

    HashMap<String, String> Hash_file_maps;
    TextView judulCampaign;
    EditText searchzakat;
    Button btnHitungZakat;
    String target, terkumpul;
    double percentage;
    private SliderLayout sliderLayout;
    private String userUrl = "bawaberkahudin";
    private String passUrl = "17aug1945";
    private RecyclerView rvZakat;
    private List<ProductWakaf> productList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fitur_zakat);

        btnHitungZakat = findViewById(R.id.btnHitungZakat);
        judulCampaign = findViewById(R.id.lblJudulCampaign);
        sliderLayout = (SliderLayout) findViewById(R.id.sliderzakat);
        searchzakat = (EditText) findViewById(R.id.searchzakat);
        rvZakat = findViewById(R.id.productZakat);

        btnHitungZakat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FiturZakatActivity.this, HitungZakatActivity.class));
            }
        });

        loadProducts();

        searchzakat.setFocusable(false);
        searchzakat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchzakat.setFocusable(true);
            }
        });

        productList = new ArrayList();
        rvZakat.setHasFixedSize(true);
        RecyclerView.LayoutManager managerCampain = new GridLayoutManager(this,2);
        rvZakat.setLayoutManager(managerCampain);

        recyclerView = findViewById(R.id.menuZakat);
        RecyclerView.LayoutManager manager = new GridLayoutManager(this,3);
        recyclerView.setLayoutManager(manager);

        ArrayList<MenuZakat> av = prepareData();
        MenuZakatAdapter mAdapter = new MenuZakatAdapter(this, av);

        recyclerView.setAdapter(mAdapter);

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int i) {

                        switch (i) {
                            case 0:
                                Util.showmsg(FiturZakatActivity.this, "Bawaberkah", "Untuk saat ini menu ini belum tersedia!");
                                break;
                            case 1:
                                Util.showmsg(FiturZakatActivity.this, "Bawaberkah", "Untuk saat ini menu ini belum tersedia!");
                                break;
                            case 2:
                                Util.showmsg(FiturZakatActivity.this, "Bawaberkah", "Untuk saat ini menu ini belum tersedia!");
                                break;
                            case 3:
                                Util.showmsg(FiturZakatActivity.this, "Bawaberkah", "Untuk saat ini menu ini belum tersedia!");
                                break;
                            case 4:
                                Util.showmsg(FiturZakatActivity.this, "Bawaberkah", "Untuk saat ini menu ini belum tersedia!");
                                break;
                            case 5:
                                Util.showmsg(FiturZakatActivity.this, "Bawaberkah", "Untuk saat ini menu ini belum tersedia!");
                                break;
                            case 6:
                                Util.showmsg(FiturZakatActivity.this, "Bawaberkah", "Untuk saat ini menu ini belum tersedia!");
                                break;
                            case 7:
                                Util.showmsg(FiturZakatActivity.this, "Bawaberkah", "Nantikan fitur kami lainnya");
                                break;
                            case 8:
                                Util.showmsg(FiturZakatActivity.this, "Bawaberkah", "Untuk saat ini menu ini belum tersedia!");
                                break;
                            case 9:
                                Util.showmsg(FiturZakatActivity.this, "Bawaberkah", "Untuk saat ini menu ini belum tersedia!");
                                break;
                            case 10:
                                Util.showmsg(FiturZakatActivity.this, "Bawaberkah", "Untuk saat ini menu ini belum tersedia!");
                                break;
                            case 11:
                                Util.showmsg(FiturZakatActivity.this, "Bawaberkah", "Nantikan fitur kami lainnya");
                                break;
                            case 12:
                                Util.showmsg(FiturZakatActivity.this, "Bawaberkah", "Untuk saat ini menu ini belum tersedia!");
                                break;
                            case 13:
                                Util.showmsg(FiturZakatActivity.this, "Bawaberkah", "Untuk saat ini menu ini belum tersedia!");
                                break;
                            case 14:
                                Util.showmsg(FiturZakatActivity.this, "Bawaberkah", "Untuk saat ini menu ini belum tersedia!");
                                break;
                        }
                    }
                })
        );

        SyncBanner();
        loadProducts();
    }

    private void SyncBanner() {

        Hash_file_maps = new HashMap<String, String>();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, getString(R.string.url_header)+"/v1/apps/banner_zakat?username="+userUrl+"&password="+passUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.d("Banner", ">>" + response);
                            //converting the string to json array object
                            JSONObject jObj = new JSONObject(response);
                            String jArray = jObj.getString("banner");
                            JSONArray array = new JSONArray(jArray);

                            //traversing through all the object
                            for (int i = 0; i < array.length(); i++) {

                                //getting product object from json array
                                JSONObject product = array.getJSONObject(i);
                                String judul = product.getString("judul");
                                String path = product.getString("pathImage");

                                Hash_file_maps.put(judul, path);

                            }

                            for (String name : Hash_file_maps.keySet()) {

                                TextSliderView textSliderView = new TextSliderView(FiturZakatActivity.this);
                                textSliderView
                                        .description(name)
                                        .image(Hash_file_maps.get(name))
                                        .setScaleType(BaseSliderView.ScaleType.Fit)
                                        .setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                                            @Override
                                            public void onSliderClick(BaseSliderView slider) {
                                                judulCampaign.setText(slider.getBundle().get("extra").toString());
                                                System.out.println("JUDL CAMPAIGN = " + judulCampaign.getText().toString().replace(" ", "_"));

                                                /*SyncCampaign orderData = new SyncCampaign();
                                                orderData.execute("");*/
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
                            sliderLayout.setDuration(5000);
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
        Volley.newRequestQueue(FiturZakatActivity.this).add(stringRequest);
    }

    private void loadProducts() {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, getString(R.string.url_header)+"/v1/apps/campaign_zakat?username="+userUrl+"&password="+passUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        /*productList.clear();*/
                        try {
                            //converting the string to json array object
                            Log.d("strrrrr", ">>" + response);

                            JSONObject jObj = new JSONObject(response);
                            String jArray = jObj.getString("campaign");
                            JSONArray array = new JSONArray(jArray);
                            //traversing through all the object
                            for (int i = 0; i < array.length(); i++) {

                                //getting product object from json array
                                JSONObject product = array.getJSONObject(i);
                                String id = product.getString("id_campaign");
                                String judul = product.getString("judul_campaign");
                                terkumpul = product.getString("dana_terkumpul");
                                String gambar_campaign = product.getString("gambar_campaign");
                                String gambar_campaigner = product.getString("gambar_campaigner");
                                String start = product.getString("tanggal_mulai");
                                String end = product.getString("tanggal_selesai");
                                String lokasi_campaigner = product.getString("lokasi_campaigner");
                                String campaigner = product.getString("campaigner");
                                String banyaknya_donatur = product.getString("banyaknya_donatur");

                                Double a = Double.parseDouble(terkumpul);
                                NumberFormat nf = NumberFormat.getNumberInstance();
                                nf.setMaximumFractionDigits(0);
                                String forDisplay = nf.format(a);

                                int z;
                                percentage = 100;
                                z = 100;

                                System.out.println("Tanggal AWAL = " + start);
                                System.out.println("Tanggal AKHIR = " + end);
                                System.out.println("TARGET = " + target);
                                System.out.println("TERKUMPUL = " + forDisplay);
                                System.out.println("PERSEN = " + z);

                                ProductWakaf _list = new ProductWakaf();

                                _list.setId(id);
                                _list.setJudul(judul);
                                _list.setCampaigner(campaigner);
                                _list.setJumDonatur(banyaknya_donatur);
                                _list.setPath(gambar_campaign);
                                _list.setPathCampaign(gambar_campaigner);
                                _list.setLokasi(lokasi_campaigner);
                                _list.setPercentage(z);
                                _list.setEnd(end);
                                _list.setTerkumpul(forDisplay);
                                productList.add(_list);

                            }

                            ProductsZakatAdapter adapter = new ProductsZakatAdapter(FiturZakatActivity.this, productList);
                            adapter.notifyDataSetChanged();
                            rvZakat.setAdapter(adapter);
                            adapter.setOnItemClickListener(new ProductsAdapter.OnItemClickListener() {
                                @Override
                                public void onDonasiClick(int position) {

                                    Intent detailIntent = new Intent(FiturZakatActivity.this, DetailCampaignWakafActivity.class);
                                    ProductWakaf clickedItem = productList.get(position);

                                    detailIntent.putExtra(EXTRA_ID,clickedItem.getId());
                                    System.out.println("ID = " + clickedItem.getId());
                                    detailIntent.putExtra(EXTRA_JUDUL, clickedItem.getJudul());
                                    detailIntent.putExtra(EXTRA_TERKUMPUL, clickedItem.getTerkumpul());
                                    detailIntent.putExtra(EXTRA_IMAGE_CAMPAIGN, clickedItem.getPath());
                                    detailIntent.putExtra(EXTRA_IMAGE_CAMPAIGNER, clickedItem.getPathCampaign());
                                    detailIntent.putExtra(EXTRA_LOKASI, clickedItem.getLokasi());
                                    detailIntent.putExtra(EXTRA_CAMPAIGNER, clickedItem.getCampaigner());
                                    detailIntent.putExtra(EXTRA_NOM_DONATUR, clickedItem.getJumDonatur());
                                    detailIntent.putExtra(EXTRA_START, clickedItem.getStart());
                                    detailIntent.putExtra(EXTRA_END, clickedItem.getEnd());
                                    startActivity(detailIntent);
                                }

                                @Override
                                public void onDetailClick(int position) {
                                    Intent detailIntent = new Intent(FiturZakatActivity.this, DetailCampaignWakafActivity.class);
                                    ProductWakaf clickedItem = productList.get(position);

                                    detailIntent.putExtra(EXTRA_ID,clickedItem.getId());
                                    System.out.println("ID = " + clickedItem.getId());
                                    detailIntent.putExtra(EXTRA_JUDUL, clickedItem.getJudul());
                                    detailIntent.putExtra(EXTRA_TERKUMPUL, clickedItem.getTerkumpul());
                                    detailIntent.putExtra(EXTRA_IMAGE_CAMPAIGN, clickedItem.getPath());
                                    detailIntent.putExtra(EXTRA_IMAGE_CAMPAIGNER, clickedItem.getPathCampaign());
                                    detailIntent.putExtra(EXTRA_LOKASI, clickedItem.getLokasi());
                                    detailIntent.putExtra(EXTRA_CAMPAIGNER, clickedItem.getCampaigner());
                                    detailIntent.putExtra(EXTRA_NOM_DONATUR, clickedItem.getJumDonatur());
                                    detailIntent.putExtra(EXTRA_START, clickedItem.getStart());
                                    detailIntent.putExtra(EXTRA_END, clickedItem.getEnd());
                                    startActivity(detailIntent);
                                }

                                @Override
                                public void onImageClick(int position) {
                                    Intent detailIntent = new Intent(FiturZakatActivity.this, DetailCampaignWakafActivity.class);
                                    ProductWakaf clickedItem = productList.get(position);

                                    detailIntent.putExtra(EXTRA_ID,clickedItem.getId());
                                    System.out.println("ID = " + clickedItem.getId());
                                    detailIntent.putExtra(EXTRA_JUDUL, clickedItem.getJudul());
                                    detailIntent.putExtra(EXTRA_TERKUMPUL, clickedItem.getTerkumpul());
                                    detailIntent.putExtra(EXTRA_IMAGE_CAMPAIGN, clickedItem.getPath());
                                    detailIntent.putExtra(EXTRA_IMAGE_CAMPAIGNER, clickedItem.getPathCampaign());
                                    detailIntent.putExtra(EXTRA_LOKASI, clickedItem.getLokasi());
                                    detailIntent.putExtra(EXTRA_CAMPAIGNER, clickedItem.getCampaigner());
                                    detailIntent.putExtra(EXTRA_NOM_DONATUR, clickedItem.getJumDonatur());
                                    detailIntent.putExtra(EXTRA_START, clickedItem.getStart());
                                    detailIntent.putExtra(EXTRA_END, clickedItem.getEnd());
                                    startActivity(detailIntent);
                                }

                                @Override
                                public void onJudulClick(int position) {
                                    Intent detailIntent = new Intent(FiturZakatActivity.this, DetailCampaignWakafActivity.class);
                                    ProductWakaf clickedItem = productList.get(position);

                                    detailIntent.putExtra(EXTRA_ID,clickedItem.getId());
                                    System.out.println("ID = " + clickedItem.getId());
                                    detailIntent.putExtra(EXTRA_JUDUL, clickedItem.getJudul());
                                    detailIntent.putExtra(EXTRA_TERKUMPUL, clickedItem.getTerkumpul());
                                    detailIntent.putExtra(EXTRA_IMAGE_CAMPAIGN, clickedItem.getPath());
                                    detailIntent.putExtra(EXTRA_IMAGE_CAMPAIGNER, clickedItem.getPathCampaign());
                                    detailIntent.putExtra(EXTRA_LOKASI, clickedItem.getLokasi());
                                    detailIntent.putExtra(EXTRA_CAMPAIGNER, clickedItem.getCampaigner());
                                    detailIntent.putExtra(EXTRA_NOM_DONATUR, clickedItem.getJumDonatur());
                                    detailIntent.putExtra(EXTRA_START, clickedItem.getStart());
                                    detailIntent.putExtra(EXTRA_END, clickedItem.getEnd());
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
        Volley.newRequestQueue(FiturZakatActivity.this).add(stringRequest);
    }

    public void goBack(View view) {
        onBackPressed();
    }

    private ArrayList<MenuZakat> prepareData() {

        ArrayList<MenuZakat> av = new ArrayList<>();
        for (int i = 0; i < recyclerViewImages.length; i++) {
            MenuZakat mAndroidVersion = new MenuZakat();
            mAndroidVersion.setrecyclerViewImage(recyclerViewImages[i]);
            av.add(mAndroidVersion);
        }
        return av;
    }


}

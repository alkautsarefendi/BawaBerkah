package org.bawaberkah.app.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.bawaberkah.app.Adapter.ProductsAdapter;
import org.bawaberkah.app.Model.Product;
import org.bawaberkah.app.R;
import org.bawaberkah.app.Util.Util;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity implements ProductsAdapter.OnItemClickListener {

    private Spinner tipe, zona;
    private EditText campaign, cariCampaign;
    private boolean success = false; // boolean
    private ProductsAdapter adapter;

    //Declare Params for MySQL Connection
    private static final String USER = "bawaberkahudin";
    private static final String PASS = "17aug1945";

    private RecyclerView recyclerView;
    private List<Product> productList;
    private Parcelable recyclerViewState;

    private ImageView buttonView, btnFilter, btnKategori, btnPrev;

    public static final String EXTRA_ID = "id";
    public static final String EXTRA_URL = "path";
    public static final String EXTRA_JUDUL = "judul";
    public static final String EXTRA_TARGET = "target";
    public static final String EXTRA_TERKUMPUL = "terkumpul";
    public static final String EXTRA_SINOPSIS = "sinopsis";
    public static final String EXTRA_DETAIL = "detail";
    public static final String EXTRA_START = "start";
    public static final String EXTRA_END = "end";

    String target, terkumpul;
    double percentage;

    private TextView itemKategori;

    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        tipe=(Spinner)findViewById(R.id.spinnerTipe);
        campaign = (EditText)findViewById(R.id.search);
        itemKategori = (TextView)findViewById(R.id.itemCategory);
        progressBar = (ProgressBar)findViewById(R.id.progressbar);

        btnKategori = (ImageView)findViewById(R.id.filterKategori);
        btnKategori.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context wrapper = new ContextThemeWrapper(SearchActivity.this, R.style.popupMenuStyle);
                final PopupMenu popupMenu = new PopupMenu(wrapper, btnKategori);
                popupMenu.getMenuInflater().inflate(R.menu.kategori_search, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        String title = menuItem.getTitle().toString();
                        if (title.equals("Pendidikan") || title.equals("Kesehatan") || title.equals("Sosial") || title.equals("Bencana Alam")
                                || title.equals("Lingkungan") || title.equals("Ekonomi") || title.equals("Keagamaan") || title.equals("Wakaf")
                                || title.equals("Lain-Lain") || title.equals("Kurban") || title.equals("Bangun Masjid") || title.equals("Bedah Rumah")
                                || title.equals("Benah Sekolah") || title.equals("Biaya Pengobatan") || title.equals("Pesantren") || title.equals("Film")
                                || title.equals("Umum") || title.equals("Yatim")) {
                            itemKategori.setText(title);
                            loadProducts();
                        }

                        //Toast.makeText(search.this, ""+menuItem.getTitle(), Toast.LENGTH_SHORT).show();
                        return true;
                    }
                });
                setForceShowIcon(popupMenu);
                popupMenu.show();
            }
        });

        btnPrev = (ImageView)findViewById(R.id.btnBack);
        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        btnFilter = (ImageView)findViewById(R.id.btnMenu);
        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Context wrapper = new ContextThemeWrapper(SearchActivity.this, R.style.popupMenuStyle);
                final PopupMenu popupMenu = new PopupMenu(wrapper, btnFilter);
                popupMenu.getMenuInflater().inflate(R.menu.menu_search, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        String title = menuItem.getTitle().toString();
                        if (title.equals("Terpopuler")){
                            loadPopuler();
                        } else if (title.equals("Baru loadProducts();- > Lama")){
                            loadBaru();
                        } else if (title.equals("Lama - > Baru")) {
                            loadLama();
                        }

                        //Toast.makeText(search.this, ""+menuItem.getTitle(), Toast.LENGTH_SHORT).show();
                        return true;
                    }
                });
                setForceShowIcon(popupMenu);
                popupMenu.show();
            }
        });

        this.productList = new ArrayList();
        recyclerView = (RecyclerView) findViewById(R.id.recyclerTemp);
        this.recyclerView.setHasFixedSize(true);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(this));

        GridLayoutManager linearLayoutManager = new GridLayoutManager(getApplicationContext(),2);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerViewState = recyclerView.getLayoutManager().onSaveInstanceState();

        buttonView = (ImageView)findViewById(R.id.tmblSearch);
        buttonView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadProducts();
            }
        });

        loadStart();

        cariCampaign = (EditText)findViewById(R.id.search);
        cariCampaign.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });
    }

    private void filter(String text) {
        ArrayList<Product> filteredList = new ArrayList<>();

        for (Product item : productList) {
            if (item.getJudul().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            } else {
                Util.showmsg(SearchActivity.this,"","Data tidak di temukan");
            }
        }

        adapter.filterList(filteredList);
        adapter.notifyDataSetChanged();
    }

    public static void setForceShowIcon(PopupMenu popupMenu) {
        try {
            Field[] fields = popupMenu.getClass().getDeclaredFields();
            for (Field field : fields) {
                if ("mPopup".equals(field.getName())) {
                    field.setAccessible(true);
                    Object menuPopupHelper = field.get(popupMenu);
                    Class<?> classPopupHelper = Class.forName(menuPopupHelper
                            .getClass().getName());
                    Method setForceIcons = classPopupHelper.getMethod(
                            "setForceShowIcon", boolean.class);
                    setForceIcons.invoke(menuPopupHelper, true);
                    break;
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private void loadStart() {

        progressBar.setVisibility(View.VISIBLE);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, getString(R.string.url_header)+"/v1/apps/listcampaign?username="+USER+"&password="+PASS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        productList.clear();
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
                                String campaigner = product.getString("campaigner");
                                String id = product.getString("id_campaign");
                                String judul = product.getString("judul_campaign");
                                target = product.getString("dana_target");
                                terkumpul = product.getString("dana_terkumpul");
                                String path = product.getString("gambar_campaign");
                                String lokasi_campaigner = product.getString("lokasi_campaigner");
                                String start = product.getString("tanggal_mulai");
                                String end = product.getString("tanggal_selesai");

                                Double a = Double.parseDouble(terkumpul);
                                NumberFormat nf = NumberFormat.getNumberInstance();
                                nf.setMaximumFractionDigits(0);
                                String forDisplay = nf.format(a);

                                int persen;

                                if (target.equals("0")) {
                                    persen = 100;
                                    percentage = 100;
                                } else {
                                    Double x1 = Double.parseDouble(target);
                                    Double x2 = Double.parseDouble(terkumpul);
                                    percentage = 100.0D * x2 / x1;
                                    int y = (int) percentage;

                                    if (y > 100) {
                                        persen = 100;
                                    } else {
                                        persen = y;
                                    }
                                }

                                System.out.println("Tanggal AWAL = " + start);
                                System.out.println("Tanggal AKHIR = " + end);
                                System.out.println("TARGET = " + target);
                                System.out.println("TERKUMPUL = " + terkumpul);
                                System.out.println("PERSEN = " + persen);

                                Product _list = new Product(id, judul, target, forDisplay, path, persen, start, end);
                                productList.add(_list);
                            }

                            ProductsAdapter adapter = new ProductsAdapter(SearchActivity.this, productList);
                            adapter.notifyDataSetChanged();
                            recyclerView.setAdapter(adapter);
                            adapter.setOnItemClickListener(new ProductsAdapter.OnItemClickListener() {
                                @Override
                                public void onDonasiClick(int position) {

                                    Intent detailIntent = new Intent(SearchActivity.this, DetailCampaignActivity.class);
                                    Product clickedItem = productList.get(position);

                                    detailIntent.putExtra(EXTRA_ID,clickedItem.getId());
                                    System.out.println("ID = " + clickedItem.getId());
                                    detailIntent.putExtra(EXTRA_JUDUL, clickedItem.getJudul());
                                    detailIntent.putExtra(EXTRA_TARGET, clickedItem.getTarget());
                                    detailIntent.putExtra(EXTRA_TERKUMPUL, clickedItem.getTerkumpul());
                                    detailIntent.putExtra(EXTRA_URL, clickedItem.getPath());
                                    detailIntent.putExtra(EXTRA_START, clickedItem.getStart());
                                    detailIntent.putExtra(EXTRA_END, clickedItem.getEnd());

                                    startActivity(detailIntent);

                                    /*Intent detailIntent = new Intent(getActivity(), BillDonasiActivity.class);
                                    Product clickedItem = productList.get(position);

                                    detailIntent.putExtra(EXTRA_ID, clickedItem.getId());
                                    startActivity(detailIntent);*/
                                }

                                @Override
                                public void onDetailClick(int position) {
                                    Intent detailIntent = new Intent(SearchActivity.this, DetailCampaignActivity.class);
                                    Product clickedItem = productList.get(position);

                                    detailIntent.putExtra(EXTRA_ID,clickedItem.getId());
                                    System.out.println("ID = " + clickedItem.getId());
                                    detailIntent.putExtra(EXTRA_JUDUL, clickedItem.getJudul());
                                    detailIntent.putExtra(EXTRA_TARGET, clickedItem.getTarget());
                                    detailIntent.putExtra(EXTRA_TERKUMPUL, clickedItem.getTerkumpul());
                                    detailIntent.putExtra(EXTRA_URL, clickedItem.getPath());
                                    detailIntent.putExtra(EXTRA_START, clickedItem.getStart());
                                    detailIntent.putExtra(EXTRA_END, clickedItem.getEnd());

                                    startActivity(detailIntent);
                                }

                                @Override
                                public void onImageClick(int position) {
                                    Intent detailIntent = new Intent(SearchActivity.this, DetailCampaignActivity.class);
                                    Product clickedItem = productList.get(position);

                                    detailIntent.putExtra(EXTRA_ID, clickedItem.getId());
                                    System.out.println("ID = " + clickedItem.getId());
                                    detailIntent.putExtra(EXTRA_JUDUL, clickedItem.getJudul());
                                    detailIntent.putExtra(EXTRA_TARGET, clickedItem.getTarget());
                                    detailIntent.putExtra(EXTRA_TERKUMPUL, clickedItem.getTerkumpul());
                                    detailIntent.putExtra(EXTRA_URL, clickedItem.getPath());
                                    detailIntent.putExtra(EXTRA_START, clickedItem.getStart());
                                    detailIntent.putExtra(EXTRA_END, clickedItem.getEnd());

                                    startActivity(detailIntent);
                                }

                                @Override
                                public void onJudulClick(int position) {
                                    Intent detailIntent = new Intent(SearchActivity.this, DetailCampaignActivity.class);
                                    Product clickedItem = productList.get(position);

                                    detailIntent.putExtra(EXTRA_ID, clickedItem.getId());
                                    System.out.println("ID = " + clickedItem.getId());
                                    detailIntent.putExtra(EXTRA_JUDUL, clickedItem.getJudul());
                                    detailIntent.putExtra(EXTRA_TARGET, clickedItem.getTarget());
                                    detailIntent.putExtra(EXTRA_TERKUMPUL, clickedItem.getTerkumpul());
                                    detailIntent.putExtra(EXTRA_URL, clickedItem.getPath());
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
        //adding our stringrequest to queue
        Volley.newRequestQueue(this).add(stringRequest);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.GONE);
            }
        },3000);
    }

    @Override
    public void onDonasiClick(int position) {
        Intent detailIntent = new Intent(this, DetailCampaignActivity.class);
        Product clickedItem = productList.get(position);

        detailIntent.putExtra(EXTRA_ID, clickedItem.getId());
        detailIntent.putExtra(EXTRA_JUDUL, clickedItem.getJudul());
        detailIntent.putExtra(EXTRA_TARGET, clickedItem.getTarget());
        detailIntent.putExtra(EXTRA_TERKUMPUL, clickedItem.getTerkumpul());
        detailIntent.putExtra(EXTRA_URL, clickedItem.getPath());
        detailIntent.putExtra(EXTRA_START, clickedItem.getStart());
        detailIntent.putExtra("kdTransaksi", "donasi");
        detailIntent.putExtra(EXTRA_END, clickedItem.getEnd());
        startActivity(detailIntent);
    }

    @Override
    public void onDetailClick(int position) {
        Intent detailIntent = new Intent(this, DetailCampaignActivity.class);
        Product clickedItem = productList.get(position);

        detailIntent.putExtra(EXTRA_ID, clickedItem.getId());
        detailIntent.putExtra(EXTRA_JUDUL, clickedItem.getJudul());
        detailIntent.putExtra(EXTRA_TARGET, clickedItem.getTarget());
        detailIntent.putExtra(EXTRA_TERKUMPUL, clickedItem.getTerkumpul());
        detailIntent.putExtra(EXTRA_URL, clickedItem.getPath());
        detailIntent.putExtra(EXTRA_START, clickedItem.getStart());
        detailIntent.putExtra(EXTRA_END, clickedItem.getEnd());

        startActivity(detailIntent);
    }

    @Override
    public void onImageClick(int position) {
        Intent detailIntent = new Intent(this, DetailCampaignActivity.class);
        Product clickedItem = productList.get(position);

        detailIntent.putExtra(EXTRA_ID, clickedItem.getId());
        detailIntent.putExtra(EXTRA_JUDUL, clickedItem.getJudul());
        detailIntent.putExtra(EXTRA_TARGET, clickedItem.getTarget());
        detailIntent.putExtra(EXTRA_TERKUMPUL, clickedItem.getTerkumpul());
        detailIntent.putExtra(EXTRA_URL, clickedItem.getPath());
        detailIntent.putExtra(EXTRA_START, clickedItem.getStart());
        detailIntent.putExtra(EXTRA_END, clickedItem.getEnd());

        startActivity(detailIntent);
    }

    @Override
    public void onJudulClick(int position) {
        Intent detailIntent = new Intent(this, DetailCampaignActivity.class);
        Product clickedItem = productList.get(position);

        detailIntent.putExtra(EXTRA_ID, clickedItem.getId());
        detailIntent.putExtra(EXTRA_JUDUL, clickedItem.getJudul());
        detailIntent.putExtra(EXTRA_TARGET, clickedItem.getTarget());
        detailIntent.putExtra(EXTRA_TERKUMPUL, clickedItem.getTerkumpul());
        detailIntent.putExtra(EXTRA_URL, clickedItem.getPath());
        detailIntent.putExtra(EXTRA_START, clickedItem.getStart());
        detailIntent.putExtra(EXTRA_END, clickedItem.getEnd());

        startActivity(detailIntent);
    }

    private void loadProducts() {

        String _kategori = itemKategori.getText().toString();
        progressBar.setVisibility(View.VISIBLE);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://bawaberkah.online/api/list_campaign_filter.php?kategori="+_kategori,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        productList.clear();
                        try {
                            //converting the string to json array object
                            JSONArray array = new JSONArray(response);

                            //traversing through all the object
                            for (int i = 0; i < array.length(); i++) {

                                //getting product object from json array
                                JSONObject product = array.getJSONObject(i);
                                String id = product.getString("id");
                                String judul = product.getString("judul");
                                String target = product.getString("target");
                                String terkumpul = product.getString("terkumpul");
                                String path = product.getString("path");
                                String sinopsis = product.getString("sinopsis");
                                String detail = product.getString("detail");
                                String start = product.getString("start");
                                String end = product.getString("end");
                                int b = product.getInt("percentage");

                                Double a = Double.parseDouble(terkumpul);
                                NumberFormat nf = NumberFormat.getNumberInstance();
                                nf.setMaximumFractionDigits(0);
                                String forDisplay = nf.format(a);

                                Double x1 = Double.parseDouble(target);
                                Double x2 = Double.parseDouble(terkumpul);
                                double percentage = 100.0D * x2 / x1;
                                int y = (int)percentage;
                                int z;

                                if(y>100){
                                    z = 100;
                                }else{
                                    z = y;
                                }


                                Product _list = new Product(id, judul, target, forDisplay, path, z, start, end);
                                SearchActivity.this.productList.add(_list);

                            }

                            adapter = new ProductsAdapter(SearchActivity.this, productList);
                            adapter.notifyDataSetChanged();
                            recyclerView.setAdapter(adapter);
                            adapter.setOnItemClickListener(SearchActivity.this);


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
        Volley.newRequestQueue(this).add(stringRequest);new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.GONE);
            }
        },3000);
    }

    private void loadPopuler() {

        progressBar.setVisibility(View.VISIBLE);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://bawaberkah.online/api/campaign_populer.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        productList.clear();
                        try {
                            //converting the string to json array object
                            JSONArray array = new JSONArray(response);

                            //traversing through all the object
                            for (int i = 0; i < array.length(); i++) {

                                //getting product object from json array
                                JSONObject product = array.getJSONObject(i);
                                String id = product.getString("id");
                                String judul = product.getString("judul");
                                String target = product.getString("target");
                                String terkumpul = product.getString("terkumpul");
                                String path = product.getString("path");
                                String sinopsis = product.getString("sinopsis");
                                String detail = product.getString("detail");
                                String start = product.getString("start");
                                String end = product.getString("end");
                                int b = product.getInt("percentage");

                                Double a = Double.parseDouble(terkumpul);
                                NumberFormat nf = NumberFormat.getNumberInstance();
                                nf.setMaximumFractionDigits(0);
                                String forDisplay = nf.format(a);

                                Double x1 = Double.parseDouble(target);
                                Double x2 = Double.parseDouble(terkumpul);
                                double percentage = 100.0D * x2 / x1;
                                int y = (int)percentage;
                                int z;

                                if(y>100){
                                    z = 100;
                                }else{
                                    z = y;
                                }


                                Product _list = new Product(id, judul, target, forDisplay, path, z, start, end);
                                SearchActivity.this.productList.add(_list);

                            }

                            adapter = new ProductsAdapter(SearchActivity.this, productList);
                            adapter.notifyDataSetChanged();
                            recyclerView.setAdapter(adapter);
                            adapter.setOnItemClickListener(SearchActivity.this);


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
        Volley.newRequestQueue(this).add(stringRequest);new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.GONE);
            }
        },3000);
    }

    private void loadBaru() {

        progressBar.setVisibility(View.VISIBLE);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://bawaberkah.online/api/campaign_baru_lama.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        productList.clear();
                        try {
                            //converting the string to json array object
                            JSONArray array = new JSONArray(response);

                            //traversing through all the object
                            for (int i = 0; i < array.length(); i++) {

                                //getting product object from json array
                                JSONObject product = array.getJSONObject(i);
                                String id = product.getString("id");
                                String judul = product.getString("judul");
                                String target = product.getString("target");
                                String terkumpul = product.getString("terkumpul");
                                String path = product.getString("path");
                                String sinopsis = product.getString("sinopsis");
                                String detail = product.getString("detail");
                                String start = product.getString("start");
                                String end = product.getString("end");
                                int b = product.getInt("percentage");

                                Double a = Double.parseDouble(terkumpul);
                                NumberFormat nf = NumberFormat.getNumberInstance();
                                nf.setMaximumFractionDigits(0);
                                String forDisplay = nf.format(a);

                                Double x1 = Double.parseDouble(target);
                                Double x2 = Double.parseDouble(terkumpul);
                                double percentage = 100.0D * x2 / x1;
                                int y = (int)percentage;
                                int z;

                                if(y>100){
                                    z = 100;
                                }else{
                                    z = y;
                                }


                                Product _list = new Product(id, judul, target, forDisplay, path, z, start, end);
                                SearchActivity.this.productList.add(_list);

                            }

                            adapter = new ProductsAdapter(SearchActivity.this, productList);
                            adapter.notifyDataSetChanged();
                            recyclerView.setAdapter(adapter);
                            adapter.setOnItemClickListener(SearchActivity.this);


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
        Volley.newRequestQueue(this).add(stringRequest);new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.GONE);
            }
        },3000);
    }

    private void loadLama() {

        progressBar.setVisibility(View.VISIBLE);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://bawaberkah.online/api/campaign_lama_baru.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        productList.clear();
                        try {
                            //converting the string to json array object
                            JSONArray array = new JSONArray(response);

                            //traversing through all the object
                            for (int i = 0; i < array.length(); i++) {

                                //getting product object from json array
                                JSONObject product = array.getJSONObject(i);
                                String id = product.getString("id");
                                String judul = product.getString("judul");
                                String target = product.getString("target");
                                String terkumpul = product.getString("terkumpul");
                                String path = product.getString("path");
                                String sinopsis = product.getString("sinopsis");
                                String detail = product.getString("detail");
                                String start = product.getString("start");
                                String end = product.getString("end");
                                int b = product.getInt("percentage");

                                Double a = Double.parseDouble(terkumpul);
                                NumberFormat nf = NumberFormat.getNumberInstance();
                                nf.setMaximumFractionDigits(0);
                                String forDisplay = nf.format(a);

                                Double x1 = Double.parseDouble(target);
                                Double x2 = Double.parseDouble(terkumpul);
                                double percentage = 100.0D * x2 / x1;
                                int y = (int)percentage;
                                int z;

                                if(y>100){
                                    z = 100;
                                }else{
                                    z = y;
                                }


                                Product _list = new Product(id, judul, target, forDisplay, path, z, start, end);
                                SearchActivity.this.productList.add(_list);

                            }

                            adapter = new ProductsAdapter(SearchActivity.this, productList);
                            adapter.notifyDataSetChanged();
                            recyclerView.setAdapter(adapter);
                            adapter.setOnItemClickListener(SearchActivity.this);


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
        Volley.newRequestQueue(this).add(stringRequest);new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.GONE);
            }
        },3000);
    }
}

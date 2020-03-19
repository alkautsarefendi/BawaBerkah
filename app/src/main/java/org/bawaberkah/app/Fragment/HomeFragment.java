package org.bawaberkah.app.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
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

import org.bawaberkah.app.Activity.DetailCampaignActivity;
import org.bawaberkah.app.Activity.FiturBarzahActivity;
import org.bawaberkah.app.Activity.FiturCeritaKebaikanActivity;
import org.bawaberkah.app.Activity.FiturQurbanActivity;
import org.bawaberkah.app.Activity.FiturWakafActivity;
import org.bawaberkah.app.Activity.FiturZakatActivity;
import org.bawaberkah.app.Adapter.MenuDataAdapter;
import org.bawaberkah.app.Adapter.ProductsAdapter;
import org.bawaberkah.app.Model.MenuModel;
import org.bawaberkah.app.Model.Product;
import org.bawaberkah.app.R;
import org.bawaberkah.app.Activity.SearchActivity;
import org.bawaberkah.app.Util.RecyclerItemClickListener;
import org.bawaberkah.app.Util.Util;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HomeFragment extends Fragment {

    public static final String EXTRA_ID = "id";
    public static final String EXTRA_URL = "path";
    public static final String EXTRA_JUDUL = "judul";
    public static final String EXTRA_TARGET = "target";
    public static final String EXTRA_TERKUMPUL = "terkumpul";
    public static final String EXTRA_SINOPSIS = "sinopsis";
    public static final String EXTRA_DETAIL = "detail";
    public static final String EXTRA_START = "start";
    public static final String EXTRA_END = "end";

    EditText txtSearch;
    RecyclerView menuRecyclerView, recyclerView;
    private SliderLayout sliderLayout;
    private LinearLayout mLinearLayout;
    HashMap<String, String> Hash_file_maps;
    SwipeRefreshLayout swipeRefreshLayout;
    private Parcelable recyclerViewState;
    private TextView idGoogle, judulCampaign, userDisplay, userEmail;
    String danaBanner, detailBanner, endBanner, idbanner, pathBanner, sinopsisBanner, startBanner,
            trkumpulBanner, pathImage, target, terkumpul;
    double percentage;
    private boolean success = false;
    private List<Product> productList;

    private final String URL_BANNER = "http://ec2-18-237-78-94.us-west-2.compute.amazonaws.com:80/v1/apps/get_banners";
    private final String URL_PRODUCTS = "http://ec2-18-237-78-94.us-west-2.compute.amazonaws.com:80/v1/apps/top_campaign";

    private final String recyclerViewTitleText[] = {
            "Donasi ",
            "Zakat",
            "Wakaf",
            "Kurban",
            "Galang Dana",
            "Cerita Kebaikan",
            "Barzah",
            "Tiket"

    };
    private final int recyclerViewImages[] = {
            R.drawable.ic_donasi,
            R.drawable.ic_zakat,
            R.drawable.ic_wakaf,
            R.drawable.ic_qurban,
            R.drawable.ic_galangdana,
            R.drawable.ic_ceritakebaikan,
            R.drawable.ic_barzah,
            R.drawable.ic_tiket
    };

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        txtSearch = view.findViewById(R.id.searchFragment);
        menuRecyclerView = view.findViewById(R.id.recycler_view);
        //Search function
        txtSearch.setFocusable(false);
        txtSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent detailIntent = new Intent(getActivity(), SearchActivity.class);
                startActivity(detailIntent);
            }
        });

        //function menu button
        RecyclerView.LayoutManager manager = new GridLayoutManager(getActivity(), 4);
        menuRecyclerView.setLayoutManager(manager);

        ArrayList<MenuModel> av = prepareData();
        MenuDataAdapter mAdapter = new MenuDataAdapter(getActivity(), av);

        menuRecyclerView.setAdapter(mAdapter);

        menuRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int i) {

                        switch (i) {
                            case 0:
                                Intent intent = new Intent(getActivity(), SearchActivity.class);
                                startActivity(intent);
                                break;
                            case 1:
                                Intent intent1 = new Intent(getActivity(), FiturZakatActivity.class);
                                startActivity(intent1);
                                break;
                            case 2:
                                Intent intent2 = new Intent(getActivity(), FiturWakafActivity.class);
                                startActivity(intent2);
                                break;
                            case 3:
                                Intent intent3 = new Intent(getActivity(), FiturQurbanActivity.class);
                                startActivity(intent3);
                                break;
                            case 4:
                                Util.showmsg(getActivity(), "Bawaberkah", "Untuk saat ini menu ini belum tersedia!");
                                break;
                            case 5:
                                Intent intent5 = new Intent(getActivity(), FiturCeritaKebaikanActivity.class);
                                startActivity(intent5);
                                break;
                            case 6:
                                Intent intent6 = new Intent(getActivity(), FiturBarzahActivity.class);
                                startActivity(intent6);
                                break;
                            case 7:
                                Util.showmsg(getActivity(), "Bawaberkah", "Nantikan fitur kami lainnya");
                                break;

                        }
                    }
                })
        );

        sliderLayout = (SliderLayout) view.findViewById(R.id.slider);
        mLinearLayout = (LinearLayout) view.findViewById(R.id.pagesContainer);
        judulCampaign = view.findViewById(R.id.lblJudulCampaign);
        //this.setupSlider();
        SyncBanner();

        productList = new ArrayList();
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerTemp);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        loadProducts();
        checkInternet();

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);

        /*LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());*/
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerViewState = recyclerView.getLayoutManager().onSaveInstanceState();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // cancel the Visual indication of a refresh
                swipeRefreshLayout.setRefreshing(false);
                loadProducts();
            }
        });

        return view;
    }

    private void SyncBanner() {

        Hash_file_maps = new HashMap<String, String>();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_BANNER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.d("Banner", ">>" + response);
                            //converting the string to json array object
                            JSONObject jObj = new JSONObject(response);
                            String jArray = jObj.getString("banners");
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

                                TextSliderView textSliderView = new TextSliderView(getActivity());
                                textSliderView
                                        .description(name)
                                        .image(Hash_file_maps.get(name))
                                        .setScaleType(BaseSliderView.ScaleType.Fit)
                                        .setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                                            @Override
                                            public void onSliderClick(BaseSliderView slider) {
                                                judulCampaign.setText(slider.getBundle().get("extra").toString());
                                                System.out.println("JUDUL CAMPAIGN = " + judulCampaign.getText().toString().replace(" ", "_"));

                                                SyncCampaign orderData = new SyncCampaign();
                                                orderData.execute("");
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
        Volley.newRequestQueue(getActivity()).add(stringRequest);
    }

    private class SyncCampaign extends AsyncTask<String, String, String> {
        String msg;
        ProgressDialog progress;

        //Get Params
        final String jdlCampaign = judulCampaign.getText().toString().replace(" ", "_");

        @Override
        protected void onPreExecute() //Starts the progress dailog
        {

        }

        @Override
        protected String doInBackground(String... strings)  // Connect to the database, write query and add items to array list
        {
            try {

                StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://ec2-18-237-78-94.us-west-2.compute.amazonaws.com:80/v1/apps/campaign_by_name?judul_campaign=" + jdlCampaign,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    Log.d("strrrrr", ">>" + response);
                                    //converting the string to json array object

                                    //traversing through all the object

                                    //getting product object from json array
                                    JSONObject campaign = new JSONObject(response);
                                    danaBanner = campaign.getString("dana");
                                    detailBanner = campaign.getString("detail");
                                    endBanner = campaign.getString("end");
                                    idbanner = campaign.getString("id");
                                    pathBanner = campaign.getString("path");
                                    sinopsisBanner = campaign.getString("sinopsis");
                                    startBanner = campaign.getString("start");
                                    trkumpulBanner = campaign.getString("terkumpul");

                                    System.out.println("dana = " + danaBanner);
                                    System.out.println("detail = " + detailBanner);
                                    System.out.println("end = " + endBanner);
                                    System.out.println("id = " + idbanner);
                                    System.out.println("path = " + pathBanner);
                                    System.out.println("sinopsis = " + sinopsisBanner);
                                    System.out.println("start = " + startBanner);
                                    System.out.println("trkmpul = " + trkumpulBanner);

                                    JSONArray objPath = new JSONArray(pathBanner);
                                    for (int j = 0; j < objPath.length(); j++) {
                                        JSONObject imgPath = objPath.getJSONObject(j);
                                        pathImage = imgPath.getString("pathImage");

                                        System.out.println("pathImage = " + pathImage);

                                    }

                                    Intent detailIntent = new Intent(getActivity(), DetailCampaignActivity.class);

                                    detailIntent.putExtra(EXTRA_ID, idbanner);
                                    detailIntent.putExtra(EXTRA_JUDUL, judulCampaign.getText().toString());
                                    detailIntent.putExtra(EXTRA_TARGET, danaBanner);
                                    detailIntent.putExtra(EXTRA_TERKUMPUL, trkumpulBanner);
                                    detailIntent.putExtra(EXTRA_URL, pathImage);
                                    detailIntent.putExtra(EXTRA_SINOPSIS, sinopsisBanner);
                                    detailIntent.putExtra(EXTRA_DETAIL, detailBanner);
                                    detailIntent.putExtra(EXTRA_START, startBanner);
                                    detailIntent.putExtra(EXTRA_END, endBanner);

                                    startActivity(detailIntent);


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
                Volley.newRequestQueue(getActivity()).add(stringRequest);
            } catch (Exception e) {
                e.printStackTrace();
                Writer writer = new StringWriter();
                e.printStackTrace(new PrintWriter(writer));
                msg = writer.toString();
                success = false;
            }
            return msg;
        }

        @Override
        protected void onPostExecute(String msg) {

        }
    }

    private void checkInternet() {

        if (!isNetworkAvailable()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Warning").
                    setMessage("Tidak ada koneksi internet")
                    .setIcon(R.drawable.ic_warning)
                    .setCancelable(false)
                    .setPositiveButton("Retry",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int id) {
                                    // Restart the Activity
                                    Intent intent = getActivity().getIntent();
                                    getActivity().finish();
                                    startActivity(intent);
                                }
                            });
            AlertDialog alert = builder.create();
            alert.show();
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager
                .getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }

    private void loadProducts() {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_PRODUCTS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        productList.clear();
                        try {
                            //converting the string to json array object

                            Log.d("strrrrr", ">>" + response);

                            JSONObject jObj = new JSONObject(response);
                            String jArray = jObj.getString("top_campaign");
                            JSONArray array = new JSONArray(jArray);
                            //traversing through all the object
                            for (int i = 0; i < array.length(); i++) {

                                //getting product object from json array
                                JSONObject product = array.getJSONObject(i);
                                String id = product.getString("id_campaign");
                                String judul = product.getString("judul_campaign");
                                target = product.getString("dana_target");
                                terkumpul = product.getString("dana_terkumpul");
                                String path = product.getString("gambar_campaign");
                                /*String sinopsis = product.getString("sinopsis");*/
                                /*String detail = product.getString("detail");*/
                                String start = product.getString("tanggal_mulai");
                                String end = product.getString("tanggal_selesai");
                                /*int b = product.getInt("percentage");*/

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

                            ProductsAdapter adapter = new ProductsAdapter(getActivity(), productList);
                            adapter.notifyDataSetChanged();
                            recyclerView.setAdapter(adapter);
                            adapter.setOnItemClickListener(new ProductsAdapter.OnItemClickListener() {
                                @Override
                                public void onDonasiClick(int position) {

                                    Intent detailIntent = new Intent(getActivity(), DetailCampaignActivity.class);
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

                                    /*Intent detailIntent = new Intent(getActivity(), BillDonasiActivity.class);
                                    Product clickedItem = productList.get(position);

                                    detailIntent.putExtra(EXTRA_ID, clickedItem.getId());
                                    startActivity(detailIntent);*/
                                }

                                @Override
                                public void onDetailClick(int position) {
                                    Intent detailIntent = new Intent(getActivity(), DetailCampaignActivity.class);
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
                                public void onImageClick(int position) {
                                    Intent detailIntent = new Intent(getActivity(), DetailCampaignActivity.class);
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
                                    Intent detailIntent = new Intent(getActivity(), DetailCampaignActivity.class);
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
        Volley.newRequestQueue(getActivity()).add(stringRequest);
    }


    private ArrayList<MenuModel> prepareData() {

        ArrayList<MenuModel> av = new ArrayList<>();
        for (int i = 0; i < recyclerViewTitleText.length; i++) {
            MenuModel mAndroidVersion = new MenuModel();
            mAndroidVersion.setAndroidVersionName(recyclerViewTitleText[i]);
            mAndroidVersion.setrecyclerViewImage(recyclerViewImages[i]);
            av.add(mAndroidVersion);
        }
        return av;
    }


}

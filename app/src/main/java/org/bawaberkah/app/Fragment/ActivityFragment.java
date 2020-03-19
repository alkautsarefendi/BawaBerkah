package org.bawaberkah.app.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.bawaberkah.app.Activity.DetailCampaignWakafActivity;
import org.bawaberkah.app.Adapter.DonaturAdapter;
import org.bawaberkah.app.Adapter.LogAktivitasAdapter;
import org.bawaberkah.app.Model.DonaturModel;
import org.bawaberkah.app.Model.LogActivityModel;
import org.bawaberkah.app.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class ActivityFragment extends Fragment {

    RecyclerView recyclerView;
    private ArrayList<LogActivityModel> logModel;
    String idName;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_activity, container, false);

        recyclerView = view.findViewById(R.id.listLog);

        logModel = new ArrayList();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        SharedPreferences prefs = getActivity().getSharedPreferences("baber", MODE_PRIVATE);
        idName = prefs.getString("idName", "");

        System.out.println("ID ANDA = "+idName);

        getLog();

        return view;
    }

    private void getLog() {

        String URL_LOG = getString(R.string.url_header)+"/v1/apps/log_activity?username=bawaberkahudin&password=17aug1945&user_id="+idName;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_LOG,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.d("UPDATE", ">>" + response);
                            //converting the string to json array object
                            JSONObject jObj = new JSONObject(response);
                            String arrDonasi = jObj.getString("activity");
                            JSONArray array = new JSONArray(arrDonasi);
                            for (int i = 0; i < array.length(); i++){
                                JSONObject objDonasi = array.getJSONObject(i);
                                String activity = objDonasi.getString("activity");
                                String tanggal = objDonasi.getString("tanggal");
                                String waktu = objDonasi.getString("waktu");

                                LogActivityModel _log = new LogActivityModel();
                                _log.setAktivitas(activity);
                                _log.setTglAktivitas(tanggal);
                                _log.setWaktuAktivitas(waktu);
                                logModel.add(_log);
                            }

                            LogAktivitasAdapter adapter = new LogAktivitasAdapter(getActivity(), logModel);
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
        Volley.newRequestQueue(getActivity()).add(stringRequest);

    }


}

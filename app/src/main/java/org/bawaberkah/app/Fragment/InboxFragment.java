package org.bawaberkah.app.Fragment;

import android.content.Context;
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

import org.bawaberkah.app.Adapter.InboxAdapter;
import org.bawaberkah.app.Adapter.LogAktivitasAdapter;
import org.bawaberkah.app.Model.InboxModel;
import org.bawaberkah.app.Model.LogActivityModel;
import org.bawaberkah.app.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class InboxFragment extends Fragment {

    RecyclerView recyclerView;
    private ArrayList<InboxModel> inboxModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_inbox, container, false);

        recyclerView = view.findViewById(R.id.listInbox);

        inboxModel = new ArrayList();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        getInbox();

        return view;
    }


    private void getInbox() {

        String URL_LOG = getString(R.string.url_header)+"/v1/apps/berita?username=bawaberkahudin&password=17aug1945";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_LOG,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.d("UPDATE", ">>" + response);
                            //converting the string to json array object
                            JSONObject jObj = new JSONObject(response);
                            String arrDonasi = jObj.getString("berita");
                            JSONArray array = new JSONArray(arrDonasi);
                            for (int i = 0; i < array.length(); i++){
                                JSONObject objDonasi = array.getJSONObject(i);
                                String id = objDonasi.getString("id");
                                String isi_berita = objDonasi.getString("isi_berita");
                                String status = objDonasi.getString("status");
                                String tanggal = objDonasi.getString("tanggal");
                                String waktu = objDonasi.getString("waktu");

                                System.out.println("WAKTU = "+waktu);

                                InboxModel _inbox = new InboxModel();
                                _inbox.setIsi_berita(isi_berita);
                                _inbox.setTglBerita(tanggal);
                                _inbox.setWaktuBerita(waktu);
                                inboxModel.add(_inbox);
                            }

                            InboxAdapter adapter = new InboxAdapter(getActivity(), inboxModel);
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

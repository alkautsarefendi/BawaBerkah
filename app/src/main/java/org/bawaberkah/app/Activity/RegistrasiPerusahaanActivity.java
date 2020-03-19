package org.bawaberkah.app.Activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.bawaberkah.app.Adapter.ProductsAdapter;
import org.bawaberkah.app.Model.Kecamatan;
import org.bawaberkah.app.Model.Kelurahan;
import org.bawaberkah.app.Model.Kota;
import org.bawaberkah.app.Model.Product;
import org.bawaberkah.app.Model.Provinsi;
import org.bawaberkah.app.R;
import org.bawaberkah.app.Util.Util;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class RegistrasiPerusahaanActivity extends AppCompatActivity {

    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat dateFormatter;
    TextView txtIdProv, txtIdKota, txtIdKec, txtIdKel;
    String selectedProvinsi, jsonDaftar;
    ArrayList<String> provlist, kotalist, keclist, kellist;
    ArrayList<Provinsi> listProv;
    ArrayList<Kota> listKota;
    ArrayList<Kecamatan> listKecamatan;
    ArrayList<Kelurahan> listKelurahan;
    Spinner spinProvinsi, spinKota, spinKecamatan, spinKelurahan, spinJenisPerusahaan;
    EditText txtNamaPerusahaan, tvDateResult, txtDomisiliPerusahaan, txtNomerAktaAwal,
            txtNomerAktaAkhir, txtNpwp, txtNamaPIC, txtSiup, txtTelephone, txtEmailPerusahaan,
            txtAlamatPerusahaan, txtKodePos, txtPasswordPerusahaan;
    Button btnConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrasi_perusahaan);

        txtNamaPerusahaan = findViewById(R.id.txtNamaPerusahaan);
        tvDateResult = findViewById(R.id.tglBerdiriPerusahaan);
        txtDomisiliPerusahaan = findViewById(R.id.txtDomisiliPerusahaan);
        txtNomerAktaAwal = findViewById(R.id.txtNomerAktaAwal);
        txtNomerAktaAkhir = findViewById(R.id.txtNomerAktaAkhir);
        txtNpwp = findViewById(R.id.txtNpwp);
        txtNamaPIC = findViewById(R.id.txtNamaPIC);
        txtSiup = findViewById(R.id.txtSiup);
        txtTelephone = findViewById(R.id.txtTelephone);
        txtEmailPerusahaan = findViewById(R.id.txtEmailPerusahaan);
        txtAlamatPerusahaan = findViewById(R.id.txtAlamatPerusahaan);
        txtKodePos = findViewById(R.id.kodepos);
        txtPasswordPerusahaan = findViewById(R.id.txtPasswordPerusahaan);

        spinJenisPerusahaan = findViewById(R.id.spinJenisPerusahaan);
        spinProvinsi = findViewById(R.id.provinsi);
        spinKota = findViewById(R.id.kota);
        spinKecamatan = findViewById(R.id.kecamatan);
        spinKelurahan = findViewById(R.id.keluarahan);

        txtIdProv = findViewById(R.id.idprov);
        txtIdKota = findViewById(R.id.idkota);
        txtIdKec = findViewById(R.id.idkec);
        txtIdKel = findViewById(R.id.idkel);

        btnConfirm = findViewById(R.id.btnConfirm);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String namaPerusahaan = txtNamaPerusahaan.getText().toString();
                String jenisPerusahaan = spinJenisPerusahaan.getSelectedItem().toString();
                String tglBerdiriPerusahaan = tvDateResult.getText().toString();
                String domisiliPerusahaan = txtDomisiliPerusahaan.getText().toString();
                String nomorAktaAwal = txtNomerAktaAwal.getText().toString();
                String nomorAktaAkhir = txtNomerAktaAkhir.getText().toString();
                String npwp = txtNpwp.getText().toString();
                String pic = txtNamaPIC.getText().toString();
                String noSiup = txtSiup.getText().toString();
                String telp = txtTelephone.getText().toString();
                String email = txtEmailPerusahaan.getText().toString();
                String alamat = txtAlamatPerusahaan.getText().toString();
                String provinsi = spinProvinsi.getSelectedItem().toString();
                String kota = spinKota.getSelectedItem().toString();
                String kelurahan = spinKelurahan.getSelectedItem().toString();
                String kecamatan = spinKecamatan.getSelectedItem().toString();
                String kodePos = txtKodePos.getText().toString();
                String password = txtPasswordPerusahaan.getText().toString();

                if (namaPerusahaan.isEmpty()){
                    Util.showmsg(RegistrasiPerusahaanActivity.this,"","Nama Perusahaan tidak boleh kosong");
                } else if (jenisPerusahaan.isEmpty()){
                    Util.showmsg(RegistrasiPerusahaanActivity.this,"","Jenis Perusahaan tidak boleh kosong");
                } else if (tglBerdiriPerusahaan.isEmpty()){
                    Util.showmsg(RegistrasiPerusahaanActivity.this,"","Tanggal Berdiri Perusahaan tidak boleh kosong");
                } else if (domisiliPerusahaan.isEmpty()){
                    Util.showmsg(RegistrasiPerusahaanActivity.this,"","Domisili Perusahaan tidak boleh kosong");
                } else if (nomorAktaAwal.isEmpty()){
                    Util.showmsg(RegistrasiPerusahaanActivity.this,"","Nomor Akta Awal tidak boleh kosong");
                } else if (nomorAktaAkhir.isEmpty()){
                    Util.showmsg(RegistrasiPerusahaanActivity.this,"","Nomor Akta Akhir tidak boleh kosong");
                } else if (npwp.isEmpty()){
                    Util.showmsg(RegistrasiPerusahaanActivity.this,"","NPWP tidak boleh kosong");
                } else  if (pic.isEmpty()){
                    Util.showmsg(RegistrasiPerusahaanActivity.this,"","PIC Perusahaan tidak boleh kosong");
                } else if (noSiup.isEmpty()){
                    Util.showmsg(RegistrasiPerusahaanActivity.this,"","Nomor SIUP tidak boleh kosong");
                } else if (telp.isEmpty()){
                    Util.showmsg(RegistrasiPerusahaanActivity.this,"","Nomor Telephone tidak boleh kosong");
                } else if (email.isEmpty()){
                    Util.showmsg(RegistrasiPerusahaanActivity.this,"","Email tidak boleh kosong");
                } else if (alamat.isEmpty()){
                    Util.showmsg(RegistrasiPerusahaanActivity.this,"","Alamat Perusahaan tidak boleh kosong");
                } else if (provinsi.isEmpty()){
                    Util.showmsg(RegistrasiPerusahaanActivity.this,"","Provinsi tidak boleh kosong");
                } else if (kota == null){
                    Util.showmsg(RegistrasiPerusahaanActivity.this,"","Kota tidak boleh kosong");
                } else if (kelurahan == null){
                    Util.showmsg(RegistrasiPerusahaanActivity.this,"","Kelurahan tidak boleh kosong");
                } else  if (kecamatan == null){
                    Util.showmsg(RegistrasiPerusahaanActivity.this,"","Kecamatan tidak boleh kosong");
                } else if (kodePos.isEmpty()){
                    Util.showmsg(RegistrasiPerusahaanActivity.this,"","Kode Pos tidak boleh kosong");
                } else if (password.isEmpty()){
                    Util.showmsg(RegistrasiPerusahaanActivity.this,"","Password tidak boleh kosong");
                } else  {
                    btnConfirm.setEnabled(true);
                    kirimData();
                }

            }
        });

        checkInternet();
        loadProvinsi();

        // Initializing a String Array
        String[] plants = new String[]{
                "Jenis Perusahaan",
                "PT",
                "CV",
                "UD",
                "FIRMA",
                "BUMN",
                "KOPERASI",
                "YAYASAN"
        };

        final List<String> plantsList = new ArrayList<>(Arrays.asList(plants));

        // Initializing an ArrayAdapter
        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                this,R.layout.spinner_item,plantsList){
            @Override
            public boolean isEnabled(int position){
                if(position == 0)
                {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                }
                else
                {
                    return true;
                }
            }
            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0){
                    // Set the hint text color gray
                    tv.setTextColor(Color.BLACK);
                }
                else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        spinJenisPerusahaan.setAdapter(spinnerArrayAdapter);

        spinJenisPerusahaan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedProvinsi = (String) parent.getItemAtPosition(position);
                // If user change the default selection
                // First item is disable and it is used for hint
                if(position > 0){
                    // Notify the selected item text
                    Toast.makeText
                            (getApplicationContext(), selectedProvinsi, Toast.LENGTH_SHORT)
                            .show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);


        tvDateResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateDialog();
            }
        });

        spinProvinsi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                txtIdProv.setText(listProv.get(position).getIdProv());

                if (listProv.get(position).getIdProv() == "0"){
                    Toast.makeText
                            (getApplicationContext(), "Silahkan Pilih provinsi", Toast.LENGTH_SHORT)
                            .show();
                } else {
                    loadKota();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinKota.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                txtIdKota.setText(listKota.get(position).getIdKota());
                if (listKota.get(position).getIdProv() == "0"){
                    Toast.makeText
                            (getApplicationContext(), "Silahkan Pilih Kota", Toast.LENGTH_SHORT)
                            .show();
                } else {
                    loadKecamatan();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinKecamatan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                txtIdKec.setText(listKecamatan.get(position).getIdKecamatan());

                if (listKecamatan.get(position).getIdKecamatan() == "0"){
                    Toast.makeText
                            (getApplicationContext(), "Silahkan Pilih Kecamatan", Toast.LENGTH_SHORT)
                            .show();
                } else {
                    loadKelurahan();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinKelurahan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                txtIdKel.setText(listKelurahan.get(position).getIdKel());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void kirimData() {

        String namaPerusahaan = txtNamaPerusahaan.getText().toString();
        String jenisPerusahaan = spinJenisPerusahaan.getSelectedItem().toString();
        String tglBerdiriPerusahaan = tvDateResult.getText().toString();
        String domisiliPerusahaan = txtDomisiliPerusahaan.getText().toString();
        String nomorAktaAwal = txtNomerAktaAwal.getText().toString();
        String nomorAktaAkhir = txtNomerAktaAkhir.getText().toString();
        String npwp = txtNpwp.getText().toString();
        String pic = txtNamaPIC.getText().toString();
        String noSiup = txtSiup.getText().toString();
        String telp = txtTelephone.getText().toString();
        String email = txtEmailPerusahaan.getText().toString();
        String alamat = txtAlamatPerusahaan.getText().toString();
        String provinsi = spinProvinsi.getSelectedItem().toString();
        String kota = spinKota.getSelectedItem().toString();
        String kelurahan = spinKelurahan.getSelectedItem().toString();
        String kecamatan = spinKecamatan.getSelectedItem().toString();
        String kodePos = txtKodePos.getText().toString();
        String password = txtPasswordPerusahaan.getText().toString();

        try{
            JSONObject obj = new JSONObject();
            obj.put("name",namaPerusahaan);
            obj.put("jenis_perusahaan",jenisPerusahaan);
            obj.put("tanggal_berdiri_perusahaan",tglBerdiriPerusahaan);
            obj.put("domisili_perusahaan",domisiliPerusahaan);
            obj.put("no_akta_pendirian", nomorAktaAwal);
            obj.put("nama_pic", pic);
            obj.put("no_akta_terakhir", nomorAktaAkhir);
            obj.put("npwp", npwp);
            obj.put("nomor_siup", noSiup);
            obj.put("alamat", alamat);
            obj.put("provinsi", provinsi);
            obj.put("kota", kota);
            obj.put("kecamatan", kecamatan);
            obj.put("kelurahan", kelurahan);
            obj.put("kode_pos", kodePos);
            obj.put("password", password);
            obj.put("e-mail",email);
            obj.put("no_telp",telp);
            obj.put("jenis","Perusahaan");

            jsonDaftar = obj.toString(2);
            Log.d("output", obj.toString(2));

        } catch (JSONException e){
            e.printStackTrace();
        }

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                String url = getString(R.string.url_header)+"/v1/apps/register?username=bawaberkahudin&password=17aug1945";
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @RequiresApi(api = Build.VERSION_CODES.O)
                            @Override
                            public void onResponse(String response) {

                                Log.d("strrrrr", ">>" + response);

                                try {
                                    JSONObject object = new JSONObject(response);
                                    String respon = object.getString("message");
                                    String responcode = object.getString("responseCode");

                                    System.out.println("Message = "+respon);
                                    System.out.println("Message = "+responcode);
                                    try{
                                        if (responcode.equals("00")){
                                            android.support.v7.app.AlertDialog.Builder alertDialog = new android.support.v7.app.AlertDialog.Builder(RegistrasiPerusahaanActivity.this);
                                            alertDialog.setTitle("Pendaftaran Berhasil");
                                            alertDialog.setIcon(R.drawable.ic_baber);
                                            alertDialog.setMessage("Silahkan Login untuk masuk ke aplikasi");
                                            alertDialog.setIcon(R.drawable.ic_baber);
                                            alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {

                                                    RegistrasiPerusahaanActivity.super.onBackPressed();
                                                    startActivity(new Intent(RegistrasiPerusahaanActivity.this,LoginActivity.class));
                                                }
                                            }).create().show();

                                        }
                                    } catch (Exception e){
                                        e.printStackTrace();
                                        Util.showmsg(RegistrasiPerusahaanActivity.this,"Pendaftaran Gagal","Pendaftaran Gagal / Nomor Handphone sudah terpakai");
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Util.showmsg(RegistrasiPerusahaanActivity.this,"Pendaftaran Gagal","Pendaftaran Gagal");
                                }

                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                //displaying the error in toast if occurrs
                                Util.showmsg(RegistrasiPerusahaanActivity.this,"Pendaftaran Gagal","Pendaftaran Gagal / Nomor Handphone sudah terpakai");
                            }
                        }) {
                    /*@Override
                    public String getBodyContentType() { return "application/json; charset=utf-8"; }*/

                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        HashMap<String, String> headers = new HashMap<String, String>();
                        headers.put("Content-Type", "application/json; charset=utf-8");
                        return headers;
                    }

                    @Override
                    public byte[] getBody() throws AuthFailureError {

                        try {
                            return jsonDaftar == null ? null : jsonDaftar.getBytes("utf-8");
                        } catch (UnsupportedEncodingException uee){
                            return null;
                        }
                    }
                };

                // request queue
                RequestQueue requestQueue = Volley.newRequestQueue(RegistrasiPerusahaanActivity.this);

                requestQueue.add(stringRequest);
            }
        });

    }

    private void loadKelurahan() {

        String idKec = txtIdKec.getText().toString();

        kellist = new ArrayList<String>();
        listKelurahan = new ArrayList<Kelurahan>();

        final ArrayAdapter<String> spinnerArrayAdapter4 = new ArrayAdapter<String>(
                this,R.layout.spinner_item,kellist){
            @Override
            public boolean isEnabled(int position){
                if(position == 0)
                {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                }
                else
                {
                    return true;
                }
            }
            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0){
                    // Set the hint text color gray
                    tv.setTextColor(Color.BLACK);
                }
                else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };

        // Create an array to populate the spinner
        StringRequest stringRequest = new StringRequest(Request.Method.GET, getString(R.string.url_header)+"/v1/apps/campaignvillage?username=bawaberkahudin&password=17aug1945&district_id="+idKec,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //converting the string to json array object

                            Log.d("strrrrr", ">>" + response);

                            JSONObject jObj = new JSONObject(response);
                            String jArray = jObj.getString("villages");
                            JSONArray array = new JSONArray(jArray);
                            //traversing through all the object
                            for (int i = 0; i < array.length(); i++) {

                                //getting product object from json array
                                JSONObject product = array.getJSONObject(i);
                                String district_id = product.getString("district_id");
                                String village_id = product.getString("village_id");
                                String village_name = product.getString("village_name");


                                Kelurahan kel = new Kelurahan();
                                kel.setIdKec(district_id);
                                kel.setIdKel(village_id);
                                kel.setKelurahan(village_name);
                                listKelurahan.add(kel);
                                kellist.add(village_name);

                            }
                            spinKelurahan.setAdapter(spinnerArrayAdapter4);
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
        Volley.newRequestQueue(RegistrasiPerusahaanActivity.this).add(stringRequest);
    }

    private void loadKecamatan() {

        String idKota = txtIdKota.getText().toString();

        keclist = new ArrayList<String>();
        listKecamatan = new ArrayList<Kecamatan>();

        final ArrayAdapter<String> spinnerArrayAdapter3 = new ArrayAdapter<String>(
                this,R.layout.spinner_item,keclist){
            @Override
            public boolean isEnabled(int position){
                if(position == 0)
                {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                }
                else
                {
                    return true;
                }
            }
            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0){
                    // Set the hint text color gray
                    tv.setTextColor(Color.BLACK);
                }
                else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };

        // Create an array to populate the spinner
        StringRequest stringRequest = new StringRequest(Request.Method.GET, getString(R.string.url_header)+"/v1/apps/campaigndistrict?username=bawaberkahudin&password=17aug1945&city_id="+idKota,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //converting the string to json array object

                            Log.d("strrrrr", ">>" + response);

                            JSONObject jObj = new JSONObject(response);
                            String jArray = jObj.getString("districts");
                            JSONArray array = new JSONArray(jArray);
                            //traversing through all the object
                            for (int i = 0; i < array.length(); i++) {

                                //getting product object from json array
                                JSONObject product = array.getJSONObject(i);
                                String city_id = product.getString("city_id");
                                String district_id = product.getString("district_id");
                                String district_name = product.getString("district_name");


                                Kecamatan kec = new Kecamatan();
                                kec.setIdKecamatan(district_id);
                                kec.setIdKota(city_id);
                                kec.setKecamatan(district_name);
                                listKecamatan.add(kec);
                                keclist.add(district_name);

                            }
                            spinKecamatan.setAdapter(spinnerArrayAdapter3);
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
        Volley.newRequestQueue(RegistrasiPerusahaanActivity.this).add(stringRequest);
    }

    private void loadKota() {

        String idProv = txtIdProv.getText().toString();
        System.out.println("ID PILIH = "+idProv);

        kotalist = new ArrayList<String>();
        listKota = new ArrayList<Kota>();

        final ArrayAdapter<String> spinnerArrayAdapter2 = new ArrayAdapter<String>(
                this,R.layout.spinner_item,kotalist){
            @Override
            public boolean isEnabled(int position){
                if(position == 0)
                {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                }
                else
                {
                    return true;
                }
            }
            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0){
                    // Set the hint text color gray
                    tv.setTextColor(Color.BLACK);
                }
                else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };

        // Create an array to populate the spinner
        StringRequest stringRequest = new StringRequest(Request.Method.GET, getString(R.string.url_header)+"/v1/apps/campaigncity?username=bawaberkahudin&password=17aug1945&province_id="+idProv,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //converting the string to json array object

                            Log.d("strrrrr", ">>" + response);

                            JSONObject jObj = new JSONObject(response);
                            String jArray = jObj.getString("cities");
                            JSONArray array = new JSONArray(jArray);
                            //traversing through all the object
                            for (int i = 0; i < array.length(); i++) {

                                //getting product object from json array
                                JSONObject product = array.getJSONObject(i);
                                String city_id = product.getString("city_id");
                                String city_name = product.getString("city_name");
                                String province_id = product.getString("province_id");


                                Kota kota = new Kota();
                                kota.setIdProv(province_id);
                                kota.setIdKota(city_id);
                                kota.setKota(city_name);
                                listKota.add(kota);
                                kotalist.add(city_name);

                            }
                            spinKota.setAdapter(spinnerArrayAdapter2);
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
        Volley.newRequestQueue(RegistrasiPerusahaanActivity.this).add(stringRequest);

    }

    private void loadProvinsi() {
        provlist = new ArrayList<String>();
        listProv = new ArrayList<Provinsi>();

        final ArrayAdapter<String> spinnerArrayAdapter1 = new ArrayAdapter<String>(
                this,R.layout.spinner_item,provlist){
            @Override
            public boolean isEnabled(int position){
                if(position == 0)
                {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                }
                else
                {
                    return true;
                }
            }
            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0){
                    // Set the hint text color gray
                    tv.setTextColor(Color.BLACK);
                }
                else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };

        // Create an array to populate the spinner
        StringRequest stringRequest = new StringRequest(Request.Method.GET, getString(R.string.url_header)+"/v1/apps/campaignprovince?username=bawaberkahudin&password=17aug1945",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //converting the string to json array object

                            Log.d("strrrrr", ">>" + response);

                            JSONObject jObj = new JSONObject(response);
                            String jArray = jObj.getString("provinces");
                            JSONArray array = new JSONArray(jArray);
                            //traversing through all the object
                            for (int i = 0; i < array.length(); i++) {

                                //getting product object from json array
                                JSONObject product = array.getJSONObject(i);
                                String id = product.getString("id");
                                String province = product.getString("province");

                                System.out.println("ID = "+id);
                                System.out.println("PROVINSI = "+province);

                                Provinsi prov = new Provinsi();
                                prov.setIdProv(id);
                                prov.setProvinsi(province);
                                listProv.add(prov);
                                provlist.add(province);

                            }
                            spinProvinsi.setAdapter(spinnerArrayAdapter1);
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
        Volley.newRequestQueue(RegistrasiPerusahaanActivity.this).add(stringRequest);

    }

    private void showDateDialog(){

        Calendar newCalendar = Calendar.getInstance();

        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);

                tvDateResult.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.show();
    }

    private void checkInternet() {

        if (!isNetworkAvailable()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(RegistrasiPerusahaanActivity.this);
            builder.setTitle("Warning").
                    setMessage("Tidak ada koneksi internet")
                    .setIcon(R.drawable.ic_warning_red_24dp)
                    .setCancelable(false)
                    .setPositiveButton("Retry",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int id) {
                                    // Restart the Activity
                                    Intent intent = RegistrasiPerusahaanActivity.this.getIntent();
                                    RegistrasiPerusahaanActivity.this.finish();
                                    startActivity(intent);
                                }
                            });
            AlertDialog alert = builder.create();
            alert.show();
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) RegistrasiPerusahaanActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager
                .getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(RegistrasiPerusahaanActivity.this, LoginActivity.class));
        //overridePendingTransition(R.anim.slide_out,  R.anim.slide_in);
        finish();
    }
}

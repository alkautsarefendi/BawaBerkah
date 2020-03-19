package org.bawaberkah.app.Activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.bawaberkah.app.R;
import org.bawaberkah.app.Util.Util;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class RegistrasiActivity extends AppCompatActivity {

    EditText txtNamaPersonal, txtPasswordPersonal, txtEmailPersonal, txtTelephonePersonal;
    Button btnRegistrasiPersonal;
    String namaPersonal, passwordPersonal, emailPersonal, teleponPersonal, jsonRegis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrasi);

        txtNamaPersonal = findViewById(R.id.txtNamaPersonal);
        txtPasswordPersonal = findViewById(R.id.txtPasswordPersonal);
        txtEmailPersonal = findViewById(R.id.txtEmailPersonal);
        txtTelephonePersonal = findViewById(R.id.txtTelephonePersonal);



        btnRegistrasiPersonal = findViewById(R.id.btnRegistrasiPersonal);
        btnRegistrasiPersonal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                namaPersonal = txtNamaPersonal.getText().toString();
                passwordPersonal = txtPasswordPersonal.getText().toString();
                emailPersonal = txtEmailPersonal.getText().toString();
                teleponPersonal = txtTelephonePersonal.getText().toString();

                if (namaPersonal.isEmpty()){
                    Util.showmsg(RegistrasiActivity.this, "","Nama Personal Tidak Boleh Kosong!");
                } else if (passwordPersonal.isEmpty()){
                    Util.showmsg(RegistrasiActivity.this, "","Password Personal Tidak Boleh Kosong!");
                } else if (emailPersonal.isEmpty()){
                    Util.showmsg(RegistrasiActivity.this, "","Email Personal Tidak Boleh Kosong!");
                } else if (teleponPersonal.isEmpty()){
                    Util.showmsg(RegistrasiActivity.this, "","No.Telephone Personal Tidak Boleh Kosong!");
                } else if (passwordPersonal.length() < 8){
                    Util.showmsg(RegistrasiActivity.this, "","Password telalu Pendek");
                } else {
                    regisPersonal();
                }
            }
        });


        checkInternet();
    }

    private void regisPersonal() {

        namaPersonal = txtNamaPersonal.getText().toString();
        passwordPersonal = txtPasswordPersonal.getText().toString();
        emailPersonal = txtEmailPersonal.getText().toString();
        teleponPersonal = txtTelephonePersonal.getText().toString();

        try{
            JSONObject obj = new JSONObject();
            obj.put("name",namaPersonal);
            obj.put("password",passwordPersonal);
            obj.put("e-mail",emailPersonal);
            obj.put("no_telp", teleponPersonal);
            obj.put("jenis","Personal");
            /*obj.put("invoice",invoice);*/

            jsonRegis = obj.toString(2);
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
                                            android.support.v7.app.AlertDialog.Builder alertDialog = new android.support.v7.app.AlertDialog.Builder(RegistrasiActivity.this);
                                            alertDialog.setTitle("Pendaftaran Berhasil");
                                            alertDialog.setIcon(R.drawable.ic_baber);
                                            alertDialog.setMessage("Silahkan Login untuk masuk ke aplikasi");
                                            alertDialog.setIcon(R.drawable.ic_baber);
                                            alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {

                                                    RegistrasiActivity.super.onBackPressed();
                                                    startActivity(new Intent(RegistrasiActivity.this,LoginActivity.class));
                                                }
                                            }).create().show();

                                        }
                                    } catch (Exception e){
                                        e.printStackTrace();
                                        Util.showmsg(RegistrasiActivity.this,"Pendaftaran Gagal","Pendaftaran Gagal / Nomor Handphone sudah terpakai");
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Util.showmsg(RegistrasiActivity.this,"Pendaftaran Gagal","Pendaftaran Gagal");
                                }

                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                //displaying the error in toast if occurrs
                                Util.showmsg(RegistrasiActivity.this,"Pendaftaran Gagal","Pendaftaran Gagal / Nomor Handphone sudah terpakai");
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
                            return jsonRegis == null ? null : jsonRegis.getBytes("utf-8");
                        } catch (UnsupportedEncodingException uee){
                            return null;
                        }
                    }
                };

                // request queue
                RequestQueue requestQueue = Volley.newRequestQueue(RegistrasiActivity.this);

                requestQueue.add(stringRequest);
            }
        });

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(RegistrasiActivity.this, LoginActivity.class));
        //overridePendingTransition(R.anim.slide_out,  R.anim.slide_in);
        finish();
    }

    private void checkInternet() {

        if (!isNetworkAvailable()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(RegistrasiActivity.this);
            builder.setTitle("Warning").
                    setMessage("Tidak ada koneksi internet")
                    .setIcon(R.drawable.ic_warning_red_24dp)
                    .setCancelable(false)
                    .setPositiveButton("Retry",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int id) {
                                    // Restart the Activity
                                    Intent intent = RegistrasiActivity.this.getIntent();
                                    RegistrasiActivity.this.finish();
                                    startActivity(intent);
                                }
                            });
            AlertDialog alert = builder.create();
            alert.show();
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) RegistrasiActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager
                .getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }
}

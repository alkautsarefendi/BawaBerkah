package org.bawaberkah.app.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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
import java.util.ArrayList;
import java.util.List;

public class LayananMobilJenazahActivity extends AppCompatActivity {

    EditText txtNamaLayananMobilJenazah, txtTelpLayananMobilJenazah, txtNamaJenazah, txtAlmtJemputJenazah, txtAlmtAntarJenazah;
    Spinner spinAkibatMeninggal;
    Button btnDaftarMobilJenazah;
    private String selectedSpinner = null;
    private String rawJson;
    private String USER = "bawaberkahudin";
    private String PASS = "17aug1945";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layanan_mobil_jenazah);

        txtNamaLayananMobilJenazah = findViewById(R.id.txtNamaLayananMobilJenazah);
        txtTelpLayananMobilJenazah = findViewById(R.id.txtTelpLayananMobilJenazah);
        txtNamaJenazah = findViewById(R.id.txtNamaJenazah);
        txtAlmtJemputJenazah = findViewById(R.id.txtAlmtJemputJenazah);
        txtAlmtAntarJenazah = findViewById(R.id.txtAlmtAntarJenazah);
        spinAkibatMeninggal = findViewById(R.id.spinAkibatMeninggal);
        btnDaftarMobilJenazah = findViewById(R.id.btnDaftarMobilJenazah);

        btnDaftarMobilJenazah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String namaClientBarzah = txtNamaLayananMobilJenazah.getText().toString();
                String noTelpClientBarzah = txtTelpLayananMobilJenazah.getText().toString();
                String namaJenazahBarzah = txtNamaJenazah.getText().toString();
                String alamatJemputBarzah = txtAlmtJemputJenazah.getText().toString();
                String alamatAntarBarzah = txtAlmtAntarJenazah.getText().toString();
                String akibatMeninggal = spinAkibatMeninggal.getSelectedItem().toString();

                if (namaClientBarzah.isEmpty()){
                    Util.showmsg(LayananMobilJenazahActivity.this,"Peringatan","Nama Tidak boleh kosong");
                    txtNamaLayananMobilJenazah.setFocusable(true);
                } else if (noTelpClientBarzah.isEmpty()){
                    Util.showmsg(LayananMobilJenazahActivity.this,"Peringatan","No.Telepon Tidak boleh kosong");
                    txtTelpLayananMobilJenazah.setFocusable(true);
                } else if (namaJenazahBarzah.isEmpty()){
                    Util.showmsg(LayananMobilJenazahActivity.this,"Peringatan","Nama Jenazah Tidak boleh kosong");
                    txtNamaJenazah.setFocusable(true);
                } else if (alamatJemputBarzah.isEmpty()){
                    Util.showmsg(LayananMobilJenazahActivity.this,"Peringatan","Alamat jemput jenazah boleh kosong");
                    txtAlmtJemputJenazah.setFocusable(true);
                } else if (alamatAntarBarzah.isEmpty()){
                    Util.showmsg(LayananMobilJenazahActivity.this,"Peringatan","Alamat antar jenazah Tidak boleh kosong");
                    txtAlmtAntarJenazah.setFocusable(true);
                } else if (akibatMeninggal.equals("- Meninggal Karena -")){
                    Util.showmsg(LayananMobilJenazahActivity.this,"Peringatan","Anda belum memilih akibat meninggal");
                } else {
                    daftarLayananBarzah();
                }

            }
        });

        spinnerDropDown();
    }

    private void daftarLayananBarzah() {

        String namaClientBarzah = txtNamaLayananMobilJenazah.getText().toString();
        String noTelpClientBarzah = txtTelpLayananMobilJenazah.getText().toString();
        String namaJenazahBarzah = txtNamaJenazah.getText().toString();
        String alamatJemputBarzah = txtAlmtJemputJenazah.getText().toString();
        String alamatAntarBarzah = txtAlmtAntarJenazah.getText().toString();
        String akibatMeninggal = spinAkibatMeninggal.getSelectedItem().toString();

        System.out.println("namaClientBarzah = "+namaClientBarzah);
        System.out.println("noTelpClientBarzah = "+noTelpClientBarzah);
        System.out.println("namaJenazahBarzah = "+namaJenazahBarzah);
        System.out.println("alamatJemputBarzah = "+alamatJemputBarzah);
        System.out.println("alamatAntarBarzah = "+alamatAntarBarzah);
        System.out.println("akibatMeninggal = "+akibatMeninggal);

        try {
            JSONObject data = new JSONObject();

            data.put("nama_lengkap",namaClientBarzah);
            data.put("no_telp", noTelpClientBarzah);
            data.put("alamat_penjemputan",alamatJemputBarzah);
            data.put("alamat_pengantaran",alamatAntarBarzah);
            data.put("meninggal_karena",akibatMeninggal);
            data.put("nama_jenazah",namaJenazahBarzah);

            rawJson = data.toString();

            Log.d("output", data.toString(2));
            System.out.println("RAW JSON = "+rawJson);

        } catch (JSONException e){
            e.printStackTrace();
        }

        String urlDetail = getString(R.string.url_header)+"/v1/apps/barzah?username="+USER+"&password="+PASS;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlDetail,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("strrrrr", ">>" + response);

                        try {
                            JSONObject object = new JSONObject(response);
                            String respon = object.getString("message");
                            System.out.println("RESPON = "+respon);
                            if (respon.equals("success")){
                                AlertDialog.Builder builder = new AlertDialog.Builder(LayananMobilJenazahActivity.this);
                                builder.setTitle("Sukses");
                                builder.setMessage("Pendaftaran Berhasil")
                                        .setCancelable(false)
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                startActivity(new Intent(LayananMobilJenazahActivity.this, FiturBarzahActivity.class));
                                            }
                                        });
                                AlertDialog alertDialog = builder.create();
                                alertDialog.show();
                            } else {
                                Util.showmsg(LayananMobilJenazahActivity.this,"Gagal","Pendaftaran Gagal");
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //displaying the error in toast if occurrs
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            public String getBodyContentType() { return "application/json; charset=utf-8"; }

            @Override
            public byte[] getBody() throws AuthFailureError {

                try {
                    return rawJson == null ? null : rawJson.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee){
                    return null;
                }
            }
        };

        // request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        requestQueue.add(stringRequest);


    }

    private void spinnerDropDown() {

        //Array list of animals to display in the spinner
        List<String> list = new ArrayList<String>();
        list.add(" - Meninggal Karena -");
        list.add("Usia");
        list.add("Kecelakaan");
        list.add("Penyakit");
        list.add("Dibunuh");
        list.add("Bunuh Diri");
        //create an ArrayAdaptar from the String Array
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        //set the view for the Drop down list
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //set the ArrayAdapter to the spinner
        spinAkibatMeninggal.setAdapter(dataAdapter);
        //attach the listener to the spinner
        spinAkibatMeninggal.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String selectedItem = parent.getItemAtPosition(position).toString();
                selectedSpinner = selectedItem;
                System.out.println("ANDA PILIH = "+selectedSpinner);

                /*if (selectedSpinner.equals("Meninggal Karena")){
                    System.out.println("ANDA PILIH = "+selectedItem);
                }*/

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }
}

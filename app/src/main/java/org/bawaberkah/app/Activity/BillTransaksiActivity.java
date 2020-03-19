package org.bawaberkah.app.Activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import org.bawaberkah.app.R;
import org.bawaberkah.app.Util.GmailSender;
import org.bawaberkah.app.Util.Util;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

public class BillTransaksiActivity extends AppCompatActivity {

    String idCampaign, namaCampaign, jumlahDonasi, pesanDonatur, kdPembayaran, jsonRaw, personName,
            personEmail, kdTransaksi, rawJson, bank, personId, status, jsonOvo, responseCode,
            ammount, invoice, jsonDana, namabank, rDonasi, jsonLog;
    int kdAnonim;
    TextView txtOrderID, txtJudulCampaign, txtJumlahDonasi, txtNamaDonatur, txtEmailDonatur, txtJudulTransaksi, txtNamaZakat, txtEmailZakat;
    Button btnBayar;

    TextView txtZakatPenghasilan, txtZakatMaal, txtTotalPebayaranZakat, txtBCA, txtBNI, txtBRI, txtMandiri;
    EditText txtNohp;

    //Google Sign-in
    int RC_SIGN_IN = 0;
    GoogleSignInClient mGoogleSignInClient;
    LinearLayout layoutDana, layoutOvo, layoutBCA, layoutTelepon, layoutBni, layoutBri, layoutMandiri;
    ProgressBar progressBar;
    LinearLayout layoutTransaksi, layoutZakat;
    ProgressDialog progressDialog;
    String JsonResponse = null;
    String rekening, jsonLogin, aktivitas, idName;
    ImageView imgRekBCA, imgRekBNI, imgRekBRI, imgRekMandiri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_transaksi);

        txtOrderID = findViewById(R.id.txtOrderIDWakaf);
        txtJudulCampaign = findViewById(R.id.txtCampaignWakaf);
        txtJumlahDonasi = findViewById(R.id.txtJumlahWakaf);
        txtNamaDonatur = findViewById(R.id.txtNamaDonaturWakaf);
        txtEmailDonatur = findViewById(R.id.txtEmailDonaturWakaf);
        btnBayar = findViewById(R.id.btnBayar);
        txtJudulTransaksi = findViewById(R.id.judulTransaksi);
        layoutBCA = findViewById(R.id.layoutBCA);
        layoutDana = findViewById(R.id.layoutDana);
        layoutOvo = findViewById(R.id.layoutOvo);
        progressBar = findViewById(R.id.progressbar);
        layoutZakat = findViewById(R.id.layoutZakat);
        layoutTransaksi = findViewById(R.id.layoutTransaksi);
        txtNohp = findViewById(R.id.txtNoHp);
        layoutTelepon = findViewById(R.id.inputTelepon);
        layoutBni = findViewById(R.id.layoutBNI);
        layoutBri = findViewById(R.id.layoutBRI);
        layoutMandiri = findViewById(R.id.layoutMandiri);
        txtBCA = findViewById(R.id.txtBCA);
        txtBNI = findViewById(R.id.txtBNI);
        txtBRI = findViewById(R.id.txtBRI);
        txtMandiri = findViewById(R.id.txtMandiri);
        imgRekBCA = findViewById(R.id.copyRekBCA);
        imgRekBNI = findViewById(R.id.copyRekBNI);
        imgRekBRI = findViewById(R.id.copyRekBRI);
        imgRekMandiri = findViewById(R.id.copyRekMandiri);

        txtTotalPebayaranZakat = findViewById(R.id.txtTotalPebayaranZakat);
        txtZakatMaal = findViewById(R.id.txtZakatMaal);
        txtZakatPenghasilan = findViewById(R.id.txtZakatPenghasilan);
        txtNamaZakat = findViewById(R.id.txtNamaZakat);
        txtEmailZakat = findViewById(R.id.txtEmailZakat);

        txtZakatPenghasilan.setText("0");
        txtZakatMaal.setText("0");
        txtTotalPebayaranZakat.setText("0");

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        SharedPreferences prefs = getSharedPreferences("baber", MODE_PRIVATE);
        idName = prefs.getString("idName", "");

        System.out.println("ID ANDA = "+idName);

        Intent i = getIntent();
        Bundle bundle = getIntent().getExtras();
        idCampaign = bundle.getString("idCampaign");
        jumlahDonasi = bundle.getString("jumlahDonasi");
        pesanDonatur = bundle.getString("pesanDonatur");
        kdAnonim = bundle.getInt("kodeAnonim");
        kdPembayaran = bundle.getString("kdPembayaran");
        namaCampaign = bundle.getString("namaCampaign");
        kdTransaksi = bundle.getString("kdTransaksi");
        rekening = bundle.getString("rekening");

        imgRekBCA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("rekening", rekening);
                clipboard.setPrimaryClip(clip);
                Toast.makeText(BillTransaksiActivity.this,"Nomor Rekening BCA disalin",Toast.LENGTH_LONG).show();
            }
        });

        imgRekBNI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("rekening", rekening);
                clipboard.setPrimaryClip(clip);
                Toast.makeText(BillTransaksiActivity.this,"Nomor Rekening BNI disalin",Toast.LENGTH_LONG).show();
            }
        });

        imgRekBRI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("rekening", rekening);
                clipboard.setPrimaryClip(clip);
                Toast.makeText(BillTransaksiActivity.this,"Nomor Rekening BRI disalin",Toast.LENGTH_LONG).show();
            }
        });

        imgRekMandiri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("rekening", rekening);
                clipboard.setPrimaryClip(clip);
                Toast.makeText(BillTransaksiActivity.this,"Nomor Rekening Mandiri disalin",Toast.LENGTH_LONG).show();
            }
        });

        String nom = jumlahDonasi.replace(",","");
        int z = Integer.parseInt(nom);

        //setRandom Nominal
        Random r = new Random();
        int a = r.nextInt(300) + 100;
        int c  = a+z;
        NumberFormat nf = NumberFormat.getCurrencyInstance();
        DecimalFormatSymbols decimalFormatSymbols = ((DecimalFormat) nf).getDecimalFormatSymbols();
        decimalFormatSymbols.setCurrencySymbol("");
        decimalFormatSymbols.setGroupingSeparator(',');
        ((DecimalFormat) nf).setDecimalFormatSymbols(decimalFormatSymbols);
        rDonasi= nf.format(c).trim();
        rDonasi = rDonasi.substring(0, rDonasi.length()-3);

        if (kdPembayaran.equals("1")){
            layoutDana.setVisibility(View.VISIBLE);
            if (kdTransaksi.equals("donasi")){
                txtJudulTransaksi.setText("Detail Donasi");
                layoutTransaksi.setVisibility(View.VISIBLE);
                txtJumlahDonasi.setText(jumlahDonasi);
                if (kdAnonim == 1){
                    txtNamaZakat.setText("Anonim");
                    txtEmailZakat.setText(personEmail);
                } else if (kdAnonim == 2){
                    txtNamaZakat.setText(personName);
                    txtEmailZakat.setText(personEmail);
                }
            } else if (kdTransaksi.equals("barzah")) {
                txtJudulTransaksi.setText("Detail Pembayaran Donasi Barzah");
                layoutTransaksi.setVisibility(View.VISIBLE);
                txtJumlahDonasi.setText(jumlahDonasi);
                if (kdAnonim == 1){
                    txtNamaZakat.setText("Anonim");
                    txtEmailZakat.setText(personEmail);
                } else if (kdAnonim == 2){
                    txtNamaZakat.setText(personName);
                    txtEmailZakat.setText(personEmail);
                }
            } else if (kdTransaksi.equals("wakaf")){
                txtJudulTransaksi.setText("Detail Pembayaran Wakaf");
                layoutTransaksi.setVisibility(View.VISIBLE);
                txtJumlahDonasi.setText(jumlahDonasi);
                if (kdAnonim == 1){
                    txtNamaZakat.setText("Anonim");
                    txtEmailZakat.setText(personEmail);
                } else if (kdAnonim == 2){
                    txtNamaZakat.setText(personName);
                    txtEmailZakat.setText(personEmail);
                }
            } else if (kdTransaksi.equals("zakatPenghasilan")){
                layoutZakat.setVisibility(View.VISIBLE);
                txtZakatPenghasilan.setText(jumlahDonasi);
                txtTotalPebayaranZakat.setText(jumlahDonasi);
                txtJumlahDonasi.setText(jumlahDonasi);
                if (kdAnonim == 1){
                    txtNamaZakat.setText("Anonim");
                    txtEmailZakat.setText(personEmail);
                } else if (kdAnonim == 2){
                    txtNamaZakat.setText(personName);
                    txtEmailZakat.setText(personEmail);
                }
            } else if (kdTransaksi.equals("zakatMaal")){
                layoutZakat.setVisibility(View.VISIBLE);
                txtZakatMaal.setText(jumlahDonasi);
                txtTotalPebayaranZakat.setText(jumlahDonasi);
                ammount = txtTotalPebayaranZakat.getText().toString().replace(",","");
                if (kdAnonim == 1){
                    txtNamaZakat.setText("Anonim");
                    txtEmailZakat.setText(personEmail);
                } else if (kdAnonim == 2){
                    txtNamaZakat.setText(personName);
                    txtEmailZakat.setText(personEmail);
                }
            }

        } else if (kdPembayaran.equals("2")){
            layoutOvo.setVisibility(View.VISIBLE);
            layoutTelepon.setVisibility(View.VISIBLE);
            if (kdTransaksi.equals("donasi")){
                txtJudulTransaksi.setText("Detail Donasi");
                layoutTransaksi.setVisibility(View.VISIBLE);
                txtJumlahDonasi.setText(jumlahDonasi);
                if (kdAnonim == 1){
                    txtNamaZakat.setText("Anonim");
                    txtEmailZakat.setText(personEmail);
                } else if (kdAnonim == 2){
                    txtNamaZakat.setText(personName);
                    txtEmailZakat.setText(personEmail);
                }
            } else if (kdTransaksi.equals("barzah")) {
                txtJudulTransaksi.setText("Detail Pembayaran Donasi Barzah");
                layoutTransaksi.setVisibility(View.VISIBLE);
                txtJumlahDonasi.setText(jumlahDonasi);
                if (kdAnonim == 1){
                    txtNamaZakat.setText("Anonim");
                    txtEmailZakat.setText(personEmail);
                } else if (kdAnonim == 2){
                    txtNamaZakat.setText(personName);
                    txtEmailZakat.setText(personEmail);
                }
            } else if (kdTransaksi.equals("wakaf")){
                txtJudulTransaksi.setText("Detail Pembayaran Wakaf");
                layoutTransaksi.setVisibility(View.VISIBLE);
                txtJumlahDonasi.setText(jumlahDonasi);
                if (kdAnonim == 1){
                    txtNamaZakat.setText("Anonim");
                    txtEmailZakat.setText(personEmail);
                } else if (kdAnonim == 2){
                    txtNamaZakat.setText(personName);
                    txtEmailZakat.setText(personEmail);
                }
            } else if (kdTransaksi.equals("zakatPenghasilan")){
                layoutZakat.setVisibility(View.VISIBLE);
                txtZakatPenghasilan.setText(jumlahDonasi);
                txtTotalPebayaranZakat.setText(jumlahDonasi);
                txtJumlahDonasi.setText(jumlahDonasi);
                if (kdAnonim == 1){
                    txtNamaZakat.setText("Anonim");
                    txtEmailZakat.setText(personEmail);
                } else if (kdAnonim == 2){
                    txtNamaZakat.setText(personName);
                    txtEmailZakat.setText(personEmail);
                }
            } else if (kdTransaksi.equals("zakatMaal")){
                layoutZakat.setVisibility(View.VISIBLE);
                txtZakatMaal.setText(jumlahDonasi);
                txtTotalPebayaranZakat.setText(jumlahDonasi);
                ammount = txtTotalPebayaranZakat.getText().toString().replace(",","");
                if (kdAnonim == 1){
                    txtNamaZakat.setText("Anonim");
                    txtEmailZakat.setText(personEmail);
                } else if (kdAnonim == 2){
                    txtNamaZakat.setText(personName);
                    txtEmailZakat.setText(personEmail);
                }
            }

        } else if (kdPembayaran.equals("3")){
            layoutBCA.setVisibility(View.VISIBLE);
            txtBCA.setText("3. Transfer zakatnya ke Yayasan Dompet Dhuafa Republika nomor Rekening "+rekening);
            if (kdTransaksi.equals("donasi")){
                txtJumlahDonasi.setText(rDonasi);
                txtJudulTransaksi.setText("Detail Donasi");
                layoutTransaksi.setVisibility(View.VISIBLE);
                if (kdAnonim == 1){
                    txtNamaZakat.setText("Anonim");
                    txtEmailZakat.setText(personEmail);
                } else if (kdAnonim == 2){
                    txtNamaZakat.setText(personName);
                    txtEmailZakat.setText(personEmail);
                }
            } else if (kdTransaksi.equals("barzah")) {
                txtJudulTransaksi.setText("Detail Pembayaran Donasi Barzah");
                layoutTransaksi.setVisibility(View.VISIBLE);
                txtJumlahDonasi.setText(rDonasi);
                if (kdAnonim == 1){
                    txtNamaZakat.setText("Anonim");
                    txtEmailZakat.setText(personEmail);
                } else if (kdAnonim == 2){
                    txtNamaZakat.setText(personName);
                    txtEmailZakat.setText(personEmail);
                }
            } else if (kdTransaksi.equals("wakaf")){
                txtJudulTransaksi.setText("Detail Pembayaran Wakaf");
                layoutTransaksi.setVisibility(View.VISIBLE);
                txtJumlahDonasi.setText(rDonasi);
                if (kdAnonim == 1){
                    txtNamaZakat.setText("Anonim");
                    txtEmailZakat.setText(personEmail);
                } else if (kdAnonim == 2){
                    txtNamaZakat.setText(personName);
                    txtEmailZakat.setText(personEmail);
                }
            } else if (kdTransaksi.equals("zakatPenghasilan")){
                layoutZakat.setVisibility(View.VISIBLE);
                txtZakatPenghasilan.setText(rDonasi);
                txtTotalPebayaranZakat.setText(rDonasi);
                txtJumlahDonasi.setText(rDonasi);
                if (kdAnonim == 1){
                    txtNamaZakat.setText("Anonim");
                    txtEmailZakat.setText(personEmail);
                } else if (kdAnonim == 2){
                    txtNamaZakat.setText(personName);
                    txtEmailZakat.setText(personEmail);
                } else if (kdTransaksi.equals("zakatMaal")){
                    layoutZakat.setVisibility(View.VISIBLE);
                    txtZakatMaal.setText(rDonasi);
                    txtTotalPebayaranZakat.setText(rDonasi);
                    ammount = txtTotalPebayaranZakat.getText().toString().replace(",","");
                    if (kdAnonim == 1){
                        txtNamaZakat.setText("Anonim");
                        txtEmailZakat.setText(personEmail);
                    } else if (kdAnonim == 2){
                        txtNamaZakat.setText(personName);
                        txtEmailZakat.setText(personEmail);
                    }
                }
            }

        } else if (kdPembayaran.equals("4")){
            layoutBni.setVisibility(View.VISIBLE);
            txtBNI.setText("3. Transfer zakatnya ke Yayasan Dompet Dhuafa Republika nomor Rekening "+rekening);
            if (kdTransaksi.equals("donasi")){
                txtJumlahDonasi.setText(rDonasi);
                txtJudulTransaksi.setText("Detail Donasi");
                layoutTransaksi.setVisibility(View.VISIBLE);
                if (kdAnonim == 1){
                    txtNamaZakat.setText("Anonim");
                    txtEmailZakat.setText(personEmail);
                } else if (kdAnonim == 2){
                    txtNamaZakat.setText(personName);
                    txtEmailZakat.setText(personEmail);
                }
            }  else if (kdTransaksi.equals("barzah")) {
                txtJudulTransaksi.setText("Detail Pembayaran Donasi Barzah");
                layoutTransaksi.setVisibility(View.VISIBLE);
                txtJumlahDonasi.setText(rDonasi);
                if (kdAnonim == 1){
                    txtNamaZakat.setText("Anonim");
                    txtEmailZakat.setText(personEmail);
                } else if (kdAnonim == 2){
                    txtNamaZakat.setText(personName);
                    txtEmailZakat.setText(personEmail);
                }
            } else if (kdTransaksi.equals("wakaf")){
                txtJudulTransaksi.setText("Detail Pembayaran Wakaf");
                layoutTransaksi.setVisibility(View.VISIBLE);
                txtJumlahDonasi.setText(rDonasi);
                if (kdAnonim == 1){
                    txtNamaZakat.setText("Anonim");
                    txtEmailZakat.setText(personEmail);
                } else if (kdAnonim == 2){
                    txtNamaZakat.setText(personName);
                    txtEmailZakat.setText(personEmail);
                }
            } else if (kdTransaksi.equals("zakatPenghasilan")){
                layoutZakat.setVisibility(View.VISIBLE);
                txtZakatPenghasilan.setText(rDonasi);
                txtTotalPebayaranZakat.setText(rDonasi);
                txtJumlahDonasi.setText(rDonasi);
                if (kdAnonim == 1){
                    txtNamaZakat.setText("Anonim");
                    txtEmailZakat.setText(personEmail);
                } else if (kdAnonim == 2){
                    txtNamaZakat.setText(personName);
                    txtEmailZakat.setText(personEmail);
                }
            } else if (kdTransaksi.equals("zakatMaal")){
                layoutZakat.setVisibility(View.VISIBLE);
                txtZakatMaal.setText(rDonasi);
                txtTotalPebayaranZakat.setText(rDonasi);
                ammount = txtTotalPebayaranZakat.getText().toString().replace(",","");
                if (kdAnonim == 1){
                    txtNamaZakat.setText("Anonim");
                    txtEmailZakat.setText(personEmail);
                } else if (kdAnonim == 2){
                    txtNamaZakat.setText(personName);
                    txtEmailZakat.setText(personEmail);
                }
            }

        } else if (kdPembayaran.equals("5")){
            layoutBri.setVisibility(View.VISIBLE);
            txtBRI.setText("3. Transfer zakatnya ke Yayasan Dompet Dhuafa Republika nomor Rekening "+rekening);
            if (kdTransaksi.equals("donasi")){
                txtJumlahDonasi.setText(rDonasi);
                txtJudulTransaksi.setText("Detail Donasi");
                layoutTransaksi.setVisibility(View.VISIBLE);
                if (kdAnonim == 1){
                    txtNamaZakat.setText("Anonim");
                    txtEmailZakat.setText(personEmail);
                } else if (kdAnonim == 2){
                    txtNamaZakat.setText(personName);
                    txtEmailZakat.setText(personEmail);
                }
            } else if (kdTransaksi.equals("barzah")) {
                txtJudulTransaksi.setText("Detail Pembayaran Donasi Barzah");
                layoutTransaksi.setVisibility(View.VISIBLE);
                txtJumlahDonasi.setText(rDonasi);
                if (kdAnonim == 1){
                    txtNamaZakat.setText("Anonim");
                    txtEmailZakat.setText(personEmail);
                } else if (kdAnonim == 2){
                    txtNamaZakat.setText(personName);
                    txtEmailZakat.setText(personEmail);
                }
            } else if (kdTransaksi.equals("wakaf")){
                txtJudulTransaksi.setText("Detail Pembayaran Wakaf");
                layoutTransaksi.setVisibility(View.VISIBLE);
                txtJumlahDonasi.setText(rDonasi);
                if (kdAnonim == 1){
                    txtNamaZakat.setText("Anonim");
                    txtEmailZakat.setText(personEmail);
                } else if (kdAnonim == 2){
                    txtNamaZakat.setText(personName);
                    txtEmailZakat.setText(personEmail);
                }
            } else if (kdTransaksi.equals("zakatPenghasilan")){
                layoutZakat.setVisibility(View.VISIBLE);
                txtZakatPenghasilan.setText(rDonasi);
                txtTotalPebayaranZakat.setText(rDonasi);
                txtJumlahDonasi.setText(rDonasi);
                if (kdAnonim == 1){
                    txtNamaZakat.setText("Anonim");
                    txtEmailZakat.setText(personEmail);
                } else if (kdAnonim == 2){
                    txtNamaZakat.setText(personName);
                    txtEmailZakat.setText(personEmail);
                }
            } else if (kdTransaksi.equals("zakatMaal")){
                layoutZakat.setVisibility(View.VISIBLE);
                txtZakatMaal.setText(rDonasi);
                txtTotalPebayaranZakat.setText(rDonasi);
                ammount = txtTotalPebayaranZakat.getText().toString().replace(",","");
                if (kdAnonim == 1){
                    txtNamaZakat.setText("Anonim");
                    txtEmailZakat.setText(personEmail);
                } else if (kdAnonim == 2){
                    txtNamaZakat.setText(personName);
                    txtEmailZakat.setText(personEmail);
                }
            }

        } else if (kdPembayaran.equals("6")){
            layoutMandiri.setVisibility(View.VISIBLE);
            txtMandiri.setText("3. Transfer zakatnya ke Yayasan Dompet Dhuafa Republika nomor Rekening "+rekening);
            if (kdTransaksi.equals("donasi")){
                txtJumlahDonasi.setText(rDonasi);
                txtJudulTransaksi.setText("Detail Donasi");
                layoutTransaksi.setVisibility(View.VISIBLE);
                if (kdAnonim == 1){
                    txtNamaZakat.setText("Anonim");
                    txtEmailZakat.setText(personEmail);
                } else if (kdAnonim == 2){
                    txtNamaZakat.setText(personName);
                    txtEmailZakat.setText(personEmail);
                }
            } else if (kdTransaksi.equals("barzah")) {
                txtJudulTransaksi.setText("Detail Pembayaran Donasi Barzah");
                layoutTransaksi.setVisibility(View.VISIBLE);
                txtJumlahDonasi.setText(rDonasi);
                if (kdAnonim == 1){
                    txtNamaZakat.setText("Anonim");
                    txtEmailZakat.setText(personEmail);
                } else if (kdAnonim == 2){
                    txtNamaZakat.setText(personName);
                    txtEmailZakat.setText(personEmail);
                }
            } else if (kdTransaksi.equals("wakaf")){
                txtJudulTransaksi.setText("Detail Pembayaran Wakaf");
                layoutTransaksi.setVisibility(View.VISIBLE);
                txtJumlahDonasi.setText(rDonasi);
                if (kdAnonim == 1){
                    txtNamaZakat.setText("Anonim");
                    txtEmailZakat.setText(personEmail);
                } else if (kdAnonim == 2){
                    txtNamaZakat.setText(personName);
                    txtEmailZakat.setText(personEmail);
                }
            } else if (kdTransaksi.equals("zakatPenghasilan")){
                layoutZakat.setVisibility(View.VISIBLE);
                txtZakatPenghasilan.setText(rDonasi);
                txtTotalPebayaranZakat.setText(rDonasi);
                txtJumlahDonasi.setText(rDonasi);
                if (kdAnonim == 1){
                    txtNamaZakat.setText("Anonim");
                    txtEmailZakat.setText(personEmail);
                } else if (kdAnonim == 2){
                    txtNamaZakat.setText(personName);
                    txtEmailZakat.setText(personEmail);
                }
            } else if (kdTransaksi.equals("zakatMaal")){
                layoutZakat.setVisibility(View.VISIBLE);
                txtZakatMaal.setText(rDonasi);
                txtTotalPebayaranZakat.setText(rDonasi);
                ammount = txtTotalPebayaranZakat.getText().toString().replace(",","");
                if (kdAnonim == 1){
                    txtNamaZakat.setText("Anonim");
                    txtEmailZakat.setText(personEmail);
                } else if (kdAnonim == 2){
                    txtNamaZakat.setText(personName);
                    txtEmailZakat.setText(personEmail);
                }
            }
        }

        getInvoice();

        System.out.println("ID = "+idCampaign);
        System.out.println("jumlahDonasi = "+jumlahDonasi);
        System.out.println("pesanDonatur = "+pesanDonatur);
        System.out.println("kdAnonim = "+kdAnonim);
        System.out.println("kdPembayaran = "+kdPembayaran);
        System.out.println("namaCampaign = "+namaCampaign);
        System.out.println("kdTransaksi = "+kdTransaksi);
        System.out.println("rekBCA = "+rekening);

        txtOrderID.setText(idCampaign);
        txtJudulCampaign.setText(namaCampaign);

        btnBayar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (kdPembayaran.equals("1")){
                    /*apiDana();*/
                    payDana();
                } else if (kdPembayaran.equals("2")){

                    if (txtNohp.getText().toString().isEmpty()){
                        Util.showmsg(BillTransaksiActivity.this,"","No Hp tidak boleh kosong");
                    } else {
                        OvoAsynctask ovoAsynctask = new OvoAsynctask();
                        ovoAsynctask.execute();
                    }
                } else {
                    /*sendMessage();*/
                    sendAws();
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(BillTransaksiActivity.this);
                    alertDialog.setTitle("BawaBerkah");
                    alertDialog.setIcon(R.drawable.ic_baber);
                    alertDialog.setMessage("Terima kasih sudah berdonasi di Bawaberkah");
                    alertDialog.setIcon(R.drawable.ic_baber);
                    alertDialog.setPositiveButton("IYA", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            startActivity(new Intent(BillTransaksiActivity.this, HalamanUtamaActivity.class));
                        }
                    }).create().show();
                }
            }
        });
    }

    private void sendMessage() {
        final String norek = rekening;
        if (kdPembayaran.equals("3")){
            namabank = "Bank BCA";
        } else if (kdPembayaran.equals("4")){
            namabank = "Bank BNI";
        } else if (kdPembayaran.equals("5")){
            namabank = "Bank BRI";
        } else if (kdPembayaran.equals("6")){
            namabank = "Bank Mandiri";
        }
        final String tDonasi = jumlahDonasi;
        final String namaMitra = txtNamaDonatur.getText().toString();
        Thread sender = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    GmailSender sender = new GmailSender("info@bawaberkah.org", "pH9xknnS#2v$#U8");
                    sender.sendMail("Notifikasi Pembayaran",
                            "Segera transfer kebaikan Anda untuk "+namaMitra+" sebesar Rp."+ rDonasi + " (sampai 3 digit akhir) ke " + namabank + " rek " + norek + " a.n Yayasan Dompet Dhuafa Republika dalam 24 jam. ",
                            "bawaberkah.online@gmail.com",
                            txtEmailDonatur.getText().toString());
                } catch (Exception e) {
                    Log.e("mylog", "Error: " + e.getMessage());
                }
            }
        });
        sender.start();
    }

    private void payDana() {

        int ammountDana = Integer.parseInt(txtJumlahDonasi.getText().toString().replace(",",""));

        try{
            JSONObject obj = new JSONObject();
            obj.put("amount",ammountDana);
            obj.put("invoice",invoice);

            jsonDana = obj.toString(2);
            Log.d("outputDANA", obj.toString(2));

        } catch (JSONException e){
            e.printStackTrace();
        }

        String UrlDana = "https://api-bawaberkah.com/dana/apps/transactions";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlDana,
                new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onResponse(String response) {

                        Log.d("strrrrr", ">>" + response);


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //displaying the error in toast if occurrs
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            public String getBodyContentType() { return "application/json; charset=utf-8"; }

            @Override
            public byte[] getBody() throws AuthFailureError {

                try {
                    return jsonDana == null ? null : jsonDana.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee){
                    return null;
                }
            }
        };

        // request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);


    }

    private void getInvoice() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, getString(R.string.url_header)+"/v1/apps/invoice?username=bawaberkahudin&password=17aug1945",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //converting the string to json array object

                            Log.d("strrrrr", ">>" + response);

                            JSONObject jObj = new JSONObject(response);
                            invoice = jObj.getString("invoice_id");
                            System.out.println("INVOICE = "+invoice);

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
        Volley.newRequestQueue(BillTransaksiActivity.this).add(stringRequest);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode ==  RC_SIGN_IN){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completeTask){
        try{
            GoogleSignInAccount account = completeTask.getResult(ApiException.class);
            personName = account.getDisplayName();
            personEmail = account.getEmail();
            Uri personPhoto = account.getPhotoUrl();
            personId = account.getId();

            if (kdAnonim == 1){
                txtNamaDonatur.setText("Anonim");
                txtEmailDonatur.setText(personEmail);
            } else if (kdAnonim == 2){
                txtNamaDonatur.setText(personName);
                txtEmailDonatur.setText(personEmail);
            }

        } catch (ApiException e){
            Toast.makeText(this, "Failed to connect Google", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onStart(){
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if (acct != null){
            personName = acct.getDisplayName();
            personEmail = acct.getEmail();
            personId = acct.getId();
            Uri personPhoto = acct.getPhotoUrl();

            if (kdAnonim == 1){
                txtNamaDonatur.setText("Anonim");
                txtEmailDonatur.setText(personEmail);
            } else if (kdAnonim == 2){
                txtNamaDonatur.setText(personName);
                txtEmailDonatur.setText(personEmail);
            }

            System.out.println("EMAIL ANDA = " +personEmail);
        }
        super.onStart();
    }

    private void sendAws() {
        int campaign_id;
        if (kdTransaksi.equals("zakatPenghasilan")){
            campaign_id = 1;
        } else {
            campaign_id = Integer.parseInt(idCampaign);
        }

        if (kdPembayaran.equals("3")){
            namabank = "BCA";
        } else if (kdPembayaran.equals("4")){
            namabank = "BNI";
        } else if (kdPembayaran.equals("5")){
            namabank = "BRI";
        } else if (kdPembayaran.equals("6")){
            namabank = "Mandiri";
        }

        String donor_name = txtNamaDonatur.getText().toString();
        String email_donatur = txtEmailDonatur.getText().toString();
        String ammount = txtJumlahDonasi.getText().toString().replace(",","");

        @SuppressLint({"NewApi", "LocalSuppress"}) LocalDateTime now = LocalDateTime.now();
        @SuppressLint({"NewApi", "LocalSuppress"}) LocalDateTime set = now.minusHours(7);
        @SuppressLint({"NewApi", "LocalSuppress"}) String reqTime = set.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        /*bank = namabank;*/
        status = "pending";

        //set 24jam
        Date dt = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(dt);
        c.add(Calendar.DATE, 1);
        dt = c.getTime();
        SimpleDateFormat date1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String expired = date1.format(dt);

        //set 1 jam
        @SuppressLint({"NewApi", "LocalSuppress"}) LocalDateTime start = LocalDateTime.now();
        @SuppressLint({"NewApi", "LocalSuppress"}) LocalDateTime end = start.minusHours(6);
        @SuppressLint({"NewApi", "LocalSuppress"}) String exp = end.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        System.out.println("WAKTU = "+exp);

        try {
            JSONObject data = new JSONObject();

            data.put("campaign_id",campaign_id);
            data.put("donors_by", personId);
            data.put("donors_name",donor_name);
            data.put("email",email_donatur);
            data.put("amount",ammount);
            data.put("paid",0);
            data.put("created_on",reqTime);
            data.put("updated_on","null");
            data.put("account_number","111");
            data.put("account_code","");
            data.put("bank",namabank);
            data.put("campaign_target_id",1);
            data.put("qty",1);
            data.put("price",ammount);
            data.put("notes", pesanDonatur);
            data.put("payment_method","midtrans_dana");
            data.put("donate_status",status);
            data.put("expired_time",exp);
            data.put("google_id",personId);
            data.put("facebook_id","null");
            data.put("type","Personal");

            rawJson = data.toString();

            Log.d("output", data.toString(2));
            System.out.println("RAW JSON = "+rawJson);

        } catch (JSONException e){
            e.printStackTrace();
        }

        String url = getString(R.string.url_header)+"/v1/apps/submit_don?username=bawaberkahudin&password=17aug1945";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("strrrrr", ">>" + response);

                        try {
                            JSONObject object = new JSONObject(response);
                            String respon = object.getString("message");
                            System.out.println("RESPON = "+respon);

                            if (respon.equals("success")){
                                aktivitas = "Pembayaran "+kdTransaksi+" Melalui Bank "+namabank;
                                insertLog();
                                /*AlertDialog.Builder builder = new AlertDialog.Builder(BillTransaksiActivity.this);
                                builder.setTitle("Sukses");
                                builder.setMessage("Pendaftaran Berhasil")
                                        .setCancelable(false)
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                startActivity(new Intent(BillTransaksiActivity.this, HalamanUtamaActivity.class));
                                            }
                                        });
                                AlertDialog alertDialog = builder.create();
                                alertDialog.show();*/

                                Toast.makeText(BillTransaksiActivity.this,"sukses",Toast.LENGTH_LONG).show();
                            } else {
                                aktivitas = "Pembayaran "+kdTransaksi+" Melalui "+namabank+" Failed";
                                insertLog();
                                Toast.makeText(BillTransaksiActivity.this,"sukses",Toast.LENGTH_LONG).show();
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

    public class OvoAsynctask extends AsyncTask<String, String , String > {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(BillTransaksiActivity.this);
            progressDialog.setMessage("Please Wait");
            progressDialog.setCancelable(false);
            progressDialog.show();

            String no_hp = txtNohp.getText().toString();
            ammount = txtJumlahDonasi.getText().toString().replace(",","");

            try{
                JSONObject obj = new JSONObject();
                obj.put("amount",ammount);
                obj.put("phone-number",no_hp);
                /*obj.put("invoice",invoice);*/

                jsonOvo = obj.toString(2);
                Log.d("output", obj.toString(2));

            } catch (JSONException e){
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(String... strings) {

            String urlOvo = getString(R.string.url_header)+"/ovo/apps/pushtransaction?username=bawaberkahudin&password=17aug1945";

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            try {
                URL url = new URL(urlOvo);
                urlConnection = (HttpURLConnection) url.openConnection();
                /*urlConnection.setDoOutput(true);*/
                // is output buffer writter
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setRequestProperty("Accept", "application/json");
                //set headers and method
                Writer writer = new BufferedWriter(new OutputStreamWriter(urlConnection.getOutputStream(), "UTF-8"));
                writer.write(jsonOvo);
                // json data
                writer.close();
                InputStream inputStream = urlConnection.getInputStream();
                //input stream
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String inputLine;
                while ((inputLine = reader.readLine()) != null)
                    buffer.append(inputLine + "\n");
                if (buffer.length() == 0) {
                    return null;
                }

                JsonResponse = buffer.toString();
                System.out.println("TOTAL = "+JsonResponse);

            } catch (IOException e) {
                e.printStackTrace();
            }
            finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("", "Error closing stream", e);
                    }
                }
            }
            return JsonResponse;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (s == null){
                progressDialog.dismiss();
                Util.showmsg(BillTransaksiActivity.this, "Transaksi Gagal","Invalid Phone Number");
            }
            else {
                Log.i("RESPON Data>>>", s);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject object = new JSONObject(JsonResponse);
                            String message = object.getString("message");
                            String response = object.getString("response");
                            aktivitas = "Pembayaran "+kdTransaksi+" Melalui OVO";
                            insertLog();
                            progressDialog.dismiss();
                            if (response.equals("00")) {
                                if (message.equals("success hit push to pay ovo directly!")){
                                    Util.showmsg(BillTransaksiActivity.this, "Transaksi Berhasil", message  );
                                } else {
                                    Util.showmsg(BillTransaksiActivity.this, "Transaksi Gagal","Time Out");
                                }

                            } else  {
                                Util.showmsg(BillTransaksiActivity.this, "Transaksi Gagal", message);
                            }

                            System.out.println("RESPON BODY = " + response);
                            System.out.println("RESPON message = " + message);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }
    }

    private void insertLog() {

        String tglLogin  = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        String waktuLogin = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
        String userId = idName;
        System.out.println("Tanggal = "+ tglLogin);
        System.out.println("Waktu = "+waktuLogin);

        try{
            JSONObject obj = new JSONObject();
            obj.put("user_id",userId);
            obj.put("tanggal",tglLogin);
            obj.put("waktu",waktuLogin);
            obj.put("activity",aktivitas);

            jsonLogin = obj.toString(2);
            Log.d("output", obj.toString(2));

        } catch (JSONException e){
            e.printStackTrace();
        }

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                String url = getString(R.string.url_header)+"/v1/apps/log_activity?username=bawaberkahudin&password=17aug1945";
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @RequiresApi(api = Build.VERSION_CODES.O)
                            @Override
                            public void onResponse(String respons) {

                                Log.d("RESPON LOG", ">>" + respons);

                                try {
                                    JSONObject object = new JSONObject(respons);
                                    String respon = object.getString("message");


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                //displaying the error in toast if occurrs
                            }
                        }) {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        HashMap<String, String> headers = new HashMap<String, String>();
                        headers.put("Content-Type", "application/json; charset=utf-8");
                        return headers;
                    }

                    @Override
                    public byte[] getBody() throws AuthFailureError {

                        try {
                            return jsonLogin == null ? null : jsonLogin.getBytes("utf-8");
                        } catch (UnsupportedEncodingException uee){
                            return null;
                        }
                    }
                };

                // request queue
                RequestQueue requestQueue = Volley.newRequestQueue(BillTransaksiActivity.this);
                requestQueue.add(stringRequest);
            }
        });
    }
}

package org.bawaberkah.app.Activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import org.bawaberkah.app.R;
import org.bawaberkah.app.Util.Util;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    //Facebook Login
    private LoginButton loginButton;
    private CallbackManager callbackManager;
    private Button btnLogin;
    private Context mContext;
    private ProgressDialog progressDialog;

    //Google Sign-in
    int RC_SIGN_IN = 0;
    SignInButton signInButton;
    GoogleSignInClient mGoogleSignInClient;

    private ImageButton fb, gl;
    private TextView _id, _nama, _email, _photo, error, lblDaftar;
    private EditText username, password;
    String jsonLogin, jsonLog, jsonLogGoogle, idGoogleAnda;

    Dialog dialog;

    private static final String EMAIL = "email";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        checkInternet();
        callbackManager = CallbackManager.Factory.create();

        fb = (ImageButton) findViewById(R.id.facebook);
        gl = (ImageButton) findViewById(R.id.google);
        //loginButton = (LoginButton) findViewById(R.id.login_button);
        username = (EditText)findViewById(R.id.txtUsername);
        password = (EditText)findViewById(R.id.txtPassword);
        lblDaftar = findViewById(R.id.lnkRegistrasi);

        //AccessToken accessToken = AccessToken.getCurrentAccessToken();
        //boolean isLoggedIn = accessToken != null && !accessToken.isExpired();

        btnLogin = (Button) findViewById(R.id.btnConfirm);
        error = (TextView)findViewById(R.id.labelError);

        //Google Sign-in
        signInButton = (SignInButton)findViewById(R.id.sign_in_button);

        //Invisible TextView
        _id = (TextView)findViewById(R.id.id);
        _nama = (TextView)findViewById(R.id.nama);
        _email = (TextView)findViewById(R.id.email);
        _photo = (TextView)findViewById(R.id.photo);

        dialog = new Dialog(LoginActivity.this, R.style.mytheme);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.popup);
        dialog.setCanceledOnTouchOutside(true);

        lblDaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Window window = dialog.getWindow();
                window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                window.setGravity(Gravity.CENTER);
                dialog.show();

                mContext = getApplicationContext();

                final Button regPersonal = dialog.findViewById(R.id.btnPersonal);
                final Button regPerusahaan = dialog.findViewById(R.id.btnPerusahaan);

                regPersonal.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(LoginActivity.this, RegistrasiActivity.class));
                        overridePendingTransition(R.anim.slide_in,  R.anim.slide_out);
                        dialog.dismiss();

                    }
                });


                regPerusahaan.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        startActivity(new Intent(LoginActivity.this, RegistrasiPerusahaanActivity.class));
                        overridePendingTransition(R.anim.slide_in,  R.anim.slide_out);
                        dialog.dismiss();

                    }
                });
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertLog();

                String user = username.getText().toString();
                String pass = password.getText().toString();

                if (user.isEmpty()){
                    Util.showmsg(LoginActivity.this,"","Username tidak boleh kosong");
                } else if (pass.isEmpty()){
                    Util.showmsg(LoginActivity.this,"","Password tidak boleh kosong");
                } else {
                    login();
                }
            }

        });
    }

    private void insertLog() {

        String tglLogin  = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        String waktuLogin = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
        String userId = username.getText().toString();
        String aktivitas = "Login Aplikasi";
        System.out.println("Tanggal = "+ tglLogin);
        System.out.println("Waktu = "+waktuLogin);

        try{
            JSONObject obj = new JSONObject();
            obj.put("user_id",idGoogleAnda);
            obj.put("tanggal",tglLogin);
            obj.put("waktu",waktuLogin);
            obj.put("activity",aktivitas);

            jsonLog = obj.toString(2);
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
                                    startActivity(new Intent(LoginActivity.this, HalamanUtamaActivity.class));


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
                            return jsonLog == null ? null : jsonLog.getBytes("utf-8");
                        } catch (UnsupportedEncodingException uee){
                            return null;
                        }
                    }
                };

                // request queue
                RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);
                requestQueue.add(stringRequest);
            }
        });
    }

    private void login() {

        final Dialog dialog = new Dialog(LoginActivity.this, R.style.mytheme);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.loading_progres);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();

        final String user = username.getText().toString();
        String pass = password.getText().toString();

        try{
            JSONObject obj = new JSONObject();
            obj.put("phone_number",user);
            obj.put("password",pass);

            jsonLogin = obj.toString(2);
            Log.d("output", obj.toString(2));

        } catch (JSONException e){
            e.printStackTrace();
        }

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                String urlLogin = "https://api-bawaberkah.com/v1/apps/login?username=bawaberkahudin&password=17aug1945";
                StringRequest stringRequest = new StringRequest(Request.Method.POST, urlLogin,
                        new Response.Listener<String>() {
                            @RequiresApi(api = Build.VERSION_CODES.O)
                            @Override
                            public void onResponse(String respons) {
                                dialog.dismiss();
                                Log.d("strrrrr", ">>" + respons);

                                try {
                                    JSONObject object = new JSONObject(respons);
                                    String respon = object.getString("message");
                                    String response = object.getString("response");
                                    JSONObject resp = new JSONObject(response);
                                    String rc = resp.getString("message");
                                    String user_info = resp.getString("user_info");
                                    JSONObject info = new JSONObject(user_info);
                                    String user_id = info.getString("user_id");
                                    String username = info.getString("username");

                                    System.out.println("user_id = "+user_id);
                                    System.out.println("username = "+username);
                                    try{
                                        if (rc.equals("00")){
                                            SharedPreferences.Editor editor = getSharedPreferences("baber", MODE_PRIVATE).edit();
                                            editor.putString("idName", username);
                                            editor.apply();

                                            String tglLogin  = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
                                            String waktuLogin = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
                                            String aktivitas = "Login Aplikasi";
                                            System.out.println("Tanggal = "+ tglLogin);
                                            System.out.println("Waktu = "+waktuLogin);

                                            try{
                                                JSONObject obj = new JSONObject();
                                                obj.put("user_id",username);
                                                obj.put("tanggal",tglLogin);
                                                obj.put("waktu",waktuLogin);
                                                obj.put("activity",aktivitas);

                                                jsonLog = obj.toString(2);
                                                Log.d("output", obj.toString(2));

                                            } catch (JSONException e){
                                                e.printStackTrace();
                                            }

                                            insertLog();

                                            progressDialog.dismiss();
                                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(LoginActivity.this);
                                            alertDialog.setTitle("Berhasil");
                                            alertDialog.setIcon(R.drawable.ic_baber);
                                            alertDialog.setMessage("Login Berhasil");
                                            alertDialog.setIcon(R.drawable.ic_baber);
                                            alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {

                                                    startActivity(new Intent(LoginActivity.this,HalamanUtamaActivity.class));
                                                }
                                            }).create().show();

                                        }
                                    } catch (Exception e){
                                        e.printStackTrace();
                                        Util.showmsg(LoginActivity.this,"Login Gagal","Login Gagal / User Tidak ditemukan");
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Util.showmsg(LoginActivity.this,"Login Gagal","Login Gagal");
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                //displaying the error in toast if occurrs
                                dialog.dismiss();
                                Util.showmsg(LoginActivity.this,"Login Gagal","Login Gagal / User Tidak ditemukan");
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
                            return jsonLogin == null ? null : jsonLogin.getBytes("utf-8");
                        } catch (UnsupportedEncodingException uee){
                            return null;
                        }
                    }
                };

                // request queue
                RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);
                requestQueue.add(stringRequest);

            }
        });

    }

    public void onClick(View v) {
        if (v == fb) {

            /*LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile"));
            loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {
                    // App code
                }

                @Override
                public void onCancel() {
                    // App code
                }

                @Override
                public void onError(FacebookException exception) {
                    // App code
                }
            });

            LoginManager.getInstance().registerCallback(callbackManager,
                    new FacebookCallback<LoginResult>() {
                        @Override
                        public void onSuccess(LoginResult loginResult) {
                            // App code
                        }

                        @Override
                        public void onCancel() {
                            // App code
                        }

                        @Override
                        public void onError(FacebookException exception) {
                            // App code
                        }
                    });*/
            Util.showmsg(LoginActivity.this,"","Login via Facebook belum tersedia");

        } else if (v == gl) {
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .build();

            mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

            signIn();
        }
    }

    private void signIn (){
        Intent signInIntent =  mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode ==  RC_SIGN_IN){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        } else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
            loginButton.setReadPermissions(Arrays.asList("email","public_profile"));
            //super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completeTask){
        try{
            GoogleSignInAccount account = completeTask.getResult(ApiException.class);
            String personName = account.getDisplayName();
            String personEmail = account.getEmail();
            Uri personPhoto = account.getPhotoUrl();
            String idGoogle = account.getId();

            _nama.setText(personName);
            _email.setText(personEmail);
            _id.setText(idGoogle);

            //Glide.with(this).load(personPhoto).into(photo);

            idGoogleAnda = _id.getText().toString();
            System.out.println("ID GOOGLE = "+idGoogleAnda);

            if (idGoogle.equals("") || idGoogle.equals(null)){
                Toast toast = Toast.makeText(LoginActivity.this, "Gagal terhubung \n Periksa koneksi internet anda", Toast.LENGTH_SHORT);
                TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
                if( v != null) v.setGravity(Gravity.CENTER);
                toast.show();
            } else {
                final Dialog dialog = new Dialog(LoginActivity.this, R.style.mytheme);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.loading_progres);
                dialog.setCanceledOnTouchOutside(true);

                Window window = dialog.getWindow();
                window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                window.setGravity(Gravity.CENTER);
                dialog.show();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        insertLogviagoogle();
                        dialog.dismiss();
                        finish();
                        SharedPreferences.Editor editor = getSharedPreferences("baber", MODE_PRIVATE).edit();
                        editor.putString("idName", idGoogleAnda);
                        editor.apply();

                        String tglLogin  = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
                        String waktuLogin = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
                        String userId = username.getText().toString();
                        String aktivitas = "Login Aplikasi";
                        System.out.println("Tanggal = "+ tglLogin);
                        System.out.println("Waktu = "+waktuLogin);

                        try{
                            JSONObject obj = new JSONObject();
                            obj.put("user_id",idGoogleAnda);
                            obj.put("tanggal",tglLogin);
                            obj.put("waktu",waktuLogin);
                            obj.put("activity",aktivitas);

                            jsonLog = obj.toString(2);
                            Log.d("output", obj.toString(2));

                        } catch (JSONException e){
                            e.printStackTrace();
                        }

                        startActivity(new Intent(LoginActivity.this, HalamanUtamaActivity.class));
                        //overridePendingTransition(R.anim.slide_in,  R.anim.slide_out);
                    }
                },3000);

                Toast toast = Toast.makeText(LoginActivity.this, personName + "\n berhasil terhubung", Toast.LENGTH_SHORT);
                TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
                if( v != null) v.setGravity(Gravity.CENTER);
                toast.show();
            }

            signInButton.setVisibility(View.GONE);
            //signout.setVisibility(View.VISIBLE);
        } catch (ApiException e){
            Toast.makeText(LoginActivity.this, "Failed to connect Google", Toast.LENGTH_SHORT).show();
        }
    }

    private void insertLogviagoogle(){
        String tglLogin  = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        String waktuLogin = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
        String userId = username.getText().toString();
        String aktivitas = "Login Aplikasi melalui Google Account";
        System.out.println("Tanggal = "+ tglLogin);
        System.out.println("Waktu = "+waktuLogin);

        try{
            JSONObject obj = new JSONObject();
            obj.put("user_id",idGoogleAnda);
            obj.put("tanggal",tglLogin);
            obj.put("waktu",waktuLogin);
            obj.put("activity",aktivitas);

            jsonLogGoogle = obj.toString(2);
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
                                    SharedPreferences.Editor editor = getSharedPreferences("baber", MODE_PRIVATE).edit();
                                    editor.putString("idName", idGoogleAnda);
                                    editor.apply();
                                    JSONObject object = new JSONObject(respons);
                                    String respon = object.getString("message");
                                    startActivity(new Intent(LoginActivity.this, HalamanUtamaActivity.class));


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
                            return jsonLogGoogle == null ? null : jsonLogGoogle.getBytes("utf-8");
                        } catch (UnsupportedEncodingException uee){
                            return null;
                        }
                    }
                };

                // request queue
                RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);
                requestQueue.add(stringRequest);
            }
        });
    }

    private void init() {
        this.progressDialog = new ProgressDialog(this);
    }

    private void showProgressDialog() {
        progressDialog.show();
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressDialog.setContentView(R.layout.loading_progres);
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(LoginActivity.this);
        alertDialog.setTitle("BawaBerkah");
        alertDialog.setIcon(R.drawable.ic_baber);
        alertDialog.setMessage("Keluar dari Aplikasi?");
        alertDialog.setIcon(R.drawable.ic_baber);
        alertDialog.setPositiveButton("IYA", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                LoginActivity.super.onBackPressed();
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//***Change Here***
                startActivity(intent);
                finish();
                System.exit(0);
            }
        });

        alertDialog.setNegativeButton("TIDAK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).create().show();

    }

    private void checkInternet() {

        if (!isNetworkAvailable()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
            builder.setTitle("Warning").
                    setMessage("Tidak ada koneksi internet")
                    .setIcon(R.drawable.ic_warning_red_24dp)
                    .setCancelable(false)
                    .setPositiveButton("Retry",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int id) {
                                    // Restart the Activity
                                    Intent intent = LoginActivity.this.getIntent();
                                    LoginActivity.this.finish();
                                    startActivity(intent);
                                }
                            });
            AlertDialog alert = builder.create();
            alert.show();
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) LoginActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager
                .getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }

    public void onClickReset(View view) {

        final Dialog dialog = new Dialog(LoginActivity.this, R.style.mytheme);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.resetpassword);
        dialog.setCanceledOnTouchOutside(true);

        // Get the application context
        mContext = getApplicationContext();

        final EditText inputEmail = dialog.findViewById(R.id.email);
        final Button btnKirim = dialog.findViewById(R.id.btnReset);

        btnKirim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast toast = Toast.makeText(LoginActivity.this, "Link reset password telah dikirim, \n cek email anda untuk proses selanjutnya", Toast.LENGTH_SHORT);
                TextView x = (TextView) toast.getView().findViewById(android.R.id.message);
                if( x != null) x.setGravity(Gravity.CENTER);
                toast.show();
                dialog.dismiss();

            }
        });

        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        dialog.show();

    }

    @Override
    public void onStart(){
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if (acct != null){
            String personName = acct.getDisplayName();
            String personEmail = acct.getEmail();
            String personId = acct.getId();
            Uri personPhoto = acct.getPhotoUrl();

            System.out.println("EMAIL ANDA = " +personEmail);

            startActivity(new Intent(LoginActivity.this, HalamanUtamaActivity.class));

        }
        super.onStart();
    }

}

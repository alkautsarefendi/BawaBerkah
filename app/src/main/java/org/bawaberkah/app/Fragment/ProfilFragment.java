package org.bawaberkah.app.Fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.bawaberkah.app.Activity.FAQActivity;
import org.bawaberkah.app.Activity.HalamanUtamaActivity;
import org.bawaberkah.app.Activity.HubungiKamiActivity;
import org.bawaberkah.app.Activity.KebijakanPrivacyActivity;
import org.bawaberkah.app.Activity.LoginActivity;
import org.bawaberkah.app.Activity.SyaratKetentuanActivity;
import org.bawaberkah.app.Activity.TentangActivity;
import org.bawaberkah.app.R;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class ProfilFragment extends Fragment {

    TextView txtNotifikasi, txtSyarat, txtKebijakan, txtFAQ, txtTentang, txtLogout, txtHubungi, idGoogle, namaDonatur, emailDonatur, idDonatur;
    GoogleSignInClient mGoogleSignInClient;
    int RC_SIGN_IN = 0;
    String jsonLogout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profil, container, false);

        txtNotifikasi = view.findViewById(R.id.txtNotifikasi);
        txtSyarat = view.findViewById(R.id.txtSyarat);
        txtKebijakan = view.findViewById(R.id.txtKebijakan);
        txtFAQ = view.findViewById(R.id.txtFAQ);
        txtTentang = view.findViewById(R.id.txtTentang);
        txtHubungi = view.findViewById(R.id.txtHubungiKami);
        namaDonatur = view.findViewById(R.id.txtNama);
        emailDonatur = view.findViewById(R.id.txtEmail);
        idDonatur = view.findViewById(R.id.lblIdDonatur);
        idGoogle = view.findViewById(R.id.lblIdGoogle);
        txtLogout = view.findViewById(R.id.txtLogout);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);

        txtNotifikasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        txtSyarat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), SyaratKetentuanActivity.class));
            }
        });

        txtKebijakan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), KebijakanPrivacyActivity.class));
            }
        });

        txtFAQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), FAQActivity.class));
            }
        });

        txtTentang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), TentangActivity.class));
            }
        });

        txtHubungi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), HubungiKamiActivity.class));
            }
        });

        txtLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
                alertDialog.setTitle("BawaBerkah");
                alertDialog.setIcon(R.drawable.ic_baber);
                alertDialog.setMessage("Apakah anda ingin Logout?");
                alertDialog.setIcon(R.drawable.ic_baber);
                alertDialog.setPositiveButton("IYA", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        logout();

                    }
                });

                alertDialog.setNegativeButton("TIDAK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).create().show();
            }
        });

        return view;
    }

    private void logout() {
        SharedPreferences pref = getActivity().getSharedPreferences("baber", Context.MODE_PRIVATE);
        String userid = pref.getString("idName","");

        try{
            JSONObject obj = new JSONObject();
            obj.put("phone_number",userid);

            jsonLogout = obj.toString(2);
            Log.d("output", obj.toString(2));

        } catch (JSONException e){
            e.printStackTrace();
        }

        String url = getString(R.string.url_header)+"/v1/apps/logout?username=bawaberkahudin&password=17aug1945";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onResponse(String respons) {

                        Log.d("RESPON LOG", ">>" + respons);

                        try {
                            JSONObject object = new JSONObject(respons);
                            String respon = object.getString("message");
                            int a = userid.length();
                            System.out.println("JODY MEMEK = "+a);

                            if (a > 12){
                                mGoogleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Toast.makeText(getActivity(), "Successfully signed out", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                                        startActivity(intent);
                                        startActivity(intent);
                                        getActivity().finish();
                                        System.exit(0);
                                    }
                                });
                            } else {
                                Intent intent = new Intent(getActivity(), LoginActivity.class);
                                startActivity(intent);
                                startActivity(intent);
                                getActivity().finish();
                                System.exit(0);
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
                    return jsonLogout == null ? null : jsonLogout.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee){
                    return null;
                }
            }
        };

        // request queue
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }

    private void signOut (){
        mGoogleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(getActivity(), "Successfully signed out", Toast.LENGTH_SHORT).show();

            }
        });
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
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completeTask){
        try{
            GoogleSignInAccount account = completeTask.getResult(ApiException.class);
            String personName = account.getDisplayName();
            String personEmail = account.getEmail();
            Uri personPhoto = account.getPhotoUrl();
            String personId = account.getId();

            System.out.println("EMAIL ANDA = " +personEmail);

            namaDonatur.setText(personName);
            emailDonatur.setText(personEmail);
            idGoogle.setText(personId);

            //Glide.with(this).load(personPhoto).into(photo);

            String idGoogleAnda = idGoogle.getText().toString();

            if (idGoogleAnda.equals("") || idGoogleAnda.equals(null)){

            } else {

            }

        } catch (ApiException e){
            Toast.makeText(getActivity(), "Failed to connect Google", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onStart(){
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getActivity());
        if (acct != null){
            String personName = acct.getDisplayName();
            String personEmail = acct.getEmail();
            String personId = acct.getId();
            Uri personPhoto = acct.getPhotoUrl();

            namaDonatur.setText(personName);
            emailDonatur.setText(personEmail);
            idGoogle.setText(personId);

            System.out.println("EMAIL ANDA = " +personEmail);

            String idGoogleAnda = idGoogle.getText().toString();

            if (idGoogleAnda.equals("") || idGoogleAnda.equals(null)){

            } else {


            }

        }
        super.onStart();
    }


}

package org.bawaberkah.app.Fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.bawaberkah.app.Util.NumberTextWatcherForThousand;
import org.bawaberkah.app.R;
import org.bawaberkah.app.Util.Util;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class ZakatPenghasilanFragment extends Fragment {

    EditText txtGaji, txtPenghasilanLain, txtHutang, txtHargaBeras, txtJumlahPenghasilan, txtPenghasilanLainEnd, txtHutangSaya, txtJumlahPenghasilanPerbulan;
    Button btnLanjutkan, btnPenghasilan;
    private String userUrl = "bawaberkahudin";
    private String passUrl = "17aug1945";
    private int harga_gabah;
    String hasil, zakat;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_zakat_penghasilan, container, false);

        txtGaji = view.findViewById(R.id.txtGaji);
        txtPenghasilanLain = view.findViewById(R.id.txtPenghasilanLain);
        txtHutang = view.findViewById(R.id.txtHutang);
        txtHargaBeras = view.findViewById(R.id.txtHargaBeras);
        txtJumlahPenghasilan = view.findViewById(R.id.txtJumlahPenghasilan);
        txtPenghasilanLainEnd = view.findViewById(R.id.txtPenghasilanLainEnd);
        txtHutangSaya = view.findViewById(R.id.txtHutangSaya);
        txtJumlahPenghasilanPerbulan = view.findViewById(R.id.txtJumlahPenghasilanPerbulan);
        btnLanjutkan = view.findViewById(R.id.btnLanjutkanZakat);

        txtGaji.addTextChangedListener(new NumberTextWatcherForThousand(txtGaji));
        txtPenghasilanLain.addTextChangedListener(new NumberTextWatcherForThousand(txtPenghasilanLain));
        txtHutang.addTextChangedListener(new NumberTextWatcherForThousand(txtHutang));
        txtHargaBeras.addTextChangedListener(new NumberTextWatcherForThousand(txtHargaBeras));
        txtJumlahPenghasilan.addTextChangedListener(new NumberTextWatcherForThousand(txtJumlahPenghasilan));
        txtPenghasilanLainEnd.addTextChangedListener(new NumberTextWatcherForThousand(txtPenghasilanLainEnd));
        txtHutangSaya.addTextChangedListener(new NumberTextWatcherForThousand(txtHutangSaya));
        txtJumlahPenghasilanPerbulan.addTextChangedListener(new NumberTextWatcherForThousand(txtJumlahPenghasilanPerbulan));

        loadGabah();

        txtHargaBeras.setText("0");
        txtHargaBeras.setEnabled(false);

        txtHutang.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                hitungZakat();
                return false;
            }
        });

        txtGaji.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (txtHargaBeras.getText().toString().equals("0")||txtHargaBeras.getText().toString() == null){

                }
                txtJumlahPenghasilanPerbulan.setText(txtGaji.getText().toString());
                txtJumlahPenghasilan.setText(txtGaji.getText().toString());
                return false;
            }
        });

        txtPenghasilanLain.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                String a = txtGaji.getText().toString();
                String b = txtPenghasilanLain.getText().toString();

                if (a.equals(null) || a.equals("")) {
                    a = "0";
                } else if (b.equals(null) || b.equals("")) {
                    b="0";
                }  else {
                    if (a.contains(",")) {
                        a = a.replace(",", "");
                    } else {
                        a = a.replace(".", "");
                    }

                    if (b.contains(",")) {
                        b = b.replace(",", "");
                    } else {
                        b = b.replace(".", "");
                    }
                }

                System.out.println("A = "+a);
                System.out.println("B = "+b);

                int x = Integer.parseInt(a);
                int y = Integer.parseInt(b);

                System.out.println("X = "+x);
                System.out.println("Y = "+y);

                int hasil = (x + y);

                System.out.println("HASIL = "+hasil);

                txtJumlahPenghasilan.setText(String.valueOf(hasil));
                txtJumlahPenghasilanPerbulan.setText(String.valueOf(hasil));
                txtPenghasilanLainEnd.setText(txtPenghasilanLain.getText().toString());


                return false;
            }
        });

        btnLanjutkan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TransaksiFragment bottomSheetFragment = new TransaksiFragment();
                Bundle data = new Bundle();
                data.putString("nominalZakat", zakat.replace(",",""));
                data.putString("kdTransaksi", "zakatPenghasilan");
                bottomSheetFragment.setArguments(data);
                bottomSheetFragment.show(getFragmentManager(), bottomSheetFragment.getTag());
            }
        });



        return view;
    }

    private void loadGabah() {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, getString(R.string.url_header)+"/v1/apps/harga_gabah?username="+userUrl+"&password="+passUrl,
                new Response.Listener<String>() {
                    @SuppressLint("NewApi")
                    @Override
                    public void onResponse(String response) {
                        /*productList.clear();*/
                        try {
                            //converting the string to json array object

                            Log.d("strrrrr", ">>" + response);

                            JSONObject jObj = new JSONObject(response);
                            //getting product object from json array
                            harga_gabah = Integer.parseInt(jObj.getString("harga_gabah_final"));

                            System.out.println("HARGA GABAH = " + harga_gabah);

                            txtHargaBeras.setText(String.valueOf(harga_gabah));

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

    private void cleartext() {
        txtJumlahPenghasilan.setText("0");
    }

    @SuppressLint({"ShowToast", "NewApi"})
    private void hitungZakat() {

        if (txtHutang.getText().toString().isEmpty()){
            Util.showmsg(getActivity(),"Peringatan!","Hutang cicilan belum terisi");
        } else if (txtGaji.getText().toString().isEmpty()){
            Util.showmsg(getActivity(),"Peringatan!","Gaji belum terisi");
        } if (txtPenghasilanLain.getText().toString().isEmpty()){
            Util.showmsg(getActivity(),"Peringatan!","Penghasilan lain-lain belum terisi");

        } else {
            String gaji = txtGaji.getText().toString().replace(",","");
            String penghasilanLain = txtPenghasilanLain.getText().toString().replace(",","");
            String hutang = txtHutang.getText().toString().replace(",","");

            int nomGaji = Integer.parseInt(gaji);
            int nomPenghasilanLain = Integer.parseInt(penghasilanLain);
            int nomHutang = Integer.parseInt(hutang);

            int jumlahPenghasilanBulan = ((nomGaji - nomHutang)+nomPenghasilanLain);



            hasil = String.valueOf(jumlahPenghasilanBulan);

            System.out.println("GAJI = "+hasil);

            txtJumlahPenghasilan.setText(hasil);
            txtJumlahPenghasilanPerbulan.setText(hasil);
            txtHutangSaya.setText(txtHutang.getText().toString());

            if (Integer.parseInt(hasil) > harga_gabah){

                int z = (Integer.parseInt(hasil) * 25)/1000;

                System.out.println("JON = "+String.valueOf(z));

                NumberFormat formatter = new DecimalFormat("#,###");
                zakat = formatter.format(z);

                btnLanjutkan.setBackgroundResource(R.drawable.custom_button_ungu);
                btnLanjutkan.setEnabled(true);


                Util.showmsg(getActivity(),"Bawaberkah", "Zakat anda Rp."+zakat+". Silahkan Lanjutkan ke Pembayaran");
            } else {
                Util.showmsg(getActivity(),"Bawaberkah", "Anda belum tergolong Zakat");
                btnLanjutkan.setEnabled(false);
            }
        }

    }

}

package org.bawaberkah.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.bawaberkah.app.Adapter.SpinnerMetodePembayaranAdapter;
import org.bawaberkah.app.Activity.BillTransaksiActivity;
import org.bawaberkah.app.Util.NumberTextWatcherForThousand;
import org.bawaberkah.app.Model.SpinnerPembayaranModel;
import org.bawaberkah.app.R;
import org.bawaberkah.app.Util.Util;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class TransaksiFragment extends BottomSheetDialogFragment {

    Spinner spinMetodePembayaran;
    EditText nominalDoansi, pesanBaik;
    CheckBox rbtAnonim;
    Button btnLanjutkan;
    TextView txtTipeTransaksi;

    private ArrayList<SpinnerPembayaranModel> mMetodeList;
    private SpinnerAdapter mAdapter;
    private String idSpin;
    String idCampaign, judulCampaign, kdTransaksi, metodePembayaran, zakat, rekBCA, rekBNI, rekBRI, rekMandiri, rekening;

    public String getIdSpin() {
        return idSpin;
    }

    public void setIdSpin(String idSpin) {
        this.idSpin = idSpin;
    }

    public TransaksiFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_transaksi, container, false);

        spinMetodePembayaran = view.findViewById(R.id.spinMetodePembayaran);
        nominalDoansi = view.findViewById(R.id.txtNominalDonasi);
        pesanBaik = view.findViewById(R.id.txtPesanDonatur);
        rbtAnonim = view.findViewById(R.id.rbtnAnonim);
        btnLanjutkan = view.findViewById(R.id.btnLanjutkanPembayaran);
        txtTipeTransaksi = view.findViewById(R.id.txtTipeTransaksi);

        idCampaign = getArguments().getString("idCampaign");
        judulCampaign = getArguments().getString("namaCampaign");
        kdTransaksi = getArguments().getString("kdTransaksi");
        zakat = getArguments().getString("nominalZakat");
        System.out.println("ID JON = "+idCampaign);
        System.out.println("NAMA JON = "+judulCampaign);
        System.out.println("NAMA JON = "+kdTransaksi);
        System.out.println("ZAKAT = "+zakat);

        getRekening();

        if (kdTransaksi.equals("wakaf")){
            txtTipeTransaksi.setText("Nominal Wakaf");
        } else if (kdTransaksi.equals("donasi")){
            txtTipeTransaksi.setText("Nominal Donasi");
        } else if (kdTransaksi.equals("zakatPenghasilan")) {
            txtTipeTransaksi.setText("Nominal Zakat");
            nominalDoansi.setText(zakat.replace(".",","));
            nominalDoansi.setEnabled(false);
        } else if (kdTransaksi.equals("zakatMaal")) {
            txtTipeTransaksi.setText("Nominal Zakat");
            nominalDoansi.setText(zakat.replace(".",","));
            nominalDoansi.setEnabled(false);
        } else {
            txtTipeTransaksi.setText("Nominal Donasi");
        }

        btnLanjutkan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nominalDoansi.getText().toString().isEmpty()){
                    Util.showmsg(getActivity(),"","Nominal Donasi tidak boleh Kosong");
                } else {
                    final Double nilai = Double.parseDouble(nominalDoansi.getText().toString().replace(",",""));
                    if (kdTransaksi.equals("ovo")){
                        lanjutOvo();
                    } else if (kdTransaksi.equals("barzah")) {
                        lanjutBarzah();
                    } else if (kdTransaksi.equals("wakaf")){
                        if (nilai < 50000){
                            Toast.makeText(getActivity(),"Nilai minimal untuk wakaf adalah Rp. 50.000,-",Toast.LENGTH_LONG).show();
                        } else {
                            lanjutWakaf();
                        }
                    } else if (kdTransaksi.equals("zakatPenghasilan")) {
                        lanjut();
                    } else {
                        lanjut();
                    }
                }
            }
        });

        nominalDoansi.addTextChangedListener(new NumberTextWatcherForThousand(nominalDoansi));

        initList();

        mAdapter = new SpinnerMetodePembayaranAdapter(getActivity(), mMetodeList);
        spinMetodePembayaran.setAdapter(mAdapter);

        spinMetodePembayaran.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SpinnerPembayaranModel clickedItem = (SpinnerPembayaranModel) parent.getItemAtPosition(position);
                String metodeDipilih = clickedItem.getMetode();
                switch (position) {
                    /*case 0:
                        Toast.makeText(getActivity(), metodeDipilih + " selected", Toast.LENGTH_SHORT).show();
                        setIdSpin("1");
                        metodePembayaran = "dana";
                        break;*/
                    case 0:
                        Toast.makeText(getActivity(), metodeDipilih + " selected", Toast.LENGTH_SHORT).show();
                        setIdSpin("2");
                        metodePembayaran = "ovo";
                        break;
                    case 1:
                        Toast.makeText(getActivity(), metodeDipilih + " selected", Toast.LENGTH_SHORT).show();
                        setIdSpin("3");
                        metodePembayaran = "bca";
                        rekening = rekBCA;
                        break;
                    case 2:
                        Toast.makeText(getActivity(), metodeDipilih + " selected", Toast.LENGTH_SHORT).show();
                        setIdSpin("4");
                        metodePembayaran = "bni";
                        rekening = rekBNI;
                        break;
                    case 3:
                        Toast.makeText(getActivity(), metodeDipilih + " selected", Toast.LENGTH_SHORT).show();
                        setIdSpin("5");
                        metodePembayaran = "bri";
                        rekening = rekBRI;
                        break;
                    case 4:
                        Toast.makeText(getActivity(), metodeDipilih + " selected", Toast.LENGTH_SHORT).show();
                        setIdSpin("6");
                        metodePembayaran = "alfamart";
                        rekening = rekMandiri;
                        break;
                    case 5:
                        Toast.makeText(getActivity(), metodeDipilih + " selected", Toast.LENGTH_SHORT).show();
                        setIdSpin("7");
                        metodePembayaran = "alfamart";
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return view;
    }

    private void getRekening() {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, getString(R.string.url_header)+"/v1/apps/banks?username=bawaberkahudin&password=17aug1945",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //converting the string to json array object

                            Log.d("strrrrr", ">>" + response);

                            JSONObject jObj = new JSONObject(response);
                            rekBCA = jObj.getString("BCA");
                            rekBNI = jObj.getString("BNI_Syariah");
                            rekBRI = jObj.getString("BRI");
                            rekMandiri = jObj.getString("Mandiri");

                            System.out.println("rekBCA = "+rekBCA);
                            System.out.println("rekBNI = "+rekBNI);
                            System.out.println("rekBRI = "+rekBRI);
                            System.out.println("rekMandiri = "+rekMandiri);

                            //getting product object from json array

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("strrrrr", ">>" + error);
                    }
                });

        //adding our stringrequest to queue
        Volley.newRequestQueue(getActivity()).add(stringRequest);

    }

    private void lanjutOvo() {

        String jmlDonasi = nominalDoansi.getText().toString();
        String pesanDonatur = pesanBaik.getText().toString();
        kdTransaksi = getArguments().getString("kdTransaksi");

        if (rbtAnonim.isChecked()){
            Intent i = new Intent (getActivity(), BillTransaksiActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("idCampaign", idCampaign);
            bundle.putString("jumlahDonasi", jmlDonasi);
            bundle.putString("pesanDonatur", pesanDonatur);
            bundle.putInt("kodeAnonim", 1);
            bundle.putString("kdPembayaran", String.valueOf(getIdSpin()));
            bundle.putString("namaCampaign", judulCampaign);
            bundle.putString("kdTransaksi", kdTransaksi);
            bundle.putString("rekening", rekening);

            i.putExtras(bundle);
            startActivity(i);
        } else {
            Intent i = new Intent (getActivity(), BillTransaksiActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("idCampaign", idCampaign);
            bundle.putString("jumlahDonasi", jmlDonasi);
            bundle.putString("pesanDonatur", pesanDonatur);
            bundle.putString("kdPembayaran", String.valueOf(getIdSpin()));
            bundle.putInt("kodeAnonim", 2);
            bundle.putString("kdTransaksi", kdTransaksi);
            bundle.putString("namaCampaign", judulCampaign);
            bundle.putString("rekening", rekening);
            i.putExtras(bundle);
            startActivity(i);
        }

    }

    private void lanjutBarzah() {

        String jmlDonasi = nominalDoansi.getText().toString();
        String pesanDonatur = pesanBaik.getText().toString();
        kdTransaksi = getArguments().getString("kdTransaksi");

        if (rbtAnonim.isChecked()){
            Intent i = new Intent (getActivity(), BillTransaksiActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("idCampaign", idCampaign);
            bundle.putString("jumlahDonasi", jmlDonasi);
            bundle.putString("pesanDonatur", pesanDonatur);
            bundle.putInt("kodeAnonim", 1);
            bundle.putString("kdPembayaran", String.valueOf(getIdSpin()));
            bundle.putString("namaCampaign", judulCampaign);
            bundle.putString("kdTransaksi", kdTransaksi);
            bundle.putString("rekening", rekening);
            i.putExtras(bundle);
            startActivity(i);
        } else {
            Intent i = new Intent (getActivity(), BillTransaksiActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("idCampaign", idCampaign);
            bundle.putString("jumlahDonasi", jmlDonasi);
            bundle.putString("pesanDonatur", pesanDonatur);
            bundle.putString("kdPembayaran", String.valueOf(getIdSpin()));
            bundle.putInt("kodeAnonim", 2);
            bundle.putString("kdTransaksi", kdTransaksi);
            bundle.putString("namaCampaign", judulCampaign);
            bundle.putString("rekening", rekening);
            i.putExtras(bundle);
            startActivity(i);
        }

    }

    private void lanjutWakaf() {

        String jmlDonasi = nominalDoansi.getText().toString();
        String pesanDonatur = pesanBaik.getText().toString();
        kdTransaksi = getArguments().getString("kdTransaksi");

        if (rbtAnonim.isChecked()){
            Intent i = new Intent (getActivity(), BillTransaksiActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("idCampaign", idCampaign);
            bundle.putString("jumlahDonasi", jmlDonasi);
            bundle.putString("pesanDonatur", pesanDonatur);
            bundle.putInt("kodeAnonim", 1);
            bundle.putString("kdPembayaran", String.valueOf(getIdSpin()));
            bundle.putString("namaCampaign", judulCampaign);
            bundle.putString("kdTransaksi", kdTransaksi);
            bundle.putString("rekening", rekening);
            i.putExtras(bundle);
            startActivity(i);
        } else {
            Intent i = new Intent (getActivity(), BillTransaksiActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("idCampaign", idCampaign);
            bundle.putString("jumlahDonasi", jmlDonasi);
            bundle.putString("pesanDonatur", pesanDonatur);
            bundle.putString("kdPembayaran", String.valueOf(getIdSpin()));
            bundle.putInt("kodeAnonim", 2);
            bundle.putString("kdTransaksi", kdTransaksi);
            bundle.putString("namaCampaign", judulCampaign);
            bundle.putString("rekening", rekening);
            i.putExtras(bundle);
            startActivity(i);
        }

    }

    private void lanjut() {

        String jmlDonasi = nominalDoansi.getText().toString();
        String pesanDonatur = pesanBaik.getText().toString();

        if (rbtAnonim.isChecked()){
            Intent i = new Intent (getActivity(), BillTransaksiActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("idCampaign", idCampaign);
            bundle.putString("jumlahDonasi", jmlDonasi);
            bundle.putString("mtdPembayaran", getIdSpin());
            bundle.putInt("kodeAnonim", 1);
            bundle.putString("kdPembayaran", String.valueOf(getIdSpin()));
            bundle.putString("namaCampaign", judulCampaign);
            bundle.putString("kdTransaksi", kdTransaksi);
            bundle.putString("rekening", rekening);
            i.putExtras(bundle);
            startActivity(i);
        } else {
            Intent i = new Intent (getActivity(), BillTransaksiActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("idCampaign", idCampaign);
            bundle.putString("jumlahDonasi", jmlDonasi);
            bundle.putString("pesanDonatur", pesanDonatur);
            bundle.putString("kdPembayaran", String.valueOf(getIdSpin()));
            bundle.putInt("kodeAnonim", 2);
            bundle.putString("namaCampaign", judulCampaign);
            bundle.putString("kdTransaksi", kdTransaksi);
            bundle.putString("rekening", rekening);
            i.putExtras(bundle);
            startActivity(i);
        }
    }

    private void initList() {

        mMetodeList = new ArrayList<>();
        /*mMetodeList.add(new SpinnerPembayaranModel("DANA", R.drawable.ic_dana));*/
        mMetodeList.add(new SpinnerPembayaranModel("OVO", R.drawable.ic_ovo));
        mMetodeList.add(new SpinnerPembayaranModel("BCA", R.drawable.ic_bca));
        mMetodeList.add(new SpinnerPembayaranModel("BNI", R.drawable.ic_bni));
        mMetodeList.add(new SpinnerPembayaranModel("Bank BRI", R.drawable.ic_bri));
        mMetodeList.add(new SpinnerPembayaranModel("Bank Mandiri", R.drawable.ic_mandiri));
        /*mMetodeList.add(new SpinnerPembayaranModel("Alfamart", R.drawable.ic_alfamart));*/
    }
}

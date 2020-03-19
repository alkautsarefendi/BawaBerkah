package org.bawaberkah.app.Fragment;

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
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;


public class ZakatMaalFragment extends Fragment {


    EditText txtHartaTabungan, txtHargaLogam, txtHartaSurat, txtHartaProperti, txtHartaKendaraan,
            txtHartaSeni, txtHartaStok, txtHartaLainnya, txtHartaPiutang, txtJumlahHartaMaal,
            txtHutangJatuhTempo, txtJumlahHartaDihitung, txtHargaEmasSaatIni, txtZakatMaalTahun,
            txtWajibZakat, txtJumlahZakatMaalTahun, txtJumlahZakatMaalBulan;

    Button btnLanjutkanZakat;

    String hTabungan, hLogam, hSurat, hProperti, hKendaraan, hSeni, hStok, hLain, hPiutang, hHutang, zakat, zakatBulan;
    private int harga_emas;

    private String userUrl = "bawaberkahudin";
    private String passUrl = "17aug1945";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_zakat_maal, container, false);

        txtHartaTabungan = view.findViewById(R.id.txtHartaTabungan);
        txtHargaLogam = view.findViewById(R.id.txtHargaLogam);
        txtHartaSurat = view.findViewById(R.id.txtHartaSurat);
        txtHartaProperti = view.findViewById(R.id.txtHartaProperti);
        txtHartaKendaraan = view.findViewById(R.id.txtHartaKendaraan);
        txtHartaSeni = view.findViewById(R.id.txtHartaSeni);
        txtHartaStok = view.findViewById(R.id.txtHargaStok);
        txtHartaLainnya = view.findViewById(R.id.txtHartaLainnya);
        txtHartaPiutang = view.findViewById(R.id.txtHartaPiutang);
        txtJumlahHartaMaal =  view.findViewById(R.id.txtJumlahHartaMaal);
        txtHutangJatuhTempo = view.findViewById(R.id.txtHutangJatuhTempo);
        txtJumlahHartaDihitung = view.findViewById(R.id.txtJumlahHartaDihitung);
        txtHargaEmasSaatIni = view.findViewById(R.id.txtHargaEmasSaatIni);
        txtZakatMaalTahun = view.findViewById(R.id.txtZakatMaalTahun);
        txtWajibZakat = view.findViewById(R.id.txtWajibZakat);
        txtJumlahZakatMaalTahun = view.findViewById(R.id.txtJumlahZakatMaalTahun);
        txtJumlahZakatMaalBulan = view.findViewById(R.id.txtJumlahZakatMaalBulan);

        btnLanjutkanZakat = view.findViewById(R.id.btnLanjutkanZakat);
        btnLanjutkanZakat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lanjut();
            }
        });

        txtHartaTabungan.addTextChangedListener(new NumberTextWatcherForThousand(txtHartaTabungan));
        txtHargaLogam.addTextChangedListener(new NumberTextWatcherForThousand(txtHargaLogam));
        txtHartaSurat.addTextChangedListener(new NumberTextWatcherForThousand(txtHartaSurat));
        txtHartaProperti.addTextChangedListener(new NumberTextWatcherForThousand(txtHartaProperti));
        txtHartaKendaraan.addTextChangedListener(new NumberTextWatcherForThousand(txtHartaKendaraan));
        txtHartaSeni.addTextChangedListener(new NumberTextWatcherForThousand(txtHartaSeni));
        txtHartaStok.addTextChangedListener(new NumberTextWatcherForThousand(txtHartaStok));
        txtHartaLainnya.addTextChangedListener(new NumberTextWatcherForThousand(txtHartaLainnya));
        txtHartaPiutang.addTextChangedListener(new NumberTextWatcherForThousand(txtHartaPiutang));
        txtJumlahHartaMaal.addTextChangedListener(new NumberTextWatcherForThousand(txtJumlahHartaMaal));
        txtHutangJatuhTempo.addTextChangedListener(new NumberTextWatcherForThousand(txtHutangJatuhTempo));
        txtJumlahHartaDihitung.addTextChangedListener(new NumberTextWatcherForThousand(txtJumlahHartaDihitung));
        txtHargaEmasSaatIni.addTextChangedListener(new NumberTextWatcherForThousand(txtHargaEmasSaatIni));
        txtZakatMaalTahun.addTextChangedListener(new NumberTextWatcherForThousand(txtZakatMaalTahun));
        txtWajibZakat.addTextChangedListener(new NumberTextWatcherForThousand(txtWajibZakat));
        txtJumlahZakatMaalTahun.addTextChangedListener(new NumberTextWatcherForThousand(txtJumlahZakatMaalTahun));
        txtJumlahZakatMaalBulan.addTextChangedListener(new NumberTextWatcherForThousand(txtJumlahZakatMaalBulan));

        loadNisab();

        txtHartaTabungan.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                txtJumlahHartaMaal.setText(hTabungan);

                return false;
            }
        });

        txtHargaLogam.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (txtHargaLogam.getText().toString().isEmpty()){
                    Util.showmsg(getActivity(),"Peringatan","Masukkan Harta Emas");
                } else {
                    hTabungan = txtHartaTabungan.getText().toString().replace(",","");
                    hLogam = txtHargaLogam.getText().toString().replace(",","");

                    Double a = Double.parseDouble(hTabungan);
                    Double b = Double.parseDouble(hLogam);
                    Double x = a + b;

                    NumberFormat nf = NumberFormat.getCurrencyInstance();
                    DecimalFormatSymbols decimalFormatSymbols = ((DecimalFormat) nf).getDecimalFormatSymbols();
                    decimalFormatSymbols.setCurrencySymbol("");
                    ((DecimalFormat) nf).setDecimalFormatSymbols(decimalFormatSymbols);

                    String hasil = nf.format(x).trim();
                    hasil = hasil.substring(0,hasil.length()-3);

                    System.out.println("TABUNGAN = "+hasil);

                    txtJumlahHartaMaal.setText(hasil);
                }

                return false;
            }
        });

        txtHartaSurat.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                hTabungan = txtHartaTabungan.getText().toString().replace(",","");
                hLogam = txtHargaLogam.getText().toString().replace(",","");
                hSurat = txtHartaSurat.getText().toString().replace(",","");

                Double a = Double.parseDouble(hTabungan);
                Double b = Double.parseDouble(hLogam);
                Double c = Double.parseDouble(hSurat);
                Double x = a + b + c;

                NumberFormat nf = NumberFormat.getCurrencyInstance();
                DecimalFormatSymbols decimalFormatSymbols = ((DecimalFormat) nf).getDecimalFormatSymbols();
                decimalFormatSymbols.setCurrencySymbol("");
                ((DecimalFormat) nf).setDecimalFormatSymbols(decimalFormatSymbols);

                String hasil = nf.format(x).trim();
                hasil = hasil.substring(0,hasil.length()-3);

                System.out.println("TABUNGAN = "+hasil);

                txtJumlahHartaMaal.setText(hasil);
                return false;
            }
        });

        txtHartaProperti.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                hTabungan = txtHartaTabungan.getText().toString().replace(",","");
                hLogam = txtHargaLogam.getText().toString().replace(",","");
                hSurat = txtHartaSurat.getText().toString().replace(",","");
                hProperti = txtHartaProperti.getText().toString().replace(",","");

                Double a = Double.parseDouble(hTabungan);
                Double b = Double.parseDouble(hLogam);
                Double c = Double.parseDouble(hSurat);
                Double d = Double.parseDouble(hProperti);
                Double x = a + b + c + d;

                NumberFormat nf = NumberFormat.getCurrencyInstance();
                DecimalFormatSymbols decimalFormatSymbols = ((DecimalFormat) nf).getDecimalFormatSymbols();
                decimalFormatSymbols.setCurrencySymbol("");
                ((DecimalFormat) nf).setDecimalFormatSymbols(decimalFormatSymbols);

                String hasil = nf.format(x).trim();
                hasil = hasil.substring(0,hasil.length()-3);

                System.out.println("TABUNGAN = "+hasil);

                txtJumlahHartaMaal.setText(hasil);

                return false;
            }
        });

        txtHartaKendaraan.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                hTabungan = txtHartaTabungan.getText().toString().replace(",","");
                hLogam = txtHargaLogam.getText().toString().replace(",","");
                hSurat = txtHartaSurat.getText().toString().replace(",","");
                hProperti = txtHartaProperti.getText().toString().replace(",","");
                hKendaraan = txtHartaKendaraan.getText().toString().replace(",","");

                Double a = Double.parseDouble(hTabungan);
                Double b = Double.parseDouble(hLogam);
                Double c = Double.parseDouble(hSurat);
                Double d = Double.parseDouble(hProperti);
                Double e = Double.parseDouble(hKendaraan);
                Double x = a + b + c + d + e;

                NumberFormat nf = NumberFormat.getCurrencyInstance();
                DecimalFormatSymbols decimalFormatSymbols = ((DecimalFormat) nf).getDecimalFormatSymbols();
                decimalFormatSymbols.setCurrencySymbol("");
                ((DecimalFormat) nf).setDecimalFormatSymbols(decimalFormatSymbols);

                String hasil = nf.format(x).trim();
                hasil = hasil.substring(0,hasil.length()-3);

                System.out.println("TABUNGAN = "+hasil);

                txtJumlahHartaMaal.setText(hasil);

                return false;
            }
        });

        txtHartaSeni.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                hTabungan = txtHartaTabungan.getText().toString().replace(",","");
                hLogam = txtHargaLogam.getText().toString().replace(",","");
                hSurat = txtHartaSurat.getText().toString().replace(",","");
                hProperti = txtHartaProperti.getText().toString().replace(",","");
                hKendaraan = txtHartaKendaraan.getText().toString().replace(",","");
                hSeni = txtHartaSeni.getText().toString().replace(",","");

                Double a = Double.parseDouble(hTabungan);
                Double b = Double.parseDouble(hLogam);
                Double c = Double.parseDouble(hSurat);
                Double d = Double.parseDouble(hProperti);
                Double e = Double.parseDouble(hKendaraan);
                Double f = Double.parseDouble(hSeni);
                Double x = a + b + c + d + e + f;

                NumberFormat nf = NumberFormat.getCurrencyInstance();
                DecimalFormatSymbols decimalFormatSymbols = ((DecimalFormat) nf).getDecimalFormatSymbols();
                decimalFormatSymbols.setCurrencySymbol("");
                ((DecimalFormat) nf).setDecimalFormatSymbols(decimalFormatSymbols);

                String hasil = nf.format(x).trim();
                hasil = hasil.substring(0,hasil.length()-3);

                System.out.println("TABUNGAN = "+hasil);

                txtJumlahHartaMaal.setText(hasil);

                return false;
            }
        });

        txtHartaStok.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                hTabungan = txtHartaTabungan.getText().toString().replace(",","");
                hLogam = txtHargaLogam.getText().toString().replace(",","");
                hSurat = txtHartaSurat.getText().toString().replace(",","");
                hProperti = txtHartaProperti.getText().toString().replace(",","");
                hKendaraan = txtHartaKendaraan.getText().toString().replace(",","");
                hSeni = txtHartaSeni.getText().toString().replace(",","");
                hStok = txtHartaStok.getText().toString().replace(",","");

                Double a = Double.parseDouble(hTabungan);
                Double b = Double.parseDouble(hLogam);
                Double c = Double.parseDouble(hSurat);
                Double d = Double.parseDouble(hProperti);
                Double e = Double.parseDouble(hKendaraan);
                Double f = Double.parseDouble(hSeni);
                Double g = Double.parseDouble(hStok);
                Double x = a + b + c + d + e + f + g;

                NumberFormat nf = NumberFormat.getCurrencyInstance();
                DecimalFormatSymbols decimalFormatSymbols = ((DecimalFormat) nf).getDecimalFormatSymbols();
                decimalFormatSymbols.setCurrencySymbol("");
                ((DecimalFormat) nf).setDecimalFormatSymbols(decimalFormatSymbols);

                String hasil = nf.format(x).trim();
                hasil = hasil.substring(0,hasil.length()-3);

                System.out.println("TABUNGAN = "+hasil);

                txtJumlahHartaMaal.setText(hasil);

                return false;
            }
        });

        txtHartaLainnya.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                hTabungan = txtHartaTabungan.getText().toString().replace(",","");
                hLogam = txtHargaLogam.getText().toString().replace(",","");
                hSurat = txtHartaSurat.getText().toString().replace(",","");
                hProperti = txtHartaProperti.getText().toString().replace(",","");
                hKendaraan = txtHartaKendaraan.getText().toString().replace(",","");
                hSeni = txtHartaSeni.getText().toString().replace(",","");
                hStok = txtHartaStok.getText().toString().replace(",","");
                hLain = txtHartaLainnya.getText().toString().replace(",","");

                Double a = Double.parseDouble(hTabungan);
                Double b = Double.parseDouble(hLogam);
                Double c = Double.parseDouble(hSurat);
                Double d = Double.parseDouble(hProperti);
                Double e = Double.parseDouble(hKendaraan);
                Double f = Double.parseDouble(hSeni);
                Double g = Double.parseDouble(hStok);
                Double h = Double.parseDouble(hLain);
                Double x = a + b + c + d + e + f + g + h;

                NumberFormat nf = NumberFormat.getCurrencyInstance();
                DecimalFormatSymbols decimalFormatSymbols = ((DecimalFormat) nf).getDecimalFormatSymbols();
                decimalFormatSymbols.setCurrencySymbol("");
                ((DecimalFormat) nf).setDecimalFormatSymbols(decimalFormatSymbols);

                String hasil = nf.format(x).trim();
                hasil = hasil.substring(0,hasil.length()-3);

                System.out.println("TABUNGAN = "+hasil);

                txtJumlahHartaMaal.setText(hasil);

                return false;
            }
        });

        txtHartaPiutang.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                hPiutang = txtHartaPiutang.getText().toString().replace(",","");
                hTabungan = txtHartaTabungan.getText().toString().replace(",","");
                hLogam = txtHargaLogam.getText().toString().replace(",","");
                hSurat = txtHartaSurat.getText().toString().replace(",","");
                hProperti = txtHartaProperti.getText().toString().replace(",","");
                hKendaraan = txtHartaKendaraan.getText().toString().replace(",","");
                hSeni = txtHartaSeni.getText().toString().replace(",","");
                hStok = txtHartaStok.getText().toString().replace(",","");
                hLain = txtHartaLainnya.getText().toString().replace(",","");

                Double a = Double.parseDouble(hTabungan);
                Double b = Double.parseDouble(hLogam);
                Double c = Double.parseDouble(hSurat);
                Double d = Double.parseDouble(hProperti);
                Double e = Double.parseDouble(hKendaraan);
                Double f = Double.parseDouble(hSeni);
                Double g = Double.parseDouble(hStok);
                Double h = Double.parseDouble(hLain);
                Double i = Double.parseDouble(hPiutang);
                Double x = a + b + c + d + e + f + g + h + i;

                NumberFormat nf = NumberFormat.getCurrencyInstance();
                DecimalFormatSymbols decimalFormatSymbols = ((DecimalFormat) nf).getDecimalFormatSymbols();
                decimalFormatSymbols.setCurrencySymbol("");
                ((DecimalFormat) nf).setDecimalFormatSymbols(decimalFormatSymbols);

                String hasil = nf.format(x).trim();
                hasil = hasil.substring(0,hasil.length()-3);

                System.out.println("TABUNGAN = "+hasil);

                txtJumlahHartaMaal.setText(hasil);

                return false;
            }
        });

        txtHutangJatuhTempo.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                String jumlahHartaSementara = txtJumlahHartaMaal.getText().toString().replace(",","");
                String hutang = txtHutangJatuhTempo.getText().toString().replace(",","");

                Double a = Double.parseDouble(jumlahHartaSementara);
                Double b = Double.parseDouble(hutang);
                Double c = (a - b);

                NumberFormat nf = NumberFormat.getCurrencyInstance();
                DecimalFormatSymbols decimalFormatSymbols = ((DecimalFormat) nf).getDecimalFormatSymbols();
                decimalFormatSymbols.setCurrencySymbol("");
                ((DecimalFormat) nf).setDecimalFormatSymbols(decimalFormatSymbols);

                String hasil = nf.format(c).trim();
                hasil = hasil.substring(0,hasil.length()-3);

                System.out.println("TABUNGAN = "+hasil);

                txtJumlahHartaDihitung.setText(hasil);

                hitungZakat();

                return false;
            }
        });

        return view;
    }

    private void lanjut() {

        TransaksiFragment bottomSheetFragment = new TransaksiFragment();
        Bundle data = new Bundle();
        data.putString("nominalZakat", zakat.replace(",",""));
        data.putString("kdTransaksi", "zakatMaal");
        bottomSheetFragment.setArguments(data);
        bottomSheetFragment.show(getFragmentManager(), bottomSheetFragment.getTag());

        /*Intent i = new Intent (getActivity(), PaymentZakatActivity.class);
        String nomzakat = zakat.replace(",","");
        i.putExtra("zakatMaal", nomzakat );
        System.out.println("KIRIM ZAKAT = "+nomzakat);
        startActivity(i);*/

    }

    private void hitungZakat() {
        hPiutang = txtHartaPiutang.getText().toString().replace(",","");
        hTabungan = txtHartaTabungan.getText().toString().replace(",","");
        hLogam = txtHargaLogam.getText().toString().replace(",","");
        hSurat = txtHartaSurat.getText().toString().replace(",","");
        hProperti = txtHartaProperti.getText().toString().replace(",","");
        hKendaraan = txtHartaKendaraan.getText().toString().replace(",","");
        hSeni = txtHartaSeni.getText().toString().replace(",","");
        hStok = txtHartaStok.getText().toString().replace(",","");
        hLain = txtHartaLainnya.getText().toString().replace(",","");
        hHutang = txtHutangJatuhTempo.getText().toString().replace(",","");

        if (hTabungan.isEmpty()){
            Util.showmsg(getActivity(),"Peringatan","Harta Tabungan Tidak Boleh Kosong");
        } else if (hLogam.isEmpty()){
            Util.showmsg(getActivity(),"Peringatan","Harta Emas Tidak Boleh Kosong");
        } else if (hSurat.isEmpty()){
            Util.showmsg(getActivity(),"Peringatan","Harta Surat Tidak Boleh Kosong");
        } else if (hProperti.isEmpty()){
            Util.showmsg(getActivity(),"Peringatan","Harta Properti Tidak Boleh Kosong");
        } else if (hKendaraan.isEmpty()){
            Util.showmsg(getActivity(),"Peringatan","Harta Kendaraan Tidak Boleh Kosong");
        } else if (hSeni.isEmpty()){
            Util.showmsg(getActivity(),"Peringatan","Harta Koleksi Tidak Boleh Kosong");
        } else if (hStok.isEmpty()){
            Util.showmsg(getActivity(),"Peringatan","Harta Dagangan Tidak Boleh Kosong");
        } else if (hLain.isEmpty()){
            Util.showmsg(getActivity(),"Peringatan","Harta Lain-lainnya Tidak Boleh Kosong");
        } else if (hPiutang.isEmpty()){
            Util.showmsg(getActivity(),"Peringatan","Harta Piutang Tidak Boleh Kosong");
        } else if (hHutang.isEmpty()){
            Util.showmsg(getActivity(),"Peringatan","Hutang Tidak Boleh Kosong");
        } else {
            String totalHarta = txtJumlahHartaDihitung.getText().toString().replace(",","");
            System.out.println("TOTAL HARTA = "+totalHarta);

            Double a = Double.parseDouble(totalHarta);

            NumberFormat nf = NumberFormat.getCurrencyInstance();
            DecimalFormatSymbols decimalFormatSymbols = ((DecimalFormat) nf).getDecimalFormatSymbols();
            decimalFormatSymbols.setCurrencySymbol("");
            ((DecimalFormat) nf).setDecimalFormatSymbols(decimalFormatSymbols);

            if (a > harga_emas){
                Double z = (a * 25)/1000;

                zakat = nf.format(z).trim();
                zakat = zakat.substring(0,zakat.length() -3);

                Double bulan = z / 12;

                zakatBulan = nf.format(bulan).trim();
                zakatBulan = zakatBulan.substring(0,zakatBulan.length()-3);

                btnLanjutkanZakat.setBackgroundResource(R.drawable.custom_button_ungu);
                btnLanjutkanZakat.setEnabled(true);

                txtZakatMaalTahun.setText(zakat);
                txtWajibZakat.setText(zakat);
                txtJumlahZakatMaalTahun.setText(zakat);
                txtJumlahZakatMaalBulan.setText(zakatBulan);

                Util.showmsg(getActivity(),"Bawaberkah", "Zakat anda Rp."+zakat+". Silahkan Lanjutkan ke Pembayaran");
            } else {
                Util.showmsg(getActivity(),"Bawaberkah", "Anda belum tergolong Zakat");
                btnLanjutkanZakat.setEnabled(false);
            }


        }

    }

    private void loadNisab() {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, getString(R.string.url_header)+"/v1/apps/harga_emas?username="+userUrl+"&password="+passUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        /*productList.clear();*/
                        try {
                            //converting the string to json array object

                            Log.d("strrrrr", ">>" + response);

                            JSONObject jObj = new JSONObject(response);
                            //getting product object from json array
                            harga_emas = Integer.parseInt(jObj.getString("harga_emas_final"));

                            System.out.println("HARGA NISAB = " + harga_emas);

                            NumberFormat nf = NumberFormat.getCurrencyInstance();
                            DecimalFormatSymbols decimalFormatSymbols = ((DecimalFormat) nf).getDecimalFormatSymbols();
                            decimalFormatSymbols.setCurrencySymbol("");
                            ((DecimalFormat) nf).setDecimalFormatSymbols(decimalFormatSymbols);

                            String gabah = nf.format(harga_emas).trim();
                            gabah = gabah.substring(0, gabah.length() -3);

                            txtHargaEmasSaatIni.setText(gabah);

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

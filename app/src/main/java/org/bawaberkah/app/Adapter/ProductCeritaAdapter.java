package org.bawaberkah.app.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.bawaberkah.app.Model.ProductCerita;
import org.bawaberkah.app.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ProductCeritaAdapter extends RecyclerView.Adapter<ProductCeritaAdapter.ProductViewHolder> {

    private Context mCtx;
    private List<ProductCerita> productList;
    private ProductCeritaAdapter.OnItemClickListener mListener;
    private String x, hasil;


    public interface OnItemClickListener {
        void onDonasiClick(int position);

        void onDetailClick(int position);

        void onImageClick(int position);

        void onJudulClick(int position);
    }

    public void setOnItemClickListener(ProductCeritaAdapter.OnItemClickListener listener) {
        mListener = listener;
    }


    public ProductCeritaAdapter(Context mCtx, List<ProductCerita> productList) {
        this.mCtx = mCtx;
        this.productList = productList;
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.row_cerita, null);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {
        ProductCerita product = productList.get(position);

        //Campaign End Date

        //loading the image
        Glide.with(mCtx)
                .load(product.getGambar())
                .into(holder.gambarCerita);

        holder.judulCerita.setText(product.getJudul());
        holder.wilayahCerita.setText(product.getWilayah());
        holder.tglCerita.setText(product.getTanggal());
        holder.waktuCerita.setText(product.getWaktu());
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    class ProductViewHolder extends RecyclerView.ViewHolder {

        TextView judulCerita, wilayahCerita, tglCerita, waktuCerita;
        ImageView gambarCerita;
        Button btnDetailCerita;

        public ProductViewHolder(View itemView) {
            super(itemView);

            judulCerita = (TextView) itemView.findViewById(R.id.judulCerita);
            wilayahCerita = (TextView) itemView.findViewById(R.id.lokasiCerita);
            gambarCerita = (ImageView) itemView.findViewById(R.id.gambarCerita);
            btnDetailCerita = (Button) itemView.findViewById(R.id.btnWakaf);
            tglCerita = itemView.findViewById(R.id.tglCerita);
            waktuCerita = itemView.findViewById(R.id.waktuCerita);
            btnDetailCerita = itemView.findViewById(R.id.btnDetailCerita);

            btnDetailCerita.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            mListener.onDonasiClick(position);
                        }
                    }
                }
            });


            /*btnDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mListener != null) {
                        int position = getAdapterPosition();

                        if (position != RecyclerView.NO_POSITION) {
                            mListener.onDetailClick(position);
                        }
                    }
                }
            });*/

            gambarCerita.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            mListener.onImageClick(position);
                        }
                    }
                }
            });

            judulCerita.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            mListener.onJudulClick(position);
                        }
                    }
                }
            });
        }

    }

    public void filterList(ArrayList<ProductCerita> filteredList) {
        productList.clear();
        productList.addAll(filteredList);
        //productList = filteredList;
        notifyDataSetChanged();
    }

    private static long daysBetween(Calendar tanggalAwal, Calendar tanggalAkhir) {
        long lama = 0;
        Calendar tanggal = (Calendar) tanggalAwal.clone();
        while (tanggal.before(tanggalAkhir)) {
            tanggal.add(Calendar.DAY_OF_MONTH, 1);
            lama++;
        }
        return lama;
    }

}

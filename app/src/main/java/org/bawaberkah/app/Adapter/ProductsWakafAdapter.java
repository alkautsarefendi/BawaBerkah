package org.bawaberkah.app.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.bawaberkah.app.Model.ProductWakaf;
import org.bawaberkah.app.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ProductsWakafAdapter extends RecyclerView.Adapter<ProductsWakafAdapter.ProductViewHolder> {

    private Context mCtx;
    private List<ProductWakaf> productList;
    private ProductsAdapter.OnItemClickListener mListener;
    private RecyclerView currentItem;
    private String x, hasil;

    public interface OnItemClickListener {
        void onDonasiClick(int position);

        void onDetailClick(int position);

        void onImageClick(int position);

        void onJudulClick(int position);
    }

    public void setOnItemClickListener(ProductsAdapter.OnItemClickListener listener) {
        mListener = listener;
    }


    public ProductsWakafAdapter(Context mCtx, List<ProductWakaf> productList) {
        this.mCtx = mCtx;
        this.productList = productList;
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.row_campaign_wakaf, null);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {
        ProductWakaf product = productList.get(position);

        //Campaign End Date

        //loading the image
        Glide.with(mCtx)
                .load(product.getPath())
                .into(holder.gambarCampaign);

        Glide.with(mCtx).
                load(product.getPathCampaign()).
                into(holder.gambarCampaigner);

        Log.d("sss", "3"+product.getPath());
        Log.d("sss1", "4"+product.getCampaigner());

        holder.judulCampaign.setText(product.getJudul());
        holder.percentage.setProgress(product.getPercentage());
        holder.danaTerkumpul.setText("Rp. "+product.getTerkumpul());
        holder.campaigner.setText(product.getCampaigner());
        holder.jumDonatur.setText(product.getJumDonatur());
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    class ProductViewHolder extends RecyclerView.ViewHolder {

        TextView judulCampaign, danaTerkumpul, jumDonatur, campaigner;
        ImageView gambarCampaign, gambarCampaigner;
        Button btnDonasi;
        ProgressBar percentage;

        public ProductViewHolder(View itemView) {
            super(itemView);

            judulCampaign = (TextView) itemView.findViewById(R.id.judulCampaignWakaf);
            danaTerkumpul = (TextView) itemView.findViewById(R.id.danaTerkumpulWakaf);
            percentage = (ProgressBar) itemView.findViewById(R.id.progressfundingWakaf);
            gambarCampaign = (ImageView) itemView.findViewById(R.id.gambarCampaignWakaf);
            gambarCampaigner = (ImageView) itemView.findViewById(R.id.imgCampaignerWakaf);
            btnDonasi = (Button) itemView.findViewById(R.id.btnWakaf);
            jumDonatur = itemView.findViewById(R.id.txtJumDonaturWakaf);
            campaigner = itemView.findViewById(R.id.txtCampaignerWakaf);



            btnDonasi.setOnClickListener(new View.OnClickListener() {
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

            gambarCampaign.setOnClickListener(new View.OnClickListener() {
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

            judulCampaign.setOnClickListener(new View.OnClickListener() {
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

    public void filterList(ArrayList<ProductWakaf> filteredList) {
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

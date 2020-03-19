package org.bawaberkah.app.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.bawaberkah.app.Model.Product;
import org.bawaberkah.app.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ProductViewHolder> {

    private Context mCtx;
    private List<Product> productList;
    private OnItemClickListener mListener;
    private RecyclerView currentItem;
    private String x, hasil;

    public interface OnItemClickListener {
        void onDonasiClick(int position);

        void onDetailClick(int position);

        void onImageClick(int position);

        void onJudulClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }


    public ProductsAdapter(Context mCtx, List<Product> productList) {
        this.mCtx = mCtx;
        this.productList = productList;
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.row_campaign, null);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {
        Product product = productList.get(position);

        //Current Date
        Date current = new Date();
        SimpleDateFormat dateAkhir = new SimpleDateFormat("yyyy-MM-dd");
        String curDate = dateAkhir.format(current);
        String tgl = product.getEnd();
        System.out.println("tgl end1 = "+tgl);

        //Campaign End Date
        DateFormat dateAwal = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date tglAwal = dateAwal.parse(curDate);
            Date tglAkhir = dateAkhir.parse(tgl);
            Date TGLAwal = tglAwal;
            Date TGLAkhir = tglAkhir;
            Calendar cal1 = Calendar.getInstance();
            cal1.setTime(TGLAwal);
            Calendar cal2 = Calendar.getInstance();
            cal2.setTime(TGLAkhir);

            if (tgl == null){
                hasil = "0";
            } else {
                hasil = String.valueOf(daysBetween(cal1, cal2));
            }



        } catch (ParseException e) {

        }

        //loading the image
        Glide.with(mCtx)
                .load(product.getPath())
                .into(holder.gambarCampaign);

        holder.judulCampaign.setText(product.getJudul());
        holder.percentage.setProgress(product.getPercentage());
        holder.targetDana.setText(product.getTarget());
        holder.danaTerkumpul.setText("Rp. "+product.getTerkumpul());
        holder.start.setText(product.getStart());
        holder.end.setText(hasil + " hari");
        holder.progress.setText(String.valueOf(product.getPercentage()));
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    class ProductViewHolder extends RecyclerView.ViewHolder {

        TextView idCampaign, judulCampaign, targetDana, danaTerkumpul, sinopsis, detail, start, end, progress;
        ImageView gambarCampaign;
        Button btnDetail, btnDonasi;
        ProgressBar percentage;

        public ProductViewHolder(View itemView) {
            super(itemView);

            judulCampaign = itemView.findViewById(R.id.judulCampaign);
            targetDana = itemView.findViewById(R.id.targetDana);
            danaTerkumpul = itemView.findViewById(R.id.danaTerkumpul);
            percentage = itemView.findViewById(R.id.progressfunding);
            gambarCampaign = itemView.findViewById(R.id.gambarCampaign);
            btnDonasi = itemView.findViewById(R.id.btnDonasi);
            start = itemView.findViewById(R.id.start);
            end = itemView.findViewById(R.id.end);
            progress = itemView.findViewById(R.id.progress);



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

    public void filterList(ArrayList<Product> filteredList) {
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

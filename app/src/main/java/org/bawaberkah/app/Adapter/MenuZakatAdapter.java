package org.bawaberkah.app.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.bawaberkah.app.Model.MenuZakat;
import org.bawaberkah.app.R;

import java.util.ArrayList;

public class MenuZakatAdapter extends RecyclerView.Adapter<MenuZakatAdapter.ViewHolder> {

    private ArrayList<MenuZakat> arrayList;
    private Context mcontext;

    public MenuZakatAdapter(Context context, ArrayList<MenuZakat> android) {
        this.arrayList = android;
        this.mcontext = context;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int i) {

        holder.imageView.setImageResource(arrayList.get(i).getrecyclerViewImage());
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup vGroup, int i) {

        View view = LayoutInflater.from(vGroup.getContext()).inflate(R.layout.menu_zakat_layout, vGroup, false);
        return new ViewHolder(view);
    }


    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textView;
        private ImageView imageView;

        public ViewHolder(View v) {
            super(v);


            imageView = (ImageView) v.findViewById(R.id.image);
        }
    }
}

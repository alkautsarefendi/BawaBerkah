package org.bawaberkah.app.Adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.bawaberkah.app.Model.DonaturModel;
import org.bawaberkah.app.Model.LogActivityModel;
import org.bawaberkah.app.R;

import java.util.List;

public class LogAktivitasAdapter extends RecyclerView.Adapter<LogAktivitasAdapter.LogViewHolder> {

    private Context mCtx;
    private List<LogActivityModel> log;

    public LogAktivitasAdapter(Context mCtx, List<LogActivityModel> log) {
        this.mCtx = mCtx;
        this.log = log;
    }

    @Override
    public LogViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.row_log, null);
        return new LogAktivitasAdapter.LogViewHolder(view);
    }

    @Override
    public void onBindViewHolder(LogViewHolder holder, int position) {
        LogActivityModel logActivityModel = log.get(position);

        holder.txtAktivity.setText(logActivityModel.getAktivitas());
        holder.txtTglAktivity.setText(logActivityModel.getTglAktivitas());
        holder.txtWaktuAktivity.setText(logActivityModel.getWaktuAktivitas());
    }

    @Override
    public int getItemCount() {
        return log.size();
    }

    class LogViewHolder extends RecyclerView.ViewHolder {

        TextView txtAktivity, txtTglAktivity, txtWaktuAktivity;

        public LogViewHolder(View itemView) {
            super(itemView);

            txtAktivity = itemView.findViewById(R.id.txtActivity);
            txtTglAktivity = itemView.findViewById(R.id.txtTglLog);
            txtWaktuAktivity = itemView.findViewById(R.id.txtWaktuLog);
        }
    }
}

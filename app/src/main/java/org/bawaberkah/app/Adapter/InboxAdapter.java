package org.bawaberkah.app.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.bawaberkah.app.Model.InboxModel;
import org.bawaberkah.app.Model.LogActivityModel;
import org.bawaberkah.app.R;

import java.util.List;

public class InboxAdapter extends RecyclerView.Adapter<InboxAdapter.InboxViewHolder> {

    private Context mCtx;
    private List<InboxModel> inbox;

    public InboxAdapter(Context mCtx, List<InboxModel> inbox) {
        this.mCtx = mCtx;
        this.inbox = inbox;
    }

    @Override
    public InboxViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.row_inbox, null);
        return new InboxViewHolder(view);
    }

    @Override
    public void onBindViewHolder(InboxViewHolder holder, int position) {
        InboxModel inboxModel = inbox.get(position);

        holder.txtAktivity.setText(inboxModel.getIsi_berita());
        holder.txtTglAktivity.setText(inboxModel.getTglBerita());
        holder.txtWaktuAktivity.setText(inboxModel.getWaktuBerita());
    }

    @Override
    public int getItemCount() {
        return inbox.size();
    }

    class InboxViewHolder extends RecyclerView.ViewHolder {

        TextView txtAktivity, txtTglAktivity, txtWaktuAktivity;

        public InboxViewHolder(View itemView) {
            super(itemView);

            txtAktivity = itemView.findViewById(R.id.txtInbox);
            txtTglAktivity = itemView.findViewById(R.id.txtTglInbox);
            txtWaktuAktivity = itemView.findViewById(R.id.txtWaktuInbox);
        }
    }

}

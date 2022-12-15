package com.example.marvel;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class DetailsAdapter extends RecyclerView.Adapter<DetailsAdapter.DetailsViewHolder> {

    ArrayList<CharacterDetail> details;
    private Context context;

    public DetailsAdapter(ArrayList<CharacterDetail> details, Context context) {
        this.details = details;
        this.context = context;
    }

    @NonNull
    @Override
    public DetailsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_detail, parent, false);
        return new DetailsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DetailsViewHolder holder, int position) {
        CharacterDetail characterDetail = details.get(position);
        holder.titleTV.setText(characterDetail.getTitle());

    }

    public static class DetailsViewHolder extends RecyclerView.ViewHolder {

        TextView titleTV;

        public DetailsViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTV = itemView.findViewById(R.id.titleTV);
        }
    }

    @Override
    public int getItemCount() {
        if(details!=null)
            return details.size();
        else
            return 0;
    }
}

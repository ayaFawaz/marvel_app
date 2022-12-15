package com.example.marvel;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class CharactersListAdapter extends RecyclerView.Adapter<CharactersListAdapter.CharactersListViewHolder> {

    private Context context;
    private ArrayList<Character> data;
    private OnItemSelected onItemSelected;


    public CharactersListAdapter(Context context, ArrayList<Character> data, OnItemSelected onItemSelected) {
        this.context = context;
        this.data = data;
        this.onItemSelected = onItemSelected;
    }

    @NonNull
    @Override
    public CharactersListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_character, parent, false);
        return new CharactersListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CharactersListViewHolder holder, int position) {
        Character character = data.get(position);
        if(character.getName()!=null)
            holder.characterNameTV.setText(character.getName());
        if(character.getDescription()!=null)
            holder.descriptionTV.setText(character.getDescription());
        if(character.getThumbnail()!=null) {
            String image = character.getThumbnail().getPath() + "." + character.getThumbnail().getExtension();
            Glide.with(context) //passing context
                    .load(image) //passing your url to load image. //this would be your default image (like default profile or logo etc). it would be loaded at initial time and it will replace with your loaded image once glide successfully load image using url.//in case of any glide exception or not able to download then this image will be appear . if you won't mention this error() then nothing to worry placeHolder image would be remain as it is.
                    .diskCacheStrategy(DiskCacheStrategy.ALL) //using to load into cache then second time it will load fast.// when image (url) will be loaded by glide then this face in animation help to replace url image in the place of placeHolder (default) image.
                    .fitCenter()//this method help to fit image into center of your ImageView
                    .into(holder.characterIV);
        }


        holder.viewRL.setOnClickListener(v -> onItemSelected.onCharacterSelected(character.getId(), character));

    }

    public class CharactersListViewHolder extends RecyclerView.ViewHolder {

        private ImageView characterIV;
        private TextView characterNameTV, descriptionTV;
        private RelativeLayout viewRL;

        public CharactersListViewHolder(@NonNull View itemView) {
            super(itemView);
            characterIV = itemView.findViewById(R.id.characterIV);
            characterNameTV = itemView.findViewById(R.id.characterNameTV);
            descriptionTV = itemView.findViewById(R.id.descriptionTV);
            viewRL = itemView.findViewById(R.id.viewRL);

        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public interface OnItemSelected {
        void onCharacterSelected(Long characterId, Character character);
    }
}

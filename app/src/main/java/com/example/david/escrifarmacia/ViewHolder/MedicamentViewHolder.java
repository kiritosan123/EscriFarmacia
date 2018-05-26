package com.example.david.escrifarmacia.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.david.escrifarmacia.Interface.ItemClickListener;
import com.example.david.escrifarmacia.R;

public class MedicamentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView medicament_name;
    public ImageView medicament_image;

    private ItemClickListener itemClickListener;

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public MedicamentViewHolder(View itemView) {
        super(itemView);

        medicament_name = (TextView)itemView.findViewById(R.id.medicament_name);
        medicament_image = (ImageView)itemView.findViewById(R.id.medicament_image);

        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view, getAdapterPosition(), false);
    }
}

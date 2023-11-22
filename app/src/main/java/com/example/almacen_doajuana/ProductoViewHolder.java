package com.example.almacen_doajuana;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ProductoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView productoNom, productoDescripci,productoprecio,productoCantidad;
    public ImageView productoImagen;
    public ItemClickListener listener;
    public ProductoViewHolder(@NonNull View itemView) {
        super(itemView);

        productoNom = (TextView) itemView.findViewById(R.id.producto_nombre);
        productoDescripci = (TextView) itemView.findViewById(R.id.producto_descripcion);
        productoprecio = (TextView) itemView.findViewById(R.id.producto_precio);
        productoCantidad = (TextView) itemView.findViewById(R.id.producto_cantidad);
        productoImagen = (ImageView) itemView.findViewById(R.id.producto_imagen);
    }

    public void setItemClickListener(ItemClickListener listener){
        this.listener=listener;

    }
    @Override
    public void onClick(View v) {
    listener.onClick(v,getAdapterPosition(),false);

    }
}

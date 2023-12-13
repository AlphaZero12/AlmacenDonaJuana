package com.example.almacen_doajuana;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CarritoViewHolder extends RecyclerView.ViewHolder {
    public TextView carProductoNombre, carProductoCantidad, carProductoPrecio;

    public CarritoViewHolder(@NonNull View itemView) {
        super(itemView);
        carProductoNombre = itemView.findViewById(R.id.car_producto_nombre);
        carProductoCantidad = itemView.findViewById(R.id.car_producto_cantidad);
        carProductoPrecio = itemView.findViewById(R.id.car_producto_precio);
    }
}

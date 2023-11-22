package com.example.almacen_doajuana;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


public class FragmentUno extends Fragment {
    private View fragmento;
    private ImageView manzana,limon,pera;
    private ImageView naranja,platano,tomate;
    private ImageView brocoli,frutilla,ciruela;
    public FragmentUno() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmento = inflater.inflate(R.layout.fragment_uno, container, false);


        manzana =(ImageView) fragmento.findViewById(R.id.manzana);
        limon =(ImageView) fragmento.findViewById(R.id.limon);
        pera =(ImageView) fragmento.findViewById(R.id.pera);

        naranja =(ImageView) fragmento.findViewById(R.id.naranja);
        platano =(ImageView) fragmento.findViewById(R.id.platano);
        tomate =(ImageView) fragmento.findViewById(R.id.tomate);

        brocoli =(ImageView) fragmento.findViewById(R.id.brocoli);
        frutilla =(ImageView) fragmento.findViewById(R.id.frutilla);
        ciruela =(ImageView) fragmento.findViewById(R.id.ciruela);

        manzana.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),AgregarProductoActivity.class);
                intent.putExtra("categoria","manzana");
                startActivity(intent);

            }
        });
        limon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),AgregarProductoActivity.class);
                intent.putExtra("categoria","limon");
                startActivity(intent);

            }
        });
        pera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),AgregarProductoActivity.class);
                intent.putExtra("categoria","pera");
                startActivity(intent);

            }
        });
        naranja.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),AgregarProductoActivity.class);
                intent.putExtra("categoria","naranja");
                startActivity(intent);

            }
        });
        platano.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),AgregarProductoActivity.class);
                intent.putExtra("categoria","platano");
                startActivity(intent);

            }
        });
        tomate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),AgregarProductoActivity.class);
                intent.putExtra("categoria","tomate");
                startActivity(intent);

            }
        });
        brocoli.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),AgregarProductoActivity.class);
                intent.putExtra("categoria","brocoli");
                startActivity(intent);

            }
        });
        frutilla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),AgregarProductoActivity.class);
                intent.putExtra("categoria","frutilla");
                startActivity(intent);

            }
        });
        ciruela.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),AgregarProductoActivity.class);
                intent.putExtra("categoria","ciruela");
                startActivity(intent);

            }
        });


        return fragmento;
    }
}
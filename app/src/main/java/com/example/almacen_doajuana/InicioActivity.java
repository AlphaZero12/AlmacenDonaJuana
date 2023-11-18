package com.example.almacen_doajuana;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class InicioActivity extends AppCompatActivity {

    private Button btnsearch;
    private Button btnProductos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);

        btnProductos = findViewById(R.id.btnProductos);
        btnsearch= findViewById(R.id.btnsearch);

        btnsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Abrir la actividad InicioActivity
                Intent intent = new Intent(InicioActivity.this, Mapa_Activity.class);
                startActivity(intent);
            }
        });


        btnProductos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Abrir la actividad ActivityProductos
                Intent intent = new Intent(InicioActivity.this, ActivityProductos.class);
                startActivity(intent);
            }
        });
    }
}

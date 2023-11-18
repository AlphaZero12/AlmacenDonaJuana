package com.example.almacen_doajuana;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Activity_Usuario_Administrador extends AppCompatActivity {
    private Button botonusuario;
    private Button botonadministrador;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario_administrador);
        botonusuario = findViewById(R.id.botonusuario);
        botonadministrador = findViewById(R.id.botonadministrador);

        botonusuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Activity_Usuario_Administrador.this, InicioActivity.class);
                startActivity(intent);

            }
        });
        botonadministrador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Activity_Usuario_Administrador.this, Login_Admin.class);
                startActivity(intent);
            }
        });
    }
}
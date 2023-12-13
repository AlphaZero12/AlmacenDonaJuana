package com.example.almacen_doajuana;

import static com.example.almacen_doajuana.R.id.dataPicker;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.example.almacen_doajuana.Modal.CartItem;
import com.example.almacen_doajuana.Modal.Productos;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ProductoDetallesActivity extends AppCompatActivity {
    Button agregarCarrito;
    private TextView productoNombre, productoDescripcion, productoPrecio;
    private ImageView productoImagen;
    private String productoID = "", estado = "Normal", CurrentUserID;
    private FirebaseAuth auth;
    private EditText numberPicker;
    private int cantidadProductos = 1;
    private Productos producto;

    @RequiresApi(api = Build.VERSION_CODES.P)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_producto_detalles);
        productoID = getIntent().getStringExtra("pid");
        agregarCarrito = findViewById(R.id.boton_siguiente_detalles);
        productoImagen = findViewById(R.id.producto_imagen_detalles);
        productoPrecio = findViewById(R.id.producto_precio_detalles);
        productoDescripcion = findViewById(R.id.producto_descripcion_detalles);
        productoNombre = findViewById(R.id.producto_nombre_detalles);
        auth = FirebaseAuth.getInstance();
        CurrentUserID = auth.getCurrentUser().getUid();
        numberPicker = findViewById(R.id.dataPicker);
        VereficarEstadoOrden();
        ObtenerDatosProducto(productoID);
        agregarCarrito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (estado.equals("Pedido") || estado.equals("Envidado")) {
                    Toast.makeText(ProductoDetallesActivity.this, "Esperando que el primer pedido termine...", Toast.LENGTH_SHORT).show();
                } else {
                    agregarAlaLista();
                }
            }
        });
    }

    private void agregarAlaLista() {
        String CurrentTime, CurrentDate;
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat data = new SimpleDateFormat("MM-dd-yyyy");
        CurrentDate = data.format(calendar.getTime());

        SimpleDateFormat time = new SimpleDateFormat("HH:mm:ss");
        CurrentTime = time.format(calendar.getTime());

        final DatabaseReference CartListRef = FirebaseDatabase.getInstance().getReference().child("Carrito");
        final HashMap<String, Object> map = new HashMap<>();
        map.put("pid", productoID);
        map.put("nombre", productoNombre.getText().toString());
        map.put("precio", productoPrecio.getText().toString());
        map.put("fecha", CurrentDate);
        map.put("hora", CurrentTime);
        map.put("cantidad", numberPicker.getText().toString());
        map.put("descuento", "");

        CartListRef.child("Usuario Compra").child(CurrentUserID).child("Productos").child(productoID).updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    CartListRef.child("Administracion").child(CurrentUserID).child("Productos").child(productoID).updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(ProductoDetallesActivity.this, "Agregado", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(ProductoDetallesActivity.this, CarritoActivity.class);
                                startActivity(intent);
                            }
                        }
                    });
                }
            }
        });
    }

    private void ObtenerDatosProducto(String productoID) {
        DatabaseReference ProductoRef = FirebaseDatabase.getInstance().getReference().child("Productos");
        ProductoRef.child(productoID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    producto = snapshot.getValue(Productos.class);
                    productoNombre.setText(producto.getNombre());
                    productoDescripcion.setText(producto.getDescripcion());
                    productoPrecio.setText(producto.getPrecioven());

                    Picasso.get().load(producto.getImagen()).into(productoImagen);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void VereficarEstadoOrden() {
        DatabaseReference OrdenRef;
        OrdenRef = FirebaseDatabase.getInstance().getReference().child("Ordenes").child(CurrentUserID);
        OrdenRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String envioStado = snapshot.child("estado").getValue().toString();
                    if (envioStado.equals("Enviado")) {
                        estado = "Enviado";
                    } else if (envioStado.equals("No enviado")) {
                        estado = "Pedido";
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void agregarProductoAlCarrito(String productoId, int cantidad) {
        DatabaseReference cartRef = FirebaseDatabase.getInstance().getReference().child("Carrito");

        // Aquí deberías agregar lógica para obtener detalles del producto, por ejemplo, mediante otra consulta a la base de datos

        // Luego, agregas el producto al carrito
        CartItem carritoItem = new CartItem(productoId, producto.getNombre(), producto.getPrecioven(), Integer.parseInt(numberPicker.getText().toString()));
        cartRef.child("Usuario Compra").child(CurrentUserID).child("Productos").child(productoId).setValue(carritoItem);
    }
}
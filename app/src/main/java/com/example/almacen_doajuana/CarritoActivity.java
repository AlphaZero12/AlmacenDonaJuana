package com.example.almacen_doajuana;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.almacen_doajuana.Modal.Carrito;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class CarritoActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private Button Siguiente;
    private TextView TotalPrecio, mensaje1;
    private double PrecioTotalID = 0.0;
    private String CurrentUserId;
    private FirebaseAuth auth;
    private DatabaseReference ordenRef;
    private static final String TAG = "CarritoActivity";
    private List<Carrito> listaProductosEnCarrito = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carrito);

        recyclerView = findViewById(R.id.carrito_lista);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        Siguiente = findViewById(R.id.siguiente_proceso);
        TotalPrecio = findViewById(R.id.precio_total);
        auth = FirebaseAuth.getInstance();
        CurrentUserId = auth.getCurrentUser().getUid();
        mensaje1 = findViewById(R.id.mensaje1);

        Siguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CarritoActivity.this, ConfirmarordenActivity.class);
                intent.putExtra("Total", String.valueOf(PrecioTotalID));
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        VerificarEstadoOrden();

        final DatabaseReference CartListRef = FirebaseDatabase.getInstance().getReference().child("Carrito");

        FirebaseRecyclerOptions<Carrito> options = new FirebaseRecyclerOptions.Builder<Carrito>()
                .setQuery(CartListRef.child("Usuario Compra").child(CurrentUserId).child("Productos"), Carrito.class).build();

        FirebaseRecyclerAdapter<Carrito, CarritoViewHolder> adapter = new FirebaseRecyclerAdapter<Carrito, CarritoViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CarritoViewHolder holder, int position, @NonNull Carrito model) {
                holder.carProductoNombre.setText(model.getNombre());
                holder.carProductoCantidad.setText("Cantidad: " + model.getCantidad());
                holder.carProductoPrecio.setText("Precio: $" + model.getPrecio());

                // Agregar el producto actual a la lista
                listaProductosEnCarrito.add(model);

                // Calcular y actualizar el precio total
                calcularPrecioTotal();

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mostrarOpcionesProducto(getRef(position).getKey()); // Obtener el ID del producto desde la posición
                    }
                });
            }

            @NonNull
            @Override
            public CarritoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.car_item_layout, parent, false);
                return new CarritoViewHolder(view);
            }
        };

        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    private void VerificarEstadoOrden() {
        ordenRef = FirebaseDatabase.getInstance().getReference().child("Ordenes").child(CurrentUserId);
        // Implementa la lógica según tus necesidades
    }

    private void calcularPrecioTotal() {
        PrecioTotalID = 0.0;

        // Iterar sobre los productos en el carrito y sumar los precios
        for (Carrito carrito : listaProductosEnCarrito) {
            double precioProducto = Double.valueOf(carrito.getPrecio());
            int cantidadProducto = Integer.valueOf(carrito.getCantidad());
            double subtotalProducto = precioProducto * cantidadProducto;
            PrecioTotalID += subtotalProducto;
        }

        // Actualizar el TextView que muestra el total
        TotalPrecio.setText("Total: " + String.valueOf(PrecioTotalID));
    }

    private void mostrarOpcionesProducto(String productoId) {
        CharSequence opciones[] = new CharSequence[]{
                "Editar",
                "Eliminar"
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Opciones del producto");

        builder.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                if (i == 0) {
                    Intent intent = new Intent(CarritoActivity.this, ProductoDetallesActivity.class);
                    intent.putExtra("pid", productoId);
                    startActivity(intent);
                } else if (i == 1) {
                    eliminarProductoDelCarrito(productoId);
                }
            }
        });

        builder.show();
    }


    private void eliminarProductoDelCarrito(String productoId) {
        DatabaseReference CartListRef = FirebaseDatabase.getInstance().getReference().child("Carrito");

        CartListRef.child("Usuario Compra")
                .child(CurrentUserId)
                .child("Productos")
                .child(productoId).removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(CarritoActivity.this, "Producto Eliminado", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(CarritoActivity.this, InicioActivity.class);
                            startActivity(intent);
                        }
                    }
                });
    }
}
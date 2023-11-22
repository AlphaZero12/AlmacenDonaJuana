package com.example.almacen_doajuana;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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

public class CarritoActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private Button Siguiente;
    private TextView TotalPrecio, mensaje1;
    private double PrecioTotalID = 0.0;
    private String CurrentUserId;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carrito);
        recyclerView = (RecyclerView) findViewById(R.id.carrito_lista);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        Siguiente = (Button) findViewById(R.id.siguiente_proceso);
        TotalPrecio=(TextView)findViewById(R.id.precio_total);
        auth=FirebaseAuth.getInstance();
        CurrentUserId=auth.getCurrentUser().getUid();
        mensaje1=(TextView)findViewById(R.id.mensaje1);

        Siguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CarritoActivity.this,ConfirmarordenActivity.class);
                intent.putExtra("Total",String.valueOf(PrecioTotalID));
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        VerificarEstadoOrden();

        TotalPrecio.setText("Total: "+String.valueOf(PrecioTotalID));
        final DatabaseReference CartListRef = FirebaseDatabase.getInstance().getReference().child("Carrito");

        FirebaseRecyclerOptions<Carrito> options = new FirebaseRecyclerOptions.Builder<Carrito>()
                .setQuery(CartListRef.child("Usuario compra").child(CurrentUserId).child("Productos"),Carrito.class).build();

        FirebaseRecyclerAdapter<Carrito,CarritoViewHolder> adapter = new FirebaseRecyclerAdapter<Carrito, CarritoViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CarritoViewHolder holder, int position, @NonNull Carrito model) {
                holder.carProductoNombre.setText(model.getNombre());
                holder.carProductoCantidad.setText(model.getCantidad());
                holder.carProductoPrecio.setText(model.getPrecio());


                double UnTipoPrecio = (Double.valueOf(model.getPrecio()))*Integer.valueOf(model.getCantidad());

                PrecioTotalID = PrecioTotalID+UnTipoPrecio;
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CharSequence options[]= new CharSequence[]{
                                "Editar",
                                "Eliminar"
                        };
                        AlertDialog.Builder builder = new AlertDialog.Builder(CarritoActivity.this);
                        builder.setTitle("Opciones del producto");

                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {
                                if (i==0){
                                    Intent intent= new Intent(CarritoActivity.this,ProductoDetallesActivity.class);
                                    intent.putExtra("pid",model.getPid());
                                    startActivity(intent);
                                }
                                if (i == 1){
                                CartListRef.child("Usuario Compra")
                                        .child(CurrentUserId)
                                        .child("Productos")
                                        .child(model.getPid()).removeValue()
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void>task){
                                                if (task.isSuccessful()){
                                                    Toast.makeText(CarritoActivity.this, "Producto Eliminado", Toast.LENGTH_SHORT).show();
                                                    Intent intent = new Intent(CarritoActivity.this,InicioActivity.class);
                                                    startActivity(intent);
                                                }
                                            }
                                        });
                                }
                            }
                        });
                        builder.show();
                    }
                });


            }

            @NonNull
            @Override
            public CarritoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.car_item_layout,parent,false);
                CarritoViewHolder holder = new CarritoViewHolder(view);

                return holder;
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();




    }

    private void VerificarEstadoOrden() {
    }
}
package com.example.almacen_doajuana;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class AgregarProductoActivity extends AppCompatActivity {
    private ImageView imagen_producto;
    private EditText nombre_producto,descripcion_producto,precio_compra_producto,precio_venta_producto,cantidad_producto;
    private Button agregar_producto;
    private static final int Gallery_Pick = 1;
    private Uri imagenUri;
    private TextView textox;
    private String productoRandomKey, dowloadUri;
    private StorageReference ProductoImagenRef;
    private DatabaseReference ProductoRef;
    private ProgressDialog dialog;
    private String Categoria, Nom , Desc, Preciocom,PrecioVen,Cant, CurrentDate, CurrentTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_producto);

        Categoria = getIntent().getExtras().get("categoria").toString();
        ProductoImagenRef = FirebaseStorage.getInstance().getReference().child("Imagenes Productos");
        ProductoRef = FirebaseDatabase.getInstance().getReference().child("Productos");

        textox = (TextView) findViewById(R.id.textox);
        imagen_producto = (ImageView) findViewById(R.id.imagen_producto);
        nombre_producto = (EditText) findViewById(R.id.nombre_producto);
        descripcion_producto = (EditText) findViewById(R.id.descripcion_producto);
        precio_compra_producto = (EditText) findViewById(R.id.precio_compra_producto);
        precio_venta_producto = (EditText) findViewById(R.id.precio_venta_producto);
        cantidad_producto = (EditText) findViewById(R.id.cantidad_producto);
        agregar_producto = (Button) findViewById(R.id.agregar_producto);
        dialog = new ProgressDialog(this);

        imagen_producto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AbrirGaleria();
            }
        });
        agregar_producto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ValidarProducto();
            }
        });
        textox.setText(Categoria+"\nAgregar producto");
    }
    private void AbrirGaleria() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/");
        startActivityForResult(intent,Gallery_Pick);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==Gallery_Pick && resultCode==RESULT_OK && data !=null){
            imagenUri = data.getData();
            imagen_producto.setImageURI(imagenUri);
        }
    }

    private void ValidarProducto() {
        Nom = nombre_producto.getText().toString();
        Desc = descripcion_producto.getText().toString();
        Preciocom= precio_compra_producto.getText().toString();
        PrecioVen= precio_venta_producto.getText().toString();
        Cant= cantidad_producto.getText().toString();
        if (imagenUri == null){
            Toast.makeText(this, "Primero agrega una imagen", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(Nom)) {
            Toast.makeText(this, "Debes ingresar el nombre del producto", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(Desc)) {
            Toast.makeText(this, "Debes ingresar la descripcion del producto", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(Preciocom)) {
            Toast.makeText(this, "Debes ingresar el precio de compra del producto", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(PrecioVen)) {
            Toast.makeText(this, "Debes ingresar el precio de venta del producto", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(Cant)) {
            Toast.makeText(this, "Debes ingresar la cantidad de productos", Toast.LENGTH_SHORT).show();
        }else{
            GuardarInformacionProducto();
        }

    }

    private void GuardarInformacionProducto() {

        dialog.setTitle("Guardando producto");
        dialog.setMessage("Por favor espere mientras guardamos el producto");
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat curreDataFormat = new SimpleDateFormat("MM-dd-yyyy");
        CurrentDate = curreDataFormat.format(calendar.getTime());

        SimpleDateFormat CurrentTimeFormat = new SimpleDateFormat("HH:mm:ss");
        CurrentTime = CurrentTimeFormat.format(calendar.getTime());

        productoRandomKey = CurrentDate + CurrentTime;

        final StorageReference filePath = ProductoImagenRef.child(imagenUri.getLastPathSegment() + productoRandomKey + ".jpg");
        final UploadTask uploadTask = filePath.putFile(imagenUri);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String mensaje = e.toString();
                Toast.makeText(AgregarProductoActivity.this, "Error:  "+mensaje, Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(AgregarProductoActivity.this, "Imagen guardada exitosamente", Toast.LENGTH_SHORT).show();

                Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()){
                            throw task.getException();
                        }
                        dowloadUri = filePath.getDownloadUrl().toString();
                        return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()){
                            dowloadUri = task.getResult().toString();
                            Toast.makeText(AgregarProductoActivity.this, "Imagen guardada en FireBase", Toast.LENGTH_SHORT).show();
                            GuardarEnFirebase();
                        }else{
                            Toast.makeText(AgregarProductoActivity.this, "Error...", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

    }

    private void GuardarEnFirebase() {
        HashMap<String,Object> map = new HashMap<>();
        map.put("pid",productoRandomKey);
        map.put("fecha",CurrentDate);
        map.put("hora",CurrentTime);
        map.put("descripcion",Desc);
        map.put("nombre",Nom);
        map.put("preciocomp",Preciocom);
        map.put("precioven",PrecioVen);
        map.put("cantidad",Cant);
        map.put("imagen",dowloadUri);
        map.put("categoria",Categoria);

        ProductoRef.child(productoRandomKey).updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Intent intent = new Intent(AgregarProductoActivity.this, AdminActivity.class);
                    startActivity(intent);
                    dialog.dismiss();
                    Toast.makeText(AgregarProductoActivity.this, "Solicitud exitosa!", Toast.LENGTH_SHORT).show();
                }else{
                    dialog.dismiss();
                    String mensaje = task.getException().toString();
                    Toast.makeText(AgregarProductoActivity.this, "Error "+mensaje, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
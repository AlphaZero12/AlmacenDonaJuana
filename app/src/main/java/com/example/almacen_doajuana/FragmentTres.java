package com.example.almacen_doajuana;

import static android.app.Activity.RESULT_OK;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;


public class FragmentTres extends Fragment {
    private View fragmento;
    private EditText nombre,direccion,telefono,ciudad;
    private Button guardar;
    private CircleImageView imagen;
    private FirebaseAuth auth;
    private DatabaseReference UserRef;
    private ProgressDialog dialog;
    private String CurrentUserId;
    private static int Galery_Pick=1;
    private StorageReference UserImagenPerfil;


    public FragmentTres() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmento = inflater.inflate(R.layout.fragment_tres, container, false);
        auth= FirebaseAuth.getInstance();
        CurrentUserId = auth.getCurrentUser().getUid();
        UserRef = FirebaseDatabase.getInstance().getReference().child("Admin");
        dialog = new ProgressDialog(getContext());
        UserImagenPerfil = FirebaseStorage.getInstance().getReference().child("Perfil");
        nombre = (EditText)fragmento.findViewById(R.id.Perfila_nombre);
        ciudad = (EditText)fragmento.findViewById(R.id.Perfila_ciudad);
        direccion = (EditText)fragmento.findViewById(R.id.Perfila_direccion);
        telefono = (EditText)fragmento.findViewById(R.id.Perfila_telefono);
        guardar = (Button)fragmento.findViewById(R.id.Perfila_boton);
        imagen = (CircleImageView) fragmento.findViewById(R.id.Perfila_imagen);

        UserRef.child(CurrentUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists() && snapshot.hasChild("imagen")) {
                    String nombres = snapshot.child("nombre").getValue().toString();
                    String direccions = snapshot.child("direccion").getValue().toString();
                    String ciudads = snapshot.child("ciudad").getValue().toString();
                    String telefonos = snapshot.child("telefono").getValue().toString();
                    String imagens = snapshot.child("imagen").getValue().toString();

                    Picasso.get()
                            .load(imagens)
                            .placeholder(R.drawable.do_a_juana)
                            .into(imagen);
                    nombre.setText(nombres);
                    direccion.setText(direccions);
                    ciudad.setText(ciudads);
                    telefono.setText(telefonos);
                } else if(snapshot.exists()) {
                    String nombres = snapshot.child("nombre").getValue().toString();
                    String direccions = snapshot.child("direccion").getValue().toString();
                    String ciudads = snapshot.child("ciudad").getValue().toString();
                    String telefonos = snapshot.child("telefono").getValue().toString();
                    nombre.setText(nombres);
                    direccion.setText(direccions);
                    ciudad.setText(ciudads);
                    telefono.setText(telefonos);


                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GuardarInformacion();
            }
        });

        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GuardarInformacion();
            }
        });
        imagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/");
                startActivityForResult(intent,Galery_Pick);
            }
        });
        UserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    if (snapshot.hasChild("imagen")){
                        String imagestr = snapshot.child("imagen").getValue().toString();
                        Picasso.get().load(imagestr).into(imagen);
                    }else {
                        Toast.makeText(getContext(),"Por favor selecione una imagen de perfil...",Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return fragmento;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==Galery_Pick && requestCode==RESULT_OK && data != null){
            Uri imageUri =data.getData();

        }
    }

    private void GuardarInformacion() {
        String nombres = nombre.getText().toString().toUpperCase();
        String direccions =direccion.getText().toString();
        String ciudads = ciudad.getText().toString();
        String phones = telefono.getText().toString();

        if (TextUtils.isEmpty(nombres)){
            Toast.makeText(getContext(),"Ingrese el nombre", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(direccions)){
            Toast.makeText(getContext(),"Ingrese su direccion", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(ciudads)){
            Toast.makeText(getContext(),"Ingrese la ciudad", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(phones)){
            Toast.makeText(getContext(),"Ingrese su numero", Toast.LENGTH_SHORT).show();
        }else{
            dialog.setTitle("Guardando");
            dialog.setMessage("Por favor espere..");
            dialog.show();
            dialog.setCanceledOnTouchOutside(true);

            HashMap map = new HashMap();
            map.put("nombre",nombres);
            map.put("direccion",direccions);
            map.put("ciudad",ciudads);
            map.put("telefono",phones);
            map.put("uid",CurrentUserId);

            UserRef.child(CurrentUserId).updateChildren(map).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if (task.isSuccessful()){
                        EnviarAlInicio();
                        dialog.dismiss();
                    }else {
                        String mensaje = task.getException().toString();
                        Toast.makeText(getContext(),"Error:"+mensaje,Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                }
            });
        }
    }

    private void EnviarAlInicio() {
        Intent  intent = new Intent(getContext(),AdminActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
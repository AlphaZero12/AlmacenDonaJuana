package com.example.almacen_doajuana;

import androidx.annotation.NonNull;
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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    private Button enviarnumero,enviarcodigo;
    private EditText numero, codigo;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks;
    private String VerificacionID;
    private PhoneAuthProvider.ForceResendingToken resendingToken;
    private FirebaseAuth auth;
    private ProgressDialog dialog;
    private String phoneNumber;
    private ImageView imageGoogle;
    private ImageView imageFacebook;
    private ImageView imageTwitter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        numero = (EditText) findViewById(R.id.numero);
        codigo=(EditText)findViewById(R.id.codigo);
        enviarcodigo=(Button) findViewById(R.id.enviarcodigo);
        enviarnumero=(Button) findViewById(R.id.enviarnumero);


        auth=FirebaseAuth.getInstance();
        dialog = new ProgressDialog(this);
        enviarnumero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phoneNumber = numero.getText().toString();
                if(TextUtils.isEmpty(phoneNumber)){
                    Toast.makeText(MainActivity.this,"Ingresa tu numero primero", Toast.LENGTH_SHORT).show();
                }else{
                    dialog.setTitle("Validando numero");
                    dialog.setMessage("Por favor espere mientras validamos su numero");
                    dialog.show();
                    dialog.setCanceledOnTouchOutside(true);
                    PhoneAuthOptions options = PhoneAuthOptions.newBuilder(auth)
                            .setPhoneNumber(phoneNumber) //+56953635374
                            .setTimeout(60l, TimeUnit.SECONDS)
                            .setActivity(MainActivity.this)
                            .setCallbacks(callbacks)
                            .build();
                    PhoneAuthProvider.verifyPhoneNumber(options);//envia el numero
                }
            }
        });
        enviarcodigo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            numero.setVisibility(View.GONE);
            enviarnumero.setVisibility(View.GONE);
            String VerificacionCode = codigo.getText().toString();
            if (TextUtils.isEmpty(VerificacionCode)){
                Toast.makeText(MainActivity.this,"Ingresa el codigo recibido", Toast.LENGTH_SHORT).show();
            }
            else {
                dialog.setTitle("Verificando");
                dialog.setMessage("Espere por favor");
                dialog.show();
                dialog.setCanceledOnTouchOutside(true);
                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(VerificacionID, VerificacionCode);
                IngresadoConExito(credential);
            }
            }
        });
        callbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                IngresadoConExito(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                dialog.dismiss();
                Toast.makeText(MainActivity.this,"Fallo el inicio Causas: \n1. Numero Invalido\n2.Sin conexion a Internet\n3.Sin codigo de region",Toast.LENGTH_SHORT).show();
                numero.setVisibility(View.VISIBLE);
                enviarnumero.setVisibility(View.VISIBLE);
                codigo.setVisibility(View.GONE);
                enviarcodigo.setVisibility(View.GONE);
            }

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken Token) {
                VerificacionID = s ;
                resendingToken=Token;
                dialog.dismiss();
                Toast.makeText(MainActivity.this,"Codigo enviado satisfactoriamente, Revisa tu bandeja de entrada",Toast.LENGTH_SHORT).show();
                numero.setVisibility(View.GONE);
                enviarnumero.setVisibility(View.GONE);
                codigo.setVisibility(View.VISIBLE);
                enviarcodigo.setVisibility(View.VISIBLE);
            }
        };




    }

    private void IngresadoConExito(PhoneAuthCredential credential) {
        auth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    dialog.dismiss();
                    Toast.makeText(MainActivity.this,"Ingresado con exito", Toast.LENGTH_SHORT).show();
                    EnviarAlaPrincipal();
            }else {
                    String err = task.getException().toString();
                    Toast.makeText(MainActivity.this,"Error: "+err, Toast.LENGTH_SHORT).show();
                }
        }

    });
}

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser firebaseUser = auth.getCurrentUser();
        if(firebaseUser != null){
            EnviarAlaPrincipal();
        }
    }

    private void EnviarAlaPrincipal() {
        Intent intent = new Intent(MainActivity.this,InicioActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("phone",phoneNumber);
        startActivity(intent);
        finish();

    }
}
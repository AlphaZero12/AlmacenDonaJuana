package com.example.almacen_doajuana;

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

import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
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

        imageGoogle = findViewById(R.id.imageGoogle);
        imageFacebook = findViewById(R.id.imageFacebook);
        imageTwitter = findViewById(R.id.imageTwitter);

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
                            .setPhoneNumber("+569"+phoneNumber) //+56953635374
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

            }
        });



        imageGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    String url ="https://www.google.com";
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(url));
                    startActivity(intent);
            }
        });
        imageFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url ="https://www.facebook.com";
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
            }
        });
        imageTwitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url ="https://www.twitter.com";
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
            }
        });

    }
}
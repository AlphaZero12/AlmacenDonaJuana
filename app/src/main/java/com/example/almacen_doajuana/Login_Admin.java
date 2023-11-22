package com.example.almacen_doajuana;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class Login_Admin extends AppCompatActivity {
    private Button enviarnumero, enviarcodigo;
    private EditText numero, codigo;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks;
    private String VerificacionID;
    private PhoneAuthProvider.ForceResendingToken resendingToken;
    private FirebaseAuth auth;
    private ProgressDialog dialog;          
    private String phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_admin);

        numero = (EditText) findViewById(R.id.numeroadmin);
        codigo = (EditText) findViewById(R.id.codigoadmin);
        enviarcodigo = (Button) findViewById(R.id.enviarcodigoadmin);
        enviarnumero = (Button) findViewById(R.id.enviarnumeroadmin);

        auth = FirebaseAuth.getInstance();
        dialog = new ProgressDialog(this);

        enviarnumero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phoneNumber = numero.getText().toString();
                if (TextUtils.isEmpty(phoneNumber)) {
                    Toast.makeText(Login_Admin.this, "Ingresa tu número primero", Toast.LENGTH_SHORT).show();
                } else {
                    dialog.setTitle("Validando número");
                    dialog.setMessage("Por favor, espere mientras validamos su número");
                    dialog.show();
                    dialog.setCanceledOnTouchOutside(true);

                    PhoneAuthOptions options = PhoneAuthOptions.newBuilder(auth)
                            .setPhoneNumber(phoneNumber) //+56953635374
                            .setTimeout(60l, TimeUnit.SECONDS)
                            .setActivity(Login_Admin.this)
                            .setCallbacks(callbacks)
                            .build();

                    PhoneAuthProvider.verifyPhoneNumber(options); // Envía el número
                }
            }
        });

        enviarcodigo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numero.setVisibility(View.GONE);
                enviarnumero.setVisibility(View.GONE);
                String verificacionCode = codigo.getText().toString();
                if (TextUtils.isEmpty(verificacionCode)) {
                    Toast.makeText(Login_Admin.this, "Ingresa el código recibido", Toast.LENGTH_SHORT).show();
                } else {
                    dialog.setTitle("Verificando");
                    dialog.setMessage("Espere por favor");
                    dialog.show();
                    dialog.setCanceledOnTouchOutside(true);

                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(VerificacionID, verificacionCode);
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
                Toast.makeText(Login_Admin.this, "Falló el inicio. Causas:\n1. Número Inválido\n2. Sin Conexión a Internet\n3. Sin Código de Región", Toast.LENGTH_SHORT).show();
                numero.setVisibility(View.VISIBLE);
                enviarnumero.setVisibility(View.VISIBLE);
                codigo.setVisibility(View.GONE);
                enviarcodigo.setVisibility(View.GONE);
            }

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken token) {
                VerificacionID = s;
                resendingToken = token;
                dialog.dismiss();
                Toast.makeText(Login_Admin.this, "Código enviado satisfactoriamente. Revisa tu bandeja de entrada", Toast.LENGTH_SHORT).show();
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
                if (task.isSuccessful()) {
                    dialog.dismiss();
                    Toast.makeText(Login_Admin.this, "Ingresado con éxito", Toast.LENGTH_SHORT).show();
                    EnviarAlaPrincipal();
                } else {
                    String err = task.getException().toString();
                    Toast.makeText(Login_Admin.this, "Error: " + err, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser firebaseUser = auth.getCurrentUser();
        if (firebaseUser != null) {
            EnviarAlaPrincipal();
        }
    }

    private void EnviarAlaPrincipal() {
        Intent intent = new Intent(Login_Admin.this, AdminActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("phone", phoneNumber);
        startActivity(intent);
        finish();
    }
}

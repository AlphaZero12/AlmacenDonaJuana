package com.example.almacen_doajuana;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class ActivityLoginCel extends AppCompatActivity {

    private EditText phoneNumberEditText, codeEditText;
    private Button sendCodeButton, verifyCodeButton;

    private FirebaseAuth mAuth;
    private String verificationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_cel);

        mAuth = FirebaseAuth.getInstance();

        phoneNumberEditText = findViewById(R.id.editTextPhoneNumber);
        codeEditText = findViewById(R.id.editTextCode);
        sendCodeButton = findViewById(R.id.buttonSendCode);
        verifyCodeButton = findViewById(R.id.buttonVerifyCode);

        sendCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phoneNumber = phoneNumberEditText.getText().toString();
                sendVerificationCode(phoneNumber);
            }
        });

        verifyCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String code = codeEditText.getText().toString();
                verifyCodeAndSignIn(code);
            }
        });
    }

    private void sendVerificationCode(String phoneNumber) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNumber)
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setActivity(this)
                        .setCallbacks(callbacks)
                        .build();

        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void verifyCodeAndSignIn(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Usuario ha iniciado sesión exitosamente
                            String phoneNumber = phoneNumberEditText.getText().toString();
                            saveUserDataToDatabase(phoneNumber);
                        } else {
                            // Fallo en la autenticación
                            Toast.makeText(ActivityLoginCel.this, "Fallo en la autenticación", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void saveUserDataToDatabase(String phoneNumber) {
        // Implementar lógica para almacenar la información del usuario en la base de datos.
        // Por ejemplo, puedes usar Firebase Realtime Database o Firestore:
        // DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("usuarios");
        // String userId = mAuth.getCurrentUser().getUid();
        // databaseReference.child(userId).child("phoneNumber").setValue(phoneNumber);

        // Después de guardar los datos del usuario, puedes iniciar la actividad principal o realizar otras acciones necesarias
        goToMainScreen();
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks =
            new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                @Override
                public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                    // Se llama cuando la verificación del número de teléfono se completa automáticamente
                    signInWithPhoneAuthCredential(phoneAuthCredential);
                }

                @Override
                public void onVerificationFailed(@NonNull FirebaseException e) {
                    // Manejar el fallo en la verificación del número de teléfono
                    Toast.makeText(ActivityLoginCel.this, "Fallo en la verificación del número de teléfono", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                    super.onCodeSent(s, forceResendingToken);
                    verificationId = s;
                    // Muestra la interfaz para ingresar el código de verificación
                    Toast.makeText(ActivityLoginCel.this, "Código de verificación enviado", Toast.LENGTH_SHORT).show();
                }
            };

    private void goToMainScreen() {
        // Agregar código para navegar a la actividad principal
        // Por ejemplo:
        // Intent intent = new Intent(ActivityLoginCel.this, MainActivity.class);
        // startActivity(intent);
    }
}

package com.example.almacen_doajuana;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.ClipData;
import android.content.Intent;
import android.media.browse.MediaBrowser;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AdminActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private String CurrentUserId;
    private DatabaseReference UserRef;
    private String Telefono = "";

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        bottomNavigationView = findViewById(R.id.boton_navegation_view);
        bottomNavigationView.setOnNavigationItemSelectedListener(Listener);


        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            Telefono = bundle.getString("phone");
        }
        auth = FirebaseAuth.getInstance();
        CurrentUserId = auth.getCurrentUser().getUid();
        UserRef = FirebaseDatabase.getInstance().getReference().child("Admin");


    }


    private BottomNavigationView.OnNavigationItemSelectedListener Listener = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            if (item.getItemId() == R.id.fragmentUno) {
                Fragmentos(new FragmentUno());
            }
            if (item.getItemId() == R.id.fragmentDos) {
                Fragmentos(new FragmentDos());
            }
            if (item.getItemId() == R.id.fragmentTres) {
                Fragmentos(new FragmentTres());
            }
            if (item.getItemId() == R.id.fragmentCuatro) {
                Fragmentos(new FragmentCuatro());
            }
            if (item.getItemId() == R.id.fragmentCinco) {
                Fragmentos(new FragmentCinco());
            }
            return true;
        }
    };

    private void Fragmentos(Fragment fragment){
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment, fragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit();

    }
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser firebaseUser = auth.getCurrentUser();
        if (firebaseUser == null){
            EnviarAlLogin();
        }else{
            VerificarUsuarioExistente();
        }
    }

    private void EnviarAlLogin() {
        Intent intent = new Intent(AdminActivity.this,Login_Admin.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void VerificarUsuarioExistente() {
        final String CurrenUserId = auth.getCurrentUser().getUid();
        UserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.hasChild(CurrenUserId)){
                    EnviarAlSetup();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    private void EnviarAlSetup() {
        Intent intent = new Intent(AdminActivity.this,SetupAdminActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("phone", Telefono);
        startActivity(intent);
        finish();
    }
}
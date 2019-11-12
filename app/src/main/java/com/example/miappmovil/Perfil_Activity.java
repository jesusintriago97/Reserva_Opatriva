package com.example.miappmovil;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.miappmovil.Model.Request;
import com.example.miappmovil.Model.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class Perfil_Activity extends AppCompatActivity implements View.OnClickListener{
    private EditText nombres, apellidos, direccion, correo,telefono;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference usuario, databaseReference;
    private String phone, contra, vericontra, foto;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private Button btnEditar, btnGuardar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_);

       setupActionBar();


        nombres = findViewById(R.id.txtNombresP);
        apellidos = findViewById(R.id.txtApellidoP);
        direccion =findViewById(R.id.txtDireccionP);
        correo = findViewById(R.id.txtCorreoP);
        telefono = findViewById(R.id.txttelefonoP);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        phone = user.getPhoneNumber();
        usuario = firebaseDatabase.getReference();
        databaseReference = firebaseDatabase.getReference();

        //Perfil();

        btnEditar = (Button) findViewById(R.id.btnEditPerfil);
        btnGuardar = (Button)findViewById(R.id.btnGuardarPerfil);

        btnEditar.setOnClickListener(this);
        btnGuardar.setOnClickListener(this);

    }

    private void setupActionBar() {
            ActionBar actionBar = getSupportActionBar();
            if (actionBar!=null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setTitle("Mi información");
            }
    }

   private void Perfil() {

        usuario.child("Requests").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (final DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    String key = usuario.child("Requests").child(phone).getKey();
                    String key2 = snapshot.getKey();
                    if (key.equals(key2)){
                        Request usuario_perfil = snapshot.getValue(Request.class);
                        nombres.setText(usuario_perfil.getName());
                        //apellidos.setText(usuario_perfil.getApellidos());
                        direccion.setText(usuario_perfil.getAddress());
                        telefono.setText(usuario_perfil.getPhone());
                        //correo.setText(usuario_perfil.getCorreo());
                    }else {

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnEditPerfil:
                nombres.setEnabled(true);
                apellidos.setEnabled(true);
                direccion.setEnabled(true);
                telefono.setEnabled(true);
                btnGuardar.setVisibility(View.VISIBLE);
                btnEditar.setVisibility(View.INVISIBLE);
                break;
            case R.id.btnGuardarPerfil:
                Request usuario1 = new Request();
                usuario1.setName(nombres.getText().toString());
               // usuario1.setApellidos(apellidos.getText().toString());
                usuario1.setAddress(direccion.getText().toString());
                usuario1.setPhone(telefono.getText().toString());
               // usuario1.setCorreo(correo.getText().toString());
                databaseReference.child("Requests").child(phone).setValue(usuario1)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(Perfil_Activity.this,"PERFIL ACTUALIZADO",Toast.LENGTH_SHORT).show();
                                nombres.setEnabled(false);
                               // apellidos.setEnabled(false);
                                direccion.setEnabled(false);
                                telefono.setEnabled(false);
                                btnGuardar.setVisibility(View.INVISIBLE);
                                btnEditar.setVisibility(View.VISIBLE);

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Toast.makeText(Perfil_Activity.this,"ERROR",Toast.LENGTH_SHORT).show();

                        nombres.setEnabled(false);
                       // apellidos.setEnabled(false);
                        direccion.setEnabled(false);
                        telefono.setEnabled(false);
                        btnGuardar.setVisibility(View.INVISIBLE);
                        btnEditar.setVisibility(View.VISIBLE);
                    }
                });
                break;
        }
    }
}

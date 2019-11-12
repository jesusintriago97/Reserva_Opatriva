package com.example.miappmovil;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.miappmovil.Common.Common;
import com.example.miappmovil.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class Registro_Activity extends AppCompatActivity {

    private EditText txtnombre, txtapellido, txtdireccion, txttelefono, txtcorreo, txtcontraseña, contraseñaRepetida;
    private Button btnregistro;

    private FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private Uri uri;

    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_);

        progressDialog = new ProgressDialog(this);

        txtnombre = (EditText) findViewById(R.id.txtNombres);
        txtapellido = (EditText) findViewById(R.id.txtApellido);
        txtdireccion = (EditText) findViewById(R.id.txtDireccion);
        txttelefono = (EditText) findViewById(R.id.txttelefono);
        txtcorreo = (EditText) findViewById(R.id.txtCorreo);
        txtcontraseña = (EditText) findViewById(R.id.txtContraseña);
        contraseñaRepetida = (EditText) findViewById(R.id.txtRcontraseña);
        btnregistro = (Button) findViewById(R.id.btnRegistrarme);

        //para ir atras
        // setupActionBar();
        //Init Firebase
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = database.getReference("User");


        btnregistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txt_nombre = txtnombre.getText().toString();
                String txt_apellido = txtapellido.getText().toString();
                final String txt_direccion = txtdireccion.getText().toString();
                final String txt_telefono = txttelefono.getText().toString();
                String txt_correo = txtcorreo.getText().toString();
                String txt_contraseña = txtcontraseña.getText().toString();
                String txt_contraseñax2 = contraseñaRepetida.getText().toString();

                if (TextUtils.isEmpty(txt_nombre) || TextUtils.isEmpty(txt_apellido) || TextUtils.isEmpty(txt_direccion) || TextUtils.isEmpty(txt_telefono) || TextUtils.isEmpty(txt_correo) || TextUtils.isEmpty(txt_contraseña) || TextUtils.isEmpty(txt_contraseñax2)) {
                    Toast.makeText(Registro_Activity.this, "Ningun campo debe estar vacio", Toast.LENGTH_SHORT).show();
                }else if (txt_contraseña.length()<6){
                    Toast.makeText(Registro_Activity.this,"ERROR: La contraseña debe tener almenos 6 caracteres",Toast.LENGTH_SHORT).show();
                }else if (!validarContraseña()) {
                    txtcontraseña.setError("CONTRASEÑA NO COINCIDEN");
                    txtcontraseña.requestFocus();
                }else {

                    if (Common.isConnectedToInternet(getBaseContext())) {
                        final ProgressDialog mDialog = new ProgressDialog(Registro_Activity.this);
                        mDialog.setMessage("Por favor espere ......");
                        mDialog.show();
                        table_user.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.child(txttelefono.getText().toString()).exists()) {
                                    mDialog.dismiss();
                                    Toast.makeText(Registro_Activity.this, "El número ya está registrado!", Toast.LENGTH_SHORT).show();

                                } else {
                                    mDialog.dismiss();
                                    User user = new User(txtnombre.getText().toString(),
                                            txtcontraseña.getText().toString(),
                                            contraseñaRepetida.getText().toString());


                                    table_user.child(txttelefono.getText().toString()).setValue(user);
                                    Toast.makeText(Registro_Activity.this, "¡Regístrate éxito!", Toast.LENGTH_SHORT).show();
                                    finish();
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    } else {
                        Toast.makeText(Registro_Activity.this, "Comprueba tu conexión a internet !", Toast.LENGTH_SHORT).show();

                    }
                }

            }

        });
    }
    public boolean validarContraseña (){
        String Contraseña,ContraseñaRepetida;
        Contraseña = txtcontraseña.getText().toString();
        ContraseñaRepetida = contraseñaRepetida.getText().toString();
        if (Contraseña.equals(ContraseñaRepetida)){
            if (Contraseña.length() >=6){
                return true;
            }else return false;
        }else return false;
    }
}

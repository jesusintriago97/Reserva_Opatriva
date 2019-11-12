package com.example.miappmovil;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.miappmovil.Common.Common;
import com.example.miappmovil.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

public class MainActivity extends AppCompatActivity {

    private EditText txtcorrreo, txtcontraseña,edtPhone;
    private Button btniniciarsecion, btnregistraruser;
    FirebaseAuth auth;

    private FirebaseDatabase database;
    private DatabaseReference table_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        edtPhone = (EditText) findViewById(R.id.txtcoorreo);
        txtcontraseña = (EditText) findViewById(R.id.txtcontraseñaa);
        btniniciarsecion = (Button) findViewById(R.id.btniniciarsecion);
        btnregistraruser = (Button) findViewById(R.id.btncrearcuenta);
        auth = FirebaseAuth.getInstance();
        //INIT FIREBASE

        database = FirebaseDatabase.getInstance();
        table_user = database.getReference("User");

        btnregistraruser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registro = new Intent(MainActivity.this, Registro_Activity.class);
                startActivity(registro);

            }
        });
        btniniciarsecion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                        if (Common.isConnectedToInternet(getBaseContext())) {

                            final ProgressDialog mDialog = new ProgressDialog(MainActivity.this);
                            mDialog.setMessage("Por favor espere...");
                            mDialog.show();

                            table_user.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    //Check if user not exist in database
                                    if (dataSnapshot.child(edtPhone.getText().toString()).exists()) {
                                        //Get User Information
                                        mDialog.dismiss();
                                        User user = dataSnapshot.child(edtPhone.getText().toString()).getValue(User.class);
                                        if (user != null) {
                                            user.setPhone(edtPhone.getText().toString()); //Set Phone
                                        } if (txtcontraseña.getText().toString().isEmpty() ||edtPhone.getText().toString().isEmpty()){
                                            Toast.makeText(MainActivity.this, "Campo no puede estar vacio ", Toast.LENGTH_SHORT).show();
                                        }else
                                        {
                                            if (user != null) {
                                                if (user.getPassword().equals(txtcontraseña.getText().toString())) {
                                                    Intent homeIntent = new Intent(MainActivity.this, Navegacion_Activity.class);
                                                    Common.currentUser = user;
                                                    startActivity(homeIntent);
                                                    finish();

                                                    table_user.removeEventListener(this);

                                                } else {
                                                    Toast.makeText(MainActivity.this, "Contraseña Incorrecta!", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        }

                                    } else {
                                        mDialog.dismiss();
                                        Toast.makeText(MainActivity.this, "El usuario no existe.", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        } else {
                            Toast.makeText(MainActivity.this, "Comprueba tu conexión a internet !", Toast.LENGTH_SHORT).show();
                        }

                    }
                });



            }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user!=null){
            startActivity(new Intent(MainActivity.this, Navegacion_Activity.class));
            finish();}
    }
}



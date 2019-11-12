package com.example.miappmovil;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.miappmovil.Database.Database;
import com.example.miappmovil.Model.Order;
import com.example.miappmovil.Model.Request;
import com.example.miappmovil.ViewHolder.CartAdapter;
import com.example.miappmovil.Common.Common;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import java.util.Locale;

public class Cart_Activity extends AppCompatActivity {

    TextView Totalprice;
    Button btnplace;

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference requests;

    List<Order> cart = new ArrayList<>();
    CartAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_);

        setupActionBar();

        //firebase
        database = FirebaseDatabase.getInstance();
        requests = database.getReference("Requests");

        //init
        Totalprice = findViewById(R.id.total);
        btnplace = findViewById(R.id.btnPlaceOrder);

        recyclerView = findViewById(R.id.listCard);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(layoutManager);

        btnplace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAlertDialog();

            }
        });


        loadListFood();

    }

    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(" ");
        }
    }

    private void showAlertDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Cart_Activity.this);
        alertDialog.setTitle("ESCRIBE TU DIRECCION: ");
        //  alertDialog.setMessage("ESCRIBE TU DIRECCION: ");

        final EditText adtAdress = new EditText(Cart_Activity.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );
        adtAdress.setLayoutParams(lp);
        alertDialog.setView(adtAdress);//add edit text to alert dialog


        alertDialog.setIcon(R.drawable.ic_shopping_cart_black_24dp);

        alertDialog.setPositiveButton("SI", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //NO OLVIDAR

               Request request = new Request(
                       Common.currentUser.getPhone(),
                       Common.currentUser.getName(),
                       adtAdress.getText().toString(),
                       Totalprice.getText().toString(),
                       cart
               );
                //submit to firebase
                // we will using system.currentTimeMillis to key
                requests.child(String.valueOf(System.currentTimeMillis())).setValue(request);

                //delete cart
                new Database(getBaseContext()).cleanCart();
                Toast.makeText(Cart_Activity.this, "Gracias por ordennar aquí", Toast.LENGTH_LONG).show();
                finish();


            }
        });
        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();


            }
        });
        alertDialog.show();
    }

    private void loadListFood() {
        cart = new Database(this).getCarts();
        adapter = new CartAdapter(cart, this);
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);

        //Calculate total price
        double total = 0;
        double fin = 0;
        //aqui Locale locale = new Locale("en", "US");
        //        NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);

        for (Order order : cart) {

            Log.e("Precio", order.getPrice());
            Log.e("cantidad", order.getQuantity());
            total = Double.parseDouble(order.getPrice()) * Double.parseDouble(order.getQuantity());
            Log.e("total", String.valueOf(total));
            fin = fin + total;
            Log.e("fin", String.valueOf(fin));

        }

        Totalprice.setText(String.format("%.2f", fin));


    }

  /*  @Override
    public boolean onContextItemSelected(MenuItem item) {

        if (item.getTitle().equals(Common.DELETE)) {
          //  deleteCategori(adapter.g(item.getOrder()).getKey());

        }

    }*/

}

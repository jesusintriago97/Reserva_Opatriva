package com.example.miappmovil;

import android.content.Intent;
import android.os.Bundle;

import com.example.miappmovil.Common.Common;
import com.example.miappmovil.Inferface.ItemClickListener;
import com.example.miappmovil.Model.Categori;
import com.example.miappmovil.Model.Order;
import com.example.miappmovil.Model.User;
import com.example.miappmovil.ViewHolder.CartAdapter;
import com.example.miappmovil.ViewHolder.MenuViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.view.menu.MenuView;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

public class Navegacion_Activity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private TextView nombre, correo;

    FirebaseDatabase database;
    DatabaseReference categori;
    private FirebaseDatabase firebaseDatabase;
    private String userId;

    TextView txtname,txtemail;
    FirebaseRecyclerAdapter<Categori, MenuViewHolder> adapter;

    RecyclerView recyclermenu;

    RecyclerView.LayoutManager layoutManager;
    private DatabaseReference usuario;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navegacion_);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Escoge el tipo  ");
        setSupportActionBar(toolbar);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View view = navigationView.getHeaderView(0);
        nombre = (TextView) view.findViewById(R.id.lblNombre);
        nombre.setText(Common.currentUser.getName());
     //   correo = (TextView) view.findViewById(R.id.lblCorreo);

        navigationView.setNavigationItemSelectedListener(this);
        database=FirebaseDatabase.getInstance();
        categori=database.getReference("Categori");
        recyclermenu =(RecyclerView)findViewById(R.id.reciclermenu);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        usuario = firebaseDatabase.getReference();
        recyclermenu =(RecyclerView)findViewById(R.id.reciclermenu);
        recyclermenu.setHasFixedSize(true);
        layoutManager= new LinearLayoutManager(this);
        recyclermenu.setLayoutManager(layoutManager);

        loadMenu();





        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                Intent cartIntent =new Intent(Navegacion_Activity.this, Cart_Activity.class);
                startActivity(cartIntent);
            }
        });
        View headerView = navigationView.getHeaderView(0);
        nombre = (TextView) headerView.findViewById(R.id.lblNombre);
        //correo=(TextView)headerView.findViewById(R.id.lblCorreo);
       // correo.setText(Common.currentUser.getPhone());
//        nombre.setText(Common.currentUser.getName());


        DrawerLayout drawer = findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();



    }
    private void loadMenu() {
        //menu principal

        adapter= new FirebaseRecyclerAdapter<Categori, MenuViewHolder>(Categori.class,R.layout.menu_item, MenuViewHolder.class, categori) {
            @Override
            protected void populateViewHolder(MenuViewHolder menuViewHolder, Categori categori, int i) {
                menuViewHolder.name_menu.setText(categori.getName());
                Picasso.get().load(categori.getImage()).into(menuViewHolder.menu_image);
                final Categori clickitem =categori;

                menuViewHolder.setItemClickListener(   new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        //get categoriid and send to new activity
                        Intent food =new Intent(Navegacion_Activity.this,Foodlist.class);
                        //becasuse CategoriId,is key so we just key of thid item
                        food.putExtra("CategoriId",adapter.getRef(position).getKey());//
                        startActivity(food);


                    }
                });
            }
        };
        recyclermenu.setAdapter(adapter);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navegacion_, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*if (id == R.id.action_settings) {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(Navegacion_Activity.this, MainActivity.class));
            finish();
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_menu) {
           // Intent puev = new Intent(Navegacion_Activity.this,pueva.class);
            //startActivity(puev);
        } else if (id == R.id.nav_cart) {
            Intent cart = new Intent(Navegacion_Activity.this,Cart_Activity.class);
            startActivity(cart);

        } else if (id == R.id.nav_order) {
            Intent order = new Intent(Navegacion_Activity.this, OrderStatus.class);
            startActivity(order);



      /*  } else if (id == R.id.nav_manage) {
            Intent nav = new Intent(Navegacion_Activity.this,Perfil_Activity.class);
            startActivity(nav);*/

        }else if (id == R.id.nav_share) {
            Intent share = new Intent(Intent.ACTION_SEND);
            share.setType("text/plain");
            share.putExtra(Intent.EXTRA_SUBJECT,"MynewApp");
            share.putExtra(Intent.EXTRA_TEXT, "Ayudanos valorando nuestra aplicacion mediante el siguiente link");
            startActivity(Intent.createChooser(share, "Compartir Mediante"));

        } else if (id == R.id.nav_logout) {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(Navegacion_Activity.this, MainActivity.class));
            finish();

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}

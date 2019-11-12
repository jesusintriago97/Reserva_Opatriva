package com.example.miappmovil;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.miappmovil.Database.Database;
import com.example.miappmovil.Model.Foods;
import com.example.miappmovil.Model.Order;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class Food_detail extends AppCompatActivity {
    TextView txtfood_name,txtfood_price,txtfood_description;
    ImageView img_food;
    CollapsingToolbarLayout collapsingToolbarLayout;
    FloatingActionButton btncard;
    ElegantNumberButton numberButton;
    String foodId="";
    FirebaseDatabase database;
    DatabaseReference foods;
    

    Foods currentFood;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_detail);

        setupActionBar();

        //firebase
        database=FirebaseDatabase.getInstance();
        foods=database.getReference( "Foods");

        //init view
        numberButton=findViewById(R.id.number_buton);
        btncard=findViewById(R.id.btncard);

        txtfood_name=findViewById(R.id.food_name);
        txtfood_price=findViewById(R.id.food_price);
        txtfood_description=findViewById(R.id.food_description);
        img_food=findViewById(R.id.img_food);
        collapsingToolbarLayout =findViewById(R.id.collapseActionView);
//        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppbar);

        btncard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Database(getBaseContext()).addToCart(new Order(
                        foodId,
                        currentFood.getName(),
                        numberButton.getNumber(),
                        currentFood.getPrice(),
                        currentFood.getDiscount()

                ));
                Toast.makeText(Food_detail.this,"Agregado en el carrito",Toast.LENGTH_LONG).show();
            }
        });

        //get food id from intent
        if (getIntent()!=null)
            foodId=getIntent().getStringExtra("FoodId");
        if (!foodId.isEmpty())
        {//lll
            getDetailFood(foodId);
        }
    }

    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar!=null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Nuestra comida");
        }
    }

    private void getDetailFood(final String foodId) {
        foods.child(foodId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                currentFood = dataSnapshot.getValue(Foods.class);
                //setImage
              //  Picasso.get().load(((Foods) food).getImage());
                //   protected void populateViewHolder(FoodViewHolder foodViewHolder, Foods food, int i) {
                //                foodViewHolder.food_menu.setText((food.getName()));
                //
                Picasso.get().load(((Foods) currentFood).getImage()) .into (img_food); ;

//                collapsingToolbarLayout.setTitle(((Foods) food).getName());

                txtfood_price.setText(((Foods) currentFood).getPrice());
                txtfood_name.setText(((Foods) currentFood).getName());
                txtfood_description.setText(((Foods) currentFood).getDescription());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}

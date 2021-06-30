package com.example.miappmovil;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.miappmovil.Inferface.ItemClickListener;
import com.example.miappmovil.Model.Foods;
import com.example.miappmovil.ViewHolder.FoodViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class Foodlist extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    FirebaseDatabase database;
    DatabaseReference foodList;

    String categoriId="";

    FirebaseRecyclerAdapter<Foods,FoodViewHolder>adapter;

    //busqueda funcionalidad
    FirebaseRecyclerAdapter<Foods,FoodViewHolder>searchadapter;
    List<String>  suggesList = new ArrayList<>();
    MaterialSearchBar materialSearchBar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foodlist);

        setupActionBar();

        //firebase
        database=FirebaseDatabase.getInstance();
        foodList=database.getReference("Foods");

        recyclerView=findViewById(R.id.recicler_food);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager );

        //get inten here
        if(getIntent()!= null){
            categoriId = getIntent().getStringExtra("CategoriId");}//
        if(!categoriId.isEmpty()&& categoriId!=null){
            loadListFood(categoriId);

        }

        //busqueda
        materialSearchBar= findViewById(R.id.searchBar);
        materialSearchBar.setHint("Busca");
        //materialSearchBar.setSpeechMode(false);//esta definida en el xml

        loadSuggest();// escribe funcion para carga sugerir desde firebase
        materialSearchBar.setLastSuggestions(suggesList);
        materialSearchBar.setCardViewElevation(10);
        materialSearchBar.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //Cuando el usuario escriba el texto, cambiaremos la lista de sugerencias.
                List<String> suggest = new ArrayList<String>();
                for (String search :suggesList){

                    if (search.toLowerCase().contains(materialSearchBar.getText().toLowerCase()))suggest.add(search);
                    suggest.add(search);
                }
                materialSearchBar.setLastSuggestions(suggest);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        materialSearchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {
                // cuando la barra de búsqueda está cerca
                //restore original adapter
                if (!enabled)
                    recyclerView.setAdapter(adapter);
            }

            @Override
            public void onSearchConfirmed(CharSequence text) {
                //cuando la busqueda finaliza
                //resultados del adapter
                startSearch(text);

            }

            @Override
            public void onButtonClicked(int buttonCode) {

            }
        });

    }

    private void startSearch(CharSequence text) {
        searchadapter = new FirebaseRecyclerAdapter<Foods, FoodViewHolder>(
                Foods.class,
                R.layout.food_item,
                FoodViewHolder.class,
                foodList.orderByChild("name").equalTo(text.toString())//compara nombre
        ) {
            @Override
            protected void populateViewHolder(FoodViewHolder foodViewHolder, Foods food, int i) {
                foodViewHolder.food_menu.setText((food.getName()));
                foodViewHolder.food_menu.setText((food.getName()));

                Picasso.get().load(food.getImage()).into(foodViewHolder.food_image);

                final Foods local=food;
                foodViewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Intent fooddetail=new Intent(Foodlist.this,Food_detail.class);
                        fooddetail.putExtra("FoodId",searchadapter  .getRef(position).getKey());
                        startActivity(fooddetail);
                    }
                });

            }
        };
        recyclerView.setAdapter(searchadapter);// set adapter for Recicler view is shearch result




    }

    private void loadSuggest() {
        foodList.orderByChild("menuId").equalTo(categoriId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot:dataSnapshot.getChildren()){

                    Foods item = postSnapshot.getValue(Foods.class);
                    suggesList.add(item.getName()); //agrega monbre de to sugges List
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar!=null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Escoge a tu gusto");
        }
    }

    private void loadListFood(String categoriId)

    {
        adapter=new FirebaseRecyclerAdapter<Foods,FoodViewHolder>
                (Foods.class,R.layout.food_item,
                FoodViewHolder.class,foodList.
                orderByChild("menuId").equalTo(categoriId))//like select from Foods where Menuid=ca

        {
            @Override
            protected void populateViewHolder(FoodViewHolder foodViewHolder, Foods food, int i) {
                foodViewHolder.food_menu.setText((food.getName()));

                 Picasso.get().load(food.getImage()).into(foodViewHolder.food_image);

                 final Foods local=food;
                 foodViewHolder.setItemClickListener(new ItemClickListener() {
                     @Override
                     public void onClick(View view, int position, boolean isLongClick) {
                         Intent fooddetail=new Intent(Foodlist.this,Food_detail.class);
                         fooddetail.putExtra("FoodId",adapter.getRef(position).getKey());
                         startActivity(fooddetail);
                     }
                 });


            }
        };
        recyclerView.setAdapter(adapter);
    }
}

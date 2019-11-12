package com.example.miappmovil.ViewHolder;


import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.example.miappmovil.Inferface.ItemClickListener;
import com.example.miappmovil.Model.Order;
import com.example.miappmovil.R;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView txt_cart_name, txt_price;
    public ImageView img_cart_count;

    private ItemClickListener itemClickListener;

    public void setTxt_cart_name(TextView txt_cart_name) {
        this.txt_cart_name = txt_cart_name;
    }

    public CartViewHolder(View itemView) {
        super(itemView);

        txt_cart_name = itemView.findViewById(R.id.cart_item_name);
        txt_price = itemView.findViewById(R.id.cart_item_price);
        img_cart_count = itemView.findViewById(R.id.card_item_count);
    }
    @Override
    public void onClick(View view) {

    }
}

/**
 * Building adapter for card recycler view
 */
public class CartAdapter extends RecyclerView.Adapter<CartViewHolder>{

    private List<Order> listData = new ArrayList<>();
    private Context context;

    public CartAdapter(List<Order> listData, Context context) {
        this.listData = listData;
        this.context = context;
    }

    @Override
    public CartViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.cart_layout, parent, false);
        return new CartViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CartViewHolder holder, int position) {
        // getting properties of cart item
        String quantity = listData.get(position).getQuantity();

        String name = listData.get(position).getProductName();
        Double cantidad = Double.valueOf(quantity);


        double price = Double.parseDouble(listData.get(position).getPrice());

        Log.e("prueba", String.valueOf(price));
        Log.e("prueba", String.valueOf(quantity));
        double fin =  price * cantidad;



        // setting image cart count
        TextDrawable drawable = TextDrawable.builder()
                .buildRound("" + quantity, Color.RED);
        holder.img_cart_count.setImageDrawable(drawable);

        // setting text price
        Locale locale = new Locale("en", "US");
        NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);
       /* int totalPrice = (Integer.parseInt(price)) * (Integer.parseInt(quantity));//
        holder.txt_price.setText(fmt.format(totalPrice));*/
        TextView txt_total;

        // setting cart name
        holder.txt_cart_name.setText(name);
        Log.e("prueba", String.valueOf(fin));

        holder.txt_price.setText(String.valueOf(fin));


    }

    @Override
    public int getItemCount() {
        return listData.size();
    }
}

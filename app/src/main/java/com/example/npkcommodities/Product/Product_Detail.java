package com.example.npkcommodities.Product;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.npkcommodities.DataModels.Item_data_model;
import com.example.npkcommodities.DataModels.MyUtility;
import com.example.npkcommodities.DataModels.Shop_Detais_Modal;
import com.example.npkcommodities.Login.Login;
import com.example.npkcommodities.MainActivity;
import com.example.npkcommodities.MyAccount.Cart_Main;
import com.example.npkcommodities.R;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class Product_Detail extends AppCompatActivity {
    ImageView product_image;
    TextView product_name,product_price,product_raiting,product_delivery,desc,category,seller_name;
    Button addToCart,buyNow;
    DocumentReference documentReference;
    ShimmerFrameLayout shimmerFrameLayout;
    ScrollView relativeLayout;

    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    String item_id,shopId;
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_info_layout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Product Detail");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Product_Detail.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
       shimmerFrameLayout = findViewById(R.id.shimmer);
        relativeLayout = findViewById(R.id.rel1);
        Intent intent = getIntent();
        item_id = intent.getStringExtra("item_id");
        init();
        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCart(v);
            }
        });
        buyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCart(v);
                Intent intent = new Intent(Product_Detail.this, Cart_Main.class);
                startActivity(intent);
                finish();
            }
        });
    }


  public  void   addCart(View v)
    {
        databaseReference = FirebaseDatabase.getInstance().getReference();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser()!=null)
        {
            databaseReference = databaseReference.child("users/"+firebaseAuth.getCurrentUser().getUid()).child("Cart").child(item_id);
            Map<String,String> m = new HashMap<>();
            m.put("itemId",item_id);
            MyUtility.m.put(item_id,"1");
            databaseReference.setValue(m);
            Snackbar.make(v, "Item added to cart", Snackbar.LENGTH_LONG).show();
        }
        else
        {
            Toast.makeText(Product_Detail.this, "Please Login First", Toast.LENGTH_LONG).show();
            Intent intent1 = new Intent(Product_Detail.this, Login.class);
            startActivity(intent1);
            finish();
        }


    }
    private void init() {
        product_image = findViewById(R.id.product_image);
        product_name = findViewById(R.id.product_name);
        product_price = findViewById(R.id.product_price);
        desc = findViewById(R.id.desc);
        category = findViewById(R.id.category);
        addToCart = findViewById(R.id.cart);
        buyNow = findViewById(R.id.buy_now);
    }

    @Override
    protected void onStart() {
        super.onStart();
        shimmerFrameLayout.startShimmer();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("products/" + item_id);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Item_data_model item_data_model = dataSnapshot.getValue(Item_data_model.class);
                product_name.setText(item_data_model.getProductName());
                product_price.setText("Rs."+ item_data_model.getProductPrice());
                category.setText(item_data_model.getProductCategory());
                desc.setText(item_data_model.getProductDescription());
                Glide.with(Product_Detail.this).load(item_data_model.getUrl()).into(product_image);
                shimmerFrameLayout.stopShimmer();
                shimmerFrameLayout.setVisibility(View.GONE);
                relativeLayout.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    @Override
    public void onResume() {
        super.onResume();
       shimmerFrameLayout.startShimmer();
    }

    @Override
    public void onPause() {
        super.onPause();
        shimmerFrameLayout.stopShimmer();
    }

}
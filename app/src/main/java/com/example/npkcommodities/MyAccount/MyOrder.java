package com.example.npkcommodities.MyAccount;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import com.example.npkcommodities.AdapterClass.MyOrderAdapter;
import com.example.npkcommodities.DataModels.OrderModel;
import com.example.npkcommodities.MainActivity;
import com.example.npkcommodities.R;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MyOrder extends AppCompatActivity {

    RecyclerView recyclerView;
    DatabaseReference databaseReference;
    List<OrderModel> list;
    ShimmerFrameLayout shimmerFrameLayout;
    RelativeLayout relativeLayout;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shops_items);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("My order");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyOrder.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        recyclerView = findViewById(R.id.recyclerview);
        shimmerFrameLayout = findViewById(R.id.shimmer);
        relativeLayout = findViewById(R.id.rel1);
        recyclerView.setHasFixedSize(true);
        list = new ArrayList<>();
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

    @Override
    protected void onStart() {
        super.onStart();
        list.clear();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("users").child(firebaseAuth.getCurrentUser().getUid()).child("orders");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                    list.add(dsp.getValue(OrderModel.class));
                }

                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MyOrder.this);
                recyclerView.setLayoutManager(layoutManager);
                MyOrderAdapter myOrderAdapter = new MyOrderAdapter(MyOrder.this,list);
                recyclerView.setAdapter(myOrderAdapter);
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
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent  = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}

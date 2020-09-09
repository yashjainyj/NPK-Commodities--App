package com.example.npkcommodities.MyAccount;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.npkcommodities.API.JavaMailAPI;
import com.example.npkcommodities.AdapterClass.Cart_Adapter;
import com.example.npkcommodities.DataModels.Item_data_model;
import com.example.npkcommodities.DataModels.MyUtility;
import com.example.npkcommodities.DataModels.OrderModel;
import com.example.npkcommodities.MainActivity;
import com.example.npkcommodities.R;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.Set;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class Cart_Main extends AppCompatActivity {
    RecyclerView recyclerView;
    DatabaseReference databaseReference,databaseReference1;

    TextView subtotal,price;
    ShimmerFrameLayout shimmerFrameLayout;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    Button buy;
    List<String> list = new ArrayList<>();
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    public static final String DATA = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    public static Random RANDOM = new Random();
    int price1 = 0;
    public static String randomString(int len) {
        StringBuilder sb = new StringBuilder(len);

        for (int i = 0; i < len; i++) {
            sb.append(DATA.charAt(RANDOM.nextInt(DATA.length())));
        }

        return sb.toString();
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cart_main);
        subtotal = findViewById(R.id.total);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("My Cart");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Cart_Main.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        price = findViewById(R.id.price);
        shimmerFrameLayout = findViewById(R.id.shimmer);
        price.setText("Rs.0");
        recyclerView = findViewById(R.id.recyclerview);
        buy = findViewById(R.id.buy_now);

        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                bookOrder();


            }
        });
    }

    private void bookOrder() {
        databaseReference = FirebaseDatabase.getInstance().getReference("users").child(firebaseAuth.getCurrentUser().getUid()).child("orders");
        databaseReference1 = FirebaseDatabase.getInstance().getReference("allOrders");
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(c);
        for(String id :list)
        {
            String finalApprovalRefNo = Cart_Main.randomString(10);
            OrderModel orderModel = new OrderModel(id,formattedDate,finalApprovalRefNo,FirebaseAuth.getInstance().getUid(),FirebaseAuth.getInstance().getCurrentUser().getEmail());
            databaseReference.child(finalApprovalRefNo).setValue(orderModel);
            databaseReference1.child(finalApprovalRefNo).setValue(orderModel);

        }

        AlertDialog.Builder builder = new AlertDialog.Builder(Cart_Main.this);
        View view = LayoutInflater.from(Cart_Main.this).inflate(R.layout.positive_popup_layput,null);
        builder.setView(view);
        //builder.setTitle("Ordered Confirmed");
        Button button = view.findViewById(R.id.btnaccept);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users").child(firebaseAuth.getCurrentUser().getUid()).child("Cart");
                databaseReference.removeValue();

                Intent intent = new Intent(Cart_Main.this, MainActivity.class);

                JavaMailAPI javaMailAPI1 = new JavaMailAPI(Cart_Main.this,FirebaseAuth.getInstance().getCurrentUser().getEmail(),"Order Confirmed","Thank You for connecting with us. \n Your order is confirmed we will get you in touch shortly.");
                javaMailAPI1.execute();
                JavaMailAPI javaMailAPI3 = new JavaMailAPI(Cart_Main.this,"npkcommodities@gmail.com","Order Received"," order" +  " is received from " + FirebaseAuth.getInstance().getCurrentUser().getEmail() + "\n"+" Please Check Order Detail To Know More");
                javaMailAPI3.execute();
                startActivity(intent);
                finishAffinity();
            }
        });
//        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//
//
//            }
//        });
        builder.create().show();
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
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("users").child(firebaseAuth.getCurrentUser().getUid()).child("Cart");
       list.clear();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                {
                    String s = dataSnapshot1.getKey();
                    list.add(s);
                }
                Log.d("List", "onStart: " + list);
                if(!list.isEmpty()) {
                    ArrayList<Item_data_model> list1 = new ArrayList<>();
                    for (String id : list) {
                        DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("products/"+id);
                        databaseReference1.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                Item_data_model item_data_model = dataSnapshot.getValue(Item_data_model.class);
                                list1.add(item_data_model);
                                RecyclerView.LayoutManager layoutManager = new GridLayoutManager(Cart_Main.this, 2);
                                Cart_Adapter cart_adapter = new Cart_Adapter(Cart_Main.this, list1);
                                recyclerView.setHasFixedSize(true);
                                recyclerView.setLayoutManager(layoutManager);
                                recyclerView.setAdapter(cart_adapter);

                                price.setText("0");

                                for(Item_data_model item_data_model1:list1)
                                {
                                    price1 = price1 + item_data_model1.getProductPrice();
                                }
//                                Set<String> s = MyUtility.m.keySet();
//                                for (String sq : s) {
//                                    Log.d("Set ", sq);
//                                    for (Item_data_model item_data_model1 : list1) {
//                                        if (sq.equalsIgnoreCase(item_data_model1.getKey())) {
//                                            int p = Integer.parseInt(MyUtility.m.get(item_data_model1.getKey()));
//                                            Log.d("Quantity ", "onSuccess: " + p);
//                                            int pe = p * item_data_model1.getProductPrice();
//                                            Log.d("Price", "onSuccess: " + pe);
//                                            price1 += pe;
//                                            Log.d("Total Price", String.valueOf(price1));
//                                        }
//                                    }
//                                }

                                price.setText("Rs " + price1 + "");
                                shimmerFrameLayout.stopShimmer();
                                shimmerFrameLayout.setVisibility(View.GONE);
                                recyclerView.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                }
                else
                {
                    shimmerFrameLayout.stopShimmer();
                    buy.setEnabled(false);
                    shimmerFrameLayout.setVisibility(View.GONE);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
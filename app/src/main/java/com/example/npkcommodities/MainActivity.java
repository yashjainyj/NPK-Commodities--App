package com.example.npkcommodities;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.npkcommodities.AdapterClass.Show_Item_Adapter;
import com.example.npkcommodities.DataModels.Item_data_model;
import com.example.npkcommodities.Login.Login;
import com.example.npkcommodities.MyAccount.AccountDetails;
import com.example.npkcommodities.MyAccount.Cart_Main;
import com.example.npkcommodities.MyAccount.MyOrder;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    ShimmerFrameLayout shimmerFrameLayout;
    FirebaseAuth mAuth;
    RecyclerView recyclerView;
    ArrayList<Item_data_model> arrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        shimmerFrameLayout = findViewById(R.id.shimmer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        recyclerView = findViewById(R.id.recyclerview);

        mAuth = FirebaseAuth.getInstance();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Login.class);
                startActivity(intent);
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View view = getLayoutInflater().inflate(R.layout.nav_header_main,null);
        navigationView.setNavigationItemSelectedListener(this);
        TextView number = view.findViewById(R.id.number);
            number.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());
        navigationView.getHeaderView(0).setVisibility(View.GONE);
        navigationView.addHeaderView(view);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.logout) {
//            mAuth.signOut();
//            Intent intent = new Intent(MainActivity.this, Login.class);
//            startActivity(intent);
//            finishAffinity();
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.admin) {
            // Handle the camera action
            Intent intent = new Intent(MainActivity.this, AccountDetails.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.order) {
            Intent intent = new Intent(MainActivity.this, MyOrder.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.cart) {
            Intent intent = new Intent(MainActivity.this, Cart_Main.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.userLogout) {
            mAuth.signOut();
            Intent intent = new Intent(MainActivity.this, Login.class);
            startActivity(intent);
            finishAffinity();

        } else if (id == R.id.nav_share) {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "NPK Commodities");
            String shareMessage= "\nLet me recommend you this application\n\n";
            shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID +"\n\n";
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
            startActivity(Intent.createChooser(shareIntent, "choose one"));


        } else if (id == R.id.nav_send) {
           Dialog builder = new Dialog(MainActivity.this);
            View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.positive_popup_layput,null);
            builder.setContentView(view);
            TextView title = view.findViewById(R.id.titletv);
            title.setText("NPK Commodities");
            TextView mess = view.findViewById(R.id.message);
            mess.setText("Gmail: Npkcommodities@gmail.com");
            Button button = view.findViewById(R.id.btnaccept);
            button.setText("Close");
            button.setTextColor(getResources().getColor(R.color.quantum_black_100));
            button.setBackgroundResource(R.drawable.red_button);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    builder.dismiss();
                }
            });
            builder.show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        shimmerFrameLayout.startShimmer();
        arrayList = new ArrayList<Item_data_model>();
        recyclerView.setHasFixedSize(true);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("products");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arrayList.clear();
                for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                {

                   // Toast.makeText(MainActivity.this, dataSnapshot1.getKey(), Toast.LENGTH_SHORT).show();
                    Item_data_model item_data_model = dataSnapshot1.getValue(Item_data_model.class);
                 //   Toast.makeText(MainActivity.this, item_data_model.getProductName(), Toast.LENGTH_SHORT).show();
                    arrayList.add(item_data_model);
                }
                if(!arrayList.isEmpty())
                {
                    RecyclerView.LayoutManager layoutManager = new GridLayoutManager(MainActivity.this,2);
                    Show_Item_Adapter show_item_adapter = new Show_Item_Adapter(MainActivity.this,arrayList);
                    //Toast.makeText(MainActivity.this, arrayList.get(0).getItemName(), Toast.LENGTH_SHORT).show();
                    recyclerView.setAdapter(show_item_adapter);
                    recyclerView.setLayoutManager(layoutManager);
                    shimmerFrameLayout.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                }
                else
                {
//                    textView.setVisibility(View.VISIBLE);
//                    relativeLayout.setVisibility(View.VISIBLE);
//                    add.setVisibility(View.VISIBLE);
                }
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

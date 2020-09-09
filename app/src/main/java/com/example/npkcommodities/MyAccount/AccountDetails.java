package com.example.npkcommodities.MyAccount;


import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import com.example.npkcommodities.DataModels.MemberModel;
import com.example.npkcommodities.DataModels.UserDetails;
import com.example.npkcommodities.MainActivity;
import com.example.npkcommodities.R;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import de.hdodenhof.circleimageview.CircleImageView;

public class AccountDetails extends AppCompatActivity {
    FloatingActionButton floatingActionButton;
    TextView name,location,email,phone,about,member,age,gender;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    DocumentReference documentReference ;
    StorageReference storageReference;
    ImageView favourite1,sell1,chatdisplay1;
    ProgressDialog progressDialog;
    private CircleImageView circleImageView;
    ScrollView scrollView;
    ShimmerFrameLayout shimmerFrameLayout;
    String s="";
    ImageView back;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_details);
        setUpToolbar();
        floatingActionButton = findViewById(R.id.edit);

        scrollView = findViewById(R.id.scrollview);
        name = findViewById(R.id.name);
        location = findViewById(R.id.location);
        email = findViewById(R.id.email);
        phone = findViewById(R.id.phonenumber);
        about = findViewById(R.id.about);
        member =findViewById(R.id.memberde);
        age = findViewById(R.id.age);
        gender = findViewById(R.id.gender);
        circleImageView = findViewById(R.id.circleImageView);
        back = findViewById(R.id.back);
        progressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
//        cart.setText("Chats");
        storageReference= FirebaseStorage.getInstance().getReference();


        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AccountDetails.this,Update_Profile.class);
                startActivity(intent);
            }
        });






    }

    public  void setUpToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Account");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AccountDetails.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
    //        shimmerFrameLayout.startShimmer();
    }

    @Override
    protected void onPause() {
        super.onPause();
       // shimmerFrameLayout.stopShimmer();
    }


    @Override
    protected void onStart() {
        super.onStart();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        databaseReference.child("userDetails").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserDetails userDetails = dataSnapshot.getValue(UserDetails.class);
                name.setText(userDetails.getFname()+" "+userDetails.getLname());
                    location.setText(userDetails.getLocation());
                    email.setText(userDetails.getEmail());
                    phone.setText(userDetails.getPhone());
                    about.setText(userDetails.getAbout());
                    Glide.with(AccountDetails.this).load(userDetails.getPic_url()).into(circleImageView);
                    Glide.with(AccountDetails.this).load(userDetails.getPic_url()).into(back);
//                    String memberModel=dataSnapshot.child("memberDetails").child("0").child("name").getValue(String.class);
//                    member.setText(memberModel);
                    age.setText(userDetails.getAge());
                    gender.setText(userDetails.getGender());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        databaseReference.child("memberDetails").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                s="";
                for (DataSnapshot dataSnapshot1 :dataSnapshot.getChildren())
                {
                    MemberModel memberModel = dataSnapshot1.getValue(MemberModel.class);
                    s=s+
                            "<b> " +"Name: "+memberModel.getName() +"</b>"+"<br>"+
                            "Age: "+memberModel.getAge() +"<br>"+
                            "Gender: "+memberModel.getGender()+"<br>"+"<br>";
                }
                member.setText(Html.fromHtml(s));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
//        if (mAuth.getUid()!=null)
//        {
//            documentReference=  firebaseFirestore.collection("Users").document(mAuth.getUid());
//            documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//                @Override
//                public void onSuccess(DocumentSnapshot documentSnapshot) {
//                    UserDetails userDetails = documentSnapshot.toObject(UserDetails.class);
//                    name.setText(userDetails.getFname()+" "+userDetails.getLname());
//                    location.setText(userDetails.getLocation());
//                    email.setText(userDetails.getEmail());
//                    phone.setText(userDetails.getPhone());
//                    about.setText(userDetails.getAbout());
//                    Glide.with(AccountDetails.this).load(userDetails.getPic_url()).into(circleImageView);
//                    Glide.with(AccountDetails.this).load(userDetails.getPic_url()).into(back);
//
//                    shimmerFrameLayout.stopShimmer();
//                    shimmerFrameLayout.setVisibility(View.GONE);
//                    scrollView.setVisibility(View.VISIBLE);
//                }
//            }).addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception e) {
//                    Toast.makeText(AccountDetails.this, "Error", Toast.LENGTH_SHORT).show();
//                }
//            });
//        }
//        else
//        {
//            Intent intent = new Intent(AccountDetails.this,MainActivity.class);
//            startActivity(intent);
//            finishAffinity();
//        }
//


    }
}

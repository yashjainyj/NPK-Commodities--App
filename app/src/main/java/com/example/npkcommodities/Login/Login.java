package com.example.npkcommodities.Login;

import android.app.ProgressDialog;
import android.content.Intent;


import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.example.npkcommodities.MainActivity;
import com.example.npkcommodities.MyAccount.EditProfile;
import com.example.npkcommodities.R;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import static android.content.ContentValues.TAG;

public class Login extends AppCompatActivity {
    Button submit,mobile;
    TextView forgetpass;
    FirebaseAuth mAuth;

    TextInputEditText email,password;
    //TrueSdkScope trueScope;
    List<AuthUI.IdpConfig> providers;
    Button SignIn;
    ProgressDialog progressDialog;
    //DocumentReference documentReference ;
    DatabaseReference databaseReference;
    public static final int MY_REQUEST_CODE = 1222;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        forgetpass = findViewById(R.id.forgetpass);
        FirebaseApp.initializeApp(Login.this);
        progressDialog = new ProgressDialog(this);
        submit= findViewById(R.id.submit);
        //mobile = findViewById(R.id.phone);
        email = findViewById(R.id.username);
        password = findViewById(R.id.etPassword);
        SignIn=findViewById(R.id.signin);
        mAuth = FirebaseAuth.getInstance();
        providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.PhoneBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build()
        );

        forgetpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(email.getText().toString().equalsIgnoreCase(""))
                {
                    Toast.makeText(Login.this, "Enter email address", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    mAuth.sendPasswordResetEmail(email.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful())
                            {
                                Toast.makeText(Login.this, "Reset link s send to your Email address", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });


        SignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Email=email.getText().toString();
                String Pass=password.getText().toString();
                if(!Pass.equalsIgnoreCase("") && !Email.equalsIgnoreCase(""))
                {

                    mAuth.signInWithEmailAndPassword(Email,Pass).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            progressDialog.setMessage("please wait a while.....");
                            progressDialog.show();
                            if(mAuth.getCurrentUser().getUid()!=null)
                            {
                                databaseReference = FirebaseDatabase.getInstance().getReference("/users/" + mAuth.getCurrentUser().getUid());
                                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.hasChild("userDetails"))
                                        {

                                            progressDialog.dismiss();
                                            Intent intent = new Intent(Login.this,MainActivity.class);
                                            startActivity(intent);
                                            finishAffinity();

                                        }
                                        else {

                                            progressDialog.dismiss();
                                            Toast.makeText(Login.this, "Please Update Your Profile", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(Login.this, EditProfile.class);
                                            startActivity(intent);
                                            finish();

                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure( @Nullable Exception e) {
                            Toast.makeText(Login.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else
                    Toast.makeText(Login.this, "Field can't be empty", Toast.LENGTH_SHORT).show();
            }
        });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSignInOption();
            }
        });
    }
    private void showSignInOption() {
        startActivityForResult(AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .setTheme(R.style.AppTheme)
                        .build()
                ,MY_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //TrueSDK.getInstance().onActivityResultObtained( Login.this,resultCode, data);
        if(requestCode==MY_REQUEST_CODE)
        {
            IdpResponse response = IdpResponse.fromResultIntent(data);
            if(RESULT_OK ==resultCode)
            {
                FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
             //   Toast.makeText(this, firebaseUser.getEmail(), Toast.LENGTH_SHORT).show();
                progressDialog.setMessage("please wait a while.....");
                progressDialog.show();
                if(mAuth.getCurrentUser().getUid()!=null)
                {
                    databaseReference = FirebaseDatabase.getInstance().getReference("/users/" + mAuth.getCurrentUser().getUid());
                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.hasChild("userDetails"))
                            {
                                progressDialog.dismiss();
                                Intent intent = new Intent(Login.this,MainActivity.class);
                                startActivity(intent);
                                finishAffinity();

                            }
                            else {
                                Toast.makeText(Login.this, "Please Update Your Profile", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(Login.this, EditProfile.class);
                                startActivity(intent);
                                finish();

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }

            }
            else if (requestCode==RESULT_CANCELED)
            {
                Toast.makeText(Login.this, response.getError()+"", Toast.LENGTH_SHORT).show();
            }

        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(mAuth.getCurrentUser()!=null)
        {
            databaseReference = FirebaseDatabase.getInstance().getReference("/users/" + mAuth.getCurrentUser().getUid());
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.hasChild("userDetails"))
                    {
                        progressDialog.dismiss();
                        Intent intent = new Intent(Login.this,MainActivity.class);
                        startActivity(intent);
                        finishAffinity();

                    }
                    else {
                        Toast.makeText(Login.this, "Please Update Your Profile", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Login.this, EditProfile.class);
                        startActivity(intent);
                        finish();

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }


}

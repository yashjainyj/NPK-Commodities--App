package com.example.npkcommodities.Login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.example.npkcommodities.MainActivity;
import com.example.npkcommodities.MyAccount.EditProfile;
import com.example.npkcommodities.R;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {
    DatabaseReference databaseReference;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);
        FirebaseApp.initializeApp(SplashActivity.this);
        AsyncTaskRunner myTask = new AsyncTaskRunner();
        myTask.execute();
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                Intent i=new Intent(SplashActivity.this, Login.class);
//                    startActivity(i);
//                    finish();
//                }
//        }, 3000);
    }
    private class AsyncTaskRunner extends AsyncTask<String, String, String> {

        private String resp;
        ProgressDialog progressDialog;

        @Override
        protected String doInBackground(String... params) {
           return "";
        }


        @Override
        protected void onPostExecute(String result) {
            // execution of result of Long time consuming operation
            progressDialog.dismiss();
            //finalResult.setText(result);
        }


        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(SplashActivity.this,
                    "ProgressDialog",
                    "Wait for a second");
            mAuth = FirebaseAuth.getInstance();
            if(mAuth.getCurrentUser()!=null)
            {
                databaseReference = FirebaseDatabase.getInstance().getReference("/users/" + mAuth.getCurrentUser().getUid());
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChild("userDetails"))
                        {
                            progressDialog.dismiss();
                            Intent intent = new Intent(SplashActivity.this,MainActivity.class);
                            startActivity(intent);
                            finishAffinity();

                        }
                        else {
                            Toast.makeText(SplashActivity.this, "Please Update Your Profile", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(SplashActivity.this, EditProfile.class);
                            startActivity(intent);
                            finish();

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
            else {
                Intent intent = new Intent(SplashActivity.this, Login.class);
                startActivity(intent);
                finish();
            }

        }


        @Override
        protected void onProgressUpdate(String... text) {
            //finalResult.setText(text[0]);

        }
    }

    @Override
    protected void onStart() {
        super.onStart();

    }
}

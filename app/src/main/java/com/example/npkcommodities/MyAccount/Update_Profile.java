package com.example.npkcommodities.MyAccount;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.npkcommodities.DataModels.UserDetails;
import com.example.npkcommodities.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import de.hdodenhof.circleimageview.CircleImageView;

public class Update_Profile extends AppCompatActivity {
    private static final int CHOOSE_IMAGE = 101;
    TextInputEditText fname,lname,location,about,phone,familymember,age1;
    Button submit;
    String url;
    CircleImageView circleImageView;
    ImageView backimage;
    private Uri uriProfileImage;
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private StorageReference storageReference;
    Uri profileUrl;
    ProgressDialog progressDialog;
    //DocumentReference documentReference ;
    DatabaseReference databaseReference;
    FirebaseAuth mAuth;
    RadioGroup radioGroup;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_profile);
        Toolbar toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        mAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        storageReference= FirebaseStorage.getInstance().getReference();
        circleImageView = findViewById(R.id.circleImageView);
        fname = findViewById(R.id.name1);
        lname = findViewById(R.id.l1);
        location= findViewById(R.id.location1);
        about = findViewById(R.id.about1);
        phone = findViewById(R.id.phonenumber1);
        submit = findViewById(R.id.submit);
        familymember = findViewById(R.id.familymemberno1);
        age1 = findViewById(R.id.age1);
        radioGroup = findViewById(R.id.radio);
        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImageChooser();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextInputEditText editText[]= {findViewById(R.id.name1),findViewById(R.id.l1),findViewById(R.id.location1),findViewById(R.id.about1),findViewById(R.id.familymemberno1),findViewById(R.id.phonenumber1)};
                if(uriProfileImage != null)
                {
                    progressDialog.setMessage("please wait a while.....");
                    progressDialog.show();
                    if(uriProfileImage!=null) {
                        StorageReference d = storageReference.child("images/user/" + mAuth.getUid() + "/" + mAuth.getUid() + ".jpg");
                        d.putFile(uriProfileImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                Task<Uri> uri = taskSnapshot.getStorage().getDownloadUrl();
                                while(!uri.isComplete());
                                profileUrl = uri.getResult();
                                //Toast.makeText(EditProfile.this, "Upload Success" + profileUrl.toString(), Toast.LENGTH_SHORT).show();
                                RadioButton radioButton =  findViewById(radioGroup.getCheckedRadioButtonId());
                                UserDetails userDetails  =  new UserDetails(fname.getText().toString(),lname.getText().toString(),phone.getText().toString(),mAuth.getCurrentUser().getEmail(),location.getText().toString(),about.getText().toString(),familymember.getText().toString(), profileUrl.toString(),age1.getText().toString(),radioButton.getText().toString());
                                databaseReference = FirebaseDatabase.getInstance().getReference("/users");
                                databaseReference.child(mAuth.getCurrentUser().getUid()).child("userDetails").setValue(userDetails).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        progressDialog.dismiss();
                                        Toast.makeText(Update_Profile.this, "Profile Updated", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(Update_Profile.this, Member_Detail.class);
                                        intent.putExtra("member",Integer.parseInt(familymember.getText().toString()));
                                        startActivity(intent);
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(Update_Profile.this, "Failed ", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressDialog.dismiss();
                                Toast.makeText(Update_Profile.this, "Failed", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                }
                else
                {
                    RadioButton radioButton =  findViewById(radioGroup.getCheckedRadioButtonId());
                    UserDetails userDetails  =  new UserDetails(fname.getText().toString(),lname.getText().toString(),phone.getText().toString(),mAuth.getCurrentUser().getEmail(),location.getText().toString(),about.getText().toString(),familymember.getText().toString(),url,age1.getText().toString(),radioButton.getText().toString());
                    databaseReference = FirebaseDatabase.getInstance().getReference("/users");
                    databaseReference.child(mAuth.getCurrentUser().getUid()).child("userDetails").setValue(userDetails).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            progressDialog.dismiss();
                            Toast.makeText(Update_Profile.this, "Profile Updated", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(Update_Profile.this, Member_Detail.class);
                            intent.putExtra("member",Integer.parseInt(familymember.getText().toString()));
                            startActivity(intent);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Update_Profile.this, "Failed ", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        CropImage.ActivityResult result = CropImage.getActivityResult(data);
        if (resultCode == RESULT_OK) {
            Uri resultUri = result.getUri();
            uriProfileImage = result.getUri();
            circleImageView.setImageURI(resultUri);
        } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
            Exception error = result.getError();
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        databaseReference = FirebaseDatabase.getInstance().getReference("/users/" + mAuth.getCurrentUser().getUid());
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild("userDetails"))
                {

                    // progressDialog.dismiss();
                    if(uriProfileImage != null){
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users").child(mAuth.getCurrentUser().getUid());
                        databaseReference.child("userDetails").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                UserDetails userDetails =dataSnapshot.getValue(UserDetails.class);
                                fname.setText(userDetails.getFname());
                                lname.setText(userDetails.getLname());
                                phone.setText(userDetails.getPhone());
                                location.setText(userDetails.getLocation());
                                about.setText(userDetails.getAbout());
                                familymember.setText(userDetails.getTotalMember());
                                age1.setText(userDetails.getAge());
                                if(userDetails.getGender().equalsIgnoreCase("Male")){
                                    RadioButton button = findViewById(R.id.male);
                                    radioGroup.check(button.getId());
                                }
                                else if(userDetails.getGender().equalsIgnoreCase("Female")) {
                                    RadioButton button = findViewById(R.id.female);
                                    radioGroup.check(button.getId());
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                    else
                    {
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users").child(mAuth.getCurrentUser().getUid());
                        databaseReference.child("userDetails").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                UserDetails userDetails =dataSnapshot.getValue(UserDetails.class);
                                fname.setText(userDetails.getFname());
                                lname.setText(userDetails.getLname());
                                phone.setText(userDetails.getPhone());
                                location.setText(userDetails.getLocation());
                                about.setText(userDetails.getAbout());
                                familymember.setText(userDetails.getTotalMember());
                                Glide.with(Update_Profile.this).load(userDetails.getPic_url()).into(circleImageView);
                                url=userDetails.getPic_url();
                                age1.setText(userDetails.getAge());
                                if(userDetails.getGender().equalsIgnoreCase("Male")){
                                    RadioButton button = findViewById(R.id.male);
                                    radioGroup.check(button.getId());
                                }
                                else if(userDetails.getGender().equalsIgnoreCase("Female")) {
                                    RadioButton button = findViewById(R.id.female);
                                    radioGroup.check(button.getId());
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }


                }
                else {
                    Toast.makeText(Update_Profile.this, "Please Update Your Profile", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void showImageChooser() {
        CropImage.activity()
                .start(Update_Profile.this);
    }


}

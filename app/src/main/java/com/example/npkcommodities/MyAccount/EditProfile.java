package com.example.npkcommodities.MyAccount;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.npkcommodities.DataModels.UserDetails;
import com.example.npkcommodities.Login.Login;
import com.example.npkcommodities.MainActivity;
import com.example.npkcommodities.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
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

public class EditProfile extends AppCompatActivity {
    private static final int CHOOSE_IMAGE = 101;
    TextInputEditText fname,lname,location,about,phone,familymember,age1;
    Button submit;
    String email="",phone1 = "";
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
        setContentView(R.layout.edit_profile);
        Toolbar toolbar = findViewById(R.id.toolbar);
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
        about.setText(Cart_Main.randomString(10));
        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImageChooser();
            }
        });
        TextInputEditText editText[]= {findViewById(R.id.name1),findViewById(R.id.l1),findViewById(R.id.location1),findViewById(R.id.about1),findViewById(R.id.familymemberno1),findViewById(R.id.phonenumber1)};

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(editText[0].getText())&&!TextUtils.isEmpty(editText[1].getText())&&!TextUtils.isEmpty(editText[2].getText())&&!TextUtils.isEmpty(editText[3].getText())&&!TextUtils.isEmpty(editText[4].getText())&&!TextUtils.isEmpty(editText[5].getText())&&uriProfileImage!=null&&!TextUtils.isEmpty(age1.getText()))
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
                                        Toast.makeText(EditProfile.this, "Profile Updated", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(EditProfile.this, Member_Detail.class);
                                        intent.putExtra("member",Integer.parseInt(familymember.getText().toString()));
                                        startActivity(intent);
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(EditProfile.this, "Failed ", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressDialog.dismiss();
                                Toast.makeText(EditProfile.this, "Failed", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    else
                    {
                        UserDetails userDetails  =  new UserDetails(fname.getText().toString(),lname.getText().toString(),phone.getText().toString(),mAuth.getCurrentUser().getEmail(),location.getText().toString(),"",about.getText().toString(), "","","");
                        databaseReference = FirebaseDatabase.getInstance().getReference("/users");
                        databaseReference.child(mAuth.getCurrentUser().getUid()).child("userDetails").setValue(userDetails).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                progressDialog.dismiss();
                                Toast.makeText(EditProfile.this, "Profile Updated", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(EditProfile.this, Member_Detail.class);
                                intent.putExtra("member",Integer.parseInt(familymember.getText().toString()));
                                startActivity(intent);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(EditProfile.this, "Failed ", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
                else {
                    Toast.makeText(EditProfile.this, "Field is Empty or Choose a Pic", Toast.LENGTH_SHORT).show();
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


    }

    private void showImageChooser() {
        CropImage.activity()
                .start(EditProfile.this);
    }


}
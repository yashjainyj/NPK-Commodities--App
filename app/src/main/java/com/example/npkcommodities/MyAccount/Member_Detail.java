package com.example.npkcommodities.MyAccount;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.npkcommodities.DataModels.MemberModel;
import com.example.npkcommodities.MainActivity;
import com.example.npkcommodities.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lib.customedittext.CustomEditText;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Member_Detail extends AppCompatActivity {
   List<MemberModel> list;
    int n ;
    List views = new ArrayList();
    ArrayList<MemberModel> list1 = new ArrayList();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.member_detail);


        list = new ArrayList<>();


        Button submit = findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!list.isEmpty()){
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("/users/" + FirebaseAuth.getInstance().getCurrentUser().getUid()+"/memberDetails");
                    databaseReference.setValue(list).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(Member_Detail.this, "Updated", Toast.LENGTH_SHORT).show();
                            Intent intent1 = new Intent(Member_Detail.this, MainActivity.class);
                            startActivity(intent1);
                            finishAffinity();
                        }
                    });
                }
                else
                {
                    Intent intent1 = new Intent(Member_Detail.this, MainActivity.class);
                    startActivity(intent1);
                    finishAffinity();
                }

            }
        });
    }
    public void getView(int n){
        LayoutInflater layoutInflator = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout insertPoint = (LinearLayout) findViewById(R.id.ll_content);




        if(!list1.isEmpty()){
            for(int i=0; i<n; i++){
                CustomEditText name,age;

                View view = layoutInflator.inflate(R.layout.member, null);
                TextView textView = view.findViewById(R.id.title);
                textView.setText("Enter Member " + (i+1) + " Details");
                name = view.findViewById(R.id.name);
                age = view.findViewById(R.id.age);
                RadioGroup radioGroup = view.findViewById(R.id.radio);
                if(i<list1.size())
                {
                    name.setText(list1.get(i).getName());
                    age.setText(list1.get(i).getAge()+"");

                    if(list1.get(i).getGender().equalsIgnoreCase("Male")){
                        RadioButton button = view.findViewById(R.id.male);
                        radioGroup.check(button.getId());
                    }if(list1.get(i).getGender().equalsIgnoreCase("Female")){
                    RadioButton button = view.findViewById(R.id.female);
                    radioGroup.check(button.getId());
                }
                    list.add(new MemberModel(list1.get(i).getName(),list1.get(i).getAge(),list1.get(i).getGender()));
                }

                Button button = view.findViewById(R.id.save);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int selectedId=radioGroup.getCheckedRadioButtonId();
                        RadioButton radioButton=findViewById(selectedId);

                        if(!TextUtils.isEmpty(name.getText()))
                        {
                            if(!TextUtils.isEmpty(age.getText()))
                            {
                                Toast.makeText(Member_Detail.this, "Added", Toast.LENGTH_SHORT).show();
                                button.setEnabled(false);
                                list.add(new MemberModel(name.getText().toString(),Integer.parseInt(age.getText().toString()),radioButton.getText().toString()));
                            }
                            else
                                age.setError("Required");
                        }
                        else
                        {
                            name.setError("Required");
                        }
                    }
                });
                view.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                views.add(view);
            }
        }
        else{
            for(int i=0; i<n; i++){
                CustomEditText name,age;
                View view = layoutInflator.inflate(R.layout.member, null);
                TextView textView = view.findViewById(R.id.title);
                textView.setText("Enter Member " + (i+1) + " Details");
                name = view.findViewById(R.id.name);
                age = view.findViewById(R.id.age);
                textView.setText("Enter Member " + (i+1) + " Details");
                RadioGroup radioGroup = view.findViewById(R.id.radio);
               Button button = view.findViewById(R.id.save);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int selectedId=radioGroup.getCheckedRadioButtonId();
                        RadioButton radioButton=findViewById(selectedId);
                        if(!TextUtils.isEmpty(name.getText()))
                        {
                            if(!TextUtils.isEmpty(age.getText()))
                            {
                                Toast.makeText(Member_Detail.this, "Added", Toast.LENGTH_SHORT).show();
                                button.setEnabled(false);
                                list.add(new MemberModel(name.getText().toString(),Integer.parseInt(age.getText().toString()),radioButton.getText().toString()));
                            }
                            else
                                age.setError("Required");
                        }
                        else
                        {
                            name.setError("Required");
                        }


                    }
                });
                view.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                views.add(view);
            }
        }

        for(int i = 0; i<views.size(); i++)
            insertPoint.addView((View) views.get(i));

    }

    @Override
    protected void onStart() {
        super.onStart();
        n=0;
        views.clear();
        Intent intent = getIntent();
        n= intent.getIntExtra("member",0);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users/"+FirebaseAuth.getInstance().getCurrentUser().getUid()+"/memberDetails");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list1.clear();
                views.clear();
                for(DataSnapshot dataSnapshot1 :dataSnapshot.getChildren())
                {
                    list1.add(dataSnapshot1.getValue(MemberModel.class));
                }
                getView(n);
               // Toast.makeText(Member_Detail.this, ""+list1.size(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}

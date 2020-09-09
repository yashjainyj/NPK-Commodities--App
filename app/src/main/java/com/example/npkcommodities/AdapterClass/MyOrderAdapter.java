package com.example.npkcommodities.AdapterClass;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import com.example.npkcommodities.DataModels.Item_data_model;
import com.example.npkcommodities.DataModels.OrderModel;
import com.example.npkcommodities.R;
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

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyOrderAdapter extends RecyclerView.Adapter<MyOrderAdapter.Adapter_ViewHolder> {

    private Context context;

    public MyOrderAdapter(Context context, List<OrderModel> list) {
        this.context = context;
        this.list = list;
    }

    private List<OrderModel> list;
    @NonNull
    @Override
    public Adapter_ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.shop_main_layout,viewGroup,false);
        Adapter_ViewHolder adapter_viewHolder=new Adapter_ViewHolder(view);
        return adapter_viewHolder;
    }
    @Override
    public void onBindViewHolder(@NonNull Adapter_ViewHolder adapter_viewHolder, int i) {
        OrderModel orderModel = list.get(i);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("products");
        databaseReference.child(orderModel.getItemId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Item_data_model item_data_model = dataSnapshot.getValue(Item_data_model.class);
                adapter_viewHolder.name.setText("Rs."+ item_data_model.getProductPrice());
                adapter_viewHolder.name1.setText(item_data_model.getProductName());
                adapter_viewHolder.date.setText(orderModel.getOrderDate());
                Glide.with(context).load(item_data_model.getUrl()).into(adapter_viewHolder.image);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        //        DocumentReference documentReference;
//        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
//        documentReference = firebaseFirestore.collection("Items").document(orderModel.getItemId());
//        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//            @Override
//            public void onSuccess(DocumentSnapshot documentSnapshot) {
//                Item_data_model item_data_model = documentSnapshot.toObject(Item_data_model.class);
//                adapter_viewHolder.name1.setText(item_data_model.getProductName());
//                Glide.with(context).load(item_data_model.getUrl()).into(adapter_viewHolder.image);
//                adapter_viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
////                        Intent intent = new Intent(context, MyOrder_Info.class);
////                        intent.putExtra("orderId", orderModel.getOrderId());
////                        context.startActivity(intent);
//                    }
//                });
//            }
//        });
    }
    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Adapter_ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView name,name1,date;
        public Adapter_ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.shop_name);
            name1 = itemView.findViewById(R.id.shop_address);
            date = itemView.findViewById(R.id.date);
            // desc = itemView.findViewById(R.id.description);
            //category = itemView.findViewById(R.id.category);
        image = itemView.findViewById(R.id.shop_image);
        }
    }
}


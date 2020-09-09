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
import com.example.npkcommodities.DataModels.MyUtility;
import com.example.npkcommodities.MyAccount.Cart_Main;
import com.example.npkcommodities.Product.Product_Detail;
import com.example.npkcommodities.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class Cart_Adapter extends RecyclerView.Adapter<Cart_Adapter.Adapter_ViewHolder> {

    private Context context;

    public Cart_Adapter(Context context, List<Item_data_model> list) {
        this.context = context;
        this.list = list;
    }

    private List<Item_data_model> list;
    @NonNull
    @Override
    public Adapter_ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cart_layout,viewGroup,false);
        Adapter_ViewHolder adapter_viewHolder=new Adapter_ViewHolder(view);
        return adapter_viewHolder;
    }
    @Override
    public void onBindViewHolder(@NonNull Adapter_ViewHolder adapter_viewHolder, int i) {
        Item_data_model item_data_model = list.get(i);
        adapter_viewHolder.name.setText(item_data_model.getProductName());
        adapter_viewHolder.price.setText("Rs."+item_data_model.getProductPrice());
        Glide.with(context).load(item_data_model.getUrl()).into(adapter_viewHolder.item_image);
//        if(MyUtility.m.isEmpty())
//        {
//            adapter_viewHolder.count.setText(1+"");
//        }
//        else
//        {
//            if(MyUtility.m.get(item_data_model.getKey())==null)
//            {
//                adapter_viewHolder.count.setText(1+"");
//            }
//            else
//                adapter_viewHolder.count.setText(MyUtility.m.get(item_data_model.getKey()));
//        }


        adapter_viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Product_Detail.class);
                intent.putExtra("item_id",item_data_model.getKey());
                context.startActivity(intent);

            }
        });
//        adapter_viewHolder.plus.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                int count1 = Integer.parseInt(adapter_viewHolder.count.getText().toString());
//                count1++;
//                //Toast.makeText(context, item_data_model.getItemId(), Toast.LENGTH_SHORT).show();
//                MyUtility.m.put(item_data_model.getKey(),count1+"");
//                adapter_viewHolder.count.setText(count1+"");
//                Intent intent = new Intent(context, Cart_Main.class);
//                context.startActivity(intent);
//
//            }
//        });
//        adapter_viewHolder.minus.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                int count1 = Integer.parseInt(adapter_viewHolder.count.getText().toString());
//                count1--;
//                if (count1<1){
//                    count1=1;
//                }
//                else
//                {
//                    adapter_viewHolder.count.setText(count1+"");
//                    MyUtility.m.put(item_data_model.getKey(),count1+"");
//                    Intent intent = new Intent(context,Cart_Main.class);
//                    context.startActivity(intent);
//                }
//            }
//        });
        adapter_viewHolder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter_viewHolder.databaseReference = adapter_viewHolder.databaseReference.child("users/"+adapter_viewHolder.firebaseAuth.getCurrentUser().getUid()).child("Cart").child(item_data_model.getKey());
                adapter_viewHolder.databaseReference.removeValue();
                MyUtility.m.remove(item_data_model.getKey());
                Intent intent = new Intent(context,Cart_Main.class);
                context.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Adapter_ViewHolder extends RecyclerView.ViewHolder {
        ImageView item_image,delete,plus,minus;
        TextView name,price,count;
        DatabaseReference databaseReference;
        FirebaseAuth firebaseAuth;
        public Adapter_ViewHolder(@NonNull View itemView) {
            super(itemView);
            item_image = itemView.findViewById(R.id.image_item);
            name = itemView.findViewById(R.id.product_name);
            price = itemView.findViewById(R.id.product_price);
            delete = itemView.findViewById(R.id.delete);
//            plus = itemView.findViewById(R.id.plus);
//            minus = itemView.findViewById(R.id.minus);
           // count = itemView.findViewById(R.id.count);
            databaseReference = FirebaseDatabase.getInstance().getReference();
            firebaseAuth = FirebaseAuth.getInstance();

        }
    }
}

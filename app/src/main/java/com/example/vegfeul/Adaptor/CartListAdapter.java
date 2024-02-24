package com.example.vegfeul.Adaptor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.vegfeul.Domain.FoodDomain;
import com.example.vegfeul.Helper.ManagementCart;
import com.example.vegfeul.Interface.ChangeNumberItemsListener;
import com.example.vegfeul.R;

import java.util.ArrayList;

public class CartListAdapter extends RecyclerView.Adapter<CartListAdapter.ViewHolder> {
    public CartListAdapter(ArrayList<FoodDomain> foodDomains, Context context, ChangeNumberItemsListener changeNumberItemsListener) {
        this.foodDomains = foodDomains;
        this.managementCart = new ManagementCart(context);
        this.changeNumberItemsListener = changeNumberItemsListener;
    }

    private ArrayList<FoodDomain> foodDomains;
    private ManagementCart managementCart;
    private ChangeNumberItemsListener changeNumberItemsListener;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_cart,parent,false);

        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull CartListAdapter.ViewHolder holder, int position) {
        FoodDomain foodItem = foodDomains.get(holder.getAdapterPosition()); // Get the current position using getAdapterPosition()

        holder.title.setText(foodItem.getTitle());
        holder.feeEachItem.setText(String.valueOf(foodItem.getFee()));
        holder.totalEachItem.setText(String.valueOf(Math.round((foodItem.getNumberInCart() * foodItem.getFee()) * 100) / 100));
        holder.num.setText(String.valueOf(foodItem.getNumberInCart()));

        int drawableResourceId = holder.itemView.getContext().getResources().getIdentifier(foodItem.getPic(), "drawable", holder.itemView.getContext().getOpPackageName());

        Glide.with(holder.itemView.getContext())
                .load(drawableResourceId)
                .into(holder.pic);

        holder.plusItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentPosition = holder.getAdapterPosition(); // Get the current position using getAdapterPosition()
                if (currentPosition != RecyclerView.NO_POSITION) { // Check if the position is valid
                    managementCart.plusNumberFood(foodDomains, currentPosition, new ChangeNumberItemsListener() {
                        @Override
                        public void changed() {
                            notifyDataSetChanged();
                            if (changeNumberItemsListener != null) {
                                changeNumberItemsListener.changed();
                            }
                        }
                    });
                }
            }
        });

        holder.minusItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentPosition = holder.getAdapterPosition(); // Get the current position using getAdapterPosition()
                if (currentPosition != RecyclerView.NO_POSITION) { // Check if the position is valid
                    managementCart.minusNumberFood(foodDomains, currentPosition, new ChangeNumberItemsListener() {
                        @Override
                        public void changed() {
                            notifyDataSetChanged();
                            if (changeNumberItemsListener != null) {
                                changeNumberItemsListener.changed();
                            }
                        }
                    });
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return foodDomains.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView title,feeEachItem;
        ImageView pic,plusItem,minusItem;
        TextView totalEachItem,num;

        public ViewHolder(@NonNull View itemView ){
            super(itemView);
            title=itemView.findViewById(R.id.titleTxt);
            feeEachItem=itemView.findViewById(R.id.feeEachItem);
            pic=itemView.findViewById(R.id.picCart);
            totalEachItem=itemView.findViewById(R.id.totalEachItem);
            num=itemView.findViewById(R.id.numberItemTxt);
            plusItem=itemView.findViewById(R.id.plusCartBtn);
            minusItem=itemView.findViewById(R.id.minusCartBtn);




        }

    }
}

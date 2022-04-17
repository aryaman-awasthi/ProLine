package com.example.proline_appcode;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class productsAdapter extends RecyclerView.Adapter<productsAdapter.MyViewHolder> {

    Context context;
    ArrayList<Product> productArrayList;

    public productsAdapter(Context context, ArrayList<Product> productArrayList) {
        this.context = context;
        this.productArrayList = productArrayList;
    }

    @NonNull
    @Override
    public productsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.layout_product, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull productsAdapter.MyViewHolder holder, int position) {
        Product product = productArrayList.get(position);
        holder.name.setText(product.productName);
        holder.price.setText(product.sellingPrice);
        holder.qty.setText(String.valueOf(product.quantity));
        holder.b_code.setText(product.barCode);
        holder.disc.setText(String.valueOf(product.discount));
        holder.cp.setText(product.purchasePrice);

    }

    @Override
    public int getItemCount() {
        return productArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView name, price, qty, b_code, disc, cp;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            price = itemView.findViewById(R.id.price);
            qty = itemView.findViewById(R.id.qty);
            b_code = itemView.findViewById(R.id.barcode);
            disc = itemView.findViewById(R.id.discount);
            cp = itemView.findViewById(R.id.cp);

        }
    }
}

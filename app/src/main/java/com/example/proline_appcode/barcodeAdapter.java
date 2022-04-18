package com.example.proline_appcode;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class barcodeAdapter extends RecyclerView.Adapter<barcodeAdapter.MyViewHolder> {


    Context context;
    ArrayList<Product> productArrayList;

    public barcodeAdapter(Context context, ArrayList<Product> barcode) {
        this.context = context;
        this.productArrayList = barcode;
    }

    @NonNull
    @Override
    public barcodeAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_barcode, parent, false);
        return new barcodeAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull barcodeAdapter.MyViewHolder holder, int position) {
        Product product = productArrayList.get(position);
        holder.number.setText(String.valueOf(position+1)+". ");
        holder.barcode_et.setText(product.getBarCode());
        holder.name.setText(product.getProductName());
        holder.price.setText(product.getSellingPrice());
        holder.disc.setText(String.valueOf(product.getDiscount()));

    }

    @Override
    public int getItemCount() {
        return productArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView number, barcode_et, name, price, disc;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            number = itemView.findViewById(R.id.index);
            barcode_et = itemView.findViewById(R.id.barcode);
            name = itemView.findViewById(R.id.name);
            price = itemView.findViewById(R.id.price);
            disc = itemView.findViewById(R.id.discount);
        }
    }
}

package com.example.proline_appcode;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class barcodeAdapter extends RecyclerView.Adapter<barcodeAdapter.MyViewHolder> {


    Context context;
    ArrayList<String> barcode;

    public barcodeAdapter(Context context, ArrayList<String> barcode) {
        this.context = context;
        this.barcode = barcode;
    }

    @NonNull
    @Override
    public barcodeAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_barcode, parent, false);
        return new barcodeAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull barcodeAdapter.MyViewHolder holder, int position) {
        holder.number.setText(String.valueOf(position+1)+". ");
        holder.barcode_et.setText(barcode.get(position));
    }

    @Override
    public int getItemCount() {
        return barcode.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView number, barcode_et;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            number = itemView.findViewById(R.id.index);
            barcode_et = itemView.findViewById(R.id.barcode);
        }
    }
}

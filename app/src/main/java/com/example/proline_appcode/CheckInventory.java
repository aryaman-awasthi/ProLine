package com.example.proline_appcode;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class CheckInventory extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<Product> productArrayList;
    productsAdapter productsAdapter;
    FirebaseFirestore db;
    private FirebaseAuth mAuth;
    Dialog dialog;
    RelativeLayout checkInv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_inventory);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        dialog = new Dialog(this);
        dialog.getWindow().getAttributes().windowAnimations = R.style.animation;


        db = FirebaseFirestore.getInstance();
        productArrayList = new ArrayList<Product>();
        productsAdapter = new productsAdapter(CheckInventory.this, productArrayList);
        mAuth = FirebaseAuth.getInstance();
        checkInv = findViewById(R.id.checkInv);

        recyclerView.setAdapter(productsAdapter);

        loading();
        EventChangeListner();
    }

    private void EventChangeListner() {

        FirebaseUser user = mAuth.getCurrentUser();
        String userID = user.getUid();
        db.collection("inventory").document(userID).collection("inventory")
            .addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                    if (error != null){
                        System.out.println("Err");
                        show_err_snackBar(error.getMessage());
                        dialog.dismiss();
                        return;
                    }
                    for (DocumentChange dc: value.getDocumentChanges()){
                        if (dc.getType() == DocumentChange.Type.ADDED){
                            productArrayList.add(dc.getDocument().toObject(Product.class));
                        }
                        productsAdapter.notifyDataSetChanged();
                        dialog.dismiss();
                    }
                }
            });


    }

    void show_err_snackBar(String err_message){

        checkInv = findViewById(R.id.addProductLayout);

        Snackbar err_snackbar = Snackbar.make(checkInv, "", Snackbar.LENGTH_INDEFINITE);
        View custom_snackbar_view = getLayoutInflater().inflate(R.layout.err_snackbar, null);
        err_snackbar.getView().setBackgroundColor(Color.TRANSPARENT);
        Snackbar.SnackbarLayout snackbarLayout =(Snackbar.SnackbarLayout) err_snackbar.getView();
        snackbarLayout.setPadding(0,0,0,0);
        TextView errText = custom_snackbar_view.findViewById(R.id.sb_error_text);
        errText.setText(err_message);
        (custom_snackbar_view.findViewById(R.id.submit_sb)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                err_snackbar.dismiss();
            }
        });
        snackbarLayout.addView(custom_snackbar_view,0);
        err_snackbar.show();

    }
    private void loading(){
        dialog.setContentView(R.layout.loading_message_layout);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }
}
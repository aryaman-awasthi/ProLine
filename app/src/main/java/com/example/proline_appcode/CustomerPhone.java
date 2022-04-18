package com.example.proline_appcode;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.HashMap;
import java.util.Map;

public class CustomerPhone extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    RelativeLayout customer_phone_layout;
    EditText cus_phone_number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_phone);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        customer_phone_layout = findViewById(R.id.phone_number_layout);

        cus_phone_number = findViewById(R.id.number_cus);
    }

    public void proceed(View view) {
        String phn_cus = cus_phone_number.getText().toString();
        if (phn_cus.isEmpty()){
            show_err_snackBar("Phone Number is mandatory!");
        }
        else {
            checkCus(phn_cus);
        }

    }

    public void checkCus(String phone_num){
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        String userID = firebaseUser.getUid();
        DocumentReference documentReference = firebaseFirestore.collection("Root").document(userID).collection("customer_info").document(phone_num);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null){
                    show_err_snackBar(error.getMessage());
                    return;
                }
                if (value!=null && value.exists()){
                    Map<String, Object> customerInfoMap = value.getData();
                    HashMap<String, Object> customerInfo = new HashMap<String, Object>();
                    for (Map.Entry<String,Object> entry : customerInfoMap.entrySet()) {
                        customerInfo.put(entry.getKey(), entry.getValue());
                    }

                    Intent intent = new Intent(CustomerPhone.this, MakeFinalBill.class);
                    intent.putExtra("Customer Data", customerInfo);
                    startActivity(intent);
                }
                else {
                    Intent intent = new Intent(CustomerPhone.this, AddCustomer.class);
                    startActivity(intent);
                }
            }
        });
    }

    void show_err_snackBar(String err_message){

        customer_phone_layout = findViewById(R.id.phone_number_layout);

        Snackbar err_snackbar = Snackbar.make(customer_phone_layout, "", Snackbar.LENGTH_INDEFINITE);
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

}
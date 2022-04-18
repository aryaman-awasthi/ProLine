package com.example.proline_appcode;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.HashMap;

public class AddProduct extends AppCompatActivity {
    EditText bar_Code_et, product_name_et, purchase_price_et, selling_price_et, discount_et, tax_et, quantity_et;
    Dialog dialog;

    RelativeLayout addProductLayout;

    private FirebaseAuth mAuth;
    FirebaseFirestore fStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        bar_Code_et = findViewById(R.id.barCode_et);
        product_name_et = findViewById(R.id.productName);
        purchase_price_et = findViewById(R.id.cost_cp);
        selling_price_et = findViewById(R.id.cost_sp);
        discount_et = findViewById(R.id.discount);
        tax_et = findViewById(R.id.tax);
        quantity_et = findViewById(R.id.quantity);

        dialog = new Dialog(this);
        dialog.getWindow().getAttributes().windowAnimations = R.style.animation;

        // [START initialize_auth]
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]
        fStore = FirebaseFirestore.getInstance();

    }

    public void addProduct(View view) {
        if(checkFields()){
            HashMap<String, Object> data = getData();
            addProduct(data, (String) data.get("barCode"));
            loading();
        }
        else
        {
            //show err dialog
            show_err_snackBar("BarCode, Product Name, Purchase Price, Quantity & Selling Price are necessary fields!");
        }
    }

    public HashMap<String, Object> getData(){
        HashMap<String, Object> data = new HashMap<>();
        String barCode = bar_Code_et.getText().toString();
        String productName = product_name_et.getText().toString();
        String purchase_price = purchase_price_et.getText().toString();
        String selling_price = selling_price_et.getText().toString();
        String discount_st = discount_et.getText().toString();
        String tax_st = tax_et.getText().toString();
        String quantity_st = quantity_et.getText().toString();


        double tax = 0, discount = 0, quantity = 0;

        if (!tax_st.isEmpty()){
            tax = Integer.parseInt(tax_st);
        }
        if (!discount_st.isEmpty()){
            discount = Integer.parseInt(discount_st);
        }
        quantity = Integer.parseInt(quantity_st);

        data.put("barCode", barCode);
        data.put("quantity", quantity);
        data.put("productName", productName);
        data.put("purchasePrice", purchase_price);
        data.put("sellingPrice", selling_price);
        data.put("discount", discount);
        data.put("tax", tax);


        return data;
    }

    public boolean checkFields(){
        String barCode = bar_Code_et.getText().toString();
        String productName = product_name_et.getText().toString();
        String purchase_price = purchase_price_et.getText().toString();
        String selling_price = selling_price_et.getText().toString();
        String quantity_st = quantity_et.getText().toString();

        if (barCode.isEmpty() || productName.isEmpty() || purchase_price.isEmpty() || selling_price.isEmpty() || quantity_st.isEmpty())
        {
            return false;
        }
        return true;
    }

    public void scanCode(View view) {
        IntentIntegrator intentIntegrator = new IntentIntegrator(AddProduct.this);
        //Set Prompt text
        intentIntegrator.setPrompt("For flash use volume key");
        //Set Beep
        intentIntegrator.setBeepEnabled(true);
        //locked orientation
        intentIntegrator.setOrientationLocked(true);
        //Set Capture activity
        intentIntegrator.setCaptureActivity(Capture.class);
        //Initiate Scan
        intentIntegrator.initiateScan();;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Initialize intent result
        IntentResult intentResult = IntentIntegrator.parseActivityResult(
                requestCode, resultCode, data
        );
        if (intentResult.getContents() != null){
            bar_Code_et.setText(intentResult.getContents());
        }
        else {
            Toast.makeText(getApplicationContext(), "OOPS", Toast.LENGTH_LONG).show();
        }
    }

    private void loading(){
        dialog.setContentView(R.layout.loading_message_layout);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    void show_err_snackBar(String err_message){
        addProductLayout = findViewById(R.id.addProductLayout);

        Snackbar err_snackbar = Snackbar.make(addProductLayout, "", Snackbar.LENGTH_INDEFINITE);
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

    private void addProduct(HashMap<String, Object> data, String barcode){
        FirebaseUser user = mAuth.getCurrentUser();
        String userID = user.getUid();
        DocumentReference documentReference = fStore.collection("Root").document(userID).collection("inventory").document(barcode);
        documentReference.set(data).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                dialog.dismiss();
                open_success_dialog("Data Added Successfully!");

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                dialog.dismiss();
                show_err_snackBar("Some Error Occurred, please try again later!");

            }
        });
    }

    private void open_success_dialog(String message){
        dialog.setContentView(R.layout.success_popup_message);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextView textView = dialog.findViewById(R.id.message);
        textView.setText(message);
        Button button = dialog.findViewById(R.id.submit_sb);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                Intent intent = new Intent(AddProduct.this, Dashboard.class);
                AddProduct.this.finish();
                startActivity(intent);
            }
        });
        dialog.show();
    }

}
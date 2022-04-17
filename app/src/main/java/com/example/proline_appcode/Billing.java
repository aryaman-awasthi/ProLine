package com.example.proline_appcode;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Billing extends AppCompatActivity {

    EditText bar_Code_et;
    Dialog dialog;
    RelativeLayout billingLayout;
    private FirebaseAuth mAuth;
    FirebaseFirestore fStore;
    RecyclerView recyclerView;
    ArrayList<String> barcodes;
    ArrayList<String> qty;
    barcodeAdapter barcodeAdapter;



    Map<String, Object> pData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_billing);


        bar_Code_et = findViewById(R.id.barCode_et);

        dialog = new Dialog(this);
        dialog.getWindow().getAttributes().windowAnimations = R.style.animation;

        // [START initialize_auth]
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]
        fStore = FirebaseFirestore.getInstance();
        billingLayout = findViewById(R.id.billing);

        bar_Code_et = findViewById(R.id.barCode_et);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        barcodes = new ArrayList<String>();

        barcodeAdapter = new barcodeAdapter(Billing.this, barcodes);
        recyclerView.setAdapter(barcodeAdapter);

    }

    public void done(View view) {

        String text = bar_Code_et.getText().toString();
        if (text.isEmpty()){
            show_err_snackBar("Enter Barcode!");
        }
        else {
            barcodes.add(text);
            barcodeAdapter.notifyDataSetChanged();
            bar_Code_et.setHint("Barcode");
            bar_Code_et.setText("");
        }
    }




    public void scanCode(View view) {
        IntentIntegrator intentIntegrator = new IntentIntegrator(Billing.this);
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

        billingLayout = findViewById(R.id.billing);
        Snackbar err_snackbar = Snackbar.make(billingLayout, "", Snackbar.LENGTH_INDEFINITE);
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


    public void makeBill(View view) {
        Intent intent = new Intent(Billing.this, MakeFinalBill.class);
        intent.putStringArrayListExtra("Barcodes", barcodes);

    }
}
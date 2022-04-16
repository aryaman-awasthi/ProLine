package com.example.proline_appcode;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.TestLooperManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUp extends AppCompatActivity {

    RelativeLayout sup_Layout;

    private FirebaseAuth mAuth;
    FirebaseFirestore fStore;

    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        // [START initialize_auth]
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]
        fStore = FirebaseFirestore.getInstance();
        dialog = new Dialog(this);
        dialog.getWindow().getAttributes().windowAnimations = R.style.animation;
    }

    public void signUp_supS(View view) {
        EditText name_et, email_et, pass_et, phone_et;
        name_et = (EditText) findViewById(R.id.name_sup);
        email_et = (EditText) findViewById(R.id.email_sup);
        pass_et = (EditText) findViewById(R.id.pass_sup);
        phone_et = (EditText) findViewById(R.id.phone_sup);



        String name, email, pass, phone;
        name = name_et.getText().toString();
        email = email_et.getText().toString();
        pass = pass_et.getText().toString();
        phone = phone_et.getText().toString();

        String checkFields = checkFields(email, phone, name, pass);
        if (checkFields.equals("OK")){
            open_loading_dialog();
            createAccount(email, pass, name, phone);

        }
        else {
            dialog.dismiss();
            show_err_snackBar(checkFields);
        }

    }

    public void sin_sup(View view) {
        Intent intent = new Intent(SignUp.this, LogIn.class);
        SignUp.this.finish();
        startActivity(intent);
    }

    String checkFields(String email, String phone_number, String name, String password){

        String output = "OK";
        if(!(email.isEmpty() && phone_number.isEmpty() &&
                name.isEmpty() && password.isEmpty())){
            /**
             * Email validation by RFC 5322 standards
             **/

            String email_regex = getString(R.string.email_regex);
            Pattern email_pattern = Pattern.compile(email_regex);
            Matcher email_matcher = email_pattern.matcher(email);
            boolean isEmailCorrect = email_matcher.matches();

            /**
             * Phone number validator
             **/

            String phone_no_regex = getString(R.string.phone_no_regex);
            Pattern phone_pattern = Pattern.compile(phone_no_regex);
            Matcher phone_matcher = phone_pattern.matcher(phone_number);
            boolean isPhoneCorrect = phone_matcher.matches();

            if (!isEmailCorrect)
                output = "Enter valid email!";

            if(!isPhoneCorrect)
                output = "Enter valid phone number";

            if (!(isEmailCorrect && isPhoneCorrect))
                output = "Enter valid email and phone number!";

        }
        else {
            output = "All fields are necessary!";
        }

        return output;
    }

    private void createAccount(String email, String password, String name, String phone_number) {
        // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in userID's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            String userID = user.getUid();
                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                            Map<String, Object> user_info = new HashMap<>();

                            user_info.put("name", name);
                            user_info.put("email", email);
                            user_info.put("phone_number", phone_number);

                            DocumentReference documentReference = fStore.collection("users").document(userID);
                            documentReference.set(user_info).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {

                                    user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            dialog.dismiss();
                                            open_success_dialog(getString(R.string.account_creation_text));
                                            mAuth.signOut();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            dialog.dismiss();
                                            show_err_snackBar("Failed to signup!");
                                        }
                                    });
                                    //open_success_dialog(getString(R.string.account_creation_text));
                                }
                            });




                        } else {
                            // If sign in fails, display a message to the user.
                            show_err_snackBar("Failed to signup!");
                        }
                    }
                });
        // [END create_user_with_email]
    }

    private void show_err_snackBar(String err_message){
        sup_Layout = findViewById(R.id.sup_layout);
        Snackbar err_snackbar = Snackbar.make(sup_Layout, "", Snackbar.LENGTH_INDEFINITE);
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
                Intent intent = new Intent(SignUp.this, WelcomeScreen.class);
                SignUp.this.finish();
                startActivity(intent);
            }
        });
        dialog.show();
    }
    private void open_loading_dialog(){
        dialog.setContentView(R.layout.loading_message_layout);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }
}
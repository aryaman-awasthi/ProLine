package com.example.proline_appcode;

import androidx.annotation.NonNull;
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

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LogIn extends AppCompatActivity {
    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]

    private Dialog dialog;

    RelativeLayout loginLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        mAuth = FirebaseAuth.getInstance();

        dialog = new Dialog(this);
        dialog.getWindow().getAttributes().windowAnimations = R.style.animation;
    }

    public void f_pass(View view) {
        Intent intent = new Intent(LogIn.this, ForgotPassword.class);
        startActivity(intent);
    }

    public void signIn_lS(View view) {
        EditText email_et, pass_et;
        email_et = (EditText) findViewById(R.id.email_sin);
        pass_et = (EditText) findViewById(R.id.pass_sin);



        String email, pass;
        email = email_et.getText().toString();
        pass = pass_et.getText().toString();

        String validator_message = validateFields(email, pass);
        if (validator_message.equals("OK")){
            loading();
            signIn(email, pass);
        }
        else {
            dialog.dismiss();
            show_err_snackBar(validator_message);
        }
    }

    public void sup_lS(View view) {
        Intent intent = new Intent(LogIn.this, SignUp.class);
        LogIn.this.finish();
        startActivity(intent);
    }

    private String validateFields(String email, String password){

        String output = "OK";
        if(!(email.isEmpty() && password.isEmpty())){
            loading();
            dialog.dismiss();
        }
        else {
            output = "All fields are necessary!";
        }

        return output;
    }

    void show_err_snackBar(String err_message){
        loginLayout = findViewById(R.id.login_layout);

        Snackbar err_snackbar = Snackbar.make(loginLayout, "", Snackbar.LENGTH_INDEFINITE);
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

    private void signIn(String email, String password) {
        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user.isEmailVerified()){
                                dialog.dismiss();
                                String user_id = user.getUid();
                                Intent intent = new Intent(LogIn.this, Dashboard.class);
                                intent.putExtra("user_id", user_id);
                                LogIn.this.finish();
                                startActivity(intent);
                            }
                            else {
                                mAuth.signOut();
                                dialog.dismiss();
                                show_err_snackBar("Your email is not verified, first verify your email!");
                            }

                        } else {
                            dialog.dismiss();
                            // If sign in fails, display a message to the user.
                            show_err_snackBar("Some error occurred, please try again later!");

                        }
                    }
                });
        // [END sign_in_with_email]
    }


    private void loading(){
        dialog.setContentView(R.layout.loading_message_layout);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

}
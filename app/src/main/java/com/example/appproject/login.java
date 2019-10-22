
package com.example.appproject;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class login extends AppCompatActivity implements View.OnClickListener{
    FirebaseAuth mAuth;
    EditText editTextEmail,editTextPassword,editTextEmailforget;
    TextView editTextforgotpassword;
    View Dialog_for;
    final  Context context=this;
    AlertDialog alertDialog;
    Boolean userloggedin=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        editTextEmail=(EditText) findViewById(R.id.login_email);
        editTextPassword=(EditText)findViewById(R.id.login_password);
        editTextEmailforget=(EditText)findViewById(R.id.email1);
        findViewById(R.id.loginBtn).setOnClickListener(this);
        findViewById(R.id.new_user_btn).setOnClickListener(this);
        findViewById(R.id.forgot_password).setOnClickListener(this);
    }
     protected void forgotpassword()
    {

        LayoutInflater li = LayoutInflater.from(context);
        final View promptsView = li.inflate(R.layout.forgot_pass, null);
        Dialog_for = promptsView;
        AlertDialog.Builder builder
                = new AlertDialog
                .Builder(login.this);
        builder.setTitle("Change Password");
        builder.setCancelable(false);
        builder.setView(promptsView);

        // Set the Negative button with No name
        // OnClickListener method is use
        // of DialogInterface interface.
        builder
                .setNegativeButton(
                        "Cancel",
                        new DialogInterface
                                .OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which)
                            {

                                // If user click no
                                // then register_dialog box is canceled.

                                dialog.cancel();
                            }
                        });

        builder
                .setPositiveButton(
                        "Submit",
                        new DialogInterface
                                .OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which)
                            {
                                editTextEmailforget=promptsView.findViewById(R.id.email1);
                                final String Email_forget=editTextEmailforget.getText().toString().trim();
                                if (!Patterns.EMAIL_ADDRESS.matcher(Email_forget).matches()) {
                                    editTextEmailforget.setError("Enter valid emailid");
                                    editTextEmailforget.requestFocus();
                                    return;
                                }

                                mAuth.sendPasswordResetEmail(Email_forget)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful())
                                                {
                                                    Toast.makeText(login.this,"Password set to your mail",Toast.LENGTH_SHORT).show();
                                                }
                                                else
                                                {
                                                    Toast.makeText(login.this,task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });


                                //Save data
                                dialog.cancel();
                            }
                        });
        alertDialog = builder.create();
        alertDialog.show();
        // is_for_active = alertDialog.isShowing();
        //  Button b = Dialog_for.findViewById(R.id.Send);




    }
    private void userlogin()
    {
        String email=editTextEmail.getText().toString().trim();
        String password=editTextPassword.getText().toString().trim();

        if (email.isEmpty()) {
            editTextEmail.setError("Email is required");
            editTextEmail.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("Enter valid emailid");
            editTextEmail.requestFocus();
            return;
        }
        if (password.isEmpty()) {
            editTextPassword.setError("Enter Password");
            editTextPassword.requestFocus();
            return;
        }
        if (password.length() < 6) {
            editTextPassword.setError("Minimun length of password is 6");
            editTextPassword.requestFocus();
            return;
        }

        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    finish();
                    Intent intent=new Intent(login.this,check.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    userloggedin=true;
                    startActivity(intent);
                }
                else {
                    Toast.makeText(getApplicationContext(),task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    protected void onStart()
    {
        super.onStart();
        if (mAuth.getCurrentUser()!=null)
        {
            finish();
            startActivity(new Intent(this,check.class));
        }
    }

    @Override
    public void onClick(View view) {
        switch(view.getId())
        {
            case R.id.new_user_btn:
                finish();
                startActivity(new Intent(this,Register.class));
                break;
            case R.id.loginBtn:
                userlogin();
                break;
            case R.id.forgot_password:
                forgotpassword();
                break;
        }

    }
}
package com.example.appproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class login extends AppCompatActivity implements View.OnClickListener{
    FirebaseAuth mAuth;
    EditText editTextEmail,editTextPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        editTextEmail=(EditText) findViewById(R.id.login_email);
        editTextPassword=(EditText)findViewById(R.id.login_password);
        findViewById(R.id.loginBtn).setOnClickListener(this);
        findViewById(R.id.new_user_btn).setOnClickListener(this);
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
        }

    }
}

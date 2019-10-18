package com.example.appproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthCredential;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    TextView tx;
    EditText t,Eemail,Eusername,Epassword,Econfirmpassword;
    Button loginbtn;
    Button cancelbtn;

    View Dialog_reg,Dialog_for,Dialog_ch;
    CountDownTimer ctimer;
    final Context context = this;
    AlertDialog alertDialog;
    boolean is_Reg_active = false,is_for_active = false,is_cpass_active;
    FirebaseAuth mFirebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mFirebaseAuth=FirebaseAuth.getInstance();
         Eusername=findViewById(R.id.username);
         Epassword=findViewById(R.id.password);
         Econfirmpassword=findViewById(R.id.confirm);
         Eemail=findViewById(R.id.email);
         loginbtn=findViewById(R.id.loginBtn);


        if(savedInstanceState != null){
            if(savedInstanceState.getBoolean("Dialog") == true)
            {
                LayoutInflater li = LayoutInflater.from(context);
                View promptsView = li.inflate(R.layout.register_dialog, null);
                Dialog_reg = promptsView;
                AlertDialog.Builder builder
                        = new AlertDialog
                        .Builder(LoginActivity.this);
                builder.setTitle("Register");
                builder.setCancelable(false);
                builder.setView(promptsView);
                builder
                        .setPositiveButton(
                                "Submit",
                                new DialogInterface
                                        .OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        is_Reg_active = false;
                                        dialog.cancel();
                                        String Semail, Susername, Spass, Sconfirmpassword;
                                        t = Dialog_reg.findViewById(R.id.username);
                                        Susername = t.getText().toString();
                                        t = Dialog_reg.findViewById(R.id.password);
                                        Spass = t.getText().toString();
                                        t = Dialog_reg.findViewById(R.id.confirm);
                                        Sconfirmpassword = t.getText().toString();
                                        t = Dialog_reg.findViewById(R.id.email);
                                        Semail = t.getText().toString();


                                        //Save data


                                        //checking if email and passwords are empty
                                        if (Susername.isEmpty()) {
                                            Eusername.setError("please enter username");    //check email is wriiten or not
                                            Eusername.requestFocus();
                                        }
                                        if (Semail.isEmpty()) {
                                            Eemail.setError("please enter email id");    //check email is wriiten or not
                                            Eemail.requestFocus();
                                        }

                                        if (Spass.isEmpty()) {
                                            Epassword.setError("please enter password");    //check email is wriiten or not
                                            Epassword.requestFocus();
                                        }
                                        if (Spass != Sconfirmpassword) {
                                            Econfirmpassword.setError("password dont match");    //check email is wriiten or not
                                            Econfirmpassword.requestFocus();
                                        }
                                        //if the email and password are not empty
                                        else {


                                            //creating a new user i.e registration
                                            mFirebaseAuth.createUserWithEmailAndPassword(Semail, Spass)
                                                    .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {

                                                        @Override
                                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                                            //checking if success
                                                            if (task.isSuccessful()) {
                                                                Toast.makeText(LoginActivity.this, "Registration successful", Toast.LENGTH_LONG).show();
                                                                finish();

                                                            } else {
                                                                //display some message here
                                                                Toast.makeText(LoginActivity.this, "Registration Error", Toast.LENGTH_LONG).show();
                                                            }

                                                        }
                                                    });
                                        }


                                    }
                                });


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
                                        is_Reg_active = false;
                                        dialog.cancel();
                                    }
                                });


                alertDialog = builder.create();
                alertDialog.show();
                t =  promptsView.findViewById(R.id.username);
                t.setText(savedInstanceState.getString("user"));
                t =  promptsView.findViewById(R.id.password);
                t.setText(savedInstanceState.getString("pass"));
                t =  promptsView.findViewById(R.id.confirm);
                t.setText(savedInstanceState.getString("cpass"));
                t =  promptsView.findViewById(R.id.email);
                t.setText(savedInstanceState.getString("email"));
                is_Reg_active = alertDialog.isShowing();

            }
            if (savedInstanceState.getBoolean("is_for_active") == true){
                LayoutInflater li = LayoutInflater.from(context);
                final View promptsView = li.inflate(R.layout.forgot_pass, null);
                Dialog_for = promptsView;
                tx = Dialog_for.findViewById(R.id.timer);
                tx.setText(savedInstanceState.getString("timer"));
                t = Dialog_for.findViewById(R.id.email1);
                t.setText(savedInstanceState.getString("email1"));
                t = Dialog_for.findViewById(R.id.otp);
                t.setText(savedInstanceState.getString("otp"));

                AlertDialog.Builder builder
                        = new AlertDialog
                        .Builder(LoginActivity.this);
                builder.setTitle("OTP Request");
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
                                        is_for_active = false;
                                        if(ctimer!=null)
                                        {
                                            ctimer.cancel();
                                        }
                                        dialog.cancel();
                                    }
                                });
                alertDialog = builder.create();
                if(savedInstanceState.getString("Visibility")=="VISIBLE")
                {
                    Dialog_for.findViewById(R.id.layout1).setVisibility(View.VISIBLE);
                }
                alertDialog.show();
                is_for_active = alertDialog.isShowing();
                Button b = Dialog_for.findViewById(R.id.Send);
                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Dialog_for.findViewById(R.id.layout1).setVisibility(View.VISIBLE);
                        tx = Dialog_for.findViewById(R.id.timer);
                        ctimer = new CountDownTimer(60000, 1000) { // adjust the milli seconds here

                            public void onTick(long millisUntilFinished) {
                                int time = (int)millisUntilFinished/1000;

                                tx.setText("Remaining : 00:"+time);
                            }

                            public void onFinish() {
                                tx.setText("done!");
                                ctimer.cancel();
                            }
                        }.start();
                    }
                });

            }
            if (savedInstanceState.getBoolean("is_cpass_active") == true){
                LayoutInflater li = LayoutInflater.from(context);


                is_for_active = false;
                final View promptsView = li.inflate(R.layout.change_pass, null);
                Dialog_ch = promptsView;
                t = Dialog_ch.findViewById(R.id.pass1);
                t.setText(savedInstanceState.getString("pass1"));
                t = Dialog_ch.findViewById(R.id.cpass2);
                t.setText(savedInstanceState.getString("cpass2") );
                AlertDialog.Builder builder
                        = new AlertDialog
                        .Builder(LoginActivity.this);
                builder.setTitle("Reset Password");
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
                                        is_cpass_active = false;
                                        dialog.cancel();
                                    }
                                });
                alertDialog = builder.create();
                alertDialog.show();

                is_cpass_active = alertDialog.isShowing();
                Log.d("Value", "m bhi true hu m bhi");
                Button b = Dialog_ch.findViewById(R.id.cpass_sub);
                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        is_cpass_active = false;
                    }
                });
            }
        }
        //login authentication
        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Semail = Eemail.getText().toString();
                String Spass=Epassword.getText().toString();
                if(Semail.isEmpty())
                {
                    Eemail.setError("please enter email id");    //check email is wriiten or not
                    Eemail.requestFocus();

                }
                if(Spass.isEmpty())                                //check pass is wriiten or not
                {
                    Epassword.setError("please enter password");
                    Epassword.requestFocus();
                }
               else                                                 //its a user.
                {
                    mFirebaseAuth.signInWithEmailAndPassword(Semail,Spass).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful())
                            {
                                Toast.makeText(LoginActivity.this, getString(R.string.failedlogin), Toast.LENGTH_LONG).show();
                            }
                            else
                            {
                                Toast.makeText(LoginActivity.this,"login successful", Toast.LENGTH_LONG).show();

                            }

                        }
                    });
                }


            }
        });
    }

    protected void forgot(View v){
        LayoutInflater li = LayoutInflater.from(context);
        final View promptsView = li.inflate(R.layout.forgot_pass, null);
        Dialog_for = promptsView;
        AlertDialog.Builder builder
                = new AlertDialog
                .Builder(LoginActivity.this);
        builder.setTitle("OTP Request");
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
                                is_for_active = false;
                                if(ctimer!=null){
                                    ctimer.cancel();
                                }
                                dialog.cancel();
                            }
                        });
        alertDialog = builder.create();
        alertDialog.show();
        is_for_active = alertDialog.isShowing();
        Button b = Dialog_for.findViewById(R.id.Send);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog_for.findViewById(R.id.layout1).setVisibility(View.VISIBLE);
                tx = Dialog_for.findViewById(R.id.timer);
                ctimer = new CountDownTimer(60000, 1000) { // adjust the milli seconds here

                    public void onTick(long millisUntilFinished) {
                        int time = (int)millisUntilFinished/1000;

                        tx.setText("Remaining : 00:"+time);
                    }

                    public void onFinish() {
                        tx.setText("done!");
                        ctimer.cancel();
                    }
                }.start();
            }
        });



    }
    protected void  submit_otp(View view){
        LayoutInflater li = LayoutInflater.from(context);
        ctimer.cancel();
        is_for_active = false;
        alertDialog.cancel();
        final View promptsView = li.inflate(R.layout.change_pass, null);
        Dialog_ch = promptsView;
        AlertDialog.Builder builder
                = new AlertDialog
                .Builder(LoginActivity.this);
        builder.setTitle("Reset Password");
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
                                is_cpass_active = false;
                                dialog.cancel();
                            }
                        });
        alertDialog = builder.create();
        alertDialog.show();
        is_cpass_active = alertDialog.isShowing();

        Button b = Dialog_ch.findViewById(R.id.cpass_sub);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                is_cpass_active = false;
            }
        });

    }
    protected void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);
        if(is_Reg_active == true){
            String EMAIL,username,password,confirmpassword ;
            t = Dialog_reg.findViewById(R.id.username);
            username = t.getText().toString();
            state.putString("user",username);
            t = Dialog_reg.findViewById(R.id.password);
            password = t.getText().toString();
            state.putString("pass", password);
            t = Dialog_reg.findViewById(R.id.confirm);
            confirmpassword = t.getText().toString();
            state.putString("cpass", confirmpassword);
            t = Dialog_reg.findViewById(R.id.email);
            EMAIL = t.getText().toString();
            state.putString("email", EMAIL);



        }
        if (is_for_active == true){
            String value;
            tx = Dialog_for.findViewById(R.id.timer);
            value = tx.getText().toString();
            state.putString("timer", value);
            t = Dialog_for.findViewById(R.id.email1);
            value = t.getText().toString();
            state.putString("email1", value);
            t = Dialog_for.findViewById(R.id.otp);
            value = t.getText().toString();
            state.putString("otp", value);
            LinearLayout l=Dialog_for.findViewById(R.id.layout1);
            if(l.getVisibility()==View.VISIBLE)
            {
                state.putString("Visibility","VISIBLE");
            }
            else
            {
                state.putString("Visibility","GONE");
            }


        }
        if(is_cpass_active == true){

            String value;
            t = Dialog_ch.findViewById(R.id.pass1);
            value = t.getText().toString();
            state.putString("pass1", value);
            t = Dialog_ch.findViewById(R.id.cpass2);
            value = t.getText().toString();
            state.putString("cpass2", value);

        }
        state.putBoolean("Dialog", is_Reg_active);
        state.putBoolean("is_for_active", is_for_active);
        state.putBoolean("is_cpass_active", is_cpass_active);

    }

    protected void Register(View view){
        LayoutInflater li = LayoutInflater.from(context);
        final View promptsView = li.inflate(R.layout.register_dialog, null);
        Dialog_reg = promptsView;
        AlertDialog.Builder builder
                = new AlertDialog
                .Builder(LoginActivity.this);
        builder.setTitle("Register");
        builder.setCancelable(false);
        builder.setView(promptsView);
        builder
                .setPositiveButton(
                        "Submit",
                        new DialogInterface
                                .OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which)
                            {
                                is_Reg_active=false;
                                String EMAIL,username,password,confirmpassword;
                                t = Dialog_reg.findViewById(R.id.username);
                                username = t.getText().toString();
                                t = Dialog_reg.findViewById(R.id.password);
                                password = t.getText().toString();
                                t = Dialog_reg.findViewById(R.id.confirm);
                                confirmpassword = t.getText().toString();
                                t = Dialog_reg.findViewById(R.id.email);
                                EMAIL = t.getText().toString();



                                //Save data
                            }
                        });

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
                                is_Reg_active = false;
                                dialog.cancel();
                            }
                        });
        alertDialog = builder.create();
        alertDialog.show();
        is_Reg_active = alertDialog.isShowing();

    }
}
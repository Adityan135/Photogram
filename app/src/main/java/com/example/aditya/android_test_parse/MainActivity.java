package com.example.aditya.android_test_parse;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.LogInCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,View.OnKeyListener{
    Boolean signupModeActive=true;
    EditText usernameEditText;
    EditText passwordEdiText;
 public void gotoNext(){
     Intent intent=new Intent(MainActivity.this, ListActivity.class);
     startActivity(intent);
 }
    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_ENTER&&event.getAction()==KeyEvent.ACTION_DOWN){
            signUpClicked(v);
        }
        return false;
    }

    @Override
    public void onClick(View v) {
       if(v.getId()==R.id.loginbut) {
           Button signupButton = findViewById(R.id.signup);
           TextView logintext = findViewById(R.id.loginbut);
           if (signupModeActive) {
               signupModeActive = false;
               signupButton.setText("Login");
               logintext.setText("or Sign up");
           } else {
               signupModeActive = true;
               signupButton.setText("Sign up");
               logintext.setText("or Login");
           }
       }
           else if(v.getId()==R.id.imageView||v.getId()==R.id.background){
           InputMethodManager inputMethodManager= (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
           inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
           }

    }

    public void signUpClicked(View view){

        if(usernameEditText.getText().toString().isEmpty()||passwordEdiText.getText().toString().isEmpty()){
            Toast.makeText(this,"A username and a password are required.",Toast.LENGTH_SHORT).show();
        }
        else {
            if (signupModeActive) {
                ParseUser user = new ParseUser();
                user.setUsername(usernameEditText.getText().toString());
                user.setPassword(passwordEdiText.getText().toString());
                user.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            Log.i("Signup", "Success");
                            gotoNext();
                        } else {
                            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
            else{
                //Login
                ParseUser.logInInBackground(usernameEditText.getText().toString().trim(), passwordEdiText.getText().toString(), new LogInCallback() {
                    @Override
                    public void done(ParseUser user, ParseException e) {
                        if(user!=null){
                            //Log.i("Login","202 ok");
                            gotoNext();

                        }
                        else{
                            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Photogram");
        TextView logintext=findViewById(R.id.loginbut);
        usernameEditText=findViewById(R.id.usernametext);
        passwordEdiText=findViewById(R.id.passwordtext);
        ConstraintLayout constraintLayout=findViewById(R.id.background);
        ImageView imageView=findViewById(R.id.imageView);
        passwordEdiText.setOnKeyListener(this);
        constraintLayout.setOnClickListener(this);
        imageView.setOnClickListener(this);
        logintext.setOnClickListener(this);
        if(ParseUser.getCurrentUser()!=null){
            gotoNext();
        }
        ParseAnalytics.trackAppOpenedInBackground(getIntent());
    }
}

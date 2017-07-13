package com.example.user.login;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {

    EditText username_et, password_et;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        username_et = (EditText) findViewById(R.id.et_username);
        password_et = (EditText) findViewById(R.id.et_password);
    }
    public void onLogin (View view){
        String username = username_et.getText().toString();
        String password = password_et.getText().toString();
        String type = "login";
        KuliBelakang kuliBelakang = new KuliBelakang(this);
        kuliBelakang.execute(type , username, password );

    }


}

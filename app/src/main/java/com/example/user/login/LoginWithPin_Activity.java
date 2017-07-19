package com.example.user.login;

import android.app.AlertDialog;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.example.user.login.Helper.url_link;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

import android.content.Context;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;


public class LoginWithPin_Activity extends Activity {
    Button login;
    EditText username_et, pin_et;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_withpin);
        login = (Button) findViewById(R.id.b_login);

        username_et = (EditText) findViewById(R.id.et_username);
        pin_et = (EditText) findViewById(R.id.et_pin);


        login.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                String username = username_et.getText().toString();
                String pin = pin_et.getText().toString();
                String type = "loginwithpin";
                if (TextUtils.isEmpty(username)) {
                    Toast msg = Toast.makeText(LoginWithPin_Activity.this, "Username is Empty.", Toast.LENGTH_LONG);
                    msg.setGravity(Gravity.CENTER, msg.getXOffset() / 2, msg.getYOffset() / 2);
                    msg.show();
                } else if (TextUtils.isEmpty(pin)) {
                    Toast msg = Toast.makeText(LoginWithPin_Activity.this, "Pin is Empty.", Toast.LENGTH_LONG);
                    msg.setGravity(Gravity.CENTER, msg.getXOffset() / 2, msg.getYOffset() / 2);
                    msg.show();
                } else {
                    new Masuk().execute(type, username, pin);
                }
            }
        });
    }

    public class Masuk extends AsyncTask<String, Void, String> {
        ProgressDialog pDialog;
        Context context;

        AlertDialog alertDialog;

        @Override
        public String doInBackground(String... params) {
            String type = params[0];
            String login_url;
            url_link link = new url_link();
            login_url = link.getUrl_link(type);
            if (type.equals("loginwithpin")) {
                try {
                    String username = params[1];
                    String pin = params[2];
                    URL url = new URL(login_url);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);
                    OutputStream outputstream = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputstream, "UTF-8"));
                    String post_data = URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8")
                            + "&" + URLEncoder.encode("pin", "UTF-8") + "=" + URLEncoder.encode(pin, "UTF-8");
                    bufferedWriter.write(post_data);
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                    String result = "";
                    String line = "";
                    while ((line = bufferedReader.readLine()) != null) {
                        result += line;

                    }
                    bufferedReader.close();
                    inputStream.close();
                    httpURLConnection.disconnect();
                    return result;
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            alertDialog = new AlertDialog.Builder(LoginWithPin_Activity.this).create();
            alertDialog.setTitle("Login Status");
            pDialog = new ProgressDialog(LoginWithPin_Activity.this);
            pDialog.setMessage("Wait a Moment...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected void onPostExecute(String result) {
            pDialog.dismiss();
            JSONObject jsonObj = null;
            String reason = "";
            int status = 100;
            try

            {
                jsonObj = XML.toJSONObject(result);
                JSONObject jObj = new JSONObject(jsonObj.toString());
                JSONObject MedMasterUser = jObj.getJSONObject("MedMasterUser");
                status = MedMasterUser.getInt("status");
                reason = MedMasterUser.getString("reason");

            } catch (JSONException e) {
                Log.e("JSON exception", e.getMessage());
                e.printStackTrace();
            }
            if (status == -1) {
                alertDialog.setMessage(reason);
                alertDialog.show();
            } else if (status == 0) {
                alertDialog.setMessage(reason);
                alertDialog.show();
                Intent Home = new Intent(LoginWithPin_Activity.this, HomeActivity.class);
                alertDialog.dismiss();
                finish();
                startActivity(Home);

            }
        }
    }
}


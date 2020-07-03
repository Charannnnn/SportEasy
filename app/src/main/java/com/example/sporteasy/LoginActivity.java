package com.example.sporteasy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    static String accessTkn;
    private RequestQueue queue;
    JsonObjectRequest objectRequest;
    JSONObject data;
    static JSONObject userData;
    EditText roll,pwd;
    static String sroll;
    TextView forgot_pwd;
    String spwd, URL = "https://sport-resources-booking-api.herokuapp.com/login";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        forgot_pwd = findViewById(R.id.forgot_pwd);
        Button logbtn =  findViewById(R.id.logbtn);
        roll =  findViewById(R.id.roll);
        pwd =  findViewById(R.id.pwd);
        queue = Volley.newRequestQueue(this);


        logbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sroll = roll.getText().toString();
                spwd = pwd.getText().toString();
                data = new JSONObject();
                try {
                    data.put("id",sroll);
                    data.put("password",spwd);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                objectRequest = new JsonObjectRequest(Request.Method.POST,
                        URL,
                        data,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    accessTkn = response.getString("access_token");
                                    //GetUserData();
                                    openActivity2();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast toast = Toast.makeText(getApplicationContext(),"Enter Valid Credentials",Toast.LENGTH_LONG);
                                toast.show();
                            }
                        });


                queue.add(objectRequest);

            }
        });
        forgot_pwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity6();
            }
        });
    }

    private void GetUserData() {
        RequestQueue uqueue;
        uqueue = Volley.newRequestQueue(this);
        String URLQ = "https://sport-resources-booking-api.herokuapp.com/users";
        JSONObject uid = new JSONObject();
        try {
            uid.put("id",sroll);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest urequest = new JsonObjectRequest(Request.Method.POST,
                URLQ,
                uid,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        userData = response;
                       //openActivity2();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(LoginActivity.this, "Failed to login contact administrator", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "Bearer " + accessTkn);
                return params;
            }
            @Override
            public Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id", sroll);
                return params;
            }
        };
        uqueue.add(urequest);
}

    private void openActivity2() {
        Intent intent = new Intent(LoginActivity.this, Home.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private  void openActivity6() {
        Intent intent = new Intent(this,Forgot_Password.class);
        startActivity(intent);
    }

}
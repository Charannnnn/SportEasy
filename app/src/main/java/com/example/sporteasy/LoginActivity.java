package com.example.sporteasy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {
    static String accessTkn;
    private RequestQueue queue;
    JsonObjectRequest objectRequest;
    JsonArrayRequest arrayRequest;
    JSONObject data;
    EditText roll,pwd;
    static String sroll;
    String spwd, URL = "https://sport-resources-booking-api.herokuapp.com/login";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        Button logbtn = (Button) findViewById(R.id.logbtn);
        roll=(EditText) findViewById(R.id.roll);
        pwd=(EditText) findViewById(R.id.pwd);
        queue = Volley.newRequestQueue(this);

        logbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sroll=roll.getText().toString();
                spwd=pwd.getText().toString();
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
                                    openActivity2();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast toast = Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_LONG);
                                toast.show();
                            }
                        });


                queue.add(objectRequest);
            }
        });
    }

    private void openActivity2() {
        Intent intent = new Intent(LoginActivity.this, Home.class);
        startActivity(intent);
    }

}
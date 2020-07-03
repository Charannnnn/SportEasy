package com.example.sporteasy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Account extends AppCompatActivity {
    TextView name,roll,fine;
    Button changePassword, logOut;
    ConstraintLayout account_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        changePassword = findViewById(R.id.changePassword);
        account_layout = findViewById(R.id.account_layout);
        account_layout.setVisibility(View.INVISIBLE);

        //Initialize and Assign
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        // Set Home Selected
        bottomNavigationView.setSelectedItemId(R.id.account);
        //Perform ItemSelecctedListener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.history:
                        startActivity(new Intent(getApplicationContext(), History.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(), Home.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.account:
                        return true;
                }
                return false;
            }
        });

        name = (TextView) findViewById(R.id.name);
        roll = (TextView) findViewById(R.id.roll);
        fine = (TextView) findViewById(R.id.fine);
        changePassword = (Button) findViewById(R.id.changePassword);
        logOut = (Button) findViewById(R.id.logout);

        GetUserData();


        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Account.this, LoginActivity.class));
            }
        });



        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openChange_Password();
            }
        });
    }

    private void GetUserData() {
        RequestQueue uqueue;
        uqueue = Volley.newRequestQueue(this);
        String URLQ = "https://sport-resources-booking-api.herokuapp.com/users";
        JSONObject uid = new JSONObject();
        try {
            uid.put("id",LoginActivity.sroll);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest urequest = new JsonObjectRequest(Request.Method.POST,
                URLQ,
                uid,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        account_layout.setVisibility(View.VISIBLE);
                        LoginActivity.userData = response;
                        try {
                            name.setText("Name : " + LoginActivity.userData.getString("name"));
                            roll.setText("Roll Number : " + LoginActivity.userData.getString("id"));
                            fine.setText("Fine : " + LoginActivity.userData.getString("fine"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }



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
                params.put("Authorization", "Bearer " + LoginActivity.accessTkn);
                return params;
            }
            @Override
            public Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id", LoginActivity.sroll);
                return params;
            }
        };
        uqueue.add(urequest);
    }

    private void openChange_Password() {
        Intent intent = new Intent(this,Change_Password.class);
        startActivity(intent);

    }
}
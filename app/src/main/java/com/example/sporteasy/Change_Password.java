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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Change_Password extends AppCompatActivity {
    EditText roll;
    EditText oldpwd;
    EditText newpwd;
    Button submit;
    JsonObjectRequest changepwdrequest;
    private RequestQueue queueD;
    JSONObject change_pwd;
    String id, pwd0, pwd1;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change__password);
        roll = findViewById(R.id.roll);
        oldpwd = findViewById(R.id.oldpwd);
        newpwd = findViewById(R.id.newpwd);
        submit = findViewById(R.id.submit);
        textView = findViewById(R.id.textView);
        change_pwd = new JSONObject();

        queueD = Volley.newRequestQueue(this);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id = roll.getText().toString();
                pwd0 = oldpwd.getText().toString();
                pwd1 = newpwd.getText().toString();

                try {
                    change_pwd.put("id", id);
                    change_pwd.put("password", pwd0);
                    change_pwd.put("new_password", pwd1);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                String URLD = "https://sport-resources-booking-api.herokuapp.com/changePassword";

                changepwdrequest = new JsonObjectRequest(Request.Method.POST,
                        URLD,
                        change_pwd,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                // toast = new Toast(getApplicationContext(), "Password changed successfully", Toast.LENGTH_LONG);
                                Toast.makeText(Change_Password.this, "Password changed successfully", Toast.LENGTH_SHORT).show();
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();

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
                        params.put("id", id);
                        return params;
                    }
                };
                queueD.add(changepwdrequest);

                gotohome();

            }
        });
    }

    private void gotohome() {
        Intent intent = new Intent(this,Account.class);
        startActivity(intent);
    }

}

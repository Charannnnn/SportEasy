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

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Forgot_Password extends AppCompatActivity {

    EditText roll;
    Button submit;
    JSONObject id;
    JsonObjectRequest forgot_password;
    private RequestQueue queueE;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot__password);

        roll = findViewById(R.id.roll);
//        textView7 = findViewById(R.id.textView7);
//        final String rollno = roll.getText().toString();
        submit = findViewById(R.id.button2);
        //final String URLE = "https://sport-resources-booking-api.herokuapp.com/forgot_password?id="+rollno;
        //textView7.setText(URLE);
        queueE = Volley.newRequestQueue(this);
        id = new JSONObject();
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                roll = findViewById(R.id.roll);

                final String rollno = roll.getText().toString();
                final String URLE = "https://sport-resources-booking-api.herokuapp.com/forgot_password?id="+rollno;
                forgot_password = new JsonObjectRequest(Request.Method.GET,
                        URLE,
                        null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Toast toast = Toast.makeText(getApplicationContext(), "A mail has been sent to your registered email to reset your password", Toast.LENGTH_LONG);
                                toast.show();
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast toast = Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_LONG);
                                toast.show();
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
                        params.put("id",rollno);
                        return params;
                    }
                };
                queueE.add(forgot_password);

                gotoLogin();
            }
        });

    }
    private  void gotoLogin() {
        Intent intent = new Intent(this,LoginActivity.class);
        startActivity(intent);

    }
}



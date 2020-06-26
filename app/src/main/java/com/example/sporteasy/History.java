package com.example.sporteasy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

public class History extends AppCompatActivity {

    JsonArrayRequest arrayRequestB;
    RequestQueue Hqueue;
    LinearLayout bookedhistorylist;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history);

        //Initialize and Assign
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        // Set Home Selected
        bottomNavigationView.setSelectedItemId(R.id.history);
        //Perform ItemSelecctedListener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.history:
                        return true;
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(), Home.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.account:
                        startActivity(new Intent(getApplicationContext(), Account.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });

        bookedhistorylist = (LinearLayout) findViewById(R.id.bookedhistorylist);
        Hqueue = Volley.newRequestQueue(this);
        String HURL = "https://sport-resources-booking-api.herokuapp.com/allBookings";

        JsonArrayRequest Hrequest = new JsonArrayRequest(Request.Method.GET,
                HURL,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        int len = response.length();
                        for (int i = 0; i<len; i++){
                            try{
                                if (response.getJSONObject(i).getString("user_id").trim().equals(LoginActivity.sroll)){
                                    add(bookedhistorylist,
                                            response.getJSONObject(i).getString("resource_name"),
                                            response.getJSONObject(i).getString("day"),
                                            response.getJSONObject(i).getString("status"),
                                            response.getJSONObject(i).getString("return_time").toString());
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast toast = Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG);
                        toast.show();
                    }
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "Bearer " + LoginActivity.accessTkn);
                return params;
            }
        };
        Hqueue.add(Hrequest);





    }

    private void add(LinearLayout bookedhistorylist, String resource_name, String day, String status, String return_time) {
        String r = resource_name;
        String c = day;

        LinearLayout bookedset = new LinearLayout(this);
        bookedset.setOrientation(LinearLayout.HORIZONTAL);
        bookedset.setBackground(getResources().getDrawable(R.drawable.lay_bg));
        bookedset.setPadding(18,32,18,32);

        LinearLayout bookedE1 = new LinearLayout(this);
        bookedE1.setOrientation(LinearLayout.VERTICAL);
        bookedE1.setPadding(40, 30, 0, 20);

        LinearLayout bookedE2 = new LinearLayout(this);
        bookedE2.setPadding(0, 40, 30, 40);

        TextView tvResourse = new TextView(this);
        tvResourse.setText(r);
        tvResourse.setTextColor(Color.WHITE);
        tvResourse.setTextSize(18);
        tvResourse.setTypeface(Typeface.SERIF);

        TextView tvDate = new TextView(this);
        tvDate.setText(c);
        tvDate.setTextColor(Color.WHITE);
        tvDate.setTextSize(12);

        Space space = new Space(this);

        TextView returnbtn = new TextView(this);
        returnbtn.setPadding(40, 20, 40, 20);
        if (status.equals("0")) {
            returnbtn.setText("Not collected");
            returnbtn.setBackground(getResources().getDrawable(R.drawable.btn_bg));
            returnbtn.setTextColor(getResources().getColor(R.color.layoutbg));
        }
        else if(status.equals("1")) {
            if (return_time.equals("null")) {
                returnbtn.setText("Not Returned");
                returnbtn.setBackground(getResources().getDrawable(R.drawable.status_bg));
                returnbtn.setTextColor(getResources().getColor(R.color.black));
                returnbtn.setTextSize(14);

            }
            else {
                returnbtn.setText("Returned");
                //tv4.setText(rtime);
                returnbtn.setBackground(getResources().getDrawable(R.drawable.status_bg_1));
                returnbtn.setTextColor(getResources().getColor(R.color.white));
                returnbtn.setTextSize(18);
            }
        }

        bookedE1.addView(tvResourse);
        bookedE1.addView(tvDate);
        bookedE2.addView(returnbtn);
        bookedset.addView(bookedE1);
        bookedset.addView(bookedE2);
        bookedhistorylist.addView(bookedset);
        bookedhistorylist.addView(space, 0, 60);
        bookedhistorylist.setPadding(50, 0, 50, 0);




    }
}
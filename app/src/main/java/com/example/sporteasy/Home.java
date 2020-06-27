package com.example.sporteasy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Home extends AppCompatActivity {

   static JSONObject user_id;
   LinearLayout bookedlist, availablelist, bookedE2;
   TextView bookedmsg,text3,text5;
   ImageView refreshButton;
   Button optionE2;
   Boolean booked, hasFine;
   BookedResource userBookedData;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        //Initialize and Assign
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        // Set Home Selected
        bottomNavigationView.setSelectedItemId(R.id.home);
        //Perform ItemSelecctedListener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.home:
                        return true;
                    case R.id.history:
                        startActivity(new Intent(getApplicationContext(), History.class));
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

        bookedlist = (LinearLayout) findViewById(R.id.bookedlist);
        availablelist = (LinearLayout) findViewById(R.id.availablelist);
        bookedE2 = (LinearLayout) findViewById(R.id.bookedE2);
        bookedmsg = (TextView) findViewById(R.id.bookedmsg);
        text3 = (TextView) findViewById(R.id.textView3);
        text5 = (TextView) findViewById(R.id.textView5);
        optionE2 = (Button) findViewById(R.id.optionE2);
        refreshButton = (ImageView) findViewById(R.id.refreshButton);
        user_id= new JSONObject();

        try {
            user_id.put("id",LoginActivity.sroll);
        } catch (JSONException e) {
            e.printStackTrace();
        }

//        SimpleDateFormat sd = new SimpleDateFormat("HH:mm:ss");
//        String time = sd.format(new Date());
//        if ((("11:40:00".compareTo(time)) < 0) && (("15:00:00".compareTo(time)) >0)) {
//
//            TodayBookingData();
//
//        }
//        else {
//            text5.setText("");
//            text3.setText("");
//            bookedmsg.setText("Bookings available from 11:40 AM to 03:00 PM only");
//            bookedmsg.setTextSize(18);
//        }
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TodayBookingData();
            }
        });

        TodayBookingData();


        bookedE2.setVisibility(View.INVISIBLE);







    }

    private void TodayBookingData() {
        RequestQueue queueB;
        queueB = Volley.newRequestQueue(this);
        String URL_bookingLog = "https://sport-resources-booking-api.herokuapp.com/userBookingslog";

        JsonObjectRequest objectRequest_bookingLog = new JsonObjectRequest(Request.Method.POST,
                URL_bookingLog,
                user_id,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        booked = true;
                        GsonBuilder gsonBuilder = new GsonBuilder();
                        Gson gson = gsonBuilder.create();
                        userBookedData = gson.fromJson(String.valueOf(response), BookedResource.class);

                        if (userBookedData.getStatus()==0){
                            bookedE2.setVisibility(View.VISIBLE);
                            bookedmsg.setText(userBookedData.getResourceName());
                            optionE2.setEnabled(true);
                            optionE2.setText("Cancel");
                            optionE2.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    cancel();
                                }
                            });
                        }
                        else if (userBookedData.getStatus()==1){
                            bookedE2.setVisibility(View.INVISIBLE);
                            bookedmsg.setText("You have already booked a resource !");
                        }
                        if (availablelist.getChildCount()>0){
                            availablelist.removeAllViews();
                        }
                        DisplayResources();




                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {


                        booked = false;
                        bookedmsg.setText("No Bookings");
                        bookedE2.setVisibility(View.INVISIBLE);
                        bookedmsg.setTextSize(20);
                        if (availablelist.getChildCount()>0){
                            availablelist.removeAllViews();
                        }
                        DisplayResources();


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
        queueB.add(objectRequest_bookingLog);
    }

    private void cancel() {

        RequestQueue Cqueue;
        Cqueue = Volley.newRequestQueue(this);
        String CURL = "https://sport-resources-booking-api.herokuapp.com/cancelBooking";

        JsonObjectRequest CobjectRequest = new JsonObjectRequest(Request.Method.POST,
                CURL,
                user_id,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(Home.this, "Cancel Successful!", Toast.LENGTH_SHORT).show();
                        TodayBookingData();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(),"Cancel Failed! Check terms and conditions",Toast.LENGTH_LONG).show();
                        TodayBookingData();

                    }
                }){
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
        Cqueue.add(CobjectRequest);

    }

    private void DisplayResources() {

        RequestQueue Dqueue;
        Dqueue = Volley.newRequestQueue(this);
        String URL = "https://sport-resources-booking-api.herokuapp.com/ResourcesPresent";

        JsonArrayRequest arrayRequest = new JsonArrayRequest(Request.Method.GET,
                URL,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        String newResponse = response.toString();
                        GsonBuilder gsonBuilder = new GsonBuilder();
                        Gson gson = gsonBuilder.create();
                        Resource[] resources = gson.fromJson(newResponse,Resource[].class);
                        int len = resources.length;
                        for(int i=0;i<len;i++){
                            Resource unit = resources[i];
                            add(availablelist,unit);

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast toast = Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_LONG);
                        toast.show();
                    }
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "Bearer "+LoginActivity.accessTkn);
                return params;
            }
        };

        Dqueue.add(arrayRequest);
    }

    private void add(LinearLayout availablelist, final Resource unit) {


        String r = unit.getResourceName();
        String c = "Count: " + unit.getResourcesAvailable().toString() + "/" + unit.getCount().toString();

        LinearLayout resourceset = new LinearLayout(this);
        resourceset.setOrientation(LinearLayout.HORIZONTAL);
        resourceset.setBackground(getResources().getDrawable(R.drawable.lay_bg));
        resourceset.setPadding(18,32,18,32);

        LinearLayout resourceE1 = new LinearLayout(this);
        resourceE1.setOrientation(LinearLayout.VERTICAL);
        resourceE1.setPadding(40, 30, 0, 20);
        resourceE1.setMinimumWidth(500);

        LinearLayout resourceE2 = new LinearLayout(this);
        resourceE2.setPadding(0, 40, 30, 40);

        TextView tvResourse = new TextView(this);
        tvResourse.setText(r);
        tvResourse.setTextColor(Color.WHITE);
        tvResourse.setTextSize(18);
        tvResourse.setTypeface(Typeface.SERIF);

        TextView tvCount = new TextView(this);
        tvCount.setText(c);
        tvCount.setTextColor(Color.WHITE);
        tvCount.setTextSize(12);

        Button bookbtn = new Button(this);
        bookbtn.setText("Book");
        try {
            hasFine = (((LoginActivity.userData.getString("fine")).compareTo("0")) > 0 );
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (booked ){
            bookbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ShowAlert(1);
                }
            });
        }
        else if (hasFine){
            bookbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ShowAlert(2);
                }
            });
        }
        else{
            bookbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    book(LoginActivity.sroll, unit.getResourceName());
                }
            });
        }
        bookbtn.setPadding(40, 20, 40, 20);
        bookbtn.setBackground(getResources().getDrawable(R.drawable.btn_bg));
        bookbtn.setTextColor(getResources().getColor(R.color.layoutbg));

        Space space = new Space(this);

        resourceE1.addView(tvResourse);
        resourceE1.addView(tvCount);
        resourceE2.addView(bookbtn);
        resourceset.addView(resourceE1);
        resourceset.addView(resourceE2);
        availablelist.addView(resourceset);
        availablelist.addView(space, 0, 60);
        availablelist.setPadding(50, 0, 50, 0);

    }

    private void ShowAlert(int i) {
        if (i ==1) {
            AlertDialog.Builder builder = new AlertDialog.Builder(Home.this);
            builder.setMessage("You have a booking today");
            builder.setTitle("Booking Failed");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            AlertDialog bookingFailDialog = builder.create();
            bookingFailDialog.show();

        }
        else if (i==2){
            AlertDialog.Builder builder = new AlertDialog.Builder(Home.this);
            builder.setMessage("You have a fine.");
            builder.setTitle("Booking Failed");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            AlertDialog bookingFailDialog = builder.create();
            bookingFailDialog.show();
        }
    }

    private void book(final String sroll, final String resourceName){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        final String date = sdf.format(new Date());
        SimpleDateFormat sd = new SimpleDateFormat("HH:mm:ss");
        String time = sd.format(new Date());

        RequestQueue Bqueue;
        Bqueue = Volley.newRequestQueue(this);
        String BURL = "https://sport-resources-booking-api.herokuapp.com/bookResource";

        JSONObject bdata = new JSONObject();
        try {
            bdata.put("id", sroll);
            bdata.put("name", resourceName);
            bdata.put("day",date);
            bdata.put("reservation_time", "12:10:00");
        } catch (JSONException e) {
            e.printStackTrace();
        }


        JsonObjectRequest brequest = new JsonObjectRequest(Request.Method.POST,
                BURL,
                bdata,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(Home.this, "Successful", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        TodayBookingData();
                        Toast.makeText(Home.this, "Booking Successful", Toast.LENGTH_SHORT).show();

                    }
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "Bearer "+LoginActivity.accessTkn);
                return params;
            }
            @Override
            public Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id", LoginActivity.sroll);
                params.put("name", resourceName);
                params.put("day", date);
                params.put("reservation_time","12:10:00");
                return params;
            }

        };
        Bqueue.add(brequest);
    }


}
package com.example.sporteasy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONException;

public class Account extends AppCompatActivity {
    TextView name,roll,fine;
    Button changePassword, logOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        changePassword = findViewById(R.id.changePassword);

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

        try {
            name.setText("Name : " + LoginActivity.userData.getString("name"));
            roll.setText("Roll Number : " + LoginActivity.userData.getString("id"));
            fine.setText("Fine : " + LoginActivity.userData.getString("fine"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

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

    private void openChange_Password() {
        Intent intent = new Intent(this,Change_Password.class);
        startActivity(intent);

    }
}
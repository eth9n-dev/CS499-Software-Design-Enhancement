package com.example.inventorytracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText usernameText = findViewById(R.id.editTextText);
        EditText passwordText = findViewById(R.id.editTextTextPassword);
        Button registerBtn = findViewById(R.id.button2);
        Button loginBtn = findViewById(R.id.button);

        DBHandler dbHandler = new DBHandler(MainActivity.this);

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = usernameText.getText().toString();
                String pass = passwordText.getText().toString();

                if (user.isEmpty() && pass.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Empty fields...", Toast.LENGTH_SHORT).show();
                    return;
                }

                dbHandler.addNewUser(user, pass);
                Toast.makeText(MainActivity.this, "Registration successful.", Toast.LENGTH_SHORT).show();
                usernameText.setText("");
                passwordText.setText("");
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = usernameText.getText().toString();
                String pass = passwordText.getText().toString();

                if (user.isEmpty() && pass.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Empty fields...", Toast.LENGTH_SHORT).show();
                    return;
                }

                String passToCheck = dbHandler.validateUser(user);

                if (passToCheck.equals("")) {
                    Toast.makeText(MainActivity.this, "Invalid password...", Toast.LENGTH_SHORT).show();
                }
                else if (passToCheck.equals(pass)) {
                    Toast.makeText(MainActivity.this, "Login successful.", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(MainActivity.this, InventoryDisplay.class));
                }
                else {
                    Toast.makeText(MainActivity.this, "Invalid password...", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
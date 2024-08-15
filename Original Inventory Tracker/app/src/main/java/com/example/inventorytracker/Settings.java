package com.example.inventorytracker;

import android.Manifest;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.ActivityCompat;

public class Settings extends AppCompatActivity {

    RadioButton s;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        s = findViewById(R.id.radioButton);

        s.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ActivityCompat.requestPermissions(Settings.this, new String[] {Manifest.permission.SEND_SMS}, 101);
                    Toast.makeText(Settings.this, "SMS permission granted!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}

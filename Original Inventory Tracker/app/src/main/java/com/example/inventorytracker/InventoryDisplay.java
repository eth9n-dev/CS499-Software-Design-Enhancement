package com.example.inventorytracker;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class InventoryDisplay extends AppCompatActivity implements RecyclerViewInterface {

    ArrayList<String> name, quantity;
    MyAdapter adapter;
    DBHandler dbHandler;
    RecyclerView recycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inventory_display);

        dbHandler = new DBHandler(InventoryDisplay.this);

        FloatingActionButton addButton = findViewById(R.id.floatingActionButton3);
        FloatingActionButton settingsBtn = findViewById(R.id.floatingActionButton5);
        recycler = findViewById(R.id.recyclerView);

        name = new ArrayList<>();
        quantity = new ArrayList<>();
        adapter = new MyAdapter(this, name, quantity, this);
        recycler.setAdapter(adapter);
        recycler.setLayoutManager(new LinearLayoutManager(this));

        displayData();

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(InventoryDisplay.this, AddItem.class));
            }
        });

        settingsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(InventoryDisplay.this, Settings.class));
            }
        });
    }

    private void displayData() {
        Cursor c =  dbHandler.getData();

        if (c.getCount() == 0) {
            return;
        }
        else {
            while (c.moveToNext()) {
                name.add(c.getString(1));
                quantity.add(c.getString(2));
            }
        }
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(InventoryDisplay.this, EditItem.class);

        intent.putExtra("NAME", name.get(position));
        intent.putExtra("QUANTITY", quantity.get(position));

        startActivity(intent);
    }
}

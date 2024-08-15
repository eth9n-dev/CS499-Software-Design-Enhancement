package com.example.inventorytracker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddItem extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_item);

        TextView itemName = findViewById(R.id.editTextText2);
        TextView quantity = findViewById(R.id.editTextText3);
        Button addBtn = findViewById(R.id.button4);

        DBHandler dbHandler = new DBHandler(AddItem.this);

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String iName = itemName.getText().toString();
                String iQuantity = quantity.getText().toString();

                int q = Integer.parseInt(iQuantity);

                if (iName.isEmpty() && iQuantity.isEmpty()) {
                    return;
                }

                dbHandler.addNewItem(iName, q);
                Toast.makeText(AddItem.this, "Item created successfully.", Toast.LENGTH_SHORT).show();

                startActivity(new Intent(AddItem.this, InventoryDisplay.class));
            }
        });
    }
}

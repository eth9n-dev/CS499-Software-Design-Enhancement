package com.example.inventorytracker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class EditItem extends AppCompatActivity {

    EditText nameText, quantityText;
    DBHandler db;
    Button submit, delete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_item);

        db = new DBHandler(EditItem.this);

        delete = findViewById(R.id.button5);
        submit = findViewById(R.id.button4);
        nameText = findViewById(R.id.editTextText2);
        quantityText = findViewById(R.id.editTextText3);

        String name = getIntent().getStringExtra("NAME");
        String quantity = getIntent().getStringExtra("QUANTITY");

        nameText.setText(name);
        quantityText.setText(quantity);

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.deleteEntry(name);
                Toast.makeText(EditItem.this, "Item successfully removed!", Toast.LENGTH_SHORT).show();

                startActivity(new Intent(EditItem.this, InventoryDisplay.class));
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.updateQuantity(name, Integer.parseInt(quantityText.getText().toString()));
                Toast.makeText(EditItem.this, "Item successfully updated!", Toast.LENGTH_SHORT).show();

                startActivity(new Intent(EditItem.this, InventoryDisplay.class));
            }
        });


    }
}

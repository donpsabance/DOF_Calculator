package com.dabance.dof_calculator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class AddLensActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_lens);

        setTitle("Lens Details");

        Button cancelButton = findViewById(R.id.cancelButton);
        Button saveButton = findViewById(R.id.saveButton);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveLensData();
                finish();
            }
        });
    }

    public static Intent makeIntent(Context context){
        return new Intent(context, AddLensActivity.class);
    }

    private void saveLensData(){


        Toast.makeText(AddLensActivity.this, "Successfully added lens!", Toast.LENGTH_SHORT).show();
    }
}

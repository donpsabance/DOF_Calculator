package com.dabance.dof_calculator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dabance.dof_calculator.model.Lens;
import com.dabance.dof_calculator.model.LensManager;
import com.dabance.dof_calculator.model.NumberManager;

public class AddLensActivity extends AppCompatActivity {

    private LensManager lensManager = LensManager.getInstance();

    public static Intent makeIntent(Context context){
        return new Intent(context, AddLensActivity.class);
    }

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
                if(saveLensData()){

                    Intent intent = getIntent();
                    setResult(RESULT_OK, intent);

                    finish();
                } else {
                    Toast.makeText(AddLensActivity.this, "Invalid lens, try again", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean saveLensData(){

        EditText make = findViewById(R.id.makeInput);
        EditText focalLength = findViewById(R.id.focalLengthInput);
        EditText aperture = findViewById(R.id.apertureInput);

        if(make.getText().toString().length() > 0){
            if(NumberManager.isIntegerInRange(focalLength.getText().toString(), 1, Integer.MAX_VALUE)){
                if(NumberManager.isDoubleInRange(aperture.getText().toString(), 1.4, Double.MAX_VALUE)){

                    lensManager.addLens(new Lens(make.getText().toString(),
                            Double.parseDouble(aperture.getText().toString()),
                            Integer.parseInt(focalLength.getText().toString())));


                    Toast.makeText(AddLensActivity.this, "Successfully added lens!", Toast.LENGTH_SHORT).show();
                    return true;

                } else {
                    aperture.setError("ERROR: Invalid aperture [MIN: 0]");
                }
            } else {
                focalLength.setError("ERROR: Invalid focal length [MIN: 1.4]");
            }
        } else{
            make.setError("ERROR: Missing name");
        }
        return false;
    }
}

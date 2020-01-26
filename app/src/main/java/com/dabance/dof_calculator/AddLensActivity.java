package com.dabance.dof_calculator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.dabance.dof_calculator.model.Lens;
import com.dabance.dof_calculator.model.NumberManager;

public class AddLensActivity extends AppCompatActivity  implements AdapterView.OnItemSelectedListener {

    private static final int LENS_INFO_DATA = 0;

    public static Intent makeIntent(Context context){
        return new Intent(context, AddLensActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_lens);
        setTitle(getString(R.string.lensDetailTitle));

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

        loadSpinner();
    }

    private void loadSpinner() {

        //image icons list
        Spinner spinner = findViewById(R.id.imageSpinnerAdd);
        Integer[] items = {R.drawable.icon1, R.drawable.icon2, R.drawable.icon3, R.drawable.icon4, R.drawable.icon5, R.drawable.icon6 };
        ArrayAdapter<Integer> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

    }

    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id){

        ImageView imageView = findViewById(R.id.iconPreviewAddLens);
        Integer[] items = {R.drawable.icon1, R.drawable.icon2, R.drawable.icon3, R.drawable.icon4, R.drawable.icon5, R.drawable.icon6 };
        imageView.setImageResource(items[pos]);

    }

    public void onNothingSelected(AdapterView<?> parent){  }

    private boolean saveLensData(){

        Spinner spinner = findViewById(R.id.imageSpinnerAdd);
        EditText make = findViewById(R.id.makeInput);
        EditText focalLength = findViewById(R.id.focalLengthInput);
        EditText aperture = findViewById(R.id.apertureInput);

        if(make.getText().toString().length() > 0){
            if(NumberManager.isIntegerInRange(focalLength.getText().toString(), 1, Integer.MAX_VALUE)){
                if(NumberManager.isDoubleInRange(aperture.getText().toString(), 1.4, Double.MAX_VALUE)){

                    //save the lens data through SharePreference because there is no LensManager list
                    //because we deleted it when we switched activities from MainActivity
                    Lens lens = new Lens(make.getText().toString(), Double.parseDouble(aperture.getText().toString()),
                            Integer.parseInt(focalLength.getText().toString()),  Integer.parseInt(spinner.getSelectedItem().toString()));

                    SharedPreferences sharedPreferences = AddLensActivity.this.getSharedPreferences(getString(R.string.sharedPrefFile), Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();

                    editor.putInt(lens.getInfo(), LENS_INFO_DATA).apply();

                    Toast.makeText(AddLensActivity.this, "Successfully added lens!", Toast.LENGTH_SHORT).show();
                    return true;

                } else {
                    aperture.setError("ERROR: Invalid aperture [MIN: 1.4]");
                }
            } else {
                focalLength.setError("ERROR: Invalid focal length [MIN: 1]");
            }
        } else{
            make.setError("ERROR: Missing name");
        }
        return false;
    }
}

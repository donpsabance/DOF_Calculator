package com.dabance.dof_calculator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dabance.dof_calculator.model.Lens;
import com.dabance.dof_calculator.model.LensManager;
import com.dabance.dof_calculator.model.NumberManager;

public class EditLensActivity extends AppCompatActivity {

    private static final String SELECTED_LENS_DATA = "com.dabance.dof_calculator.EditLensActivity - lens data";
    private LensManager lensManager = LensManager.getInstance();

    String lensData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_lens);
        setTitle(getString(R.string.lensDetailTitle));

        Button cancelButton = findViewById(R.id.editCancelButton);
        Button saveButton = findViewById(R.id.editSaveButton);

        cancelButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                finish();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(saveLensData()){

                    //send back the new len's data that was just edited back to Calculations so the new information can be show
                    Intent intent = new Intent();
                    intent.putExtra(SELECTED_LENS_DATA, lensData);
                    setResult(RESULT_OK, intent);
                    finish();

                } else {
                    Toast.makeText(EditLensActivity.this, "Invalid lens, try again", Toast.LENGTH_SHORT).show();
                }
            }
        });

        extractDataFromIntent();
        loadLensInfo();
    }

    private void extractDataFromIntent() {
        Intent intent = getIntent();
        lensData = intent.getStringExtra(SELECTED_LENS_DATA);
    }

    public static Intent makeIntent(Context context, String lensData){
        Intent intent = new Intent(context, EditLensActivity.class);
        intent.putExtra(SELECTED_LENS_DATA, lensData);
        return intent;
    }

    private void loadLensInfo(){

        TextView make = findViewById(R.id.editMakeInput);
        TextView focal = findViewById(R.id.editFocalLengthInput);
        TextView aperture = findViewById(R.id.editApertureInput);

        String[] listValues = lensData.split(",");

        make.setText(listValues[0]);
        aperture.setText(listValues[1]);
        focal.setText(listValues[2]);

    }

    private boolean saveLensData(){

        EditText make = findViewById(R.id.editMakeInput);
        EditText focalLength = findViewById(R.id.editFocalLengthInput);
        EditText aperture = findViewById(R.id.editApertureInput);

        if(make.getText().toString().length() > 0){
            if(NumberManager.isIntegerInRange(focalLength.getText().toString(), 1, Integer.MAX_VALUE)){
                if(NumberManager.isDoubleInRange(aperture.getText().toString(), 1.4, Double.MAX_VALUE)){

                    //save the lens data through SharePreference because there is no LensManager list
                    //because we deleted it when we switched activities from MainActivity
                    //first delete the old lens, then save the new lens to SharedPreference
                    Lens newLens = new Lens(make.getText().toString(), Double.parseDouble(aperture.getText().toString()), Integer.parseInt(focalLength.getText().toString()));
                    SharedPreferences sharedPreferences = EditLensActivity.this.getSharedPreferences(getString(R.string.sharedPrefFile), Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();

                    editor.remove(lensData).apply();
                    editor.putInt(newLens.getInfo(), 1).apply();

                    lensData = newLens.getInfo();

                    Toast.makeText(EditLensActivity.this, "Successfully edited lens!", Toast.LENGTH_SHORT).show();
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

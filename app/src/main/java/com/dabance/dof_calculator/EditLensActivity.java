package com.dabance.dof_calculator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
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

    private static final String SELECTED_LENS_INDEX = "com.dabance.dof_calculator.EditLensActivity - lens index";
    private LensManager lensManager = LensManager.getInstance();

    int selectedLens;

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

                    Intent intent = getIntent();
                    setResult(RESULT_OK);
                    finish();

                } else {
                    Toast.makeText(EditLensActivity.this, "Invalid lens, try again", Toast.LENGTH_SHORT).show();
                }
            }
        });

        loadLensInfo();
    }

    public static Intent makeIntent(Context context, int lensIndex){
        Intent intent = new Intent(context, EditLensActivity.class);
        intent.putExtra(SELECTED_LENS_INDEX, lensIndex);
        return intent;
    }

    private void loadLensInfo(){

        TextView make = findViewById(R.id.editMakeInput);
        TextView focal = findViewById(R.id.editFocalLengthInput);
        TextView aperture = findViewById(R.id.editApertureInput);

        Lens lens = lensManager.getLensList().get(selectedLens);

        make.setText(lens.getMake());
        focal.setText(Integer.toString(lens.getFocalLength()));
        aperture.setText(Double.toString(lens.getMaxAperture()));

    }

    private boolean saveLensData(){

        Lens lens = lensManager.getLensList().get(selectedLens);

        EditText make = findViewById(R.id.editMakeInput);
        EditText focalLength = findViewById(R.id.editFocalLengthInput);
        EditText aperture = findViewById(R.id.editApertureInput);

        if(make.getText().toString().length() > 0){
            if(NumberManager.isIntegerInRange(focalLength.getText().toString(), 1, Integer.MAX_VALUE)){
                if(NumberManager.isDoubleInRange(aperture.getText().toString(), 1.4, Double.MAX_VALUE)){

                    lens.setMake(make.getText().toString());
                    lens.setFocalLength(Integer.parseInt(focalLength.getText().toString()));
                    lens.setMaxAperture(Double.parseDouble(aperture.getText().toString()));

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

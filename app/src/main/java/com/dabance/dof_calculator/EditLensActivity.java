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
import android.widget.TextView;
import android.widget.Toast;

import com.dabance.dof_calculator.model.Lens;
import com.dabance.dof_calculator.model.NumberManager;

import java.util.Arrays;

/*

    SPINNER ADAPTED FROM: https://stackoverflow.com/questions/13377361/how-to-create-a-drop-down-list

 */

public class EditLensActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private static final String SELECTED_LENS_DATA = "com.dabance.dof_calculator.EditLensActivity - lens data";
    private static final int LENS_INFO_DATA = 0;

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

    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id){

        ImageView imageView = findViewById(R.id.iconPreviewEditLens);
        Integer[] items = {R.drawable.icon1, R.drawable.icon2, R.drawable.icon3, R.drawable.icon4, R.drawable.icon5, R.drawable.icon6 };
        imageView.setImageResource(items[pos]);

    }

    public void onNothingSelected(AdapterView<?> parent){  }

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

        //image icons list
        Spinner spinner = findViewById(R.id.imageSpinner);
        Integer[] items = {R.drawable.icon1, R.drawable.icon2, R.drawable.icon3, R.drawable.icon4, R.drawable.icon5, R.drawable.icon6 };
        ArrayAdapter<Integer> adapter = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_dropdown_item, items);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        TextView make = findViewById(R.id.editMakeInput);
        TextView focal = findViewById(R.id.editFocalLengthInput);
        TextView aperture = findViewById(R.id.editApertureInput);

        String[] listValues = lensData.split(",");

        make.setText(listValues[0]);
        aperture.setText(listValues[1]);
        focal.setText(listValues[2]);
        spinner.setSelection(Arrays.asList(items).indexOf(Integer.parseInt(listValues[3])));

    }

    private boolean saveLensData(){

        EditText make = findViewById(R.id.editMakeInput);
        EditText focalLength = findViewById(R.id.editFocalLengthInput);
        EditText aperture = findViewById(R.id.editApertureInput);
        Spinner spinner = findViewById(R.id.imageSpinner);

        if(make.getText().toString().length() > 0){
            if(NumberManager.isIntegerInRange(focalLength.getText().toString(), 1, Integer.MAX_VALUE)){
                if(NumberManager.isDoubleInRange(aperture.getText().toString(), 1.4, Double.MAX_VALUE)){

                    //save the lens data through SharePreference because there is no LensManager list
                    //because we deleted it when we switched activities from MainActivity
                    //first delete the old lens, then save the new lens to SharedPreference
                    Lens newLens = new Lens(make.getText().toString(), Double.parseDouble(aperture.getText().toString()),
                            Integer.parseInt(focalLength.getText().toString()), Integer.parseInt(spinner.getSelectedItem().toString()));

                    SharedPreferences sharedPreferences = EditLensActivity.this.getSharedPreferences(getString(R.string.sharedPrefFile), Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();

                    editor.remove(lensData).apply();
                    editor.putInt(newLens.getInfo(), LENS_INFO_DATA).apply();

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

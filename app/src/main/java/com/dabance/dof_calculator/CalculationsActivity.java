package com.dabance.dof_calculator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dabance.dof_calculator.model.DOF_Calculator;
import com.dabance.dof_calculator.model.Lens;
import com.dabance.dof_calculator.model.NumberManager;

import java.text.DecimalFormat;

public class CalculationsActivity extends AppCompatActivity {

    private static final String SELECTED_LENS_DATA = "com.dabance.dof_calculator.CalculationActivity - lens data";
    private static final String SELECTED_LENS_DATA_FROM_EDIT = "com.dabance.dof_calculator.EditLensActivity - lens data";
    private static final int EDIT_REQUEST_CODE = 1;

    String lensData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculations);
        setTitle(getString(R.string.calculateTitle));

        Button deleteButton = findViewById(R.id.deleteButton);
        Button editButton = findViewById(R.id.editButton);
        Button backButton = findViewById(R.id.backButton);

        editButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

                Intent intent = EditLensActivity.makeIntent(CalculationsActivity.this, lensData);
                startActivityForResult(intent, EDIT_REQUEST_CODE);

            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

                SharedPreferences sharedPreferences = CalculationsActivity.this.getSharedPreferences(getString(R.string.sharedPrefFile), Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.remove(lensData).apply();

                Toast.makeText(CalculationsActivity.this, "Successfully deleted lens!", Toast.LENGTH_SHORT).show();

                Intent intent = getIntent();
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        extractDataFromIntent();
        loadLensInfoThroughData(lensData);
        calculateData();

    }

    private void extractDataFromIntent() {
        Intent intent = getIntent();
        lensData = intent.getStringExtra(SELECTED_LENS_DATA);
    }


    public static Intent makeIntent(Context context, String lensData){
        Intent intent = new Intent(context, CalculationsActivity.class);
        intent.putExtra(SELECTED_LENS_DATA, lensData);
        return intent;
    }

    //used when receiving data because there is no LensManager list to retrieve lenses
    private void loadLensInfoThroughData(String lensData){

        TextView tv = findViewById(R.id.selectedCamera);
        String[] listValues = lensData.split(",");
        String message = "Selected lens: " + listValues[0] + " " + listValues[2] + "mm F" + listValues[1];
        tv.setText(message);

    }


    private void calculateData() {

        //input fields
        final EditText COC = findViewById(R.id.circleOfConfusionInput);
        final EditText distance = findViewById(R.id.distanceInput);
        final EditText aperture = findViewById(R.id.selectedApertureInput);

        //output text fields
        final TextView nfp = findViewById(R.id.nearFocalPoint);
        final TextView ffp = findViewById(R.id.farFocalPoint);
        final TextView dof = findViewById(R.id.depthOfField);
        final TextView hfd = findViewById(R.id.hyperfocalDistance);

        final DecimalFormat decimalFormat = new DecimalFormat("#.##");

        String[] listValues = lensData.split(",");
        final Lens lens = new Lens(listValues[0], Double.parseDouble(listValues[1]), Integer.parseInt(listValues[2]));

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {  }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {  }

            @Override
            public void afterTextChanged(Editable s) {

                //make sure string is not empty so when we try to parse it, we dont crash the program
                if (!s.toString().trim().equals("")) {

                    //calculates the aperture even though there is no distance filled as distance is not taken into
                    //account when calculating
                    if (NumberManager.isDoubleInRange(aperture.getText().toString(), lens.getMaxAperture(), Double.MAX_VALUE)) {

                        double hfdValue = DOF_Calculator.calculateHyperfocalDistance(lens,
                                Double.parseDouble(s.toString())) / 1000;

                        hfd.setText((decimalFormat.format(hfdValue) + " m"));

                    } else {
                        hfd.setText("");
                    }

                    //calculate NFP, FFP, and DOF if it has distance and aperture filled out
                    if (NumberManager.isIntegerInRange(distance.getText().toString(), 0, Integer.MAX_VALUE) &&
                            NumberManager.isDoubleInRange(aperture.getText().toString(), lens.getMaxAperture(), Double.MAX_VALUE)) {

                        double nfpValue = DOF_Calculator.calculateNearFocalPoint(lens, Double.parseDouble(distance.getText().toString()), Double.parseDouble(aperture.getText().toString()));
                        double ffpValue = DOF_Calculator.calculateFarFocalPoint(lens, Double.parseDouble(distance.getText().toString()), Double.parseDouble(aperture.getText().toString()));
                        double dofValue = DOF_Calculator.calculateDOF(lens, Double.parseDouble(distance.getText().toString()), Double.parseDouble(aperture.getText().toString()));

                        dof.setText((decimalFormat.format(dofValue) + " m"));
                        ffp.setText((decimalFormat.format(ffpValue) + " m"));
                        nfp.setText((decimalFormat.format(nfpValue) + " m"));


                    } else {

                        nfp.setText("");
                        ffp.setText("");
                        dof.setText("");

                    }
                } else {

                    nfp.setText("");
                    ffp.setText("");
                    dof.setText("");

                }
            }
        };

        COC.addTextChangedListener(textWatcher);
        distance.addTextChangedListener(textWatcher);
        aperture.addTextChangedListener(textWatcher);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        //check if editing lens was successful, if it was, tell main screen that it was successful
        //so it can update the lens list
        if(requestCode == EDIT_REQUEST_CODE){
            if(resultCode == RESULT_OK){

                lensData = data.getStringExtra(SELECTED_LENS_DATA_FROM_EDIT);
                loadLensInfoThroughData(lensData);
                setResult(RESULT_OK);

            }
        }
    }
}

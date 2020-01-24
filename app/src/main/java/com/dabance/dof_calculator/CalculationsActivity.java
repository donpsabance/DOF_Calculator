package com.dabance.dof_calculator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
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
import com.dabance.dof_calculator.model.LensManager;
import com.dabance.dof_calculator.model.NumberManager;

import org.w3c.dom.Text;

import java.text.DecimalFormat;

public class CalculationsActivity extends AppCompatActivity {

    private static final String SELECTED_LENS_INDEX = "com.dabance.dof_calculator.CalculationActivity - lens index";
    private static final int EDIT_REQUEST_CODE = 1;
//    private static final int DELETE_REQUEST_CODE = 2;

    private LensManager lensManager = LensManager.getInstance();

    int selectedLens;

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

                Intent intent = EditLensActivity.makeIntent(CalculationsActivity.this, selectedLens);
                startActivityForResult(intent, EDIT_REQUEST_CODE);

            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

                lensManager.getLensList().remove(selectedLens);
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
        loadLensInfo(selectedLens);
        calculateData();

    }

    private void extractDataFromIntent() {
        Intent intent = getIntent();
        selectedLens = intent.getIntExtra(SELECTED_LENS_INDEX, 0);
    }


    public static Intent makeIntent(Context context, int lensIndex){
        Intent intent = new Intent(context, CalculationsActivity.class);
        intent.putExtra(SELECTED_LENS_INDEX, lensIndex);
        return intent;
    }

    private void loadLensInfo(int lensIndex){

        TextView tv = findViewById(R.id.selectedCamera);
        tv.setText("Selected lens: " + lensManager.getLensList().get(lensIndex));

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
        final Lens lens = lensManager.getLensList().get(selectedLens);

        //used for near and far focal point and depth of field
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }


            @Override
            public void afterTextChanged(Editable s) {

                if(NumberManager.isIntegerInRange(s.toString(), 0, Integer.MAX_VALUE) &&
                    NumberManager.isDoubleInRange(aperture.getText().toString(), 1.4, Double.MAX_VALUE)) {

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
            }
        };

        COC.addTextChangedListener(textWatcher);
        distance.addTextChangedListener(textWatcher);

        //seperate because distance is not needed to calculate hyperfocal distance
        aperture.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if(NumberManager.isDoubleInRange(s.toString(), lens.getMaxAperture(), Double.MAX_VALUE)) {

                    double hfdValue = DOF_Calculator.calculateHyperfocalDistance(lens,
                            Double.parseDouble(s.toString())) / 1000;

                    hfd.setText((decimalFormat.format(hfdValue) + " m"));
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        //check if editing lens was successful, if it was, tell main screen that it was successful
        //so it can update the lens list
        if(requestCode == EDIT_REQUEST_CODE){
            if(resultCode == RESULT_OK){

                loadLensInfo(selectedLens);

                Intent intent = getIntent();
                setResult(RESULT_OK);

            }
        }
    }
}

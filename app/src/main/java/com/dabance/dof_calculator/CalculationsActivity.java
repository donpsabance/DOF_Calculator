package com.dabance.dof_calculator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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

    private static final String SELECTED_LENS_INDEX = "com.dabance.dof_calculator.CalculationActivity - the lens index";
    private LensManager lensManager = LensManager.getInstance();

    int selectedLens;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculations);

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

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                //Near and far local point and depth of field
                if(NumberManager.isDoubleInRange(s.toString(), 0.0, Double.MAX_VALUE) &&
                        (NumberManager.isDoubleInRange(s.toString(), lens.getMaxAperture(), Double.MAX_VALUE))){


                    double nfpValue = Double.parseDouble(decimalFormat.format(DOF_Calculator.calculateNearFocalPoint(lens,
                            Double.parseDouble(distance.getText().toString()), Double.parseDouble(aperture.getText().toString()))));

                    double ffpValue = Double.parseDouble(decimalFormat.format(DOF_Calculator.calculateFarFocalPoint(lens,
                            Double.parseDouble(distance.getText().toString()), Double.parseDouble(aperture.getText().toString()))));

                    double dofValue = Double.parseDouble(decimalFormat.format(DOF_Calculator.calculateDOF(lens,
                            Double.parseDouble(distance.getText().toString()), Double.parseDouble(aperture.getText().toString()))));

                    nfp.setText(Double.toString(nfpValue));
                    ffp.setText(Double.toString(ffpValue));
                    dof.setText(Double.toString(dofValue));

                } else{

                    nfp.setText("hey");
                    ffp.setText("hey");
                    dof.setText("hey");

                }
            }


            @Override
            public void afterTextChanged(Editable s) {


            }
        };

        COC.addTextChangedListener(textWatcher);
        distance.addTextChangedListener(textWatcher);
        aperture.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(NumberManager.isDoubleInRange(s.toString(), lens.getMaxAperture(), Double.MAX_VALUE)) {

                        double hfdValue = Double.parseDouble(decimalFormat.format(DOF_Calculator.calculateHyperfocalDistance(lens,
                                Double.parseDouble(s.toString())) / 1000));

                        hfd.setText(Double.toString(hfdValue));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
}

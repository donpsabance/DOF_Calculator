package com.dabance.dof_calculator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.dabance.dof_calculator.model.Lens;
import com.dabance.dof_calculator.model.LensManager;

import org.w3c.dom.Text;

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

        TextView tv = findViewById(R.id.selected_camera);
        tv.setText("Selected lens: " + lensManager.getLensList().get(lensIndex));


    }
}
